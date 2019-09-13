package fr.inra.urgi.faidare.api.faidare.v1;

import fr.inra.urgi.faidare.domain.JSONView;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

/**
 * @author gcornut
 */
@ControllerAdvice(basePackages = "fr.inra.urgi.faidare.api.faidare.v1")
public class GnpISJSONViewHandler extends AbstractMappingJacksonResponseBodyAdvice {

    @Override
    protected void beforeBodyWriteInternal(
        MappingJacksonValue bodyContainer,
        MediaType contentType,
        MethodParameter returnType,
        ServerHttpRequest req,
        ServerHttpResponse response
    ) {
        // Default: display only GnpIS API Fields (BrAPI + GnpIS fileds)
        bodyContainer.setSerializationView(JSONView.GnpISAPI.class);
    }
}
