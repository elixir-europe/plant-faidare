package fr.inra.urgi.gpds.api.brapi.v1;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Lists;
import com.google.common.reflect.ClassPath;
import fr.inra.urgi.gpds.domain.JSONView;
import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiCall;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiListResponse;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiResponseFactory;
import fr.inra.urgi.gpds.domain.criteria.base.PaginationCriteriaImpl;
import fr.inra.urgi.gpds.domain.data.impl.CallVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author gcornut
 */
@Api(tags = {"Breeding API"}, description = "BrAPI endpoints")
@RestController
public class CallsController {

    private static final List<String> BRAPI_VERSIONS = Arrays.asList(
        "1.0",
        "1.1",
        "1.2"
    );

    /**
     * @link https://github.com/plantbreeding/API/blob/master/Specification/Calls/Calls.md
     */
    @ApiOperation("List implemented Breeding API calls")
    @RequestMapping(value = "/brapi/v1/calls", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(JSONView.BrapiFields.class)
    public BrapiListResponse<BrapiCall> calls(@Valid PaginationCriteriaImpl criteria) {
        return BrapiResponseFactory.createSubListResponse(
            criteria.getPageSize(), criteria.getPage(), listImplementedCalls()
        );
    }

    /**
     * Generate {@link BrapiCall} by reflectively reading Spring REST
     * annotations
     */
    private List<BrapiCall> listImplementedCalls() {
        List<BrapiCall> calls = new ArrayList<>();

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
                RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);

                if (annotation == null || apiOperation != null && apiOperation.hidden()) {
                    // Method is not a Rest endpoint or is hidden
                    continue;
                }

                String callName = annotation.value()[0];
                callName = callName.split("/brapi/v[0-9]/")[1];

                RequestMethod[] httpMethods = annotation.method();
                List<String> httpMethodNames = Lists.newArrayList();
                for (RequestMethod httpMethod : httpMethods) {
                    httpMethodNames.add(httpMethod.name());
                }

                List<String> datatypes = new ArrayList<>();
                for (String produces : annotation.produces()) {
                    datatypes.add(produces.replace("application/", ""));
                }

                CallVO call = new CallVO();
                call.setCall(callName);
                call.setMethods(httpMethodNames);
                call.setDataTypes(datatypes);
                call.setVersions(BRAPI_VERSIONS);
                calls.add(call);
            }
        }

        return calls;
    }

}
