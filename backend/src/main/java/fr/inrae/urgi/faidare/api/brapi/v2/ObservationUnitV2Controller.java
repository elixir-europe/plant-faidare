package fr.inrae.urgi.faidare.api.brapi.v2;

import fr.inrae.urgi.faidare.dao.v2.ObservationUnitV2Criteria;
import fr.inrae.urgi.faidare.dao.v2.ObservationUnitV2Dao;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.ObservationLevelVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.ObservationUnitV2VO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Breeding API latest version (V2)", description = "BrAPI full specifications : https://brapi.org/specification")
@RestController
@RequestMapping({"/brapi/v2"})
public class ObservationUnitV2Controller {

    private final ObservationUnitV2Dao observationUnitDao;

    public ObservationUnitV2Controller(ObservationUnitV2Dao observationUnitDao) {
        this.observationUnitDao = observationUnitDao;
    }

    @GetMapping("/observationunits")
    public BrapiListResponse<ObservationUnitV2VO> observationUnits(
        @ModelAttribute ObservationUnitV2Criteria obsUnitCrit) {
        return observationUnitDao.findObservationUnitByCriteria(obsUnitCrit);
    }

    @PostMapping(value = "/search/observationunits", consumes = "application/json", produces = "application/json")
    public BrapiListResponse<ObservationUnitV2VO> searchObservationUnits(@RequestBody ObservationUnitV2Criteria obsUnitCrit) {
        return observationUnitDao.findObservationUnitByCriteria(obsUnitCrit);
    }

    @GetMapping("/observationunits/{observationUnitDbId}")
    public BrapiSingleResponse<ObservationUnitV2VO> byObservationUnitDbId(@PathVariable String observationUnitDbId) throws Exception {

        ObservationUnitV2VO obsUnitV2Vo = observationUnitDao.getByObservationUnitDbId(observationUnitDbId);
        return BrapiSingleResponse.brapiResponseOf(obsUnitV2Vo, Pageable.ofSize(1));
    }

    @GetMapping("/observationlevels")
    public BrapiListResponse<ObservationLevelVO> observationLevels(@ModelAttribute ObservationUnitV2Criteria obsUnitCrit) throws Exception {

        return observationUnitDao.findObservationLevels(obsUnitCrit);
    }

}
