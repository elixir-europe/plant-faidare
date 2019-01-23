package fr.inra.urgi.gpds.domain.brapi.v1.response;

import fr.inra.urgi.gpds.api.brapi.v1.exception.BrapiPaginationException;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Factory creating BrAPI responses
 *
 * @author gcornut
 */
public class BrapiResponseFactory {

    public static <T> BrapiData<T> createData(List<T> data) {
        return new BrapiDataImpl<>(data);
    }

    public static BrapiMetadata createMetadata(BrapiPagination pagination, List<BrapiStatus> status) {
        return new BrapiMetadataImpl(pagination, status, Collections.<String>emptyList());
    }

    public static BrapiPagination createPagination(long pageSize, long currentPage, long totalCount, long totalPages) {
        return new BrapiPaginationImpl(pageSize, currentPage, totalCount, totalPages);
    }

    public static BrapiPagination createPagination(long pageSize, long currentPage, long totalCount) {
        long totalPages = (long) Math.ceil((double) totalCount / (double) pageSize);
        return new BrapiPaginationImpl(pageSize, currentPage, totalCount, totalPages);
    }

    public static BrapiStatus createStatus(String name, String code) {
        return new BrapiStatusImpl(name, code);
    }

    public static BrapiStatus createStatus(Exception exception, HttpStatus httpStatus) {
        // Ex: Bad Request: Invalid page number
        String message = httpStatus.getReasonPhrase();
        if (exception.getMessage() != null) {
            message += ": " + exception.getMessage();
        }

        // Ex: 400
        String code = String.valueOf(httpStatus.value());

        return createStatus(message, code);
    }

    /**
     * Create a BrAPI response with only status (when an error occurred)
     */
    public static <T> BrapiResponse<T> createSingleObjectResponse(T result, List<BrapiStatus> status) {
        BrapiMetadata metadata = createMetadata(null, status);
        return new BrapiResponseImpl<>(metadata, result);
    }

    /**
     * Create a BrAPI list response with pagination and error statuses (with or
     * without pagination checking)
     *
     * @param doPaginationChecks whether or not this method should check the pagination for inconsistencies
     */
    public static <T> BrapiListResponse<T> createListResponse(
        BrapiPagination pagination, List<BrapiStatus> status, List<T> results, boolean doPaginationChecks
    ) {
        if (doPaginationChecks && pagination != null) {
            if (results != null && results.size() > pagination.getPageSize()) {
                throw new BrapiPaginationException("The result size is greater than the pageSize", pagination);
            }
            if (pagination.getTotalPages() > 0 && pagination.getCurrentPage() >= pagination.getTotalPages()) {
                throw new BrapiPaginationException(
                    "The current page should be strictly less than the total number of pages.",
                    pagination
                );
            }
        }

        BrapiData<T> result = null;
        if (results != null) {
            result = createData(results);
        }
        BrapiMetadata metadata = createMetadata(pagination, status);
        return new BrapiListResponseImpl<>(metadata, result);
    }

    /**
     * Create a BrAPI list response with pagination and error statuses (with
     * pagination checking)
     */
    public static <T> BrapiListResponse<T> createListResponse(BrapiPagination pagination, List<BrapiStatus> status,
                                                              List<T> results) {
        return createListResponse(pagination, status, results, true);
    }

    /**
     * Create a BrAPI list response from a non paginated list and pagination
     * info (page & pageSize)
     * <p>
     * This method will generate a sub list of the given result list to match
     * the given pagination info
     */
    public static <T> BrapiListResponse<T> createSubListResponse(long pageSize, long page, List<T> bigList) {
        long totalCount = bigList.size();

        BrapiPagination pagination = createPagination(pageSize, page, totalCount);
        long totalPages = pagination.getTotalPages();

        // Sub list with page & pageSize
        List<T> partData;
        if (page >= totalPages) {
            partData = new ArrayList<>();
        } else {
            long start = page * pageSize;
            long end = Math.min(totalCount, start + pageSize);
            partData = bigList.subList((int) start, (int) end);
        }

        return createListResponse(pagination, null, partData, true);
    }
}
