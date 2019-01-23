package fr.inra.urgi.gpds.api.brapi.v1;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.api.NotFoundException;
import fr.inra.urgi.gpds.domain.JSONView;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiListResponse;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiResponse;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiResponseFactory;
import fr.inra.urgi.gpds.domain.criteria.LocationCriteria;
import fr.inra.urgi.gpds.domain.data.impl.LocationVO;
import fr.inra.urgi.gpds.domain.response.PaginatedList;
import fr.inra.urgi.gpds.repository.es.LocationRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

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
    @RequestMapping(value = "/brapi/v1/locations/{locationDbId}", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(JSONView.BrapiFields.class)
    public BrapiResponse<LocationVO> getLocation(@PathVariable String locationDbId) {
        LocationVO location = repository.getById(locationDbId);
        if (location == null) {
            throw new NotFoundException("Location not found for id '" + locationDbId + "'");
        }
        return BrapiResponseFactory.createSingleObjectResponse(location, null);
    }

    /**
     * @link https://github.com/plantbreeding/API/blob/master/Specification/Locations/ListLocations.md
     */
    @ApiOperation("List locations")
    @RequestMapping(value = "/brapi/v1/locations", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(JSONView.BrapiFields.class)
    public BrapiListResponse<LocationVO> listLocations(
        @Valid LocationCriteria criteria
    ) {
        PaginatedList<LocationVO> result = repository.find(criteria);
        return BrapiResponseFactory.createListResponse(result.getPagination(), null, result);
    }
}
