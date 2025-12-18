package fr.inrae.urgi.faidare.api.brapi.v1;

/**
 * Unit tests for {@link GermplasmController}
 *
 * @author Cpommier
 */

import com.jayway.jsonpath.JsonPath;
import fr.inrae.urgi.faidare.Application;
import fr.inrae.urgi.faidare.domain.variable.ObservationVariableV1VO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ObservationVariableV1ControllerTest {

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
    void should_call_observationVariable_by_id() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
            createURLWithPort("/brapi/v1/variables/CO_321:1000074"),
            HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        String obsVarName = JsonPath.parse(response.getBody()).read("$.result.name");
        assertThat(obsVarName).isEqualTo("GY_q/ha");
        String traitDbId = JsonPath.parse(response.getBody()).read("$.result.trait.traitDbId");
        assertThat(traitDbId).isEqualTo("CO_321:0000013");
        String ontologyName = JsonPath.parse(response.getBody()).read("$.result.ontologyName");
        assertThat(ontologyName).isNotEqualTo("Foo Ontology");
    }

    @Test
    void should_call_ontologies() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
            createURLWithPort("/brapi/v1/ontologies"),
            HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        List<String> ontologyNames = JsonPath.parse(response.getBody()).read("$.result.data[*].ontologyName");
        assertThat(ontologyNames).contains("Wheat Crop Ontology");
        List<String> ontologyIds = JsonPath.parse(response.getBody()).read("$.result.data[*].ontologyDbId");
        assertThat(ontologyIds).contains("CO_321");
        int totalCount = JsonPath.parse(response.getBody()).read("$.metadata.pagination.totalCount");
        int page = JsonPath.parse(response.getBody()).read("$.metadata.pagination.currentPage");
        int pageSize = JsonPath.parse(response.getBody()).read("$.metadata.pagination.pageSize");
        assertThat(totalCount).as("totalCount check").isGreaterThan(10);
        assertThat(page).as("page check").isEqualTo(0);
        assertThat(pageSize).as("pageSize check").isEqualTo(1000);
    }

    @Test
    void should_call_variables() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
            createURLWithPort("/brapi/v1/variables?page=0&pageSize=10000"),
            HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        List<String> variableNames = JsonPath.parse(response.getBody()).read("$.result.data[*].name");
        assertThat(variableNames).contains("GY_q/ha");
        assertThat(variableNames).doesNotContain("foo");
        List<String> traitDbIds = JsonPath.parse(response.getBody()).read("$.result.data[*].trait.traitDbId");
        assertThat(traitDbIds).contains("CO_321:0000013");
    }


    @Test
    void should_paginate_variables() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
            createURLWithPort("/brapi/v1/variables?page=1&pageSize=8"),
            HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        List<ObservationVariableV1VO> variables = JsonPath.parse(response.getBody()).read("$.result.data[*]");
        assertThat(variables).isNotNull();
        assertThat(variables).isNotEmpty();
        assertThat(variables).hasSize(8);

    }


    @Test
    void should_paginate_variablesTraitClass_with_total_count() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
            createURLWithPort("/brapi/v1/variables?page=1&pageSize=8&traitClass=Biotic stress"),
            HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        int totalCount = JsonPath.parse(response.getBody()).read("$.metadata.pagination.totalCount");
        int page = JsonPath.parse(response.getBody()).read("$.metadata.pagination.currentPage");
        int pageSize = JsonPath.parse(response.getBody()).read("$.metadata.pagination.pageSize");
        assertThat(totalCount).as("totalCount check").isGreaterThan(100);
        assertThat(page).as("page check").isEqualTo(1);
        assertThat(pageSize).as("pageSize check").isEqualTo(8);

    }


//    @Test
//    void should_call_variables_with_traitClass() throws Exception {
//        HttpEntity<String> entity = new HttpEntity<>(null, headers);
//
//        ResponseEntity<String> response = testRestTemplate.exchange(
//            createURLWithPort("/brapi/v1/variables?traitClass=Biotic stress"),
//            HttpMethod.GET, entity, String.class);
//        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
//        List<String> variableNames = JsonPath.parse(response.getBody()).read("$.result.data[*].name");
//        assertThat(variableNames).contains("Canker_GI");
//        assertThat(variableNames).doesNotContain("foo");
//        List<String> traitDbIds = JsonPath.parse(response.getBody()).read("$.result.data[*].trait.traitDbId");
//        assertThat(traitDbIds).contains("CO_321:1010097");
//    }


    @Test
    void should_call_variables_with_traitClass_urlencoded() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
            createURLWithPort("/brapi/v1/variables?traitClass=Biotic%20stress&page=0&pageSize=10000"),
            HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        List<String> variableNames = JsonPath.parse(response.getBody()).read("$.result.data[*].name");
        assertThat(variableNames).contains("FUS-SPK.350DD_score");
        assertThat(variableNames).doesNotContain("foo");
        List<String> traitDbIds = JsonPath.parse(response.getBody()).read("$.result.data[*].trait.traitDbId");
        assertThat(traitDbIds).contains("CO_321:1010097");
    }
}
