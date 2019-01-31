package fr.inra.urgi.gpds.elasticsearch.criteria.mapping;

import java.util.List;

/**
 * A criteria mapping from document field tree to criterion field (from criteria
 * object)
 *
 * @author gcornut
 */
public interface CriteriaMappingTree {

    /**
     * Get the full JSON path for the document field this criterion maps to
     */
    List<String> getDocumentFieldPath();

    /**
     * Get the document field name
     */
    String getDocumentFieldName();

}
