package fr.inra.urgi.gpds.api.brapi.v1;

import fr.inra.urgi.gpds.api.NotFoundException;
import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiObservationVariable;
import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiOntology;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiListResponse;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiResponse;
import fr.inra.urgi.gpds.domain.criteria.ObservationVariableCriteria;
import fr.inra.urgi.gpds.domain.criteria.base.PaginationCriteriaImpl;
import fr.inra.urgi.gpds.domain.response.ApiResponseFactory;
import fr.inra.urgi.gpds.repository.file.CropOntologyRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author gcornut
 */
@Api(tags = {"Breeding API"}, description = "BrAPI endpoint")
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
    @ApiOperation("Get variable")
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
    @ApiOperation("List ontologies")
    @GetMapping("/brapi/v1/ontologies")
    public BrapiListResponse<? extends BrapiOntology> listOntologies(
        @Valid @ApiParam PaginationCriteriaImpl criteria
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
    @ApiOperation("List variables")
    @GetMapping("/brapi/v1/variables")
    public BrapiListResponse<? extends BrapiObservationVariable> listVariables(
        @Valid @ApiParam ObservationVariableCriteria criteria
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
