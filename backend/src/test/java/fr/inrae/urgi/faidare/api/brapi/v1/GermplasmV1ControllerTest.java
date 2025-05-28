package fr.inrae.urgi.faidare.api.brapi.v1;

/**
 * Unit tests for {@link GermplasmController}
 *
 * @author Cpommier
 */

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import fr.inrae.urgi.faidare.Application;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ResourceUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GermplasmV1ControllerTest {

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
    void should_call_calls() throws Exception {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("/brapi/v1/calls"),
                HttpMethod.GET, entity, String.class);
            assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode serverInfoFileJson = mapper.readTree(ResourceUtils.getFile("classpath:calls.json"));
        JSONAssert.assertEquals(
                serverInfoFileJson.toString(),
                response.getBody(), false);


    }


    @Test
    void should_get_germplasm_by_germplasmDbId() throws Exception {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("/brapi/v1/germplasm/dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI3ODA3"),
                HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        String accNumber = JsonPath.parse(response.getBody()).read("$.result.accessionNumber");
        assertThat(accNumber).isEqualTo("36785");
    }

//    To be implemented in July
//    @Test
//NB: onlyt part of the actual data are copied, especially for the site and panels ....
// Use LENIET JSONAssert
//    void should_get_germplasm_by_germplasmDbId_full_check() throws Exception {
//
//        HttpEntity<String> entity = new HttpEntity<>(null, headers);
//
//        ResponseEntity<String> response = testRestTemplate.exchange(
//                createURLWithPort("/brapi/v1/germplasm/dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzYxMDM5"),
//                HttpMethod.GET, entity, String.class);
//        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
//        String accNumber = JsonPath.parse(response.getBody()).read("$.result.accessionNumber");
//        assertThat(accNumber).isEqualTo("AO14022");
//        JSONObject actualgermplasmJson = new JSONObject(response.getBody()).getJSONObject("result");
//
//        assertThat(actualgermplasmJson).isNotNull();
//
//        JSONAssert.assertEquals(Fixtures.APACHE_GERMPLASM, actualgermplasmJson, JSONCompareMode.LENIENT);
//
//    }
//
    @Test
    void should_get_germplasm_by_accessionNumber_by_page_O_pageSize_1() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("/brapi/v1/germplasm?accessionNumber=36785&page=0&pageSize=1"),
                HttpMethod.GET, entity, String.class);
        String accNumber = JsonPath.parse(response.getBody()).read("$.result.data.[0].accessionNumber");
        assertThat(accNumber).isEqualTo("36785");
        Integer pageSize = JsonPath.parse(response.getBody()).read("$.metadata.pagination.pageSize");
        assertThat(pageSize).isEqualTo(1);
    }


    @Test
    void should_get_germplasm_by_accessionNumber() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("/brapi/v1/germplasm?accessionNumber=36785"),
                HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        String accNumber = JsonPath.parse(response.getBody()).read("$.result.data.[0].accessionNumber");
        assertThat(accNumber).isEqualTo("36785");
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
    void should_get_all_germplasm_first_page(){
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("/brapi/v1/germplasm"),
                HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Integer pageSize = JsonPath.parse(response.getBody()).read("$.metadata.pagination.pageSize");
        assertThat(pageSize).isEqualTo(10);
        Integer page = JsonPath.parse(response.getBody()).read("$.metadata.pagination.currentPage");
        assertThat(page).isEqualTo(0);
    }

    @Test
    void should_get_all_germplasm_2nd_page(){
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("/brapi/v1/germplasm?page=1"),
                HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Integer pageSize = JsonPath.parse(response.getBody()).read("$.metadata.pagination.pageSize");
        assertThat(pageSize).isEqualTo(10);
        Integer page = JsonPath.parse(response.getBody()).read("$.metadata.pagination.currentPage");
        assertThat(page).isEqualTo(1);
    }

    @Test
    void germplasmAttribute() {
    }
}
