package fr.inra.urgi.faidare.api.brapi.v1;

import fr.inra.urgi.faidare.api.NotFoundException;
import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiTrial;
import fr.inra.urgi.faidare.domain.brapi.v1.response.BrapiListResponse;
import fr.inra.urgi.faidare.domain.brapi.v1.response.BrapiResponse;
import fr.inra.urgi.faidare.domain.criteria.TrialCriteria;
import fr.inra.urgi.faidare.domain.data.TrialVO;
import fr.inra.urgi.faidare.domain.response.ApiResponseFactory;
import fr.inra.urgi.faidare.domain.response.PaginatedList;
import fr.inra.urgi.faidare.repository.es.TrialRepository;
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
    public BrapiResponse<BrapiTrial> getTrial(@PathVariable String trialDbId) {
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
    public BrapiListResponse<? extends BrapiTrial> listTrials(@Valid TrialCriteria criteria) {
        PaginatedList<? extends BrapiTrial> result = repository.find(criteria);
        return ApiResponseFactory.createListResponse(result.getPagination(), null, result);
    }

}
