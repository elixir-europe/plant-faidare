package fr.inra.urgi.gpds.domain.brapi.v1.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.inra.urgi.gpds.api.brapi.v1.exception.BrapiPaginationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Create BrAPI response and test their serialization in JSON
 *
 * @author gcornut
 */
class BrapiResponseTest {

    private static ObjectMapper objectMapper;

    @BeforeAll
    static void beforeClass() {
        objectMapper = new ObjectMapper();

        // Making sure we always serialize json fields in the same order for test purpose
        objectMapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);

        status1 = BrapiResponseFactory.createStatus("status1", "code1");
        status2 = BrapiResponseFactory.createStatus("status2", "code2");

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
    void should_Create_Null_Result_Response() throws JsonProcessingException {
        BrapiResponse<Void> response = BrapiResponseFactory.createSingleObjectResponse(null, null);

        String json = objectMapper.writeValueAsString(response);
        assertThat(json).isEqualTo(
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
    void should_Create_Single_Object_Response() throws JsonProcessingException {
        BrapiResponse<DataObject> response = BrapiResponseFactory.createSingleObjectResponse(o1, null);

        String json = objectMapper.writeValueAsString(response);
        assertThat(json).isEqualTo(
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
    void should_Create_Multi_Error_Response() throws JsonProcessingException {
        BrapiResponse<Void> response = BrapiResponseFactory.createSingleObjectResponse(null, Arrays.asList(status1, status2));

        String json = objectMapper.writeValueAsString(response);
        assertThat(json).isEqualTo(
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
    void should_Create_Single_Object_And_Multi_Error_Response() throws JsonProcessingException {
        BrapiResponse<DataObject> response = BrapiResponseFactory.createSingleObjectResponse(o1, Arrays.asList(status1, status2));

        String json = objectMapper.writeValueAsString(response);
        assertThat(json).isEqualTo(
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
    void should_Create_Multi_Object_And_Multi_Error_Response() throws JsonProcessingException {
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

        BrapiListResponse<DataObject> response = BrapiResponseFactory.createListResponse(
            BrapiResponseFactory.createPagination(pageSize, currentPage, totalCount, totalPages),
            Arrays.asList(status1, status2), Arrays.asList(o1, o2)
        );

        String actualJson = objectMapper.writeValueAsString(response);
        assertThat(actualJson).isEqualTo(expectedJson);
    }

    @Test
    void should_Fail_When_Result_Count_Greater_Than_Page_Size() {
        long totalPages = 1;
        long totalCount = 2;
        long currentPage = 0;
        long pageSize = 1;

        assertThrows(BrapiPaginationException.class,
            () -> BrapiResponseFactory.createListResponse(
                BrapiResponseFactory.createPagination(pageSize, currentPage, totalCount, totalPages),
                Arrays.asList(status1, status2), Arrays.asList(o1, o2)
            ));
    }

    @Test
    void should_Fail_When_Page_Equal_Total_Pages() {
        long totalPages = 1;
        long totalCount = 2;
        long currentPage = totalPages;
        long pageSize = 1;


        assertThrows(BrapiPaginationException.class,
            () -> {
                BrapiResponseFactory.createListResponse(
                    BrapiResponseFactory.createPagination(pageSize, currentPage, totalCount, totalPages),
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

        assertThrows(BrapiPaginationException.class,
            () -> BrapiResponseFactory.createListResponse(
                BrapiResponseFactory.createPagination(pageSize, currentPage, totalCount, totalPages),
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
