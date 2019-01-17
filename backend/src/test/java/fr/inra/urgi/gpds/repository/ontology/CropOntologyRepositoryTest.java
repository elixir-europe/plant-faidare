package fr.inra.urgi.gpds.repository.ontology;

import com.google.common.collect.Sets;
import fr.inra.urgi.gpds.config.GPDSProperties;
import fr.inra.urgi.gpds.domain.data.impl.variable.ObservationVariableVO;
import fr.inra.urgi.gpds.domain.data.impl.variable.OntologyVO;
import fr.inra.urgi.gpds.repository.file.CropOntologyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Test CropOntologyRepository service on a test fixture repository
 * see the 'application-test.properties' file
 *
 * @author gcornut
 */
@ExtendWith(SpringExtension.class)
class CropOntologyRepositoryTest {

	@Autowired
    GPDSProperties properties;

	@Autowired
    CropOntologyRepository ontologyRepository;

	@Test
    void should_Return_All_Ontologies() {
		List<OntologyVO> ontologies = ontologyRepository.getOntologies();
		assertThat(ontologies).isNotNull().isNotEmpty()
				.hasSize(3)
				.extracting("ontologyName").containsOnly(
					"Woody Plant Ontology",
					"Vitis inra ontology",
					"Wheat Inra Phenotype Ontology"
				);
	}

	@Test
    void should_Return_All_Variables() {
		List<ObservationVariableVO> variables = ontologyRepository.getVariables();
		assertThat(variables).isNotNull().isNotEmpty()
				.hasSize(655)
				.extracting("name").contains("MI-DEB-relatif");

		// Check that the trait class and method class mapping succeeded
		assertThat(variables)
				.extracting("method.methodClass").isNotEmpty();
		assertThat(variables)
				.extracting("trait.traitClass").isNotEmpty();
	}

	@Test
    void should_Return_Phenology_Variables() {
		String traitClass = "Phenological";
		List<ObservationVariableVO> variables = ontologyRepository
				.getVariablesByTraitClass(traitClass);
		assertThat(variables).isNotNull().isNotEmpty()
				.extracting("name").contains("MI-DEB-relatif");
	}

	@Test
    void should_Return_Variable_By_Identifier() {
		String identifier = "WIPO:0000033";
		ObservationVariableVO variable = ontologyRepository
				.getVariableById(identifier);
		assertThat(variable).isNotNull();
		assertThat(variable.getName()).isEqualTo("D.Z30_d");
	}

	@Test
    void should_Return_Variable_By_Identifiers() {
		Set<String> identifiers = Sets.newHashSet("WIPO:0000033", "WIPO:0000032");
		List<ObservationVariableVO> variable = ontologyRepository
				.getVariableByIds(identifiers);
		assertThat(variable).isNotNull().isNotEmpty();
		assertThat(variable).extracting("name").containsExactly("D.Z30_jj/mm/aa", "D.Z30_d");
	}
}
