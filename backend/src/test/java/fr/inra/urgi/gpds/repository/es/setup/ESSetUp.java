package fr.inra.urgi.gpds.repository.es.setup;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;
import fr.inra.urgi.gpds.config.GPDSProperties;
import fr.inra.urgi.gpds.elasticsearch.document.DocumentAnnotationUtil;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

/**
 * @author gcornut
 */
@Service
public class ESSetUp {

	@Resource
	Client client;

	@Autowired
    GPDSProperties properties;

	/**
	 * Delete index/alias if it exists
	 */
	private void deleteIndex(String indexName) {
		boolean exists = client.admin().indices().exists(new IndicesExistsRequest(indexName)).actionGet().isExists();
		if (!exists) {
			// Do not delete non existing index
			return;
		}

		DeleteIndexRequestBuilder request = client.admin().indices().prepareDelete(indexName);
		AcknowledgedResponse response;
		try {
			response = request.execute().get();
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}

		if (!response.isAcknowledged()) {
			throw new RuntimeException("Index deletion not acknowledged (index name: '" + indexName + "'");
		}
	}

	/**
	 * Create index with data
	 */
	protected void addIndex(String indexName, String jsonPath, String documentType) throws IOException {
		// Create index
		CreateIndexRequest createIndex = new CreateIndexRequest(indexName);

		// with settings
		String settings = readResource("./index/settings.json");
		createIndex.settings(toXContentBuilder(settings));

		// with document mappings
		String mapping = readResource("./index/" + documentType + "_mapping.json");
		createIndex.mapping(documentType, toXContentBuilder(mapping));

		CreateIndexResponse createResponse = client.admin().indices().create(createIndex).actionGet();
		if (!createResponse.isAcknowledged()) {
			throw new RuntimeException("Could not create index '" + indexName + "': " + createResponse.toString());
		}

		// Bulk index fixture data
		InputStream jsonStream = getClass().getResourceAsStream(jsonPath);
		JsonNode jsonNode = new ObjectMapper().readTree(jsonStream);

		BulkRequestBuilder request = client.prepareBulk();

		Iterator<JsonNode> elements = jsonNode.elements();
		while (elements.hasNext()) {
			JsonNode document = elements.next();

			request.add(client.prepareIndex(indexName, documentType).setSource(toXContentBuilder(document.toString())));
		}

		BulkResponse response;
		try {
			response = request.execute().get();
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}

		if (response.hasFailures()) {
			System.err.println(response.buildFailureMessage());
			throw new RuntimeException("Indexing failed (index name: '" + indexName + "'");
		}
	}

	/**
	 * Initialize fixture indices for a document and a list of group IDs
	 */
	public boolean initialize(Class<?> documentClass, long... groupIds) {
		String documentType = DocumentAnnotationUtil.getDocumentObjectMetadata(documentClass).getDocumentType();
		try {
			for (long groupId : groupIds) {
				String indexName = properties.getIndexName("fixture", documentType, groupId);

				// Delete fixture index if exists
				deleteIndex(indexName);

				// Add fixture index (with data)
				String jsonPath = "./fixture/" + documentType + groupId + ".json";
				addIndex(indexName, jsonPath, documentType);
			}
			refreshIndex();
			return true;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Refresh indices to make sure the new documents are correctly indexed
	 */
	private void refreshIndex() {
		RefreshResponse refreshResponse = client.admin().indices().prepareRefresh().execute().actionGet();
		if (refreshResponse.getFailedShards() > 0) {
			throw new RuntimeException("Could not refresh Elasticsearch indices.");
		}
	}

	/**
	 * Read package resource in a String
	 */
	private String readResource(String path) {
		try {
			return CharStreams.toString(new InputStreamReader(getClass().getResourceAsStream(path)));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	private XContentBuilder toXContentBuilder(String json) throws IOException {
		OutputStream out = new ByteArrayOutputStream();
		out.write(json.getBytes());
		return XContentFactory.jsonBuilder(out);
	}

}
