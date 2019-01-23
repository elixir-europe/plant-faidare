package fr.inra.urgi.gpds.repository.file;

import fr.inra.urgi.gpds.domain.data.impl.variable.ObservationVariableVO;
import fr.inra.urgi.gpds.domain.data.impl.variable.OntologyVO;

import java.util.List;
import java.util.Set;

/**
 * @author gcornut
 */
public interface CropOntologyRepository {

    /**
     * List all ontologies
     */
    List<OntologyVO> getOntologies();

    /**
     * List all variables
     */
    List<ObservationVariableVO> getVariables();

    /**
     * Get variables with specified trait class
     */
    List<ObservationVariableVO> getVariablesByTraitClass(String searchedTraitClass);

    /**
     * Get variable by identifier
     */
    ObservationVariableVO getVariableById(String identifier);

    /**
     * Get variables matching the specified list of identifiers
     */
    List<ObservationVariableVO> getVariableByIds(Set<String> identifiers);

}
