package fr.inra.urgi.gpds.api.brapi.v1;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiListResponse;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiResponseFactory;
import fr.inra.urgi.gpds.domain.criteria.ObservationUnitCriteria;
import fr.inra.urgi.gpds.domain.data.impl.ObservationUnitVO;
import fr.inra.urgi.gpds.domain.response.PaginatedList;
import fr.inra.urgi.gpds.repository.es.ObservationUnitRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author gcornut
 */
@Api(tags = {"Breeding API"}, description = "BrAPI endpoint")
@RestController
public class PhenotypeController {

    private final ObservationUnitRepository repository;

    @Autowired
    public PhenotypeController(ObservationUnitRepository repository) {
        this.repository = repository;
    }

    /**
     * @link https://github.com/plantbreeding/API/blob/master/Specification/Phenotypes/PhenotypeSearch.md
     */
    @ApiOperation("Search phenotypes")
    @RequestMapping(value = "/brapi/v1/phenotypes-search", method = POST, produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(JSONView.BrapiFields.class)
    public BrapiListResponse<ObservationUnitVO> searchPhenotypes(@Valid @RequestBody(required = false) ObservationUnitCriteria criteria) {
        PaginatedList<ObservationUnitVO> result = repository.find(criteria);
        return BrapiResponseFactory.createListResponse(result.getPagination(), null, result);
    }

}
