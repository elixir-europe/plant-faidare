package fr.inrae.urgi.faidare.api.brapi.v2;

import com.jayway.jsonpath.JsonPath;
import fr.inrae.urgi.faidare.Application;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest (classes = Application.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class StudyV2ControllerTest {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @LocalServerPort
    private int port;

    HttpHeaders headers = new HttpHeaders();
    TestRestTemplate testRestTemplate = new TestRestTemplate();

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + contextPath + uri;
    }

    @Test
    void should_get_study_by_studyDbId() throws Exception{
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = testRestTemplate.exchange(createURLWithPort("/brapi/v2/study/dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0VzdHIlQzMlQTllcy1Nb25zXzIwMTBfVEVDSA=="),
            HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        String studyName = JsonPath.parse(response.getBody()).read("$.result.studyName");
        assertThat(studyName).isEqualTo("BTH_Estrées-Mons_2010_TECH");

    }

    @Test
    void should_get_study_by_studyName() throws Exception{
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = testRestTemplate.exchange(createURLWithPort("/brapi/v2/study?studyName=BTH_Estrées-Mons_2004_TECH"),
            HttpMethod.GET, entity, String.class);
        String studyName = JsonPath.parse(response.getBody()).read("$.result.data.[0].studyName");
        assertThat(studyName).isEqualTo("BTH_Estrées-Mons_2004_TECH");

    }
    @Test
    void should_get_study_by_studyName_locationDbId_locationName() throws Exception{
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = testRestTemplate.exchange(createURLWithPort("/brapi/v2/study?studyName=BTH_Le_Moulon_2004_TECH&locationDbId=dXJuOklOUkFFLVVSR0kvbG9jYXRpb24vMzQwNjQ=&locationName=Le Moulon"),
            HttpMethod.GET, entity, String.class);
        String studyName = JsonPath.parse(response.getBody()).read("$.result.data.[0].studyName");
        assertThat(studyName).isEqualTo("BTH_Le_Moulon_2004_TECH");

    }

    @Test
    void should_search_study_by_dbids_POST() throws URISyntaxException {
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>("{\"studyDbIds\": [\"dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0x1c2lnbmFuXzIwMDRfVEVDSA==\"]}", headers);

        URI postRequestUri = new URI(createURLWithPort("/brapi/v2/search/study"));
        ResponseEntity<String> response = testRestTemplate.postForEntity(postRequestUri, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }
//    @Test
//    void should_search_study_by_dbids_GET() throws URISyntaxException {
//        HttpEntity<String> entity = new HttpEntity<>(null, headers);
//
//        ResponseEntity<String> response = testRestTemplate.exchange(createURLWithPort("/brapi/v2/search/study?studyDbId=dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0x1c2lnbmFuXzIwMDRfVEVDSA=="), HttpMethod.GET, entity, String.class);
//        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
//    }

    @Test
    void should_get_studies_by_trialDbId() throws Exception{
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = testRestTemplate.exchange(createURLWithPort("/brapi/v2/studies/dXJuOklOUkFFLVVSR0kvdHJpYWwvNw=="), HttpMethod.GET, entity, String.class);
        System.out.println(response.getBody());
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

    }

}
