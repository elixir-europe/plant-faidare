package fr.inra.urgi.faidare.api.brapi.v1;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.validation.Valid;

import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiObservationUnit;
import fr.inra.urgi.faidare.domain.brapi.v1.response.BrapiListResponse;
import fr.inra.urgi.faidare.domain.criteria.ObservationUnitCriteria;
import fr.inra.urgi.faidare.domain.response.ApiResponseFactory;
import fr.inra.urgi.faidare.domain.response.PaginatedList;
import fr.inra.urgi.faidare.repository.es.ObservationUnitRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gcornut
 */
@Tag(name = "Breeding API", description = "BrAPI endpoint")
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
    @Operation(summary = "Search phenotypes")
    @PostMapping(value = "/brapi/v1/phenotypes-search", consumes = APPLICATION_JSON_VALUE)
    public BrapiListResponse<? extends BrapiObservationUnit> searchPhenotypes(@Valid @RequestBody(required = false) ObservationUnitCriteria criteria) {
        PaginatedList<? extends BrapiObservationUnit> result = repository.find(criteria);
        return ApiResponseFactory.createListResponse(result.getPagination(), null, result);
    }

}
