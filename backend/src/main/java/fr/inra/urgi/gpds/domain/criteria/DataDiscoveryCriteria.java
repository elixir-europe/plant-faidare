package fr.inra.urgi.gpds.domain.criteria;

import fr.inra.urgi.gpds.domain.criteria.base.PaginationCriteria;
import fr.inra.urgi.gpds.domain.criteria.base.SortCriteria;

import java.util.List;

/**
 * @author gcornut
 *
 *
 */
public interface DataDiscoveryCriteria extends PaginationCriteria, SortCriteria {

	List<String> getCrops();

	List<String> getGermplasmLists();

	List<String> getAccessions();

	List<String> getObservationVariableIds();

	List<String> getSources();

	List<String> getTypes();

	List<String> getFacetFields();

	@Override
	Long getPage();

	@Override
	Long getPageSize();

	@Override
	String getSortBy();

	@Override
	String getSortOrder();

}
