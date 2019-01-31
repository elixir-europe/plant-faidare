package fr.inra.urgi.gpds.elasticsearch.criteria;

import com.google.common.collect.ImmutableList;
import fr.inra.urgi.gpds.elasticsearch.criteria.annotation.CriteriaForDocument;
import fr.inra.urgi.gpds.elasticsearch.criteria.annotation.DocumentPath;
import fr.inra.urgi.gpds.elasticsearch.criteria.annotation.NoDocumentMapping;
import fr.inra.urgi.gpds.elasticsearch.criteria.mapping.CriteriaMapping;
import fr.inra.urgi.gpds.elasticsearch.criteria.mapping.CriteriaMappingTree;
import fr.inra.urgi.gpds.elasticsearch.criteria.mapping.CriteriaMappingTreeNode;
import fr.inra.urgi.gpds.elasticsearch.criteria.mapping.impl.CriteriaMappingCriterionImpl;
import fr.inra.urgi.gpds.elasticsearch.criteria.mapping.impl.CriteriaMappingImpl;
import fr.inra.urgi.gpds.elasticsearch.criteria.mapping.impl.CriteriaMappingTreeNodeImpl;
import fr.inra.urgi.gpds.elasticsearch.document.DocumentAnnotationUtil;
import fr.inra.urgi.gpds.elasticsearch.document.DocumentMetadata;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class generating mapping from value object to criteria using
 * {@link CriteriaForDocument} and {@link DocumentPath} annotations on the given criteria.
 *
 * @author gcornut
 */
public class AnnotatedCriteriaMapper {

    private static final Map<Class, CriteriaMapping> mappingCache = new HashMap<>();

