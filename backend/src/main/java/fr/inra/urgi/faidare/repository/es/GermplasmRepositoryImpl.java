package fr.inra.urgi.faidare.repository.es;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.inra.urgi.faidare.domain.criteria.FaidareGermplasmPOSTShearchCriteria;
import fr.inra.urgi.faidare.domain.criteria.GermplasmSearchCriteria;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmVO;
import fr.inra.urgi.faidare.domain.data.germplasm.PedigreeVO;
import fr.inra.urgi.faidare.domain.data.germplasm.ProgenyVO;
import fr.inra.urgi.faidare.domain.datadiscovery.data.FacetImpl;
import fr.inra.urgi.faidare.domain.datadiscovery.data.FacetTermImpl;
import fr.inra.urgi.faidare.domain.datadiscovery.response.GermplasmSearchResponse;
import fr.inra.urgi.faidare.domain.response.ApiResponseFactory;
import fr.inra.urgi.faidare.domain.response.PaginatedList;
import fr.inra.urgi.faidare.domain.response.Pagination;
import fr.inra.urgi.faidare.domain.response.PaginationImpl;
import fr.inra.urgi.faidare.elasticsearch.ESRequestFactory;
import fr.inra.urgi.faidare.elasticsearch.ESResponseParser;
import fr.inra.urgi.faidare.elasticsearch.ESScrollIterator;
import fr.inra.urgi.faidare.elasticsearch.criteria.AnnotatedCriteriaMapper;
import fr.inra.urgi.faidare.elasticsearch.criteria.mapping.CriteriaMapping;
import fr.inra.urgi.faidare.elasticsearch.document.DocumentAnnotationUtil;
import fr.inra.urgi.faidare.elasticsearch.document.DocumentMetadata;
import fr.inra.urgi.faidare.elasticsearch.query.impl.ESGenericQueryFactory;
import fr.inra.urgi.faidare.elasticsearch.repository.ESFindRepository;
import fr.inra.urgi.faidare.elasticsearch.repository.ESGetByIdRepository;
import fr.inra.urgi.faidare.elasticsearch.repository.impl.ESGenericFindRepository;
import fr.inra.urgi.faidare.elasticsearch.repository.impl.ESGenericGetByIdRepository;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.elasticsearch.search.aggregations.AggregationBuilders.filter;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;

