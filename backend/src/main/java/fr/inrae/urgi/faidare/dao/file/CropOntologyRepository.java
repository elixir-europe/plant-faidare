package fr.inrae.urgi.faidare.dao.file;

import java.util.List;
import java.util.Set;

import fr.inrae.urgi.faidare.domain.variable.ObservationVariableVO;
import fr.inrae.urgi.faidare.domain.variable.OntologyVO;

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
