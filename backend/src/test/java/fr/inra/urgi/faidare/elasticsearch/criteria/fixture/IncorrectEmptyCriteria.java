package fr.inra.urgi.faidare.elasticsearch.criteria.fixture;

import fr.inra.urgi.faidare.elasticsearch.criteria.annotation.CriteriaForDocument;
import fr.inra.urgi.faidare.elasticsearch.criteria.annotation.NoDocumentMapping;

/**
 * @author gcornut
 */
@CriteriaForDocument(DocumentObject.class)
public class IncorrectEmptyCriteria {

    @NoDocumentMapping
    String hiddenCriteria;

}
