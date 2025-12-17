package fr.inrae.urgi.faidare.dao.file;

import java.util.List;
import java.util.Set;

import fr.inrae.urgi.faidare.domain.variable.ObservationVariableV1VO;
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
    List<ObservationVariableV1VO> getVariables();
    Integer getVariablesCount();

    /**
     * Get variables with specified trait class
     */
    List<ObservationVariableV1VO> getVariablesByTraitClass(String searchedTraitClass);
    Integer getVariablesByTraitClassCount(String searchedTraitClass);

    /**
     * Get variable by identifier
     */
    ObservationVariableV1VO getVariableById(String identifier);

    /**
     * Get variables matching the specified list of identifiers
     */
    List<ObservationVariableV1VO> getVariableByIds(Set<String> identifiers);

}
