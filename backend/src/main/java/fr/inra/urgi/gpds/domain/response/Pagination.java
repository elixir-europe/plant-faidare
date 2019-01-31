package fr.inra.urgi.gpds.domain.response;

import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiPagination;

/**
 * @author gcornut
 */
public interface Pagination extends BrapiPagination {

    @Override
    long getCurrentPage();

    @Override
    long getPageSize();

    @Override
    long getTotalCount();

    @Override
    long getTotalPages();
}
