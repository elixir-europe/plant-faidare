package fr.inra.urgi.gpds.api.brapi.v1;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.api.NotFoundException;
import fr.inra.urgi.gpds.domain.JSONView;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiListResponse;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiResponse;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiResponseFactory;
import fr.inra.urgi.gpds.domain.criteria.ProgramCriteria;
import fr.inra.urgi.gpds.domain.data.impl.ProgramVO;
import fr.inra.urgi.gpds.domain.response.PaginatedList;
import fr.inra.urgi.gpds.repository.es.ProgramRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author gcornut
 *
 *
 */
@Api(tags = { "Breeding API" }, description = "BrAPI endpoints")
@RestController
public class ProgramController {

	private final ProgramRepository repository;

	@Autowired
    public ProgramController(ProgramRepository repository) {
        this.repository = repository;
    }

    /**
	 * Not officially present in BrAPI
	 */
	@ApiOperation("Get program")
	@RequestMapping(value = "/brapi/v1/programs/{programDbId}", method = GET, produces = APPLICATION_JSON_VALUE)
	@ResponseBody
	@JsonView(JSONView.BrapiFields.class)
	public BrapiResponse<ProgramVO> getProgram(@PathVariable String programDbId) {
		ProgramVO program = repository.getById(programDbId);
		if (program == null) {
			throw new NotFoundException("Program not found for id '" + programDbId + "'");
		}
		return BrapiResponseFactory.createSingleObjectResponse(program, null);
	}

	/**
	 * @link https://github.com/plantbreeding/API/blob/master/Specification/Programs/ListPrograms.md
	 */
	@ApiOperation("List programs")
	@RequestMapping(value = "/brapi/v1/programs", method = GET, produces = APPLICATION_JSON_VALUE)
	@ResponseBody
	@JsonView(JSONView.BrapiFields.class)
	public BrapiListResponse<ProgramVO> listPrograms(@Valid @ApiParam ProgramCriteria criteria) {
		PaginatedList<ProgramVO> result = repository.find(criteria);
		return BrapiResponseFactory.createListResponse(result.getPagination(), null, result);
	}

	/**
	 * @link https://github.com/plantbreeding/API/blob/master/Specification/Programs/ProgramSearch.md
	 */
	@ApiOperation("Search programs")
	@RequestMapping(value = "/brapi/v1/programs-search", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
	@ResponseBody
	@JsonView(JSONView.BrapiFields.class)
	public BrapiListResponse<ProgramVO> searchPrograms(@Valid @RequestBody(required = false) ProgramCriteria criteria) {
		return listPrograms(criteria);
	}

}
