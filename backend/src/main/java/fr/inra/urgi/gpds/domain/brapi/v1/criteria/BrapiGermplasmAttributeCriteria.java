package fr.inra.urgi.gpds.domain.brapi.v1.criteria;

import java.util.List;

/**
 * @author gcornut
 *
 *
 */
public interface BrapiGermplasmAttributeCriteria {
	List<String> getAttributeList();

	String getGermplasmDbId();
}
