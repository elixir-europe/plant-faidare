package fr.inra.urgi.faidare.api.brapi.v1;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableSet;
import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiCall;
import fr.inra.urgi.faidare.domain.brapi.v1.response.BrapiListResponse;
import fr.inra.urgi.faidare.domain.criteria.base.PaginationCriteriaImpl;
import fr.inra.urgi.faidare.domain.data.CallVO;
import fr.inra.urgi.faidare.domain.response.ApiResponseFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.webmvc.api.OpenApiWebMvcResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gcornut
 */
@Tag(name = "Breeding API", description = "BrAPI endpoint")
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
    private final OpenApiWebMvcResource openApiResource;
    private final ObjectMapper objectMapper;

    private AtomicReference<List<BrapiCall>> implementedCalls = new AtomicReference<>(null);

    public CallsController(OpenApiWebMvcResource openApiResource, ObjectMapper objectMapper) {
        this.openApiResource = openApiResource;
        this.objectMapper = objectMapper;
    }

    /**
     * @link https://github.com/plantbreeding/API/blob/master/Specification/Calls/Calls.md
     */
    @Operation(summary = "List implemented Breeding API calls")
    @GetMapping("/brapi/v1/calls")
    public BrapiListResponse<BrapiCall> calls(@Valid PaginationCriteriaImpl criteria, HttpServletRequest request) throws JsonProcessingException {
        if (implementedCalls.get() == null) {
            implementedCalls.set(swaggerToBrapiCalls(request));
        }

        return ApiResponseFactory.createSubListResponse(
            criteria.getPageSize(), criteria.getPage(), implementedCalls.get()
        );
    }

    /**
     * Get swagger API documentation and transform it to list of BrAPI calls
     *
     * This must be done after swagger has time to generate the API
     * documentation and thus can't be done in this class constructor
     */
    @SuppressWarnings("unchecked")
    private List<BrapiCall> swaggerToBrapiCalls(HttpServletRequest request) throws JsonProcessingException {
        String json = openApiResource.openapiJson(request, "/v3/api-docs", Locale.ENGLISH);

        Map<String, Object> map = objectMapper.readValue(json,
                                                         new TypeReference<Map<String, Object>>() {});

        Map<String, Object> pathMap = (Map<String, Object>) map.get("paths");
        return pathMap.entrySet().stream()
            .filter(entry -> entry.getKey().startsWith(BRAPI_PATH))
            .map(entry -> {
                String path = entry.getKey();
                Map<String, Object> methodMap = (Map<String, Object>) entry.getValue();
                Set<String> methods = methodMap.keySet().stream().map(String::toUpperCase).collect(Collectors.toSet());
                CallVO call = new CallVO(path.replace(BRAPI_PATH, ""));
                call.setMethods(methods);
                return call;
            })
            .sorted(Comparator.comparing(CallVO::getCall))
            .collect(Collectors.toList());
    }
}
