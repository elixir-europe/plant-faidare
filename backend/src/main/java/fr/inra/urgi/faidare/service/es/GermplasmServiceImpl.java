package fr.inra.urgi.faidare.service.es;

import com.opencsv.CSVWriter;
import fr.inra.urgi.faidare.api.faidare.v1.GnpISGermplasmController;
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
import fr.inra.urgi.faidare.elasticsearch.criteria.AnnotatedCriteriaMapper;
import fr.inra.urgi.faidare.elasticsearch.criteria.mapping.CriteriaMapping;
import fr.inra.urgi.faidare.elasticsearch.document.DocumentAnnotationUtil;
import fr.inra.urgi.faidare.elasticsearch.document.DocumentMetadata;
import fr.inra.urgi.faidare.elasticsearch.query.impl.ESGenericQueryFactory;
import fr.inra.urgi.faidare.elasticsearch.repository.ESSuggestRepository;
import fr.inra.urgi.faidare.elasticsearch.repository.impl.ESGenericFindRepository;
import fr.inra.urgi.faidare.elasticsearch.repository.impl.ESGenericSuggestRepository;
import fr.inra.urgi.faidare.repository.es.GermplasmRepository;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import static org.elasticsearch.search.aggregations.AggregationBuilders.filter;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;

/**
 * @author cpommier, gcornut
 */
@Service("germplasmService")
public class GermplasmServiceImpl implements GermplasmService {

    private final static Logger LOGGER = LoggerFactory.getLogger(GnpISGermplasmController.class);

    private final DocumentMetadata<GermplasmVO> documentMetadata;
    private final CriteriaMapping criteriaMapping;

    private final RestHighLevelClient client;
    private final ESRequestFactory requestFactory;
    private final ESSuggestRepository<FaidareGermplasmPOSTShearchCriteria> suggestRepository;
    private final ESGenericQueryFactory<FaidareGermplasmPOSTShearchCriteria> queryFactory;
    private final ESResponseParser parser;

    public GermplasmServiceImpl (
        RestHighLevelClient client,
        ESRequestFactory requestFactory,
        ESResponseParser parser
    ){
        this.client = client;
        this.parser = parser;

        Class<GermplasmVO> documentClass = GermplasmVO.class;
        Class<FaidareGermplasmPOSTShearchCriteria> criteriaClass = FaidareGermplasmPOSTShearchCriteria.class;

        this.requestFactory = requestFactory;
        this.documentMetadata = DocumentAnnotationUtil.getDocumentObjectMetadata(documentClass);

        this.criteriaMapping = AnnotatedCriteriaMapper.getMapping(criteriaClass);
        this.queryFactory = new ESGenericQueryFactory<>();
        this.suggestRepository = new ESGenericSuggestRepository<>(client, requestFactory, documentClass, queryFactory, parser);
    }

    @Resource
    GermplasmRepository germplasmRepository;

    @Override
    public File exportCSV(GermplasmSearchCriteria criteria) {
        try {
            Iterator<GermplasmVO> allGermplasm = germplasmRepository.scrollAll(criteria);
            Path tmpDirPath = Files.createTempDirectory("germplasm");
            Path tmpFile = Files.createTempFile(tmpDirPath, "germplasm_", ".csv");
            writeToCSV(tmpFile, allGermplasm);
            return tmpFile.toFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public File exportListGermplasmCSV(FaidareGermplasmPOSTShearchCriteria criteria) {
        try {
            Iterator<GermplasmVO> allGermplasm = germplasmRepository.scrollAllGermplasm(criteria);
            Path tmpDirPath = Files.createTempDirectory("germplasm");
            Path tmpFile = Files.createTempFile(tmpDirPath, "germplasm_", ".csv");
            writeToCSV(tmpFile, allGermplasm);
            return tmpFile.toFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeToCSV(Path tmpFile, Iterator<GermplasmVO> germplasms) throws IOException {
        Writer fileStream = new OutputStreamWriter(new FileOutputStream(tmpFile.toFile()), StandardCharsets.UTF_8);
        CSVWriter csvWriter = new CSVWriter(fileStream, ';', '"', '\\', "\n");
        String[] header = new String[]{
            "DOI", "AccessionNumber", "AccessionName", "TaxonGroup", "HoldingInstitution",
            "LotName", "LotSynonym", "CollectionName", "CollectionType", "PanelName", "PanelSize"};
        csvWriter.writeNext(header);

        while (germplasms.hasNext()) {
            GermplasmVO germplasmVO = germplasms.next();
            String[] line = new String[header.length];
            line[0] = germplasmVO.getGermplasmPUI();
            line[1] = germplasmVO.getAccessionNumber();
            line[2] = germplasmVO.getGermplasmName();
            line[3] = germplasmVO.getCommonCropName();
            line[4] = germplasmVO.getInstituteName();
            csvWriter.writeNext(line);
        }
        csvWriter.close();
    }

    @Override
    public GermplasmVO getById(String germplasmDbId) {
        return germplasmRepository.getById(germplasmDbId);
    }

    @Override
    public PaginatedList<GermplasmVO> find(GermplasmSearchCriteria sCrit) {
        return germplasmRepository.find(sCrit);
    }

    @Override
    public GermplasmSearchResponse esShouldFind(FaidareGermplasmPOSTShearchCriteria germSearCrit) {
        try {
            // QueryBuilder query = queryFactory.createOrQuery(germSearCrit);

            // Prepare search request
            SearchRequest request = prepareSearchRequest(germSearCrit);

            // Execute request
            LOGGER.debug(request.toString());
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);

            // Return paginated list
            return parseResponse (germSearCrit, response);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public LinkedHashSet<String> suggest(
        String criteriaField, String searchText, Integer fetchSize, FaidareGermplasmPOSTShearchCriteria criteria
    ) {
        String documentFieldPath = criteriaMapping.getDocumentPath(criteriaField, true);
        return suggestRepository.suggest(documentFieldPath, searchText, fetchSize, criteria);
    }

    @Override
    public PedigreeVO getPedigree(String germplasmDbId) {
        return germplasmRepository.findPedigree(germplasmDbId);
    }

    @Override
    public ProgenyVO getProgeny(String germplasmDbId) {
        return germplasmRepository.findProgeny(germplasmDbId);
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
