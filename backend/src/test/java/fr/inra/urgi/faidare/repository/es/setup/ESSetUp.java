package fr.inra.urgi.faidare.repository.es.setup;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;
import fr.inra.urgi.faidare.config.FaidareProperties;
import fr.inra.urgi.faidare.domain.xref.XRefDocumentVO;
import fr.inra.urgi.faidare.elasticsearch.document.DocumentAnnotationUtil;
import fr.inra.urgi.faidare.repository.es.XRefDocumentRepositoryImpl;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.xcontent.XContentBuilder ;
import org.elasticsearch.xcontent.XContentFactory;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Arrays;
import java.util.Iterator;

/**
 * @author gcornut
 */
@Component
public class ESSetUp {

    private final RestHighLevelClient client;
    private final FaidareProperties properties;

    @Autowired
    public ESSetUp(RestHighLevelClient client, FaidareProperties properties) {
        this.client = client;
        this.properties = properties;
    }

    /**
     * Delete index/alias if it exists
     */
    private void deleteIndex(String indexName) throws IOException {
        GetIndexRequest existsRequest = new GetIndexRequest(indexName);
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
        createIndex.mapping(mapping, XContentType.JSON);

        CreateIndexResponse createResponse = client.indices().create(createIndex, RequestOptions.DEFAULT);
        if (!createResponse.isAcknowledged()) {
            throw new RuntimeException("Could not create index '" + indexName + "': " + createResponse);
        }

        // Bulk index fixture data
        InputStream jsonStream = getClass().getResourceAsStream(jsonPath);
        JsonNode jsonNode = new ObjectMapper().readTree(jsonStream);

        BulkRequest bulkRequest = new BulkRequest();

        Iterator<JsonNode> elements = jsonNode.elements();
        while (elements.hasNext()) {
            JsonNode document = elements.next();

            IndexRequest indexRequest = new IndexRequest(indexName);
            indexRequest.source(toXContentBuilder(document.toString()));
            bulkRequest.add(indexRequest);
        }

        BulkResponse response;
        response = client.bulk(bulkRequest, RequestOptions.DEFAULT);

        if (response.hasFailures()) {
            System.err.println(response.buildFailureMessage());
            throw new RuntimeException("Indexing failed (index name: '" + indexName + "')");
        }
    }

    /**
     * Initialize fixture indices for a document and a list of group IDs
     */
    public void initialize(Class<?> documentClass, Long... groupIds) {
        String documentType = DocumentAnnotationUtil.getDocumentObjectMetadata(documentClass).getDocumentType();
        try {
            for (long groupId : groupIds) {
                String indexName = properties.getAliasName(documentType, groupId);

                // Delete fixture index if exists
                deleteIndex(indexName);

                // Add fixture index (with data)
                String jsonPath = "./fixture/" + documentType + groupId + ".json";
                addIndex(indexName, jsonPath, documentType);
            }
            refreshIndex();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initialize fixture indices for a document and a list of group IDs
     */
    public void initializeXref(Long... groupIds) {
        Class<?> documentClass = XRefDocumentVO.class;
        String documentType = DocumentAnnotationUtil.getDocumentObjectMetadata(documentClass).getDocumentType();
        try {
            String baseIndex = properties.getElasticsearchXrefIndexName();
            String[] indexNames = XRefDocumentRepositoryImpl.getGroupAliases(baseIndex, Arrays.asList(groupIds));
            for (String indexName : indexNames) {

                // Delete fixture index if exists
                deleteIndex(indexName);

                // Add fixture index (with data)
                String jsonPath = "./fixture/" + indexName + ".json";
                addIndex(indexName, jsonPath, documentType);
            }
            refreshIndex();
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
    @SuppressWarnings("UnstableApiUsage")
    private String readResource(String path) {
        try {
            InputStream resource = getClass().getResourceAsStream(path);
            if (resource == null) {
                throw new RuntimeException("Could not find resource at path '"+path+"'");
            }
            return CharStreams.toString(new InputStreamReader(resource));
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
