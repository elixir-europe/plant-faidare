package fr.inra.urgi.faidare.elasticsearch.criteria.fixture;

import fr.inra.urgi.faidare.elasticsearch.criteria.annotation.CriteriaForDocument;
import fr.inra.urgi.faidare.elasticsearch.criteria.annotation.DocumentPath;
import fr.inra.urgi.faidare.elasticsearch.criteria.annotation.QueryType;
import org.elasticsearch.index.query.RangeQueryBuilder;

import java.util.List;

/**
 * @author gcornut
 */
@CriteriaForDocument(DocumentObject.class)
public class ComplexAnnotatedCriteria {

    @DocumentPath("field1")
    @QueryType(RangeQueryBuilder.class)
    List<String> criteria1;

    @DocumentPath({"field2", "field3"})
    String criteria2;

    @DocumentPath(value = {"field2", "field4"}, virtualField = "subField")
    String criteria3;

    @DocumentPath({"field2", "identifier"})
    String criteria4;

    @DocumentPath({"field2", "name"})
    String criteria5;

    public String getCriteria2() {
        return criteria2;
    }

    public List<String> getCriteria1() {
        return criteria1;
    }

    public void setCriteria1(List<String> criteria1) {
        this.criteria1 = criteria1;
    }

    public void setCriteria2(String criteria2) {
        this.criteria2 = criteria2;
    }

    public String getCriteria3() {
        return criteria3;
    }

    public void setCriteria3(String criteria3) {
        this.criteria3 = criteria3;
    }

    public String getCriteria4() {
        return criteria4;
    }

    public void setCriteria4(String criteria4) {
        this.criteria4 = criteria4;
    }

    public String getCriteria5() {
        return criteria5;
    }

    public void setCriteria5(String criteria5) {
        this.criteria5 = criteria5;
    }
}
