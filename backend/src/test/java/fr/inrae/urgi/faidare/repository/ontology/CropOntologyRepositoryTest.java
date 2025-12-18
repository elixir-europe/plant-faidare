package fr.inrae.urgi.faidare.repository.ontology;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;
import fr.inrae.urgi.faidare.dao.file.CropOntologyRepository;
import fr.inrae.urgi.faidare.dao.file.CropOntologyRepositoryImpl;
import fr.inrae.urgi.faidare.domain.variable.ObservationVariableV1VO;
import fr.inrae.urgi.faidare.domain.variable.OntologyVO;
import fr.inrae.urgi.faidare.domain.variable.TraitVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.test.autoconfigure.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import tools.jackson.databind.ObjectMapper;


/**
 * Test CropOntologyRepository
 *
 * @author gcornut
 */
@RestClientTest(
    value = CropOntologyRepository.class,
    properties = {
        "faidare.cropOntologyRepositoryUrl=http://repo.com/ontology-repository.json",
        "faidare.cropOntologyPortalLink=http://repo.com/ontology#termIdentifier="
    }
)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CropOntologyRepositoryTest {

    @Autowired
    private CropOntologyRepositoryImpl repository;

    @Autowired
    private MockRestServiceServer mockServer;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void should_Return_All_Ontologies() {
        List<OntologyVO> ontologies = mockOntologies();
        mockServer.expect(requestTo("http://repo.com/ontology-repository.json")).andRespond(
            withSuccess(objectMapper.writeValueAsBytes(ontologies), MediaType.APPLICATION_JSON)
        );

        List<OntologyVO> actualOntologies = repository.getOntologies();
        assertThat(actualOntologies).isNotNull().isNotEmpty()
            .hasSize(3)
            .isEqualTo(ontologies);
    }

    @Test
    void should_Return_All_Variables() {
        List<ObservationVariableV1VO> expectedVariables = mockVariables();

        List<ObservationVariableV1VO> actualVariables = repository.getVariables();
        assertThat(actualVariables).extracting(ObservationVariableV1VO::getObservationVariableDbId)
            .isEqualTo(expectedVariables.stream().map(ObservationVariableV1VO::getObservationVariableDbId).toList());

        assertThat(actualVariables).extracting(ObservationVariableV1VO::getDocumentationURL).doesNotContainNull();
    }

    @Test
    void should_Return_Phenology_Variables() {
        List<ObservationVariableV1VO> expectedVariables = mockVariables();

        String traitClass = "Phenological";
        List<ObservationVariableV1VO> variables = repository.getVariablesByTraitClass(traitClass);

        assertThat(variables)
            .extracting(ObservationVariableV1VO::getObservationVariableDbId)
            .containsExactly("WIPO:0000033", "WIPO:0000032");
    }

    @Test
    void should_Return_Variable_By_Identifier() {
        List<ObservationVariableV1VO> expectedVariables = mockVariables();

        ObservationVariableV1VO expectedVariable = expectedVariables.get(0);
        String identifier = expectedVariable.getObservationVariableDbId();

        ObservationVariableV1VO actualVariable = repository.getVariableById(identifier);

        assertThat(actualVariable).isNotNull();
        assertThat(actualVariable.getObservationVariableDbId()).isEqualTo(identifier);
        assertThat(actualVariable.getDocumentationURL()).isEqualTo("http://repo.com/ontology#termIdentifier="+identifier);
    }

    @Test
    void should_Return_Variable_By_Identifiers() {
        List<ObservationVariableV1VO> expectedVariables = mockVariables();

        ObservationVariableV1VO expectedVariable1 = expectedVariables.get(0);
        ObservationVariableV1VO expectedVariable2 = expectedVariables.get(1);

        Set<String> identifiers = Sets.newHashSet(
            expectedVariable1.getObservationVariableDbId(),
            expectedVariable2.getObservationVariableDbId()
        );

        List<ObservationVariableV1VO> actualVariables = repository.getVariableByIds(identifiers);

        assertThat(actualVariables).isNotNull().hasSize(2);
        assertThat(actualVariables).extracting(ObservationVariableV1VO::getObservationVariableDbId)
            .containsExactly(expectedVariable1.getObservationVariableDbId(),
                expectedVariable2.getObservationVariableDbId());
    }

    private List<OntologyVO> mockOntologies() {
        OntologyVO ontology3 = new OntologyVO();
        ontology3.setOntologyDbId("3");
        ontology3.setOntologyName("O3");

        OntologyVO ontology2 = new OntologyVO();
        ontology2.setOntologyDbId("2");
        ontology2.setOntologyName("O2");

        OntologyVO ontology1 = new OntologyVO();
        ontology1.setOntologyDbId("1");
        ontology1.setOntologyName("O1");

        OntologyVO[] expectedOntologies = new OntologyVO[]{
            ontology1, ontology2, ontology3
        };

        return Arrays.asList(expectedOntologies);
    }

    private List<ObservationVariableV1VO> mockVariables() {
        List<OntologyVO> ontologies = mockOntologies();

        // Response for ontology 1
        OntologyVO ontology1 = ontologies.get(0);

        TraitVO trait = new TraitVO();
        trait.setTraitClass("Phenological");

        ObservationVariableV1VO variable1 = new ObservationVariableV1VO();
        variable1.setOntologyDbId(ontology1.getOntologyDbId());
        variable1.setObservationVariableDbId("WIPO:0000033");
        variable1.setTrait(trait);

        ObservationVariableV1VO variable2 = new ObservationVariableV1VO();
        variable2.setOntologyDbId(ontology1.getOntologyDbId());
        variable2.setObservationVariableDbId("WIPO:0000032");
        variable2.setTrait(trait);

        ObservationVariableV1VO variable3 = new ObservationVariableV1VO();
        variable3.setOntologyDbId(ontology1.getOntologyDbId());

        ObservationVariableV1VO[] response1 = new ObservationVariableV1VO[]{
            variable1, variable2, variable3
        };

        // Response for ontology 2
        OntologyVO ontology2 = ontologies.get(1);
        ObservationVariableV1VO variable4 = new ObservationVariableV1VO();
        variable4.setOntologyDbId(ontology2.getOntologyDbId());

        ObservationVariableV1VO[] response2 = new ObservationVariableV1VO[]{
            variable4
        };

        // Response for ontology 3
        ObservationVariableV1VO[] response3 = new ObservationVariableV1VO[]{};

        mockServer.expect(requestTo("http://repo.com/1-O1.json")).andRespond(
            withSuccess(objectMapper.writeValueAsBytes(response1), MediaType.APPLICATION_JSON)
        );
        mockServer.expect(requestTo("http://repo.com/2-O2.json")).andRespond(
            withSuccess(objectMapper.writeValueAsBytes(response2), MediaType.APPLICATION_JSON)
        );
        mockServer.expect(requestTo("http://repo.com/3-O3.json")).andRespond(
            withSuccess(objectMapper.writeValueAsBytes(response3), MediaType.APPLICATION_JSON)
        );

        return Arrays.asList(
            variable1, variable2, variable3, variable4
        );
    }
}
