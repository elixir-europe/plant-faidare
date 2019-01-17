package fr.inra.urgi.gpds.elasticsearch.criteria.fixture;

import fr.inra.urgi.gpds.elasticsearch.criteria.annotation.CriteriaForDocument;

/**
 * @author gcornut
 */
@CriteriaForDocument(DocumentObject.class)
public class IncorrectFieldAnnotationCriteria {

	// Missing @DocumentPath annotation here:
	String criteria1;

	public String getCriteria1() {
		return criteria1;
	}

	public void setCriteria1(String criteria1) {
		this.criteria1 = criteria1;
	}
}
