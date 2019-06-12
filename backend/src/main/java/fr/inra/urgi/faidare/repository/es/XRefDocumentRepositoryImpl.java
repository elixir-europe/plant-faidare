package fr.inra.urgi.faidare.repository.es;

import fr.inra.urgi.faidare.config.FaidareProperties;
import fr.inra.urgi.faidare.domain.response.PaginatedList;
import fr.inra.urgi.faidare.domain.response.Pagination;
import fr.inra.urgi.faidare.domain.response.PaginationImpl;
import fr.inra.urgi.faidare.domain.xref.XRefDocumentSearchCriteria;
import fr.inra.urgi.faidare.domain.xref.XRefDocumentVO;
import fr.inra.urgi.faidare.elasticsearch.ESResponseParser;
import fr.inra.urgi.faidare.elasticsearch.query.impl.ESGenericQueryFactory;
import fr.inra.urgi.faidare.repository.http.UserGroupsResourceClient;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

/**
 * Imported and adapted from unified-interface legacy
 */
@Repository
public class XRefDocumentRepositoryImpl implements XRefDocumentRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(XRefDocumentRepositoryImpl.class);

    private final FaidareProperties properties;
    private final RestHighLevelClient client;
    private final ESResponseParser parser;
    private final UserGroupsResourceClient userGroupsResourceClient;
    private final ESGenericQueryFactory<XRefDocumentSearchCriteria> queryFactory;

    @Autowired
    public XRefDocumentRepositoryImpl(
        FaidareProperties properties,
        RestHighLevelClient client,
        ESResponseParser parser,
        UserGroupsResourceClient userGroupsResourceClient
    ) {
        this.properties = properties;
        this.client = client;
        this.parser = parser;
        this.userGroupsResourceClient = userGroupsResourceClient;
        this.queryFactory = new ESGenericQueryFactory<>();
    }

    @Override
    public PaginatedList<XRefDocumentVO> find(XRefDocumentSearchCriteria criteria) {
        try {
            List<Integer> userGroups = userGroupsResourceClient.getUserGroups();
            String baseIndex = properties.getElasticsearchXrefIndexName();
            String[] aliases = getGroupAliases(baseIndex, userGroups);

            QueryBuilder query = queryFactory.createQuery(criteria);

            int size = criteria.getPageSize().intValue();
            int from = criteria.getPage().intValue() * size;

            SearchSourceBuilder source = new SearchSourceBuilder()
                .query(query).from(from).size(size);

            LOGGER.debug("Search Xref aliases: " + String.join(",", aliases));
            LOGGER.debug("Query:\n " + source.toString());

            SearchRequest searchRequest = new SearchRequest()
                .indices(aliases)
                .source(source)
                .indicesOptions(IndicesOptions.fromOptions(true, false, false, false));

            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

            List<XRefDocumentVO> documents = parser.parseHits(response, XRefDocumentVO.class);
            Pagination pagination = PaginationImpl.create(criteria, parser.parseTotalHits(response));
            return new PaginatedList<>(pagination, documents);
        } catch (IOException | ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static String[] getGroupAliases(String baseIndex, List<? extends Number> userGroups) {
        String[] aliases = new String[userGroups.size()];
        for (int i = 0; i < userGroups.size(); i++) {
            Number groupId = userGroups.get(i);
            aliases[i] = baseIndex + "-group" + groupId;
        }
        return aliases;
    }

}
