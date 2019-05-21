package fr.inra.urgi.faidare.elasticsearch.criteria.fixture;

import fr.inra.urgi.faidare.elasticsearch.criteria.annotation.CriteriaForDocument;
import fr.inra.urgi.faidare.elasticsearch.criteria.annotation.DocumentPath;

/**
 * Criteria missing the {@link CriteriaForDocument} annotation
 *
 * @author gcornut
 */
public class IncorrectClassAnnotationCriteria {

    @DocumentPath({"field2"})
    String criteria1;

    public String getCriteria1() {
        return criteria1;
    }

    public void setCriteria1(String criteria1) {
        this.criteria1 = criteria1;
    }
}
