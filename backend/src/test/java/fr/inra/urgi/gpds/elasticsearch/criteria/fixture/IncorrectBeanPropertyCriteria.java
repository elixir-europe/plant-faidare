package fr.inra.urgi.gpds.elasticsearch.criteria.fixture;

import fr.inra.urgi.gpds.elasticsearch.criteria.annotation.CriteriaForDocument;
import fr.inra.urgi.gpds.elasticsearch.criteria.annotation.DocumentPath;

/**
 * @author gcornut
 */
@CriteriaForDocument(DocumentObject.class)
public class IncorrectBeanPropertyCriteria {

    // Missing getter & setter here:
    @DocumentPath("field1")
    String criteria1;

}
