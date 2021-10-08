package fr.inra.urgi.faidare.domain.xref;

/**
 * Imported and adapted from unified-interface legacy
 */
public enum DocumentFields {

	ENTRY_TYPE("entryType"), LINKED_RESOURCES_ID("linkedResourcesID");

	private String field;

	DocumentFields(String field) {
		this.field = field;
	}

	public static DocumentFields from(String value) {
		try {
			for (DocumentFields field : DocumentFields.values()) {
				if (field.toString().equalsIgnoreCase(value)) {
					return field;
				}
			}
		} catch (Exception e) {
			throw new DocumentFieldsException(
					"Cannot create a " + DocumentFields.class.getName() + " because of: " + e.getMessage() + "!", e);
		}
		throw new DocumentFieldsException(
				"Cannot create a " + DocumentFields.class.getName() + " using value: " + value + "!");
	}

	@Override
	public String toString() {
		return field;
	}
}
