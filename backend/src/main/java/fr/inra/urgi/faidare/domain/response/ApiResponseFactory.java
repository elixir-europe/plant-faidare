package fr.inra.urgi.faidare.domain.response;

import fr.inra.urgi.faidare.api.brapi.v1.exception.BrapiPaginationException;
import fr.inra.urgi.faidare.domain.brapi.v1.response.*;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmVO;
import fr.inra.urgi.faidare.domain.datadiscovery.data.DataDiscoveryDocument;
import fr.inra.urgi.faidare.domain.datadiscovery.data.FacetImpl;
import fr.inra.urgi.faidare.domain.datadiscovery.response.DataDiscoveryResponse;
import fr.inra.urgi.faidare.domain.datadiscovery.response.GermplasmSearchResponse;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Factory creating API responses
 *
 * @author gcornut
 */
public class ApiResponseFactory {

    public static BrapiMetadata createMetadata(BrapiPagination pagination, List<BrapiStatus> status) {
        return new ApiMetadataImpl(pagination, status, Collections.emptyList());
    }

    public static BrapiStatus createStatus(String name, String code) {
        return new ApiStatusImpl(name, code);
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
        return new ApiResponseImpl<>(metadata, result);
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

        BrapiMetadata metadata = createMetadata(pagination, status);
        return new ApiListResponseImpl<>(metadata, results);
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

        BrapiPagination pagination = PaginationImpl.create(pageSize, page, totalCount);
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

    /**
     * Create data discovery response (brapi list response with facets)
     */
    public static DataDiscoveryResponse createListResponseWithFacets(Pagination pagination, List<DataDiscoveryDocument> results, List<FacetImpl> facets) {
        BrapiMetadata metadata = ApiResponseFactory.createMetadata(pagination, null);
        return new DataDiscoveryResponseImpl(metadata, results, facets);
    }

    /**
     * Create germplasm search response (brapi list response with facets)
     */
    public static GermplasmSearchResponse createGermplasmListResponseWithFacets(Pagination pagination, List<GermplasmVO> results, List<FacetImpl> facets) {
        BrapiMetadata metadata = ApiResponseFactory.createMetadata(pagination, null);
        return new GermplasmSearchResponseImpl(metadata, results, facets);
    }
}
