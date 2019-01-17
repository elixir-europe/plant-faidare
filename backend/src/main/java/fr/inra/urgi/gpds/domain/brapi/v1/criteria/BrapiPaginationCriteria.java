package fr.inra.urgi.gpds.domain.brapi.v1.criteria;

/**
 * @author gcornut
 *
 *
 */
public interface BrapiPaginationCriteria {

	Long DEFAULT_PAGE = 0L;
	Long DEFAULT_PAGE_SIZE = 10L;

	Long getPage();

	Long getPageSize();

}
