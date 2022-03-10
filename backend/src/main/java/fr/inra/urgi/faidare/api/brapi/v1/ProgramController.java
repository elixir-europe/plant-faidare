package fr.inra.urgi.faidare.api.brapi.v1;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.validation.Valid;

import fr.inra.urgi.faidare.api.NotFoundException;
import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiProgram;
import fr.inra.urgi.faidare.domain.brapi.v1.response.BrapiListResponse;
import fr.inra.urgi.faidare.domain.brapi.v1.response.BrapiResponse;
import fr.inra.urgi.faidare.domain.criteria.ProgramCriteria;
import fr.inra.urgi.faidare.domain.data.ProgramVO;
import fr.inra.urgi.faidare.domain.response.ApiResponseFactory;
import fr.inra.urgi.faidare.domain.response.PaginatedList;
import fr.inra.urgi.faidare.repository.es.ProgramRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gcornut
 */
@Tag(name = "Breeding API", description = "BrAPI endpoint")
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
    @Operation(summary = "Get program")
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
    @Operation(summary = "List programs")
    @GetMapping("/brapi/v1/programs")
    public BrapiListResponse<? extends BrapiProgram> listPrograms(@Valid @Parameter ProgramCriteria criteria) {
        PaginatedList<ProgramVO> result = repository.find(criteria);
        return ApiResponseFactory.createListResponse(result.getPagination(), null, result);
    }

    /**
     * @link https://github.com/plantbreeding/API/blob/master/Specification/Programs/ProgramSearch.md
     */
    @Operation(summary = "Search programs")
    @PostMapping(value = "/brapi/v1/programs-search", consumes = APPLICATION_JSON_VALUE)
    public BrapiListResponse<? extends BrapiProgram> searchPrograms(@Valid @RequestBody(required = false) ProgramCriteria criteria) {
        return listPrograms(criteria);
    }

}
