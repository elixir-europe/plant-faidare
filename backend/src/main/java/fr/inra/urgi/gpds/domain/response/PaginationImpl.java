package fr.inra.urgi.gpds.domain.response;

import fr.inra.urgi.gpds.domain.criteria.base.PaginationCriteria;

/**
 * @author gcornut
 *
 *
 */
public class PaginationImpl implements Pagination {

	private final long pageSize;
	private final long currentPage;
	private final long totalCount;
	private final long totalPages;

	private PaginationImpl(long pageSize, long currentPage, long totalCount, long totalPages) {
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

	public static Pagination create(long pageSize, long currentPage, long totalCount) {
		long totalPages = (long) Math.ceil((double) totalCount / (double) pageSize);
		return new PaginationImpl(pageSize, currentPage, totalCount, totalPages);
	}

	public static <C extends PaginationCriteria> Pagination create(C criteria, Long totalCount) {
		return create(criteria.getPageSize(), criteria.getPage(), totalCount);
	}

}