    public static <C> CriteriaMapping getMapping(Class<C> criteriaClass) throws CriteriaMappingException {
        if (mappingCache.containsKey(criteriaClass)) {
            return mappingCache.get(criteriaClass);
        }
        try {
            Map<List<String>, CriteriaMappingTree> tree = new HashMap<>();

            CriteriaForDocument documentClass = criteriaClass.getAnnotation(CriteriaForDocument.class);
            if (documentClass == null) {
                throw CriteriaMappingException.missingDocumentClassAnnotation(criteriaClass);
            }

            DocumentMetadata documentMetadata = DocumentAnnotationUtil
                .getDocumentObjectMetadata(documentClass.value());

            for (Field field : criteriaClass.getDeclaredFields()) {
                PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(criteriaClass, field.getName());
                if (!field.isSynthetic() && descriptor == null) {
                    throw CriteriaMappingException.fieldNotBeanProperty(field);
                }
            }

            for (PropertyDescriptor criteriaProperty : BeanUtils.getPropertyDescriptors(criteriaClass)) {
                String criteriaName = criteriaProperty.getName();
                if (criteriaName.equals("class")) {
                    // Ignore java class property
                    continue;
                }

                Field criterionField = getField(criteriaClass, criteriaName);
                if (criterionField == null) {
                    throw CriteriaMappingException.fieldNotFound(criteriaClass, criteriaName);
                }

                NoDocumentMapping noDocumentMapping = criterionField.getAnnotation(NoDocumentMapping.class);
                if (noDocumentMapping != null) {
                    // Ignore criteria properties explicitly declared with no VO mapping
                    continue;
                }

                DocumentPath documentPath = criterionField.getAnnotation(DocumentPath.class);
                if (documentPath == null) {
                    // Throw exception on criteria properties not annotated (to avoid ignored properties)
                    throw CriteriaMappingException.mappingNotFound(criterionField);
                }

                Class clazz = documentMetadata.getDocumentClass();

                String virtualField = documentPath.virtualField();
                boolean isVirtualField = !virtualField.isEmpty();

                List<String> fullDocumentFieldPath = ImmutableList.copyOf(documentPath.value());
                ImmutableList.Builder<String> javaFieldPathBuilder = new ImmutableList.Builder<>();
                List<String> jsonFieldPath = new ArrayList<>();

                Map<List<String>, CriteriaMappingTree> subTree = tree;
                for (int index = 0, size = fullDocumentFieldPath.size(); index < size; index++) {
                    String documentFieldName = fullDocumentFieldPath.get(index);

                    javaFieldPathBuilder.add(documentFieldName);
                    List<String> javaFieldPath = javaFieldPathBuilder.build();

                    DocumentMetadata.Field field = documentMetadata.getByPath(javaFieldPath);
                    if (field == null) {
                        throw CriteriaMappingException.fieldNotFound(clazz, documentFieldName);
                    }
                    clazz = field.getFieldClass();

                    jsonFieldPath = field.getJsonPath();

                    boolean isLeaf = !isVirtualField && index == size - 1;

                    if (isLeaf) {
                        subTree.put(jsonFieldPath, new CriteriaMappingCriterionImpl(
                            documentFieldName, jsonFieldPath, criteriaProperty, criterionField,
                            isVirtualField
                        ));
                    } else {
                        CriteriaMappingTree treeNode = subTree.get(jsonFieldPath);
                        if (!(treeNode instanceof CriteriaMappingTreeNode)) {
                            treeNode = new CriteriaMappingTreeNodeImpl(documentFieldName, jsonFieldPath);
                            subTree.put(jsonFieldPath, treeNode);
                        }
                        subTree = ((CriteriaMappingTreeNode) treeNode).getMappingTree();
                    }

                }

                if (isVirtualField) {
                    List<String> documentFieldPath = ImmutableList.<String>builder()
                        .addAll(jsonFieldPath)
                        .add(virtualField)
                        .build();

                    subTree.put(documentFieldPath, new CriteriaMappingCriterionImpl(
                        virtualField, documentFieldPath, criteriaProperty, criterionField,
                        isVirtualField
                    ));
                }
            }

            if (tree.isEmpty()) {
                throw CriteriaMappingException.noPropertyFoundInCriteria(criteriaClass);
            }

            CriteriaMapping mapping = new CriteriaMappingImpl(criteriaClass, tree, documentMetadata);
            mappingCache.put(criteriaClass, mapping);
            return mapping;
        } catch (Throwable e) {
            throw new CriteriaMappingException(e);
        }
    }

    private static Field getField(Class<?> aClass, String fieldName) {
        try {
            if (aClass == null) return null;
            return aClass.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return getField(aClass.getSuperclass(), fieldName);
        }
    }

    public static class CriteriaMappingException extends RuntimeException {
        CriteriaMappingException(String message) {
            super(message);
        }

        CriteriaMappingException(Throwable e) {
            super(e);
        }


        static CriteriaMappingException noPropertyFoundInCriteria(Class clazz) {
            return new CriteriaMappingException("Could not find any property for criteria class '" + clazz + "'");

        }

        static CriteriaMappingException fieldNotFound(Class clazz, String field) {
            return new CriteriaMappingException("Could not find field '" + field + "' for class '" + clazz + "'");

        }

        static CriteriaMappingException mappingNotFound(Field criteriaField) {
            return new CriteriaMappingException(
                "Could not find any mapping annotation on criteria field '"
                    + criteriaField.getName() + "'. Please use the '@" + DocumentPath.class.getSimpleName()
                    + "' annotation"
            );
        }

        static CriteriaMappingException fieldNotBeanProperty(Field field) {
            return new CriteriaMappingException(
                "Field '" + field.getName() + "' from class '" + field.getDeclaringClass().getSimpleName() +
                    "' is not a valid java bean property (probably due to incorrect getter or setter definition)."
            );
        }

        static <C> CriteriaMappingException missingDocumentClassAnnotation(Class<C> criteriaClass) {
            return new CriteriaMappingException("Criteria '" + criteriaClass + "' should be annotated with '@"
                + CriteriaForDocument.class.getSimpleName() + "' annotation");
        }
    }
}
