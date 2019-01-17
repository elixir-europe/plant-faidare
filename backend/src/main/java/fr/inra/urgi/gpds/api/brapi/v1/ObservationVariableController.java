package fr.inra.urgi.gpds.api.brapi.v1;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.api.NotFoundException;
import fr.inra.urgi.gpds.domain.JSONView;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiListResponse;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiResponse;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiResponseFactory;
import fr.inra.urgi.gpds.domain.criteria.ObservationVariableCriteria;
import fr.inra.urgi.gpds.domain.criteria.base.PaginationCriteriaImpl;
import fr.inra.urgi.gpds.domain.data.impl.variable.ObservationVariableVO;
import fr.inra.urgi.gpds.domain.data.impl.variable.OntologyVO;
import fr.inra.urgi.gpds.repository.file.CropOntologyRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author gcornut
 *
 *
 */
@Api(tags={"Breeding API"}, description = "BrAPI endpoints")
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
	@RequestMapping(value = "/brapi/v1/variables/{observationVariableDbId}", method = GET, produces= APPLICATION_JSON_VALUE)
	@ResponseBody
	@JsonView(JSONView.BrapiFields.class)
	public BrapiResponse<ObservationVariableVO> getVariable(@PathVariable String observationVariableDbId) {
		ObservationVariableVO variable = repository.getVariableById(observationVariableDbId);
		if (variable == null) {
			throw new NotFoundException("Variable not found for id '" + observationVariableDbId + "'");
		}
		return BrapiResponseFactory.createSingleObjectResponse(variable, null);
	}

	/**
	 * @link https://github.com/plantbreeding/API/blob/master/Specification/ObservationVariables/VariableOntologyList.md
	 */
	@ApiOperation("List ontologies")
	@RequestMapping(value = "/brapi/v1/ontologies", method = GET, produces= APPLICATION_JSON_VALUE)
	@ResponseBody
	@JsonView(JSONView.BrapiFields.class)
	public BrapiListResponse<OntologyVO> listOntologies(
			@Valid @ApiParam PaginationCriteriaImpl criteria
	) {
		List<OntologyVO> ontologies = repository.getOntologies();

		return BrapiResponseFactory.createSubListResponse(
				criteria.getPageSize(), criteria.getPage(),
				ontologies
		);
	}

	/**
	 * @link https://github.com/plantbreeding/API/blob/master/Specification/ObservationVariables/VariableList.md
	 */
	@ApiOperation("List variables")
	@RequestMapping(value = "/brapi/v1/variables", method = GET, produces= APPLICATION_JSON_VALUE)
	@ResponseBody
	@JsonView(JSONView.BrapiFields.class)
	public BrapiListResponse<ObservationVariableVO> listVariables(
			@Valid @ApiParam ObservationVariableCriteria criteria
	) {
		// Get variables by trait class or get all variables
		List<ObservationVariableVO> variables;
		if (StringUtils.isNotBlank(criteria.getTraitClass())) {
			variables = repository.getVariablesByTraitClass(criteria.getTraitClass());
		} else {
			variables = repository.getVariables();
		}

		return BrapiResponseFactory.createSubListResponse(
				criteria.getPageSize(), criteria.getPage(),
				variables
		);
	}

}
