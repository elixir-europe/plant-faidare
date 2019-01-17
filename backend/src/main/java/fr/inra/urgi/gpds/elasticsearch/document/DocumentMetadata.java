package fr.inra.urgi.gpds.elasticsearch.document;

import com.google.common.collect.ImmutableList;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Metadata about an Elasticsearch document
 *
 * @author gcornut
 *
 *
 */
public class DocumentMetadata<VO> {

	private final String documentType;
	private final String idField;
	private final Class<VO> documentClass;
	private final String[] excludedFields;
	private final Map<String, Field> fieldsByName;
	private final Map<List<String>, Field> fieldByPath;

	public DocumentMetadata(String documentType, String idField, Class<VO> documentClass, String[] excludedFields,
			Map<String, Field> fieldsByName) {
		this.documentType = documentType;
		this.idField = idField;
		this.documentClass = documentClass;
		this.excludedFields = excludedFields;
		this.fieldsByName = fieldsByName;
		this.fieldByPath = flattenDocumentFieldTree(ImmutableList.<String>of(),  fieldsByName);
	}

	public String getDocumentType() {
		return documentType;
	}

	/**
	 * Flatten the document field paths into a single map indexed by document field path (java path or JSON path)
	 */
	private static Map<List<String>, Field> flattenDocumentFieldTree(List<String> path, Map<String, Field> values) {
		Map<List<String>, Field> fieldByPath = new HashMap<>();

		for (String name : values.keySet()) {
			Field field = values.get(name);
			List<String> newPath = ImmutableList.<String>builder().addAll(path).add(name).build();
			fieldByPath.put(newPath, field);
			Map<String, Field> fieldsByName = field.fieldsByName;
			if (fieldsByName != null) {
				fieldByPath.putAll(flattenDocumentFieldTree(newPath, fieldsByName));
			}
		}
		return fieldByPath;
	}

	public String getIdField() {
		return idField;
	}

	public String[] getExcludedFields() {
		return excludedFields;
	}

	public Class<VO> getDocumentClass() {
		return documentClass;
	}

	public Map<String, Field> getFieldsByName() {
		return fieldsByName;
	}

	public Field getByPath(List<String> documentFieldPath) {
		return fieldByPath.get(documentFieldPath);
	}

	public static class Field {
		private final boolean nestedObject;
		private final List<String> path;
		private final List<String> jsonPath;
		private final PropertyDescriptor descriptor;
		private final Map<String, Field> fieldsByName;
		private final Class<?> fieldClass;

		Field(List<String> path, List<String> jsonPath, Class<?> fieldClass, boolean nestedObject, PropertyDescriptor descriptor,
			  Map<String, Field> fieldsByName) {
			this.jsonPath = jsonPath;
			this.nestedObject = nestedObject;
			this.path = path;
			this.fieldClass = fieldClass;
			this.descriptor = descriptor;
			this.fieldsByName = fieldsByName;
		}

		public Class<?> getFieldClass() {
			return fieldClass;
		}

		public boolean isNestedObject() {
			return nestedObject;
		}

		public List<String> getPath() {
			return path;
		}

		public List<String> getJsonPath() {
			return jsonPath;
		}

		public PropertyDescriptor getDescriptor() {
			return descriptor;
		}
	}
}
