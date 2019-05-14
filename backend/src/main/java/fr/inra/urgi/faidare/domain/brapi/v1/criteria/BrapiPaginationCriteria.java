package fr.inra.urgi.faidare.domain.brapi.v1.criteria;

/**
 * @author gcornut
 */
public interface BrapiPaginationCriteria {

    Long DEFAULT_PAGE = 0L;
    Long DEFAULT_PAGE_SIZE = 100L;

    Long getPage();

    Long getPageSize();

}
