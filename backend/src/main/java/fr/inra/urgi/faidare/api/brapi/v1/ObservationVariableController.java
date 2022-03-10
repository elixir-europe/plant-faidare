package fr.inra.urgi.faidare.api.brapi.v1;

import java.util.List;
import javax.validation.Valid;

import fr.inra.urgi.faidare.api.NotFoundException;
import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiObservationVariable;
import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiOntology;
import fr.inra.urgi.faidare.domain.brapi.v1.response.BrapiListResponse;
import fr.inra.urgi.faidare.domain.brapi.v1.response.BrapiResponse;
import fr.inra.urgi.faidare.domain.criteria.ObservationVariableCriteria;
import fr.inra.urgi.faidare.domain.criteria.base.PaginationCriteriaImpl;
import fr.inra.urgi.faidare.domain.response.ApiResponseFactory;
import fr.inra.urgi.faidare.repository.file.CropOntologyRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gcornut
 */
@Tag(name = "Breeding API", description = "BrAPI endpoint")
@RestController
public class ObservationVariableController {

    private final CropOntologyRepository repository;

    @Autowired
    public ObservationVariableController(CropOntologyRepository repository) {
        this.repository = repository;
    }

    /**
     * @link https://github.com/plantbreeding/API/blob/master/Specification/ObservationVariables/VariableDetails.md
     */
    @Operation(summary = "Get variable")
    @GetMapping("/brapi/v1/variables/{observationVariableDbId}")
    public BrapiResponse<BrapiObservationVariable> getVariable(@PathVariable String observationVariableDbId) {
        BrapiObservationVariable variable = repository.getVariableById(observationVariableDbId);
        if (variable == null) {
            throw new NotFoundException("Variable not found for id '" + observationVariableDbId + "'");
        }
        return ApiResponseFactory.createSingleObjectResponse(variable, null);
    }

    /**
     * @link https://github.com/plantbreeding/API/blob/master/Specification/ObservationVariables/VariableOntologyList.md
     */
    @Operation(summary = "List ontologies")
    @GetMapping("/brapi/v1/ontologies")
    public BrapiListResponse<? extends BrapiOntology> listOntologies(
        @Valid @Parameter PaginationCriteriaImpl criteria
    ) {
        List<? extends BrapiOntology> ontologies = repository.getOntologies();

        return ApiResponseFactory.createSubListResponse(
            criteria.getPageSize(), criteria.getPage(),
            ontologies
        );
    }

    /**
     * @link https://github.com/plantbreeding/API/blob/master/Specification/ObservationVariables/VariableList.md
     */
    @Operation(summary = "List variables")
    @GetMapping("/brapi/v1/variables")
    public BrapiListResponse<? extends BrapiObservationVariable> listVariables(
        @Valid @Parameter ObservationVariableCriteria criteria
    ) {
        // Get variables by trait class or get all variables
        List<? extends BrapiObservationVariable> variables;
        if (StringUtils.isNotBlank(criteria.getTraitClass())) {
            variables = repository.getVariablesByTraitClass(criteria.getTraitClass());
        } else {
            variables = repository.getVariables();
        }

        return ApiResponseFactory.createSubListResponse(
            criteria.getPageSize(), criteria.getPage(),
            variables
        );
    }

}
