package fr.inrae.urgi.faidare.api.brapi.v2;

import com.jayway.jsonpath.JsonPath;
import fr.inrae.urgi.faidare.Application;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
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
class TrialV2ControllerTest {

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
    void should_get_trial_by_trialDbId() throws Exception{
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = testRestTemplate.exchange(createURLWithPort("/brapi/v2/trials/dXJuOklOUkFFLVVSR0kvdHJpYWwvNw=="),
            HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        String trialName = JsonPath.parse(response.getBody()).read("$.result.trialName");
        assertThat(trialName).isEqualTo("INRA Wheat Network technological variables");
    }

    @Test
    void should_get_trial_by_trialName() throws Exception{
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = testRestTemplate.exchange(createURLWithPort("/brapi/v2/trials?trialName=POPYOMICS"),
            HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        String trialName = JsonPath.parse(response.getBody()).read("$.result.data.[0].trialName");
        assertThat(trialName).isEqualTo("POPYOMICS");

    }

    @Test
    void should_search_trial_by_dbids_POST() throws URISyntaxException {
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>("{\"trialDbIds\": [\"dXJuOklOUkFFLVVSR0kvdHJpYWwvMjQ=\"]}", headers);

        URI uri = new URI(createURLWithPort("/brapi/v2/search/trials"));
        ResponseEntity<String> response = testRestTemplate.postForEntity(uri, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    void should_get_studies_by_trialDbId() throws Exception{
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = testRestTemplate.exchange(createURLWithPort("/brapi/v2/trials/dXJuOklOUkFFLVVSR0kvdHJpYWwvNw==/studies"), HttpMethod.GET, entity, String.class);
        System.out.println(response.getBody());
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

    }

//    @Test
//    void should_search_trial_by_dbids_GET() throws Exception {
//        HttpEntity<String> entity = new HttpEntity<>(null, headers);
//
//        ResponseEntity<String> response = testRestTemplate.exchange(createURLWithPort("/brapi/v2/search/trial?trialDbId=dXJuOklOUkFFLVVSR0kvdHJpYWwvMjQ="), HttpMethod.GET, entity, String.class);
//        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
//    }
}
