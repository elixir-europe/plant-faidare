package fr.inra.urgi.gpds.elasticsearch.repository.impl;

import fr.inra.urgi.gpds.elasticsearch.ESRequestFactory;
import fr.inra.urgi.gpds.elasticsearch.ESResponseParser;
import fr.inra.urgi.gpds.elasticsearch.document.DocumentAnnotationUtil;
import fr.inra.urgi.gpds.elasticsearch.document.DocumentMetadata;
import fr.inra.urgi.gpds.elasticsearch.repository.ESGetByIdRepository;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Generic implementation of an ElasticSearch query for document by its identifier
 *
 * @author gcornut
 *
 *
 */
public class ESGenericGetByIdRepository<VO> implements ESGetByIdRepository<VO> {

	private final RestHighLevelClient client;
	private final ESRequestFactory requestFactory;
	private final Class<VO> voClass;
	private DocumentMetadata<VO> documentMetadata;

	private Logger logger = LoggerFactory.getLogger(getClass());

	public ESGenericGetByIdRepository(
        RestHighLevelClient client,
        ESRequestFactory requestFactory,
        Class<VO> voClass
    ) {
        this.client = client;
        this.requestFactory = requestFactory;
		this.voClass = voClass;
		documentMetadata = DocumentAnnotationUtil.getDocumentObjectMetadata(voClass);
		if (documentMetadata.getIdField() == null) {
			throw new RuntimeException("Could not identify an identifier field on document "+ voClass.getSimpleName());
		}
	}

	@Override
	public VO getById(String id) {
		try {
			String documentType = documentMetadata.getDocumentType();

			// Build term id query
			QueryBuilder query = QueryBuilders.termQuery(documentMetadata.getIdField(), id);

			// Build request
			SearchRequest request = requestFactory.prepareSearch(documentType, query);

			// Execute request
            SearchResponse result = client.search(request, RequestOptions.DEFAULT);

			// Parse result list
			List<? extends VO> resultList = ESResponseParser.parseHits(result, voClass);

			if (resultList != null && !resultList.isEmpty()) {
				if (resultList.size() > 1) {
					// Should never happen
					throw new RuntimeException("More than one document " + documentType + " for identifier '" + id + "'");
				}

				return resultList.get(0);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

}
