package fr.inra.urgi.gpds.elasticsearch.criteria.fixture;

import fr.inra.urgi.gpds.elasticsearch.criteria.annotation.CriteriaForDocument;
import fr.inra.urgi.gpds.elasticsearch.criteria.annotation.NoDocumentMapping;

/**
 * @author gcornut
 */
@CriteriaForDocument(DocumentObject.class)
public class IncorrectEmptyCriteria {

    @NoDocumentMapping
    String hiddenCriteria;

}
