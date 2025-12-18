package fr.inrae.urgi.faidare.api.brapi.v2;

import com.jayway.jsonpath.JsonPath;
import fr.inrae.urgi.faidare.Application;
import fr.inrae.urgi.faidare.dao.v2.LocationV2Dao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LocationV2ControllerTest {
    @Autowired
    protected LocationV2Dao locationV2Dao;

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
    void should_get_location_by_locationDbId() throws Exception {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
            createURLWithPort("/brapi/v2/locations/dXJuOklOUkFFLVVSR0kvbG9jYXRpb24vMzQwNjU="),
            HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        String locationName = JsonPath.parse(response.getBody()).read("$.result.locationName");
        assertThat(locationName).isEqualTo("Orgeval");
    }

    @Test
    void should_get_location_by_criteria() throws Exception{

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = testRestTemplate.exchange(
            createURLWithPort("/brapi/v2/search/locations?locationName=Rennes&countryCode=FRA&locationType=Collecting and Evaluation site&page=0&pageSize=1"),
            HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        String locationName = JsonPath.parse(response.getBody()).read("$.result.data.[0].locationName");
        assertThat(locationName).isEqualTo("Rennes");
        String countryCode = JsonPath.parse(response.getBody()).read("$.result.data.[0].countryCode");
        assertThat(countryCode).isEqualTo("FRA");
        String locationType = JsonPath.parse(response.getBody()).read("$.result.data.[0].locationType");
        assertThat(locationType).isEqualTo("Collecting and Evaluation site");
        Integer pageSize = JsonPath.parse(response.getBody()).read("$.metadata.pagination.pageSize");
        assertThat(pageSize).isEqualTo(1);

    }

    @Test
    void should_get_location_by_locationName() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
            createURLWithPort("/brapi/v2/search/locations?locationName=Ardon"),
            HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        String locationName = JsonPath.parse(response.getBody()).read("$.result.data.[0].locationName");
        assertThat(locationName).isEqualTo("Ardon");
    }

    @Test
    void should_get_location_by_countryCode() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
            createURLWithPort("/brapi/v2/search/locations?countryCode=GBR"),
            HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        String countryCode = JsonPath.parse(response.getBody()).read("$.result.data.[0].countryCode");
        assertThat(countryCode).isEqualTo("GBR");
    }

    @Test
    void should_get_location_by_countryName() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
            createURLWithPort("/brapi/v2/search/locations?countryName=United Kingdom of Great Britain and Northern Ireland"),
            HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        String countryName = JsonPath.parse(response.getBody()).read("$.result.data.[0].countryName");
        assertThat(countryName).isEqualTo("United Kingdom of Great Britain and Northern Ireland");
    }

    @Test
    void should_get_location_by_locationType() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
            createURLWithPort("/brapi/v2/search/locations?locationType=Evaluation site"),
            HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        String locationType = JsonPath.parse(response.getBody()).read("$.result.data.[0].locationType");
        assertThat(locationType).isEqualTo("Evaluation site");
    }

    @Test
    void should_search_location_by_dbids() throws URISyntaxException {
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>("{\"locationDbId\": [\"dXJuOklOUkFFLVVSR0kvbG9jYXRpb24vMzM4MTg=\"]}", headers);

        URI uri = new URI(createURLWithPort("/brapi/v2/search/locations"));
        ResponseEntity<String> response = testRestTemplate.postForEntity(uri, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        String locationName = JsonPath.parse(response.getBody()).read("$.result.data.[0].locationName");
        assertThat(locationName).isEqualTo("Dijon");

    }


}
