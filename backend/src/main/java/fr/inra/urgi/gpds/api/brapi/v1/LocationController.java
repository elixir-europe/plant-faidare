package fr.inra.urgi.gpds.api.brapi.v1;

import fr.inra.urgi.gpds.api.NotFoundException;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiListResponse;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiResponse;
import fr.inra.urgi.gpds.domain.criteria.LocationCriteria;
import fr.inra.urgi.gpds.domain.data.LocationVO;
import fr.inra.urgi.gpds.domain.response.ApiResponseFactory;
import fr.inra.urgi.gpds.domain.response.PaginatedList;
import fr.inra.urgi.gpds.repository.es.LocationRepository;
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
public class LocationController {

    private final LocationRepository repository;

    @Autowired
    public LocationController(LocationRepository repository) {
        this.repository = repository;
    }

    /**
     * @link https://github.com/plantbreeding/API/blob/master/Specification/Locations/LocationDetails.md
     */
    @ApiOperation("Get location")
    @GetMapping("/brapi/v1/locations/{locationDbId}")
    public BrapiResponse<LocationVO> getLocation(@PathVariable String locationDbId) {
        LocationVO location = repository.getById(locationDbId);
        if (location == null) {
            throw new NotFoundException("Location not found for id '" + locationDbId + "'");
        }
        return ApiResponseFactory.createSingleObjectResponse(location, null);
    }

    /**
     * @link https://github.com/plantbreeding/API/blob/master/Specification/Locations/ListLocations.md
     */
    @ApiOperation("List locations")
    @GetMapping("/brapi/v1/locations")
    public BrapiListResponse<LocationVO> listLocations(
        @Valid LocationCriteria criteria
    ) {
        PaginatedList<LocationVO> result = repository.find(criteria);
        return ApiResponseFactory.createListResponse(result.getPagination(), null, result);
    }
}
