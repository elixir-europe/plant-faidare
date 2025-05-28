package fr.inrae.urgi.faidare.api.brapi.v2;


import fr.inrae.urgi.faidare.dao.v2.LocationV2Criteria;
import fr.inrae.urgi.faidare.dao.v2.LocationV2Dao;
import fr.inrae.urgi.faidare.domain.brapi.v2.LocationV2VO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Breeding API", description = "BrAPI endpoint")
@RestController
@RequestMapping({"/brapi/v2"})
public class LocationV2Controller {
    private final LocationV2Dao locationDao;

    public LocationV2Controller(LocationV2Dao locationDao) {
        this.locationDao = locationDao;
    }

    @GetMapping("/locations/{locationDbId}")
    public BrapiSingleResponse<LocationV2VO> byLocationDbId(@PathVariable String locationDbId) throws Exception {

        LocationV2VO lV2Vo = locationDao.getByLocationDbId(locationDbId);
        return BrapiSingleResponse.brapiResponseOf(lV2Vo, Pageable.ofSize(1));
    }

    @GetMapping("/search/locations")
    public BrapiListResponse<LocationV2VO> location(
        @ModelAttribute LocationV2Criteria lCrit){

        return locationDao.findLocationsByCriteria(lCrit);
    }
    @PostMapping(value = "/search/locations", consumes = "application/json", produces = "application/json")
    public BrapiListResponse<LocationV2VO> searchLocation(@RequestBody LocationV2Criteria lCrit){

        return locationDao.findLocationsByCriteria(lCrit);
    }

}
