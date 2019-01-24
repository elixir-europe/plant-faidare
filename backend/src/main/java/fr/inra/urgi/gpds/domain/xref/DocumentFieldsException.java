package fr.inra.urgi.gpds.domain.xref;


/**
 * Imported and adapted from unified-interface legacy
 */
class DocumentFieldsException extends RuntimeException{

	DocumentFieldsException(String message, Exception e) {
        super(message, e);
    }

    DocumentFieldsException(String message) {
        super(message);
    }

}
