package fr.inra.urgi.faidare.elasticsearch.repository.impl;

import fr.inra.urgi.faidare.domain.criteria.base.PaginationCriteria;
import fr.inra.urgi.faidare.domain.criteria.base.SortCriteria;
import fr.inra.urgi.faidare.domain.response.PaginatedList;
import fr.inra.urgi.faidare.domain.response.Pagination;
import fr.inra.urgi.faidare.domain.response.PaginationImpl;
import fr.inra.urgi.faidare.elasticsearch.ESRequestFactory;
import fr.inra.urgi.faidare.elasticsearch.ESResponseParser;
import fr.inra.urgi.faidare.elasticsearch.document.DocumentAnnotationUtil;
import fr.inra.urgi.faidare.elasticsearch.document.DocumentMetadata;
import fr.inra.urgi.faidare.elasticsearch.query.ESQueryFactory;
import fr.inra.urgi.faidare.elasticsearch.query.impl.ESGenericQueryFactory;
import fr.inra.urgi.faidare.elasticsearch.repository.ESFindRepository;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Generic implementation of an ElasticSearch document repository which can find documents by criteria.
 *
 * @author gcornut
 */
public class ESGenericFindRepository<C extends PaginationCriteria, VO> implements ESFindRepository<C, VO> {

    private final RestHighLevelClient client;
    private final ESRequestFactory requestFactory;
    private final ESQueryFactory<C> queryFactory;
    private final DocumentMetadata<VO> documentMetadata;
    private final ESResponseParser parser;

    public ESGenericFindRepository(
        RestHighLevelClient client,
        ESRequestFactory requestFactory,
        Class<VO> voClass,
        ESQueryFactory<C> queryFactory,
        ESResponseParser parser
    ) {
        this.client = client;
        this.requestFactory = requestFactory;
        this.queryFactory = queryFactory;
        this.documentMetadata = DocumentAnnotationUtil.getDocumentObjectMetadata(voClass);
        this.parser = parser;
    }

    public ESGenericFindRepository(
        RestHighLevelClient client,
        ESRequestFactory requestFactory,
        Class<VO> voClass,
        ESResponseParser parser
    ) {
        this(client, requestFactory, voClass, new ESGenericQueryFactory<>(), parser);
    }

    public static <C extends PaginationCriteria, D> SearchRequest prepareSearchRequest(
        QueryBuilder query, C criteria, DocumentMetadata<D> documentMetadata, ESRequestFactory requestFactory
    ) {
        // Build query
        int size = criteria.getPageSize().intValue();
        int from = criteria.getPage().intValue() * size;

        // Build request
        String documentType = documentMetadata.getDocumentType();
        SearchRequest request = requestFactory.prepareSearch(documentType, query);
        request.source()
            .from(from)
            .size(size);

        // Add sort if requested
        if (criteria instanceof SortCriteria) {
            SortCriteria sortCriteria = (SortCriteria) criteria;
            String field = sortCriteria.getSortBy();
            if (field == null) {
                // Default ES sort
                field = "_doc";
            }

            String strOrder = sortCriteria.getSortOrder();
            SortOrder order = SortOrder.ASC;
            if (strOrder != null) {
                order = SortOrder.valueOf(strOrder.toUpperCase());
            }
            request.source().sort(field, order);
        }

        // Add excluded fields if requested
        String[] excludedFields = documentMetadata.getExcludedFields();
        if (excludedFields != null && excludedFields.length >= 1) {
            request.source().fetchSource(null, excludedFields);
        }

        Logger logger = LoggerFactory.getLogger(ESGenericFindRepository.class);
        if (logger.isDebugEnabled()) {
            logger.debug(request.toString());
        }
        return request;
    }

    @Override
    public PaginatedList<VO> find(C criteria) {
        try {
            QueryBuilder query = queryFactory.createQuery(criteria);

            SearchRequest request = prepareSearchRequest(
                query, criteria, documentMetadata, requestFactory
            );

            // Execute request
            SearchResponse result = client.search(request, RequestOptions.DEFAULT);

            // Prepare pagination info
            Pagination pagination = PaginationImpl.create(criteria, parser.parseTotalHits(result));

            // Parse result list
            List<VO> resultList = parser.parseHits(result, documentMetadata.getDocumentClass());

            // Return paginated list
            return new PaginatedList<>(pagination, resultList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
