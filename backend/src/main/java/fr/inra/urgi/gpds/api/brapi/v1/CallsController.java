package fr.inra.urgi.gpds.api.brapi.v1;

import com.google.common.collect.ImmutableSet;
import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiCall;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiListResponse;
import fr.inra.urgi.gpds.domain.criteria.base.PaginationCriteriaImpl;
import fr.inra.urgi.gpds.domain.data.CallVO;
import fr.inra.urgi.gpds.domain.response.ApiResponseFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.Documentation;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.plugins.Docket;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author gcornut
 */
@Api(tags = {"Breeding API"}, description = "BrAPI endpoint")
@RestController
public class CallsController {
    private static final String BRAPI_PATH = "/brapi/v1/";

    public static final Set<String> DEFAULT_DATA_TYPES = ImmutableSet.of(
        "json"
    );

    public static final Set<String> DEFAULT_BRAPI_VERSIONS = ImmutableSet.of(
        "1.0",
        "1.1",
        "1.2"
    );

    private List<BrapiCall> implementedCalls;

    private final DocumentationCache documentationCache;

    @Autowired
    public CallsController(DocumentationCache documentationCache) {
        this.documentationCache = documentationCache;
    }

    /**
     * @link https://github.com/plantbreeding/API/blob/master/Specification/Calls/Calls.md
     */
    @ApiOperation("List implemented Breeding API calls")
    @GetMapping("/brapi/v1/calls")
    public BrapiListResponse<BrapiCall> calls(@Valid PaginationCriteriaImpl criteria) {
        if (implementedCalls == null) {
            implementedCalls = swaggerToBrapiCalls();
        }

        return ApiResponseFactory.createSubListResponse(
            criteria.getPageSize(), criteria.getPage(), implementedCalls
        );
    }

    /**
     * Get swagger API documentation and transform it to list of BrAPI calls
     *
     * This must be done after swagger has time to generate the API
     * documentation and thus can't be done in this class constructor
     */
    private List<BrapiCall> swaggerToBrapiCalls() {
        Documentation apiDocumentation = this.documentationCache.documentationByGroup(Docket.DEFAULT_GROUP_NAME);

        // Get all endpoints
        return apiDocumentation.getApiListings().values().stream()
            .flatMap(endpointListing -> endpointListing.getApis().stream())
            // Only with BrAPI path
            .filter(endpointDescription -> endpointDescription.getPath().startsWith(BRAPI_PATH))
            // Group by endpoint path (ex: /brapi/v1/phenotype => [GET, POST, ...])
            .collect(Collectors.groupingBy(ApiDescription::getPath))
            .entrySet().stream()
            // Convert to BrAPI call
            .map(endpointGroup -> {
                String path = endpointGroup.getKey();
                List<ApiDescription> endpoints = endpointGroup.getValue();

                // BrAPI call path should not include the base BrAPI path
                CallVO call = new CallVO(path.replace(BRAPI_PATH, ""));

                // List every endpoint for current path
                Set<String> methods = endpoints.stream()
                    // List all operations for each endpoint
                    .flatMap(endpointDescription -> endpointDescription.getOperations().stream())
                    // List all methods
                    .map(operation -> operation.getMethod().toString())
                    .collect(Collectors.toSet());
                call.setMethods(methods);

                return call;
            })
            // Sort by call name
            .sorted(Comparator.comparing(CallVO::getCall))
            .collect(Collectors.toList());
    }

}
