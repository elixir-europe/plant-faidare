package fr.inra.urgi.gpds.domain.brapi.v1.criteria;

import java.util.List;

/**
 * @author gcornut
 *
 *
 */
public interface BrapiGermplasmPOSTSearchCriteria extends BrapiPaginationCriteria {
	List<String> getGermplasmPUIs();

	List<String> getGermplasmDbIds();

	List<String> getGermplasmSpecies();

	List<String> getGermplasmGenus();

	List<String> getGermplasmNames();

	List<String> getAccessionNumbers();
}
