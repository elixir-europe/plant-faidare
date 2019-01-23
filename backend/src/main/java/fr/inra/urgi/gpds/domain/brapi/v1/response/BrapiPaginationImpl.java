package fr.inra.urgi.gpds.domain.brapi.v1.response;

/**
 * @author gcornut
 */
class BrapiPaginationImpl implements BrapiPagination {

    private final long pageSize;
    private final long currentPage;
    private final long totalCount;
    private final long totalPages;

    BrapiPaginationImpl(long pageSize, long currentPage, long totalCount, long totalPages) {
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.totalCount = totalCount;
        this.totalPages = totalPages;
    }

    @Override
    public long getPageSize() {
        return pageSize;
    }

    @Override
    public long getCurrentPage() {
        return currentPage;
    }

    @Override
    public long getTotalCount() {
        return totalCount;
    }

    @Override
    public long getTotalPages() {
        return totalPages;
    }

}
