package fr.inra.urgi.faidare.elasticsearch.criteria.fixture;

import fr.inra.urgi.faidare.elasticsearch.criteria.annotation.CriteriaForDocument;
import fr.inra.urgi.faidare.elasticsearch.criteria.annotation.DocumentPath;

/**
 * @author gcornut
 */
@CriteriaForDocument(DocumentObject.class)
public class IncorrectDocumentPathCriteria {

    // This document path does not exist in document
    @DocumentPath({"field2", "foo"})
    String criteria1;

    public String getCriteria1() {
        return criteria1;
    }

    public void setCriteria1(String criteria1) {
        this.criteria1 = criteria1;
    }

}
