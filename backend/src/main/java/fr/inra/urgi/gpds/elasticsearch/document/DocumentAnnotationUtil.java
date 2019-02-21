package fr.inra.urgi.gpds.elasticsearch.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.ImmutableList;
import fr.inra.urgi.gpds.elasticsearch.document.annotation.Document;
import fr.inra.urgi.gpds.elasticsearch.document.annotation.Id;
import fr.inra.urgi.gpds.elasticsearch.document.annotation.Nested;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author gcornut
 */
public class DocumentAnnotationUtil {

    private static final Map<Class, DocumentMetadata> metadataCache = new HashMap<>();

    /**
     * Describes a value object document annotated with {@link Document}, {@link Id}
     * and {@link Nested} annotations
     */
    public static <VO> DocumentMetadata<VO> getDocumentObjectMetadata(Class<VO> valueObjectClass) {
        DocumentMetadata metadata = metadataCache.get(valueObjectClass);
        if (metadata == null) {
            Document document = valueObjectClass.getAnnotation(Document.class);
            String valueObjectName = valueObjectClass.getSimpleName();

            if (document == null) {
                throw new ValueObjectMetadataException(
                    "Could not find @Document annotation on value object '" + valueObjectName + "'");
            }
            String documentType = document.type();

            String idFieldName = null;
            for (Field field : valueObjectClass.getDeclaredFields()) {
                String fieldName = field.getName();

                Id id = field.getAnnotation(Id.class);
                if (id != null) {
                    if (id.jsonName().isEmpty()) {
                        idFieldName = fieldName;
                    } else {
                        idFieldName = id.jsonName();
                    }
                }
            }

            Map<String, DocumentMetadata.Field> fields = findDocumentFields(ImmutableList.<String>of(),
                valueObjectClass);

            String[] excludedFields = document.excludedFields();
            metadata = new DocumentMetadata<>(documentType, idFieldName, valueObjectClass, excludedFields, fields);
            metadataCache.put(valueObjectClass, metadata);
        }
        return metadata;
    }

    private static Set<String> getAllField(Class<?> clazz) {
        Set<String> fields = new HashSet<>();
        for (PropertyDescriptor property : BeanUtils.getPropertyDescriptors(clazz)) {
            if (property.getName().equals("class")) {
                // Ignore java class property
                continue;
            }
            fields.add(property.getName());
        }
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isSynthetic()) {
                continue;
            }
            fields.add(field.getName());
        }
        return fields;
    }

    /**
     * Recursively find document fields
     */
    private static Map<String, DocumentMetadata.Field> findDocumentFields(List<String> parentPath, Class clazz) {
        Map<String, DocumentMetadata.Field> fields = new HashMap<>();

        for (String fieldName : getAllField(clazz)) {
            Class<?> fieldClass = null;
            boolean isNestedObject = false;
            boolean isObject = false;
            JsonDeserialize deserialize = null;
            String jsonName = null;

            // Get annotation from Java Field
            try {
                Field field = clazz.getDeclaredField(fieldName);
                fieldClass = field.getType();

                deserialize = field.getAnnotation(JsonDeserialize.class);
                if (deserialize != null && !deserialize.as().equals(Void.class)) {
                    fieldClass = deserialize.as();
                }

                isNestedObject = field.getAnnotation(Nested.class) != null;
                isObject = isObject(fieldClass);

                JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
                if (jsonProperty != null) {
                    jsonName = jsonProperty.value();
                }
            } catch (NoSuchFieldException ignored) {
            }

            // Get annotations from Java bean getters
            PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(clazz, fieldName);
            if (descriptor != null) {
                fieldClass = descriptor.getPropertyType();

                Method getter = descriptor.getReadMethod();
                if (getter != null) {
                    Class<?> returnType = getter.getReturnType();
                    isObject = isObject(returnType);
                    isNestedObject = isNestedObject || getter.getAnnotation(Nested.class) != null;

                    if (deserialize == null) {
                        deserialize = getter.getAnnotation(JsonDeserialize.class);
                    }

                    if (jsonName == null) {
                        JsonProperty jsonProperty = recursivelyGetAnnotation(getter, JsonProperty.class);
                        if (jsonProperty != null) {
                            jsonName = jsonProperty.value();
                        }
                    }
                }
            }

            boolean isCollection = fieldClass != null && Collection.class.isAssignableFrom(fieldClass);
            if (isCollection) {
                if (deserialize != null && !deserialize.contentAs().equals(Void.class)) {
                    fieldClass = deserialize.contentAs();
                    isObject = isObject(fieldClass);
                } else if (isObject || isNestedObject) {
                    throw new ValueObjectMetadataException("Could not determine content type for collection field "
                        + "'" + fieldName + "' in class '" + clazz + "'. "
                        + "Please use the `@JsonDeserialize(contentAs= ...)` annotation on this field to set the "
                        + "content class.");
                }
            }

            List<String> path = ImmutableList.<String>builder().addAll(parentPath).add(fieldName).build();

            if (jsonName == null) {
                jsonName = fieldName;
            }
            List<String> jsonPath = ImmutableList.<String>builder().addAll(parentPath).add(jsonName).build();

            Map<String, DocumentMetadata.Field> subFields = null;
            if (isObject || isNestedObject) {
                subFields = findDocumentFields(path, fieldClass);
            }

            DocumentMetadata.Field field = new DocumentMetadata.Field(
                path, jsonPath, fieldClass, isNestedObject, descriptor, subFields);

            // Index using java name
            fields.put(fieldName, field);

            // Index using JSON name
            fields.put(jsonName, field);
        }
        return fields;
    }

    private static boolean isObject(Class<?> returnType) {
        return !returnType.getName().startsWith("java.lang") &&
            !returnType.getName().startsWith("java.util");
    }

    /**
     * Recursively search annotation on a method or on the parent class/interface
     */
    private static <A extends Annotation> A recursivelyGetAnnotation(Method method, Class<A> annotationClass) {
        A annotation = method.getAnnotation(annotationClass);
        if (annotation != null) {
            return annotation;
        }

        Class<?> parentClass = method.getDeclaringClass();
        String name = method.getName();
        Class<?>[] param = method.getParameterTypes();

        Method searchedMethod;

        // Search in parent interface
        for (Class<?> parentInterface : parentClass.getInterfaces()) {
            try {
                searchedMethod = parentInterface.getMethod(name, param);
                return recursivelyGetAnnotation(searchedMethod, annotationClass);
            } catch (NoSuchMethodException ignored) {
            }
        }

        // Search in parent classes
        for (; ; ) {
            parentClass = parentClass.getSuperclass();
            if (parentClass == null) {
                break;
            }
            try {
                searchedMethod = parentClass.getMethod(name, param);
                return recursivelyGetAnnotation(searchedMethod, annotationClass);
            } catch (NoSuchMethodException ignored) {
            }
        }

        return null;
    }

    public static class ValueObjectMetadataException extends RuntimeException {
        ValueObjectMetadataException(String message) {
            super(message);
        }
    }
}
