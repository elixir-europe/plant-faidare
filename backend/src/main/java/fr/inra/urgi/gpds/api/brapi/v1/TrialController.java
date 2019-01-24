package fr.inra.urgi.gpds.api.brapi.v1;

import fr.inra.urgi.gpds.api.NotFoundException;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiListResponse;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiResponse;
import fr.inra.urgi.gpds.domain.criteria.TrialCriteria;
import fr.inra.urgi.gpds.domain.data.TrialVO;
import fr.inra.urgi.gpds.domain.response.ApiResponseFactory;
import fr.inra.urgi.gpds.domain.response.PaginatedList;
import fr.inra.urgi.gpds.repository.es.TrialRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author gcornut
 */
@Api(tags = {"Breeding API"}, description = "BrAPI endpoint")
@RestController
public class TrialController {

    private final TrialRepository repository;

    @Autowired
    public TrialController(TrialRepository repository) {
        this.repository = repository;
    }

    /**
     * @link https://github.com/plantbreeding/API/blob/master/Specification/Trials/GetTrialById.md
     */
    @ApiOperation("Get trial")
    @GetMapping("/brapi/v1/trials/{trialDbId}")
    public BrapiResponse<TrialVO> getTrial(@PathVariable String trialDbId) {
        TrialVO program = repository.getById(trialDbId);
        if (program == null) {
            throw new NotFoundException("Trial not found for id '" + trialDbId + "'");
        }
        return ApiResponseFactory.createSingleObjectResponse(program, null);
    }

    /**
     * @link https://github.com/plantbreeding/API/blob/master/Specification/Trials/ListTrialSummaries.md
     */
    @ApiOperation("List trials")
    @GetMapping("/brapi/v1/trials")
    public BrapiListResponse<TrialVO> listTrials(@Valid TrialCriteria criteria) {
        PaginatedList<TrialVO> result = repository.find(criteria);
        return ApiResponseFactory.createListResponse(result.getPagination(), null, result);
    }

}
