package fr.inra.urgi.faidare.domain.criteria.base;

import fr.inra.urgi.faidare.elasticsearch.criteria.annotation.NoDocumentMapping;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author gcornut
 */
@ValidPagination // global validation rule
public class PaginationCriteriaImpl implements PaginationCriteria {
    public static final long MIN_PAGE = 0;
    public static final long MIN_PAGE_SIZE = 1;
    public static final long MAX_PAGE_SIZE = 1000;

    public static final String ERROR_PAGE_NULL = "Page number cannot be null";
    public static final String ERROR_PAGE_SIZE_NULL = "Page size cannot be null";
    public static final String ERROR_PAGE_MIN = "Page number cannot be below " + MIN_PAGE;
    public static final String ERROR_PAGE_SIZE_MIN = "Page size cannot be below " + MIN_PAGE_SIZE;
    public static final String ERROR_PAGE_SIZE_MAX = "Page size cannot be above " + MAX_PAGE_SIZE;

    @NoDocumentMapping // No mapping to VO (used for pagination)
    @NotNull(message = ERROR_PAGE_NULL)
    @Min(value = MIN_PAGE, message = ERROR_PAGE_MIN)
    private Long page = DEFAULT_PAGE;

    @NoDocumentMapping // No mapping to VO (used for pagination)
    @NotNull(message = ERROR_PAGE_SIZE_NULL)
    @Min(value = MIN_PAGE_SIZE, message = ERROR_PAGE_SIZE_MIN)
    @Max(value = MAX_PAGE_SIZE, message = ERROR_PAGE_SIZE_MAX)
    private Long pageSize = DEFAULT_PAGE_SIZE;

    @Override
    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    @Override
    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

}
