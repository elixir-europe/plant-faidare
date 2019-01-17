package fr.inra.urgi.gpds.domain.criteria.base;

import fr.inra.urgi.gpds.domain.brapi.v1.criteria.BrapiPaginationCriteria;

/**
 * @author gcornut
 *
 *
 */
public interface PaginationCriteria extends BrapiPaginationCriteria {

	@Override
	Long getPage();

	@Override
	Long getPageSize();

}
