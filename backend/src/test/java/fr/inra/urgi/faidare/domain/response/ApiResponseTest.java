package fr.inra.urgi.faidare.domain.response;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import fr.inra.urgi.faidare.api.brapi.v1.exception.BrapiPaginationException;
import fr.inra.urgi.faidare.domain.JSONView;
import fr.inra.urgi.faidare.domain.brapi.v1.response.BrapiListResponse;
import fr.inra.urgi.faidare.domain.brapi.v1.response.BrapiResponse;
import fr.inra.urgi.faidare.domain.brapi.v1.response.BrapiStatus;
import fr.inra.urgi.faidare.elasticsearch.query.impl.ESGenericQueryFactoryTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

/**
 * Create BrAPI response and test their serialization in JSON
 *
 * @author gcornut
 */
class ApiResponseTest {

    private static ObjectMapper objectMapper;

    @BeforeAll
    static void beforeClass() {
        objectMapper = JsonMapper.builder()
            // Making sure we always serialize json fields in the same order for test purpose
            .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true)
            .build();

        status1 = ApiResponseFactory.createStatus("status1", "code1");
        status2 = ApiResponseFactory.createStatus("status2", "code2");

        o1 = new DataObject();
        o1.id = 1;
        o1.name = "name1";
        o2 = new DataObject();
        o2.id = 2;
        o2.name = "name2";
    }

    private static DataObject o1;
    private static DataObject o2;
    private static BrapiStatus status1;
    private static BrapiStatus status2;

    @Test
    void should_Create_Null_Result_Response() throws IOException {
        BrapiResponse<Void> response = ApiResponseFactory.createSingleObjectResponse(null, null);

        String json = objectMapper.writerWithView(JSONView.BrapiFields.class).writeValueAsString(response);
        ESGenericQueryFactoryTest.assertJsonEquals(json,
            "{" +
                "\"metadata\":{" +
                    "\"datafiles\":[]," +
                    "\"pagination\":{\"currentPage\":0,\"pageSize\":0,\"totalCount\":0,\"totalPages\":0}," +
                    "\"status\":[]" +
                "}," +
                "\"result\":null" +
            "}"
        );
    }

    @Test
    void should_Create_Single_Object_Response() throws IOException {
        BrapiResponse<DataObject> response = ApiResponseFactory.createSingleObjectResponse(o1, null);

        String json = objectMapper.writerWithView(JSONView.BrapiFields.class).writeValueAsString(response);
        ESGenericQueryFactoryTest.assertJsonEquals(json,
            "{" +
                "\"metadata\":{" +
                    "\"datafiles\":[]," +
                    "\"pagination\":{\"currentPage\":0,\"pageSize\":0,\"totalCount\":0,\"totalPages\":0}," +
                    "\"status\":[]" +
                "}," +
                "\"result\":{" +
                    "\"id\":" + o1.id + "," +
                    "\"name\":\"" + o1.name + "\"" +
                "}" +
            "}"
        );
    }

    @Test
    void should_Create_Multi_Error_Response() throws IOException {
        BrapiResponse<Void> response = ApiResponseFactory.createSingleObjectResponse(null, Arrays.asList(status1, status2));

        String json = objectMapper.writerWithView(JSONView.BrapiFields.class).writeValueAsString(response);
        ESGenericQueryFactoryTest.assertJsonEquals(json,
            "{" +
                "\"metadata\":{" +
                    "\"datafiles\":[]," +
                    "\"pagination\":{\"currentPage\":0,\"pageSize\":0,\"totalCount\":0,\"totalPages\":0}," +
                    "\"status\":[" +
                        "{\"code\":\"" + status1.getCode() + "\",\"name\":\"" + status1.getName() + "\"}," +
                        "{\"code\":\"" + status2.getCode() + "\",\"name\":\"" + status2.getName() + "\"}" +
                    "]" +
                "}," +
                "\"result\":null" +
            "}"
        );
    }

    @Test
    void should_Create_Single_Object_And_Multi_Error_Response() throws IOException {
        BrapiResponse<DataObject> response = ApiResponseFactory.createSingleObjectResponse(o1, Arrays.asList(status1, status2));

        String json = objectMapper.writerWithView(JSONView.BrapiFields.class).writeValueAsString(response);
        ESGenericQueryFactoryTest.assertJsonEquals(json,
            "{" +
                "\"metadata\":{" +
                    "\"datafiles\":[]," +
                    "\"pagination\":{\"currentPage\":0,\"pageSize\":0,\"totalCount\":0,\"totalPages\":0}," +
                    "\"status\":[" +
                        "{\"code\":\"" + status1.getCode() + "\",\"name\":\"" + status1.getName() + "\"}," +
                        "{\"code\":\"" + status2.getCode() + "\",\"name\":\"" + status2.getName() + "\"}" +
                    "]" +
                "}," +
                "\"result\":{" +
                    "\"id\":" + o1.id + "," +
                    "\"name\":\"" + o1.name + "\"" +
                "}" +
            "}"
        );
    }

    @Test
    void should_Create_Multi_Object_And_Multi_Error_Response() throws IOException {
        long totalPages = 1;
        long totalCount = 2;
        long currentPage = 0;
        long pageSize = 2;

        String expectedJson =
            "{" +
                "\"metadata\":{" +
                    "\"datafiles\":[]," +
                    "\"pagination\":{" +
                        "\"currentPage\":" + currentPage + "," +
                        "\"pageSize\":" + pageSize + "," +
                        "\"totalCount\":" + totalCount + "," +
                        "\"totalPages\":" + totalPages +
                    "}," +
                    "\"status\":[" +
                        "{\"code\":\"" + status1.getCode() + "\",\"name\":\"" + status1.getName() + "\"}," +
                        "{\"code\":\"" + status2.getCode() + "\",\"name\":\"" + status2.getName() + "\"}" +
                    "]" +
                "}," +
                "\"result\":{" +
                    "\"data\":[" +
                        "{" +
                            "\"id\":" + o1.id + "," +
                            "\"name\":\"" + o1.name + "\"" +
                        "}," +
                        "{" +
                            "\"id\":" + o2.id + "," +
                            "\"name\":\"" + o2.name + "\"" +
                        "}" +
                    "]" +
                "}" +
            "}";

        BrapiListResponse<DataObject> response = ApiResponseFactory.createListResponse(
            PaginationImpl.create(pageSize, currentPage, totalCount, totalPages),
            Arrays.asList(status1, status2), Arrays.asList(o1, o2)
        );

        String actualJson = objectMapper.writerWithView(JSONView.BrapiFields.class).writeValueAsString(response);
        ESGenericQueryFactoryTest.assertJsonEquals(actualJson, expectedJson);
    }

    @Test
    void should_Fail_When_Result_Count_Greater_Than_Page_Size() {
        long totalPages = 1;
        long totalCount = 2;
        long currentPage = 0;
        long pageSize = 1;

        Assertions.assertThrows(BrapiPaginationException.class,
            () -> ApiResponseFactory.createListResponse(
                PaginationImpl.create(pageSize, currentPage, totalCount, totalPages),
                Arrays.asList(status1, status2), Arrays.asList(o1, o2)
            ));
    }

    @Test
    void should_Fail_When_Page_Equal_Total_Pages() {
        long totalPages = 1;
        long totalCount = 2;
        long currentPage = totalPages;
        long pageSize = 1;


        Assertions.assertThrows(BrapiPaginationException.class,
            () -> {
                ApiResponseFactory.createListResponse(
                    PaginationImpl.create(pageSize, currentPage, totalCount, totalPages),
                    Arrays.asList(status1, status2), Arrays.asList(o1, o2)
                );
            });
    }

    @Test
    void should_Fail_When_Page_Greater_Than_Total_Pages() {
        long totalPages = 1;
        long totalCount = 2;
        long currentPage = totalPages + 2;
        long pageSize = 1;

        Assertions.assertThrows(BrapiPaginationException.class,
            () -> ApiResponseFactory.createListResponse(
                PaginationImpl.create(pageSize, currentPage, totalCount, totalPages),
                Arrays.asList(status1, status2), Arrays.asList(o1, o2)
            ));
    }

    /**
     * Example data object VO for test purpose
     */
    public static class DataObject {
        public long id;
        public String name;
    }
}
