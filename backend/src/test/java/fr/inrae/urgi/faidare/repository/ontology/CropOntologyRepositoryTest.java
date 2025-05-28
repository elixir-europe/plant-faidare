package fr.inrae.urgi.faidare.repository.ontology;

import com.google.common.collect.Sets;
import fr.inrae.urgi.faidare.config.FaidareProperties;
import fr.inrae.urgi.faidare.dao.file.CropOntologyRepositoryImpl;
import fr.inrae.urgi.faidare.domain.variable.ObservationVariableVO;
import fr.inrae.urgi.faidare.domain.variable.OntologyVO;
import fr.inrae.urgi.faidare.domain.variable.TraitVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;


/**
 * Test CropOntologyRepository
 *
 * @author gcornut
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CropOntologyRepositoryTest {

    @Mock
    FaidareProperties properties;

    @Mock
    RestTemplate restClient;

    @InjectMocks
    CropOntologyRepositoryImpl repository;

    @Test
    void should_Return_All_Ontologies() {
        List<OntologyVO> expectedOntologies = mockOntologies();

        List<OntologyVO> actualOntologies = repository.getOntologies();
        assertThat(actualOntologies).isNotNull().isNotEmpty()
            .hasSize(3)
            .isEqualTo(expectedOntologies);
    }

    @Test
    void should_Return_All_Variables() {
        List<ObservationVariableVO> expectedVariables = mockVariables();

        List<ObservationVariableVO> actualVariables = repository.getVariables();
        assertThat(actualVariables).isEqualTo(expectedVariables);

        assertThat(actualVariables).extracting("documentationURL").doesNotContainNull();
    }

    @Test
    void should_Return_Phenology_Variables() {
        mockVariables();

        String traitClass = "Phenological";
        List<ObservationVariableVO> variables = repository.getVariablesByTraitClass(traitClass);

        assertThat(variables).isNotNull().isNotEmpty()
            .extracting("observationVariableDbId").contains("WIPO:0000033", "WIPO:0000032");
    }

    @Test
    void should_Return_Variable_By_Identifier() {
        List<ObservationVariableVO> expectedVariables = mockVariables();

        ObservationVariableVO expectedVariable = expectedVariables.get(0);
        String identifier = expectedVariable.getObservationVariableDbId();

        ObservationVariableVO actualVariable = repository.getVariableById(identifier);

        assertThat(actualVariable).isNotNull();
        assertThat(actualVariable.getObservationVariableDbId()).isEqualTo(identifier);
        assertThat(actualVariable.getDocumentationURL()).isEqualTo("http://repo.com/ontology#termIdentifier="+identifier);
    }

    @Test
    void should_Return_Variable_By_Identifiers() {
        List<ObservationVariableVO> expectedVariables = mockVariables();

        ObservationVariableVO expectedVariable1 = expectedVariables.get(0);
        ObservationVariableVO expectedVariable2 = expectedVariables.get(1);

        Set<String> identifiers = Sets.newHashSet(
            expectedVariable1.getObservationVariableDbId(),
            expectedVariable2.getObservationVariableDbId()
        );

        List<ObservationVariableVO> actualVariables = repository.getVariableByIds(identifiers);

        assertThat(actualVariables).isNotNull().hasSize(2);
        assertThat(actualVariables).extracting("observationVariableDbId")
            .containsExactly(expectedVariable1.getObservationVariableDbId(),
                expectedVariable2.getObservationVariableDbId());
    }


    private List<OntologyVO> mockOntologies() {
        String repositoryUrl = "ontology-repository.json";
        when(properties.getCropOntologyRepositoryUrl()).thenReturn(repositoryUrl);

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

        ResponseEntity<OntologyVO[]> response = new ResponseEntity<>(expectedOntologies, HttpStatus.OK);
        when(restClient.getForEntity(repositoryUrl, OntologyVO[].class))
            .thenReturn(response);

        return Arrays.asList(expectedOntologies);
    }

    private List<ObservationVariableVO> mockVariables() {
        when(properties.getCropOntologyPortalLink()).thenReturn("http://repo.com/ontology#termIdentifier=");
        List<OntologyVO> ontologies = mockOntologies();

        // Response for ontology 1
        OntologyVO ontology1 = ontologies.get(0);

        TraitVO trait = new TraitVO();
        trait.setTraitClass("Phenological");

        ObservationVariableVO variable1 = new ObservationVariableVO();
        variable1.setOntologyDbId(ontology1.getOntologyDbId());
        variable1.setObservationVariableDbId("WIPO:0000033");
        variable1.setTrait(trait);

        ObservationVariableVO variable2 = new ObservationVariableVO();
        variable2.setOntologyDbId(ontology1.getOntologyDbId());
        variable2.setObservationVariableDbId("WIPO:0000032");
        variable2.setTrait(trait);

        ObservationVariableVO variable3 = new ObservationVariableVO();
        variable3.setOntologyDbId(ontology1.getOntologyDbId());

        ResponseEntity<ObservationVariableVO[]> response1 = new ResponseEntity<>(new ObservationVariableVO[]{
            variable1, variable2, variable3
        }, HttpStatus.OK);
        doReturn(response1).when(restClient).getForEntity("1-O1.json", ObservationVariableVO[].class);

        // Response for ontology 2
        OntologyVO ontology2 = ontologies.get(1);
        ObservationVariableVO variable4 = new ObservationVariableVO();
        variable4.setOntologyDbId(ontology2.getOntologyDbId());

        ResponseEntity<ObservationVariableVO[]> response2 = new ResponseEntity<>(new ObservationVariableVO[]{
            variable4
        }, HttpStatus.OK);
        doReturn(response2).when(restClient).getForEntity("2-O2.json", ObservationVariableVO[].class);

        // Response for ontology 3
        ResponseEntity<ObservationVariableVO[]> response3 = new ResponseEntity<>(new ObservationVariableVO[]{}, HttpStatus.OK);
        doReturn(response3).when(restClient).getForEntity("3-O3.json", ObservationVariableVO[].class);

        return Arrays.asList(
            variable1, variable2, variable3, variable4
        );
    }
}
