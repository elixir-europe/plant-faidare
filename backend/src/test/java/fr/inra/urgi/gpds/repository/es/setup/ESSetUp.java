package fr.inra.urgi.gpds.repository.es.setup;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;
import fr.inra.urgi.gpds.config.GPDSProperties;
import fr.inra.urgi.gpds.elasticsearch.document.DocumentAnnotationUtil;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Iterator;

/**
 * @author gcornut
 */
@Component
public class ESSetUp {

    private final RestHighLevelClient client;
    private final GPDSProperties properties;

    public ESSetUp(RestHighLevelClient client, GPDSProperties properties) {
        this.client = client;
        this.properties = properties;
    }

    /**
     * Delete index/alias if it exists
     */
    private void deleteIndex(String indexName) throws IOException {
        GetIndexRequest existsRequest = new GetIndexRequest();
        existsRequest.indices(indexName);
        boolean exists = client.indices().exists(existsRequest, RequestOptions.DEFAULT);
        if (!exists) {
            // Do not delete non existing index
            return;
        }

        DeleteIndexRequest deleteRequest = new DeleteIndexRequest(indexName);
        AcknowledgedResponse response = client.indices().delete(deleteRequest, RequestOptions.DEFAULT);

        if (!response.isAcknowledged()) {
            throw new RuntimeException("Index deletion not acknowledged (index name: '" + indexName + "'");
        }
    }

    /**
     * Create index with data
     */
    private void addIndex(String indexName, String jsonPath, String documentType) throws IOException {
        // Create index
        CreateIndexRequest createIndex = new CreateIndexRequest(indexName);

        // with settings
        String settings = readResource("./index/settings.json");
        createIndex.settings(toXContentBuilder(settings));

        // with document mappings
        String mapping = readResource("./index/" + documentType + "_mapping.json");
        createIndex.mapping(documentType, toXContentBuilder(mapping));

        CreateIndexResponse createResponse = client.indices().create(createIndex, RequestOptions.DEFAULT);
        if (!createResponse.isAcknowledged()) {
            throw new RuntimeException("Could not create index '" + indexName + "': " + createResponse.toString());
        }

        // Bulk index fixture data
        InputStream jsonStream = getClass().getResourceAsStream(jsonPath);
        JsonNode jsonNode = new ObjectMapper().readTree(jsonStream);

        BulkRequest bulkRequest = new BulkRequest();

        Iterator<JsonNode> elements = jsonNode.elements();
        while (elements.hasNext()) {
            JsonNode document = elements.next();

            IndexRequest indexRequest = new IndexRequest(indexName, documentType);
            indexRequest.source(toXContentBuilder(document.toString()));
            bulkRequest.add(indexRequest);
        }

        BulkResponse response;
        response = client.bulk(bulkRequest, RequestOptions.DEFAULT);

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
    private void refreshIndex() throws IOException {
        RefreshRequest refreshRequest = new RefreshRequest();
        RefreshResponse refreshResponse = client.indices().refresh(refreshRequest, RequestOptions.DEFAULT);
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