@Repository
public class GermplasmRepositoryImpl implements GermplasmRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(GermplasmRepositoryImpl.class);

    private final DocumentMetadata<GermplasmVO> documentMetadata;
    private final CriteriaMapping criteriaMapping;

    private final RestHighLevelClient client;
    private final ObjectMapper mapper;
    private final ESRequestFactory requestFactory;
    private final ESResponseParser parser;

    private final ESFindRepository<GermplasmSearchCriteria, GermplasmVO> findRepository;
    private final ESGetByIdRepository<GermplasmVO> getByIdRepository;
    private final ESGenericQueryFactory<GermplasmSearchCriteria> queryFactory;

    @Autowired
    public GermplasmRepositoryImpl(
        RestHighLevelClient client,
        ObjectMapper mapper,
        ESRequestFactory requestFactory,
        ESResponseParser parser
    ) {
        this.client = client;
        this.requestFactory = requestFactory;
        this.mapper = mapper;
        this.parser = parser;
        Class<GermplasmVO> voClass = GermplasmVO.class;
        this.queryFactory = new ESGenericQueryFactory<>();
        this.findRepository = new ESGenericFindRepository<>(client, requestFactory, voClass, this.parser);
        this.getByIdRepository = new ESGenericGetByIdRepository<>(client, requestFactory, voClass, this.parser);
        Class<GermplasmVO> documentClass = GermplasmVO.class;
        Class<FaidareGermplasmPOSTShearchCriteria> criteriaClass = FaidareGermplasmPOSTShearchCriteria.class;
        this.documentMetadata = DocumentAnnotationUtil.getDocumentObjectMetadata(documentClass);
        this.criteriaMapping = AnnotatedCriteriaMapper.getMapping(criteriaClass);
    }

    @Override
    public Iterator<GermplasmVO> scrollAll(GermplasmSearchCriteria criteria) {
        QueryBuilder query = queryFactory.createQuery(criteria);
        int fetchSize = criteria.getPageSize().intValue();
        return new ESScrollIterator<>(client, requestFactory, parser, GermplasmVO.class, query, fetchSize);
    }

    @Override
    public Iterator<GermplasmVO> scrollAllGermplasm(FaidareGermplasmPOSTShearchCriteria criteria) {
        QueryBuilder query = queryFactory.createEsShouldQuery(criteria);
        int fetchSize = criteria.getPageSize().intValue();
        return new ESScrollIterator<>(client, requestFactory, parser, GermplasmVO.class, query, fetchSize);
    }

    @Override
    public GermplasmVO getById(String germplasmDbId) {
        return getByIdRepository.getById(germplasmDbId);
    }

    @Override
    public PaginatedList<GermplasmVO> find(GermplasmSearchCriteria criteria) {
        return findRepository.find(criteria);
    }

    @Override
    public GermplasmSearchResponse esShouldFind(FaidareGermplasmPOSTShearchCriteria germplasmSearchCriteria) {
        try {

            // Prepare search request
            SearchRequest request = prepareSearchRequest(germplasmSearchCriteria);

            // Execute request
            LOGGER.debug(request.toString());
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);

            // Return paginated list
            return parseResponse (germplasmSearchCriteria, response);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PedigreeVO findPedigree(String germplasmDbId) {
        QueryBuilder termQuery = QueryBuilders.termQuery("germplasmDbId", germplasmDbId);
        SearchRequest request = requestFactory.prepareSearch("germplasmPedigree", termQuery);
        SearchResponse response;
        try {
            response = client.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        PedigreeVO pedigreeVO = null;
        final SearchHits hits = response.getHits();

        LOGGER.debug("\n\nQuery from findPedigree :\n" + termQuery.toString() + "\n\n");

        if (hits.totalHits == 1) {
            // result found! \o/
            SearchHit hit = hits.getAt(0);
            String source = hit.getSourceAsString();
            try {
                pedigreeVO = mapper.readValue(source, PedigreeVO.class);
            } catch (IOException e) {
                LOGGER.error("Error occured when converting ES response to PedigreeVO: " + e.getMessage(), e);
            }
        } else if (hits.totalHits > 1) {
            throw new IllegalStateException("Expected only 1 result for pedigree with germplasmDbId: " + germplasmDbId);
        }
        return pedigreeVO;
    }

    @Override
    public ProgenyVO findProgeny(String germplasmDbId) {
        QueryBuilder termQuery = QueryBuilders.termQuery("germplasmDbId", germplasmDbId);
        SearchRequest request = requestFactory.prepareSearch("germplasmProgeny", termQuery);
        SearchResponse response;
        try {
            response = client.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ProgenyVO progenyVO = null;
        final SearchHits hits = response.getHits();
        LOGGER.debug("\n\nQuery from findProgeny :\n" + termQuery.toString() + "\n\n");

        if (hits.totalHits == 1) {
            SearchHit hit = hits.getAt(0);
            String source = hit.getSourceAsString();
            try {
                progenyVO = mapper.readValue(source, ProgenyVO.class);
            } catch (IOException e) {
                LOGGER.error("Error occured when converting ES response to ProgenyVO: " + e.getMessage(), e);
            }
        } else if (hits.totalHits > 1) {
            throw new IllegalStateException("Expected only 1 result for progeny with germplasmDbId: " + germplasmDbId);
        }
        return progenyVO;
    }





    private SearchRequest prepareSearchRequest(FaidareGermplasmPOSTShearchCriteria criteria) {
        List<String> facetFields = criteria.getFacetFields();
        String[] documentFieldsInFacets = criteriaFieldsToDocumentFields(facetFields);

        // Build search query (excluding document fields used in facets)
        QueryBuilder query = queryFactory.createEsShouldQueryExcludingFields(criteria, documentFieldsInFacets);

        // Prepare search request with query
        SearchRequest request = ESGenericFindRepository.prepareSearchRequest(
            query, criteria, documentMetadata, requestFactory);

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
                QueryBuilder facetFilter = queryFactory.createEsShouldQueryExcludingFields(criteria, documentPath);
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

    private String[] criteriaFieldsToDocumentFields(List<String> criteriaFields) {
        List<String> fields = new ArrayList<>();
        if (criteriaFields != null) {
            for (String facetField : criteriaFields) {
                fields.add(criteriaMapping.getDocumentPath(facetField, true));
            }
        }
        return fields.toArray(new String[]{});
    }

    /**
     * Parse Elasticsearch search response into data discovery response
     */
    private GermplasmSearchResponse parseResponse(
        FaidareGermplasmPOSTShearchCriteria criteria, SearchResponse response
    ) throws IOException, ReflectiveOperationException {
        // Parse pagination
        Pagination pagination = PaginationImpl.create(criteria, parser.parseTotalHits(response));

        // Parse result list
        List<GermplasmVO> resultList = parser.parseHits(response,
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
        return ApiResponseFactory.createGermplasmListResponseWithFacets(pagination, resultList, facets);
    }
}
