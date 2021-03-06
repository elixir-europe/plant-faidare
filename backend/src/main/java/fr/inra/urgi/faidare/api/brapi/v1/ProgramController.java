package fr.inra.urgi.faidare.api.brapi.v1;

import fr.inra.urgi.faidare.api.NotFoundException;
import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiProgram;
import fr.inra.urgi.faidare.domain.brapi.v1.response.BrapiListResponse;
import fr.inra.urgi.faidare.domain.brapi.v1.response.BrapiResponse;
import fr.inra.urgi.faidare.domain.criteria.ProgramCriteria;
import fr.inra.urgi.faidare.domain.data.ProgramVO;
import fr.inra.urgi.faidare.domain.response.ApiResponseFactory;
import fr.inra.urgi.faidare.domain.response.PaginatedList;
import fr.inra.urgi.faidare.repository.es.ProgramRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author gcornut
 */
@Api(tags = {"Breeding API"}, description = "BrAPI endpoint")
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
    @GetMapping("/brapi/v1/programs/{programDbId}")
    public BrapiResponse<BrapiProgram> getProgram(@PathVariable String programDbId) {
        ProgramVO program = repository.getById(programDbId);
        if (program == null) {
            throw new NotFoundException("Program not found for id '" + programDbId + "'");
        }
        return ApiResponseFactory.createSingleObjectResponse(program, null);
    }

    /**
     * @link https://github.com/plantbreeding/API/blob/master/Specification/Programs/ListPrograms.md
     */
    @ApiOperation("List programs")
    @GetMapping("/brapi/v1/programs")
    public BrapiListResponse<? extends BrapiProgram> listPrograms(@Valid @ApiParam ProgramCriteria criteria) {
        PaginatedList<ProgramVO> result = repository.find(criteria);
        return ApiResponseFactory.createListResponse(result.getPagination(), null, result);
    }

    /**
     * @link https://github.com/plantbreeding/API/blob/master/Specification/Programs/ProgramSearch.md
     */
    @ApiOperation("Search programs")
    @PostMapping(value = "/brapi/v1/programs-search", consumes = APPLICATION_JSON_VALUE)
    public BrapiListResponse<? extends BrapiProgram> searchPrograms(@Valid @RequestBody(required = false) ProgramCriteria criteria) {
        return listPrograms(criteria);
    }

}
