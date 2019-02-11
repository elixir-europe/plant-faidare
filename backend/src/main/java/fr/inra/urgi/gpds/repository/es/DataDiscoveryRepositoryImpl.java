package fr.inra.urgi.gpds.repository.es;

import fr.inra.urgi.gpds.api.gnpis.v1.DataDiscoveryController;
import fr.inra.urgi.gpds.domain.datadiscovery.criteria.DataDiscoveryCriteria;
import fr.inra.urgi.gpds.domain.datadiscovery.criteria.DataDiscoveryCriteriaImpl;
import fr.inra.urgi.gpds.domain.datadiscovery.data.DataDiscoveryDocument;
import fr.inra.urgi.gpds.domain.datadiscovery.data.FacetImpl;
import fr.inra.urgi.gpds.domain.datadiscovery.data.FacetTermImpl;
import fr.inra.urgi.gpds.domain.datadiscovery.response.DataDiscoveryResponse;
import fr.inra.urgi.gpds.domain.response.ApiResponseFactory;
import fr.inra.urgi.gpds.domain.response.Pagination;
import fr.inra.urgi.gpds.domain.response.PaginationImpl;
import fr.inra.urgi.gpds.elasticsearch.ESRequestFactory;
import fr.inra.urgi.gpds.elasticsearch.ESResponseParser;
import fr.inra.urgi.gpds.elasticsearch.criteria.AnnotatedCriteriaMapper;
import fr.inra.urgi.gpds.elasticsearch.criteria.mapping.CriteriaMapping;
import fr.inra.urgi.gpds.elasticsearch.document.DocumentAnnotationUtil;
import fr.inra.urgi.gpds.elasticsearch.document.DocumentMetadata;
import fr.inra.urgi.gpds.elasticsearch.query.impl.ESGenericQueryFactory;
import fr.inra.urgi.gpds.elasticsearch.repository.ESSuggestRepository;
import fr.inra.urgi.gpds.elasticsearch.repository.impl.ESGenericFindRepository;
import fr.inra.urgi.gpds.elasticsearch.repository.impl.ESGenericSuggestRepository;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import static org.elasticsearch.search.aggregations.AggregationBuilders.filter;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;

/**
 * @author gcornut
 */
@Repository
public class DataDiscoveryRepositoryImpl implements DataDiscoveryRepository {

    private final static Logger LOGGER = LoggerFactory.getLogger(DataDiscoveryController.class);

    private final DocumentMetadata<DataDiscoveryDocument> documentMetadata;
    private final CriteriaMapping criteriaMapping;

    private final RestHighLevelClient client;
    private final ESRequestFactory requestFactory;
    private final ESSuggestRepository<DataDiscoveryCriteria> suggestRepository;
    private final ESGenericQueryFactory<DataDiscoveryCriteria> queryFactory;
    private final ESResponseParser parser;

    @Autowired
    public DataDiscoveryRepositoryImpl(
        RestHighLevelClient client,
        ESRequestFactory requestFactory,
        ESResponseParser parser
    ) {
        this.client = client;
        this.parser = parser;
        Class<DataDiscoveryDocument> documentClass = DataDiscoveryDocument.class;
        Class<DataDiscoveryCriteriaImpl> criteriaClass = DataDiscoveryCriteriaImpl.class;

        this.requestFactory = requestFactory;
        this.documentMetadata = DocumentAnnotationUtil.getDocumentObjectMetadata(documentClass);

        this.criteriaMapping = AnnotatedCriteriaMapper.getMapping(criteriaClass);
        this.queryFactory = new ESGenericQueryFactory<>();
        this.suggestRepository = new ESGenericSuggestRepository<>(client, requestFactory, documentClass, queryFactory, parser);
    }

    @Override
    public LinkedHashSet<String> suggest(
        String criteriaField, String searchText, Long fetchSize, DataDiscoveryCriteria criteria
    ) {
        String documentFieldPath = criteriaMapping.getDocumentPath(criteriaField, true);
        return suggestRepository.suggest(documentFieldPath, searchText, fetchSize, criteria);
    }

