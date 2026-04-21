package fr.inrae.urgi.faidare.api.brapi.v2;

/**
 * Unit tests for {@link GermplasmController}
 * @author Cpommier
 */

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import fr.inrae.urgi.faidare.Application;
import fr.inrae.urgi.faidare.dao.v2.*;
import fr.inrae.urgi.faidare.utils.GermplasmV2Mapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ResourceUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GermplasmV2ControllerTest {


    @Autowired
    protected GermplasmV2Dao germplasmDao;

    @Value("${server.servlet.context-path}")
    private String contextPath;


    @LocalServerPort
    private int port;

    TestRestTemplate testRestTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();


    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + contextPath + uri;
    }

    @Test
    void should_call_serverinfo() throws Exception{

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("/brapi/v2/serverinfo"),
                HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode serverInfoFileJson = mapper.readTree(ResourceUtils.getFile("classpath:serverinfo.json"));
        JSONAssert.assertEquals(
            serverInfoFileJson.toString(),
            response.getBody(),
            JSONCompareMode.LENIENT
        );


    }



    @Test
    void should_get_germplasm_by_germplasmDbId() throws Exception {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("/brapi/v2/germplasm/dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI2ODkx"),
                HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        String accNumber = JsonPath.parse(response.getBody()).read("$.result.accessionNumber");
        assertThat(accNumber).isEqualTo("29814");
    }

    @Test
    void should_get_germplasm_by_criteria() throws Exception{

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = testRestTemplate.exchange(
            createURLWithPort("/brapi/v2/germplasm?accessionNumber=13481&genus=Triticum&species=aestivum&page=0&pageSize=1"),
            HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        String accNumber = JsonPath.parse(response.getBody()).read("$.result.data.[0].accessionNumber");
        assertThat(accNumber).isEqualTo("13481");
        String genus = JsonPath.parse(response.getBody()).read("$.result.data.[0].genus");
        assertThat(genus).isEqualTo("Triticum");
        String species = JsonPath.parse(response.getBody()).read("$.result.data.[0].species");
        assertThat(species).isEqualTo("aestivum");
        Integer pageSize = JsonPath.parse(response.getBody()).read("$.metadata.pagination.pageSize");
        assertThat(pageSize).isEqualTo(1);

    }



    @Test
    void should_get_germplasm_by_accessionNumber_by_page_O_pageSize_1() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("/brapi/v2/germplasm?accessionNumber=29814&page=0&pageSize=1"),
                HttpMethod.GET, entity, String.class);
        String accNumber = JsonPath.parse(response.getBody()).read("$.result.data.[0].accessionNumber");
        assertThat(accNumber).isEqualTo("29814");
        Integer pageSize = JsonPath.parse(response.getBody()).read("$.metadata.pagination.pageSize");
        assertThat(pageSize).isEqualTo(1);
    }


    @Test
    void should_get_germplasm_by_accessionNumber() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("/brapi/v2/germplasm?accessionNumber=29814"),
                HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        String accNumber = JsonPath.parse(response.getBody()).read("$.result.data.[0].accessionNumber");
        assertThat(accNumber).isEqualTo("29814");
    }

    @Test
    void should_search_germplasm_by_germplasmDbId() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
            createURLWithPort("/brapi/v2/germplasm?germplasmDbId=dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI2NDI2"),
            HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        String germplasmDbId = JsonPath.parse(response.getBody()).read("$.result.data.[0].germplasmDbId");
        assertThat(germplasmDbId).isEqualTo("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI2NDI2");
    }


    @Test
    void should_get_germplasm_by_trial(){
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
            createURLWithPort("/brapi/v2/germplasm?trialDbId=dXJuOklOUkFFLVVSR0kvdHJpYWwvMjQ="),
            HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        List<String> accNumbers = JsonPath.parse(response.getBody()).read("$.result.data.[*].accessionNumber");
        assertThat(accNumbers).containsExactlyInAnyOrder("661300224", "661300227", "661300228", "661300229", "661300230", "661300232", "661300233", "661300234", "661300235", "661300236");

    }

    @Test
    void should_search_germplasm_by_dbids() throws URISyntaxException {
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>("{\"germplasmDbIds\": [\"dXJuOklCRVQvYmU0ZTljZGMtNTgwMC00NDU3LWE2YzgtNDA1NjNjMDI3ZGQ5\"]}", headers);

        URI uri = new URI(createURLWithPort("/brapi/v2/search/germplasm"));
        ResponseEntity<String> response = testRestTemplate.postForEntity(uri, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }


    @Test
    void should_get_collection(){
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("/brapi/v2/collection"),
                HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        List<String> collName = JsonPath.parse(response.getBody()).read("$.result.data.[*].collectionName");
        assertThat(collName).contains("Collection blé INRA");
    }
    @Test
    void should_convert_get_to_post_with_constructor() {
        GermplasmV2Dao germplasmDaoMock = mock(GermplasmV2Dao.class);
        GermplasmMcpdDao germplasmMcpdDaoMock = mock(GermplasmMcpdDao.class);

        GermplasmV2Criteria post = getGermplasmV2Criteria(germplasmDaoMock, germplasmMcpdDaoMock);

        assertThat(post.getAccessionNumbers()).containsExactly("12345");
        assertThat(post.getBinomialNames()).containsExactly("Triticum aestivum");
        assertThat(post.getCollections()).containsExactly("Wheat INRA");
        assertThat(post.getCommonCropNames()).containsExactly("Wheat");
        assertThat(post.getGermplasmDbIds()).containsExactly("dbId123");
        assertThat(post.getGermplasmNames()).containsExactly("APACHE");
        assertThat(post.getPage()).isEqualTo(0);
        assertThat(post.getPageSize()).isEqualTo(10);
    }

    @Test
    void should_handle_empty_get_to_post() {
        GermplasmV2Dao gDao = mock(GermplasmV2Dao.class);
        GermplasmMcpdDao mDao = mock(GermplasmMcpdDao.class);

        GermplasmV2Controller controller = new GermplasmV2Controller(gDao, mDao);

        GermplasmV2CriteriaGet emptyGet = new GermplasmV2CriteriaGet();
        emptyGet.setPage(0);
        emptyGet.setPageSize(5);

        GermplasmV2Criteria post = null;
        try {
            var method = GermplasmV2Mapper.class.getDeclaredMethod("convertGetToPost", GermplasmV2CriteriaGet.class);
            method.setAccessible(true);
            post = (GermplasmV2Criteria) method.invoke(controller, emptyGet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        assertThat(post.getAccessionNumbers()).isNullOrEmpty();
        assertThat(post.getPage()).isEqualTo(0);
        assertThat(post.getPageSize()).isEqualTo(5);
    }

    private static GermplasmV2Criteria getGermplasmV2Criteria(GermplasmV2Dao germplasmDaoMock, GermplasmMcpdDao germplasmMcpdDaoMock) {
        GermplasmV2Controller controller =
            new GermplasmV2Controller(germplasmDaoMock, germplasmMcpdDaoMock);

        GermplasmV2CriteriaGet get = new GermplasmV2CriteriaGet();
        get.setAccessionNumber("12345");
        get.setBinomialName("Triticum aestivum");
        get.setCollection("Wheat INRA");
        get.setCommonCropName("Wheat");
        get.setGermplasmDbId("dbId123");
        get.setGermplasmName("APACHE");
        get.setPage(0);
        get.setPageSize(10);

        GermplasmV2Criteria post = null;
        try {
            var method = GermplasmV2Mapper.class.getDeclaredMethod("convertGetToPost", GermplasmV2CriteriaGet.class);
            method.setAccessible(true);
            post = (GermplasmV2Criteria) method.invoke(controller, get);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return post;
    }
}


