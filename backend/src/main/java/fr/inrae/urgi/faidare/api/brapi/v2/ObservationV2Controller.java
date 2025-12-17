package fr.inrae.urgi.faidare.api.brapi.v2;

import fr.inrae.urgi.faidare.dao.v2.ObservationV2Criteria;
import fr.inrae.urgi.faidare.dao.v2.ObservationV2Dao;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.ObservationLevelVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.ObservationVO;
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
    public BrapiListResponse<ObservationVO> searchObservations(@RequestBody ObservationV2Criteria obsCrit){
        return observationDao.findObservationByCriteria(obsCrit);
    }

    @GetMapping("/observations/{observationDbId}")
    public BrapiSingleResponse<ObservationVO> byObservationDbId(@PathVariable String observationDbId) throws Exception {

        ObservationVO obsV2Vo = observationDao.getByObservationDbId(observationDbId);
        return BrapiSingleResponse.brapiResponseOf(obsV2Vo, Pageable.ofSize(1));
    }
}
