package fr.inra.urgi.faidare.elasticsearch.repository.impl;

import fr.inra.urgi.faidare.domain.criteria.base.PaginationCriteria;
import fr.inra.urgi.faidare.elasticsearch.ESRequestFactory;
import fr.inra.urgi.faidare.elasticsearch.ESResponseParser;
import fr.inra.urgi.faidare.elasticsearch.criteria.AnnotatedCriteriaMapper;
import fr.inra.urgi.faidare.elasticsearch.criteria.mapping.CriteriaMapping;
import fr.inra.urgi.faidare.elasticsearch.criteria.mapping.CriteriaMappingCriterion;
import fr.inra.urgi.faidare.elasticsearch.document.DocumentAnnotationUtil;
import fr.inra.urgi.faidare.elasticsearch.document.DocumentMetadata;
import fr.inra.urgi.faidare.elasticsearch.query.ESQueryFactory;
import fr.inra.urgi.faidare.elasticsearch.query.impl.ESGenericQueryFactory;
import fr.inra.urgi.faidare.elasticsearch.repository.ESSuggestRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.matchPhraseQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.filter;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;

/**
 * Generic implementation of an ElasticSearch document repository which can suggest document field values
 *
 * @author gcornut
 */
public class ESGenericSuggestRepository<C extends PaginationCriteria, VO> implements ESSuggestRepository<C> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ESGenericSuggestRepository.class);

    private final RestHighLevelClient client;
    private final ESRequestFactory requestFactory;
    private final ESQueryFactory<C> queryFactory;
    private final DocumentMetadata<VO> documentMetadata;
    private final ESResponseParser parser;

    public ESGenericSuggestRepository(
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

    public ESGenericSuggestRepository(
        RestHighLevelClient client,
        ESRequestFactory requestFactory,
        Class<VO> voClass,
        ESResponseParser parser) {
        this(client, requestFactory, voClass, new ESGenericQueryFactory<C>(), parser);
    }

    @Override
    public LinkedHashSet<String> suggest(String field, String searchText, Long fetchSize, C criteria) {
        int size = fetchSize != null ? fetchSize.intValue() : 10;

        QueryBuilder query = null;
        if (criteria != null) {
            removeCriterion(criteria, field);
            query = queryFactory.createQuery(criteria);
        }

        String documentType = documentMetadata.getDocumentType();
        List<String> aggregationPath = new ArrayList<>();

        String termAggName = "termAgg";
        aggregationPath.add(0, termAggName);
        AggregationBuilder agg = terms(termAggName).field(field).size(size);

        if (StringUtils.isNotBlank(searchText)) {
            MatchPhraseQueryBuilder matchQuery = matchPhraseQuery(field + ".suggest", searchText);
            if (query != null) {
                query = ESGenericQueryFactory.andQueries(query, matchQuery);
            } else {
                query = matchQuery;
            }
        }

        if (query != null) {
            String filterAggName = "filterAgg";
            aggregationPath.add(0, filterAggName);
            agg = filter(filterAggName, query).subAggregation(agg);
        }

        SearchRequest request = requestFactory.prepareSearch(documentType);
        request.source().size(0);
        request.source().aggregation(agg);

        LOGGER.debug(request.toString());

        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<String> terms = parser.parseTermAggKeys(searchResponse, aggregationPath);
        if (terms == null)
            return null;
        return new LinkedHashSet<>(sortShortestMatch(terms, searchText));
    }


    /**
     * Remove criterion from criteria using field path from the document (ex:
     * germplasm.search.cropName)
     */
    private static <C> void removeCriterion(C criteria, String field) {
        try {
            CriteriaMapping mapping = AnnotatedCriteriaMapper.getMapping(criteria.getClass());
            List<String> path = Arrays.asList(field.split("\\."));
            CriteriaMappingCriterion criterion = mapping.getMapping(CriteriaMappingCriterion.class, path);
            criterion.setValue(criteria, null);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new ESGenericSuggestRepositoryException(e);
        }
    }


    /**
     * Sort by shortest match if possible and return a distinct list of terms
     */
    private static List<String> sortShortestMatch(List<String> terms, final String searchText) {
        if (StringUtils.isNotBlank(searchText)) {
            final String flatSearchText = foldAndLower(searchText);
            Collections.sort(terms, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    boolean inO1 = foldAndLower(o1).contains(flatSearchText);
                    boolean inO2 = foldAndLower(o2).contains(flatSearchText);
                    if (inO1 && inO2) {
                        return (o1.length() < o2.length()) ? -1 : 1;
                    }
                    return inO1 ? -1 : inO2 ? 1 : 0;
                }
            });
        }
        return terms;
    }

    /**
     * Fold ascii & lower case string
     */
    private static String foldAndLower(String input) {
        int length = input.length();
        char[] output = new char[length * 4];
        ASCIIFoldingFilter.foldToASCII(input.toLowerCase().toCharArray(), 0, output, 0, length);
        return new String(output).trim();
    }


    public static class ESGenericSuggestRepositoryException extends RuntimeException {

        ESGenericSuggestRepositoryException(Throwable t) {
            super(t);
        }

    }
}
