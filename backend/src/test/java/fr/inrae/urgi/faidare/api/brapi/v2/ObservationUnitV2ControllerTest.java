package fr.inrae.urgi.faidare.api.brapi.v2;

import com.jayway.jsonpath.JsonPath;
import fr.inrae.urgi.faidare.Application;
import fr.inrae.urgi.faidare.dao.v2.ObservationUnitV2Dao;
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

@SpringBootTest(classes = Application .class,
webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ObservationUnitV2ControllerTest {

    @Autowired
    protected ObservationUnitV2Dao observationUnitDao;

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
    void should_get_observationUnits_by_observationUnitDbId() throws Exception {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
            createURLWithPort("/brapi/v2/observationunits/dXJuOklOUkFFLVVSR0kvb2JzZXJ2YXRpb25Vbml0LzMzNDEyNTItMjAxMw=="),
            HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        String obsUnitName = JsonPath.parse(response.getBody()).read("$.result.observationUnitName");
        assertThat(obsUnitName).isEqualTo("3132water_regimewatered");
    }

    @Test
    void should_get_obsUnits_by_criteria() throws Exception{

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = testRestTemplate.exchange(
            createURLWithPort("/brapi/v2/observationunits?observationUnitName=402477"),
            HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        String obsUnitName = JsonPath.parse(response.getBody()).read("$.result.data.[0].observationUnitName");
        assertThat(obsUnitName).isEqualTo("402477");
//        String studyDbId = JsonPath.parse(response.getBody()).read("$.result.data.[0].studyDbId");
//        assertThat(studyDbId).isEqualTo("dXJuOklOUkFFLVVSR0kvc3R1ZHkvQ3JhMTI");
//        String trialName = JsonPath.parse(response.getBody()).read("$.result.data.[0].trialName");
//        assertThat(trialName).isEqualTo("Drops Phenotyping Network");
//        Integer pageSize = JsonPath.parse(response.getBody()).read("$.metadata.pagination.pageSize");
//        assertThat(pageSize).isEqualTo(1);

    }



    @Test
    void should_get_germplasm_by_accessionNumber_by_page_O_pageSize_1() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
            createURLWithPort("/brapi/v2/germplasm?accessionNumber=32100&page=0&pageSize=1"),
            HttpMethod.GET, entity, String.class);
        String accNumber = JsonPath.parse(response.getBody()).read("$.result.data.[0].accessionNumber");
        assertThat(accNumber).isEqualTo("32100");
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
//
//    @Test
//    void should_get_obsLevels_by_criteria() throws Exception{
//
//        HttpEntity<String> entity = new HttpEntity<>(null, headers);
//        ResponseEntity<String> response = testRestTemplate.exchange(
//            createURLWithPort("/brapi/v2/observationlevels?trialDbId=dXJuOklOUkFFLVVSR0kvdHJpYWwvNDI=&studyDbId=dXJuOklOUkFFLVVSR0kvc3R1ZHkvQ2FtMTM==page=0&pageSize=1"),
//            HttpMethod.GET, entity, String.class);
//        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
//        String obsUnitName = JsonPath.parse(response.getBody()).read("$.result.data.[0].observationUnitName");
//        assertThat(obsUnitName).isEqualTo("402477");
//        String studyDbId = JsonPath.parse(response.getBody()).read("$.result.data.[0].studyDbId");
//        assertThat(studyDbId).isEqualTo("dXJuOklOUkFFLVVSR0kvc3R1ZHkvQ3JhMTI");
//        String trialName = JsonPath.parse(response.getBody()).read("$.result.data.[0].trialName");
//        assertThat(trialName).isEqualTo("Drops Phenotyping Network");
//        Integer pageSize = JsonPath.parse(response.getBody()).read("$.metadata.pagination.pageSize");
//        assertThat(pageSize).isEqualTo(1);
//
//    }


}