    @Override
    public DataDiscoveryResponse find(DataDiscoveryCriteria criteria) {
        try {
            // Prepare search request
            SearchRequest request = prepareSearchRequest(criteria);

            // Execute request
            LOGGER.debug(request.toString());
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);

            // Parse request result
            return parseResponse(criteria, response);
        } catch (Exception e) {
            throw new DataDiscoveryRepositoryException(e);
        }
    }

    /**
     * Generate Elasticsearch search request based on the data discovery criteria
     */
    private SearchRequest prepareSearchRequest(DataDiscoveryCriteria criteria) {
        List<String> facetFields = criteria.getFacetFields();
        String[] documentFieldsInFacets = criteriaFieldsToDocumentFields(facetFields);
        int size = criteria.getPageSize().intValue();
        int from = criteria.getPage().intValue() * size;

        // Build search query (excluding document fields used in facets)
        QueryBuilder query = queryFactory.createQueryExcludingFields(criteria, documentFieldsInFacets);

        // Prepare search request with query
        SearchRequest request = ESGenericFindRepository.prepareSearchRequest(
            query, criteria, documentMetadata, requestFactory);
        request.source(new SearchSourceBuilder());
        request.source()
            .from(from)
            .size(size);
        request.source().query(query);

        // Build facet aggregations
        if (facetFields != null) {
            for (String facetField : facetFields) {
                String documentPath = criteriaMapping.getDocumentPath(facetField, true);

                String filterAggName = facetField + "Filter";

                // Create facet term agg
                TermsAggregationBuilder termAgg = terms(facetField)
                    .field(documentPath)
                    .size(ESRequestFactory.MAX_TERM_AGG_SIZE);

                // Create filter agg for this facet excluding it self
                QueryBuilder facetFilter = queryFactory.createQueryExcludingFields(criteria, documentPath);
                if (facetFilter == null) {
                    facetFilter = QueryBuilders.matchAllQuery();
                }

                request.source().aggregation(
                    filter(filterAggName, facetFilter).subAggregation(termAgg)
                );
            }
        }

        // Build post filter (including document fields used in facets)
        QueryBuilder postFilter = queryFactory.createQueryIncludingFields(criteria, documentFieldsInFacets);
        request.source().postFilter(postFilter);

        return request;
    }

    /**
     * Parse Elasticsearch search response into data discovery response
     */
    private DataDiscoveryResponse parseResponse(
        DataDiscoveryCriteria criteria, SearchResponse response
    ) throws IOException, ReflectiveOperationException {
        // Parse pagination
        Pagination pagination = PaginationImpl.create(criteria, parser.parseTotalHits(response));

        // Parse result list
        List<DataDiscoveryDocument> resultList = parser.parseHits(response,
            documentMetadata.getDocumentClass());

        // Parse facet terms
        List<String> facetFields = criteria.getFacetFields();
        List<FacetImpl> facets = null;
        if (facetFields != null) {
            facets = new ArrayList<>();
            for (String facetField : facetFields) {
                String filterAggName = facetField + "Filter";
                List<FacetTermImpl> terms = parser.parseFacetTerms(
                    response, filterAggName, facetField
                );
                facets.add(new FacetImpl(facetField, terms));
            }
        }

        // Return paginated list
        return ApiResponseFactory.createListResponseWithFacets(pagination, resultList, facets);
    }

    private String[] criteriaFieldsToDocumentFields(List<String> criteriaFields) {
        List<String> fields = new ArrayList<>();
        if (criteriaFields != null) {
            for (String facetField : criteriaFields) {
                fields.add(criteriaMapping.getDocumentPath(facetField, true));
            }
        }
        return fields.toArray(new String[]{});
    }

    public static class DataDiscoveryRepositoryException extends RuntimeException {

        DataDiscoveryRepositoryException(Throwable t) {
            super(t);
        }
    }
}
