package fr.inrae.urgi.faidare.api.brapi.v2;

import fr.inrae.urgi.faidare.dao.v2.ObservationV2Criteria;
import fr.inrae.urgi.faidare.dao.v2.ObservationV2Dao;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.ObservationLevelVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.ObservationVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Breeding API latest version (V2)", description = "BrAPI full specifications : https://brapi.org/specification")
@RestController
@RequestMapping({"/brapi/v2"})
public class ObservationV2Controller {

    private final ObservationV2Dao observationDao;

    public ObservationV2Controller(ObservationV2Dao observationDao) {
        this.observationDao = observationDao;
    }

    @GetMapping("/observations")
    public BrapiListResponse<ObservationVO> observations(
        @ModelAttribute ObservationV2Criteria obsCrit){
        return observationDao.findObservationByCriteria(obsCrit);
    }

    @PostMapping(value = "/search/observations", consumes = "application/json", produces = "application/json")
    @Operation(
        summary = "Search observations",
        description = """
            This endpoint supports two pagination modes:

            1) Classic pagination (UI usage)
               - Use: page + pageSize
               - Limitation: page × pageSize must not exceed 20000
               - If exceeded, a 400 error is returned.

            2) Cursor-based pagination (recommended for scripts and large exports)
               - Do NOT send 'page'
               - Send 'pageSize'
               - Use the returned 'nextPageToken' as 'searchAfter' in the next request
               - This mode allows deep pagination beyond 20000 results.

            Example (cursor mode - first request):
            {
              "observationVariableDbId": ["MIPO:0000024"],
              "pageSize": 1000
            }

            Example (cursor mode - next page):
            {
              "observationVariableDbId": ["MIPO:0000024"],
              "pageSize": 1000,
              "searchAfter": "value_from_nextPageToken"
            }
            """
    )
    public BrapiListResponse<ObservationVO> searchObservations(@RequestBody ObservationV2Criteria obsCrit){
        return observationDao.findObservationByCriteria(obsCrit);
    }

    @GetMapping("/observations/{observationDbId}")
    public BrapiSingleResponse<ObservationVO> byObservationDbId(@PathVariable String observationDbId) throws Exception {

        ObservationVO obsV2Vo = observationDao.getByObservationDbId(observationDbId);
        return BrapiSingleResponse.brapiResponseOf(obsV2Vo, Pageable.ofSize(1));
    }
}
