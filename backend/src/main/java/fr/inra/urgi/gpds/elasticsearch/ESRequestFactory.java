package fr.inra.urgi.gpds.elasticsearch;

import com.google.common.base.Joiner;
import fr.inra.urgi.gpds.config.GPDSProperties;
import fr.inra.urgi.gpds.repository.http.UserGroupsResourceClient;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ESRequestFactory {

	private static final Logger logger = LoggerFactory.getLogger(ESRequestFactory.class);

	public static final int MAX_TERM_AGG_SIZE = Integer.MAX_VALUE;

	private UserGroupsResourceClient userGroupsResourceClient;
	private GPDSProperties properties;

	private final IndicesOptions indicesOptions;

    public ESRequestFactory(
        UserGroupsResourceClient userGroupsResourceClient,
        GPDSProperties properties
    ) {
        this.userGroupsResourceClient = userGroupsResourceClient;
        this.properties = properties;
        boolean ignoreUnavailable = true;
		boolean allowNoIndices = true;
		boolean expandToOpenIndices = true;
		boolean expandToClosedIndices = false;
		indicesOptions = IndicesOptions.fromOptions(
				ignoreUnavailable, allowNoIndices, expandToOpenIndices, expandToClosedIndices
		);
    }

	/**
	 * Utility method to retrieve a new {@link SearchRequestBuilder} already
	 * configured with the index. This method allows to not forget the index and
	 * so to not query the whole cluster!
	 *
	 * @return the {@link SearchRequestBuilder} configured with index
	 */
	public SearchRequest prepareSearch(String documentType, QueryBuilder query) {
        String[] aliases = getAliases(documentType, null);

        SearchRequest request = new SearchRequest();
        request.indices(aliases);
        request.source(new SearchSourceBuilder());
        request.indicesOptions(indicesOptions);

		if (documentType != null) {
            request.types(documentType);
		}

		if (query != null) {
            request.source().query(query);
		}

		// Debug
		if (logger.isDebugEnabled()) {
			String aliasesString = Joiner.on(",").join(aliases);
			StringBuilder message = new StringBuilder();
			message.append("Search ES:\n");
			message.append("POST ").append(aliasesString);
			if (documentType != null) {
				message.append("/").append(documentType);
			}
			message.append("/_search");
			if (query != null) {
				message.append("\n");
				message.append(request.source().toString());
			}
			logger.debug(message.toString());
		}

		return request;
	}

	/**
	 * List ElasticSearch index group id filtered aliases to a document type
	 */
	public String[] getAliases(String documentType) {
		List<String> aliasList = getAliasList(documentType, null);
		return aliasList.toArray(new String[0]);
	}

	/**
	 * List ElasticSearch index group id filtered aliases to a document type
	 */
	public String[] getAliases(String documentType, List<String> sourceIds) {
		List<String> aliasList = getAliasList(documentType, sourceIds);
		return aliasList.toArray(new String[0]);
	}

	/**
	 * List ElasticSearch index group id filtered aliases to a document type
	 */
	private List<String> getAliasList(String documentType, List<String> sourceIds) {
		if (sourceIds == null) {
			sourceIds = new ArrayList<>();
			sourceIds.add("*");
		}
		List<Integer> groupIds = userGroupsResourceClient.getUserGroups();
		List<String> aliases = new ArrayList<>();
		for (Integer groupId : groupIds) {
			for (String sourceId : sourceIds) {
				aliases.add(properties.getIndexName(sourceId, documentType, groupId));
			}
		}
		return aliases;
	}

	public SearchRequest prepareSearch(String documentType) {
		return prepareSearch(documentType, null);
	}

	public SearchScrollRequest getSearchScroll(String scrollId) {
        return new SearchScrollRequest(scrollId);
    }

}
