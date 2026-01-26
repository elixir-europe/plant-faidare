package fr.inrae.urgi.faidare.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.assertj.core.api.Assertions.assertThat;

class SwaggerConfigTest {

    @AfterEach
    void cleanup() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void serverUrlCustomizer() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setScheme("https");
        request.setServerName("beta-urgi.versailles.inrae.fr");
        request.setServerPort(443);
        request.setContextPath("/faidare");

        RequestContextHolder.setRequestAttributes(
            new ServletRequestAttributes(request)
        );

        SwaggerConfig swaggerConfig = new SwaggerConfig();
        OpenApiCustomizer customizer = swaggerConfig.serverUrlCustomizer();

        OpenAPI openAPI = new OpenAPI();

        customizer.customise(openAPI);

        assertThat(openAPI.getServers()).isNotNull();
        assertThat(openAPI.getServers()).hasSize(1);
        assertThat(openAPI.getServers().get(0).getUrl())
            .isEqualTo("https://beta-urgi.versailles.inrae.fr/faidare");
    }
}
