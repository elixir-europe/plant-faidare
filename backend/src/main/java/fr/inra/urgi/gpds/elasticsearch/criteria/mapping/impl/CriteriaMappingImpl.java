package fr.inra.urgi.gpds.elasticsearch.criteria.mapping.impl;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import fr.inra.urgi.gpds.elasticsearch.criteria.mapping.CriteriaMapping;
import fr.inra.urgi.gpds.elasticsearch.criteria.mapping.CriteriaMappingCriterion;
import fr.inra.urgi.gpds.elasticsearch.criteria.mapping.CriteriaMappingTree;
import fr.inra.urgi.gpds.elasticsearch.criteria.mapping.CriteriaMappingTreeNode;
import fr.inra.urgi.gpds.elasticsearch.document.DocumentMetadata;

import java.util.*;

/**
 * See {@link CriteriaMapping} docs
 *
 * @author gcornut
 */
public class CriteriaMappingImpl implements CriteriaMapping {

    private final Class<?> criteriaClass;
    private final Map<List<String>, CriteriaMappingTree> flattenTreeByPath;
    private final Map<List<String>, CriteriaMappingTree> mappingSubTree;
    private final DocumentMetadata<?> documentMetadata;

    public CriteriaMappingImpl(Class<?> criteriaClass, Map<List<String>, CriteriaMappingTree> mappingSubTree, DocumentMetadata<?> documentMetadata) {
        this.criteriaClass = criteriaClass;
        this.mappingSubTree = mappingSubTree;
        this.flattenTreeByPath = flattenTree(null, mappingSubTree.values());
        this.documentMetadata = documentMetadata;
    }

    /**
     * Walk the recursive tree to produce a flatten representation
     */
    private static Map<List<String>, CriteriaMappingTree> flattenTree(CriteriaMappingTree parent,
                                                                      Collection<CriteriaMappingTree> nodes) {
        Map<List<String>, CriteriaMappingTree> flattenTree = new HashMap<>();
        for (CriteriaMappingTree tree : nodes) {
            flattenTree.put(tree.getDocumentFieldPath(), tree);
            if (tree instanceof CriteriaMappingTreeNode) {
                CriteriaMappingTreeNode node = (CriteriaMappingTreeNode) tree;
                flattenTree.putAll(flattenTree(node, node.getMappingTree().values()));
            } else if (tree instanceof CriteriaMappingCriterion) {
                CriteriaMappingCriterion criterion = (CriteriaMappingCriterion) tree;
                if (criterion.isVirtualField() && parent != null) {
                    flattenTree.put(parent.getDocumentFieldPath(), criterion);
                }
            }
        }
        return flattenTree;
    }

    /**
     * Copy input list with all its element except the last one
     */
    private static <T> List<T> allButLast(List<T> list) {
        ImmutableList.Builder<T> builder = ImmutableList.builder();
        for (int i = 0; i < list.size() - 1; i++) {
            builder.add(list.get(i));
        }
        return builder.build();
    }

    @Override
    public String getDocumentPath(String criterionName, boolean noVirtualField) {
        for (List<String> documentFieldPath : flattenTreeByPath.keySet()) {
            CriteriaMappingTree tree = flattenTreeByPath.get(documentFieldPath);

            if (tree instanceof CriteriaMappingCriterion) {
                CriteriaMappingCriterion criterion = (CriteriaMappingCriterion) tree;
                if (criterion.getName().equals(criterionName)) {
                    List<String> path = criterion.getDocumentFieldPath();
                    if (noVirtualField && criterion.isVirtualField()) {
                        // truncate path because the last part is the virtual field
                        path = allButLast(path);
                    }
                    return Joiner.on(".").join(path);
                }
            }
        }
        throw new RuntimeException("Could not find document field path for criteria named '" + criterionName + "' "
            + "in '" + criteriaClass.getSimpleName() + "'");
    }

    @Override
    public <N extends CriteriaMappingTree> N getMapping(Class<N> clazz, List<String> documentFieldPath) {
        CriteriaMappingTree node = flattenTreeByPath.get(documentFieldPath);
        try {
            return clazz.cast(node);
        } catch (ClassCastException | NullPointerException e) {
            throw new RuntimeException("Could not find criterion for document field path '"
                + Joiner.on(".").join(documentFieldPath) + "'");
        }
    }

    @Override
    public Map<List<String>, CriteriaMappingTree> getMappingTree() {
        return mappingSubTree;
    }

    @Override
    public List<String> getDocumentFieldPath() {
        return Collections.emptyList();
    }

    @Override
    public String getDocumentFieldName() {
        return null;
    }

    @Override
    public Class<?> getCriteriaClass() {
        return criteriaClass;
    }

    @Override
    public DocumentMetadata<?> getDocumentMetadata() {
        return documentMetadata;
    }
}
