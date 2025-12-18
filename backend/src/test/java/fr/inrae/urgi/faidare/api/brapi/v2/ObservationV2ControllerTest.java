package fr.inrae.urgi.faidare.api.brapi.v2;

import com.jayway.jsonpath.JsonPath;
import fr.inrae.urgi.faidare.Application;
import fr.inrae.urgi.faidare.dao.v2.ObservationV2Dao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = Application.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ObservationV2ControllerTest {

    @Autowired
    protected ObservationV2Dao observationDao;

    TestRestTemplate testRestTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();

    @Value("${server.servlet.context-path}")
    private String contextPath;


    @LocalServerPort
    private int port;

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + contextPath + uri;
    }
    @Test
    void should_get_observations_by_observationDbId() throws Exception {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
            createURLWithPort("/brapi/v2/observations/dXJuOklOUkFFLVVSR0kvb2JzZXJ2YXRpb24vNzAwOTQwMA=="),
            HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        String value = JsonPath.parse(response.getBody()).read("$.result.value");
        assertThat(value).isEqualTo("88.0415676541");
    }

    @Test
    void should_get_observations_by_criteria() throws Exception{

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = testRestTemplate.exchange(
            createURLWithPort("/brapi/v2/observations?value=248.8376451592&observationUnitDbIds=dXJuOklOUkFFLVVSR0kvb2JzZXJ2YXRpb25Vbml0LzMzMzY3MzEtMjAxMw==&trialNames=Drops Phenotyping Network&page=0&pageSize=1"),
            HttpMethod.GET, entity, String.class);
        System.out.println(response.getBody());
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        String obsUnitName = JsonPath.parse(response.getBody()).read("$.result.data.[0].observationUnitName");
        assertThat(obsUnitName).isEqualTo("3170water_regimerainfed");
        String studyDbId = JsonPath.parse(response.getBody()).read("$.result.data.[0].studyDbId");
        assertThat(studyDbId).isEqualTo("dXJuOklOUkFFLVVSR0kvc3R1ZHkvRGViMTM=");
        String trialName = JsonPath.parse(response.getBody()).read("$.result.data.[0].trialName");
        assertThat(trialName).isEqualTo("Drops Phenotyping Network");
        Integer pageSize = JsonPath.parse(response.getBody()).read("$.metadata.pagination.pageSize");
        assertThat(pageSize).isEqualTo(1);

    }

    @Test
    void should_get_observations_by_observationUnitDbId() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = testRestTemplate.exchange(
            createURLWithPort("/brapi/v2/observations?observationUnitDbId=dXJuOklOUkFFLVVSR0kvb2JzZXJ2YXRpb25Vbml0LzMzMzY3MzEtMjAxMw=="),
            HttpMethod.GET, entity, String.class);
        System.out.println(response.getBody());
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        String obsUnitName = JsonPath.parse(response.getBody()).read("$.result.data.[0].observationUnitName");
        assertThat(obsUnitName).isEqualTo("3170water_regimerainfed");

    }

}
