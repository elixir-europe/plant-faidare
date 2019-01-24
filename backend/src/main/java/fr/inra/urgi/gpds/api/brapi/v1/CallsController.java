package fr.inra.urgi.gpds.api.brapi.v1;

import com.google.common.collect.Lists;
import com.google.common.reflect.ClassPath;
import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiCall;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiListResponse;
import fr.inra.urgi.gpds.domain.criteria.base.PaginationCriteriaImpl;
import fr.inra.urgi.gpds.domain.data.CallVO;
import fr.inra.urgi.gpds.domain.response.ApiResponseFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author gcornut
 */
@Api(tags = {"Breeding API"}, description = "BrAPI endpoint")
@RestController
public class CallsController {

    private static final List<String> DATA_TYPES = Arrays.asList(
        "json"
    );

    private static final List<String> BRAPI_VERSIONS = Arrays.asList(
        "1.0",
        "1.1",
        "1.2"
    );

    private final List<BrapiCall> implementedCalls;

    public CallsController() {
        this.implementedCalls = listImplementedCalls();
    }

    /**
     * @link https://github.com/plantbreeding/API/blob/master/Specification/Calls/Calls.md
     */
    @ApiOperation("List implemented Breeding API calls")
    @GetMapping("/brapi/v1/calls")
    public BrapiListResponse<BrapiCall> calls(@Valid PaginationCriteriaImpl criteria) {
        return ApiResponseFactory.createSubListResponse(
            criteria.getPageSize(), criteria.getPage(), implementedCalls
        );
    }

    /**
     * Generate {@link BrapiCall} by reflectively reading Spring REST
     * annotations
     */
    private List<BrapiCall> listImplementedCalls() {
        Map<String, BrapiCall> calls = new HashMap<>();

        Class<?> aClass = getClass();
        ClassLoader classLoader = aClass.getClassLoader();
        ClassPath classPath;
        try {
            classPath = ClassPath.from(classLoader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String brapiControllerPackage = aClass.getPackage().getName();
        Set<ClassPath.ClassInfo> controllerClasses = classPath.getTopLevelClasses(brapiControllerPackage);
        for (ClassPath.ClassInfo controllerClassInfo : controllerClasses) {
            Class<?> controllerClass = controllerClassInfo.load();
            if (controllerClass.getAnnotation(RestController.class) == null) {
                // Class is not a RestController
                continue;
            }

            for (Method method : controllerClass.getMethods()) {
                ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
                if (apiOperation != null && apiOperation.hidden()) {
                    // Rest call is hidden = ignore
                    continue;
                }

                String[] paths = getCallPath(method);
                if (paths == null) {
                    // No rest path mapping => ignore
                    continue;
                }
                for (String path : paths) {
                    path = path.split("/brapi/v1/")[1];

                    RequestMethod[] httpMethods = getCallMethods(method);
                    List<String> httpMethodNames = Lists.newArrayList();
                    for (RequestMethod httpMethod : httpMethods) {
                        httpMethodNames.add(httpMethod.name());
                    }

                    BrapiCall call = calls.get(path);
                    if (call == null) {
                        call = new CallVO(path);
                        calls.put(path, call);
                    }
                    call.getMethods().addAll(httpMethodNames);
                    call.getDatatypes().addAll(DATA_TYPES);
                    call.getVersions().addAll(BRAPI_VERSIONS);
                }
            }
        }

        return new ArrayList<>(calls.values());
    }

    private String[] getCallPath(Method method) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        if (annotation != null) {
            return annotation.value();
        }
        GetMapping getAnnotation = method.getAnnotation(GetMapping.class);
        if (getAnnotation != null) {
            return getAnnotation.value();
        }
        PostMapping postAnnotation = method.getAnnotation(PostMapping.class);
        if (postAnnotation != null) {
            return postAnnotation.value();
        }
        return null;
    }

    private RequestMethod[] getCallMethods(Method method) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        if (annotation != null) {
            return annotation.method();
        }
        GetMapping getAnnotation = method.getAnnotation(GetMapping.class);
        if (getAnnotation != null) {
            return new RequestMethod[]{GET};
        }
        PostMapping postAnnotation = method.getAnnotation(PostMapping.class);
        if (postAnnotation != null) {
            return new RequestMethod[]{POST};
        }
        return null;
    }

}
