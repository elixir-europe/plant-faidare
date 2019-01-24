package fr.inra.urgi.gpds.api.brapi.v1;

import fr.inra.urgi.gpds.domain.JSONView;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

import java.util.List;

/**
 * Switch which Jackson View to use depending on the HTTP Accept header
 *
 * When the request accepts JSON-LD, switch to BrAPI with JSON-LD fields
 *
 * @author gcornut
 */
@ControllerAdvice(basePackages = "fr.inra.urgi.gpds.api.brapi.v1")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BrapiJSONViewHandler extends AbstractMappingJacksonResponseBodyAdvice {

    public static final MediaType APPLICATION_LD_JSON = new MediaType("application","ld+json");

    @Override
    protected void beforeBodyWriteInternal(
        MappingJacksonValue bodyContainer,
        MediaType contentType,
        MethodParameter returnType,
        ServerHttpRequest req,
        ServerHttpResponse response
    ) {
        // Default: display only BrAPI Fields
        bodyContainer.setSerializationView(JSONView.BrapiFields.class);

        // But if requested, also display JSON-LD fields
        List<MediaType> accept = req.getHeaders().getAccept();
        for (MediaType mediaType : accept) {
            if (APPLICATION_LD_JSON.includes(mediaType)) {
                bodyContainer.setSerializationView(JSONView.BrapiWithJSONLD.class);
                break;
            }
        }
    }
}
