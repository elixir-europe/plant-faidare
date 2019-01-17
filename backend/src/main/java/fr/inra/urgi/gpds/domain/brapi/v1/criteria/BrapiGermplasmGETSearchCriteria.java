package fr.inra.urgi.gpds.domain.brapi.v1.criteria;

import java.util.List;

/**
 * @author gcornut
 *
 *
 */
public interface BrapiGermplasmGETSearchCriteria extends BrapiPaginationCriteria {
	List<String> getGermplasmPUI();

	List<String> getGermplasmDbId();

	List<String> getGermplasmName();
}
