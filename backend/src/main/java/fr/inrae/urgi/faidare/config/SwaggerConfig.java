package fr.inrae.urgi.faidare.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

/**
 * @author gcornut
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI springFaidareOpenAPI() {
        return new OpenAPI()
            .info(new Info().title("FAIDARE and BrAPI APIs"));
    }

    /**
     * Customizer qui ajoute dynamiquement l'URL du serveur basée sur la requête entrante.
     *
     * Fonctionne en analysant :
     * - Headers X-Forwarded-* (si derrière un proxy)
     * - Sinon utilise l'URL directe de la requête
     *
     * @return OpenApiCustomizer configuré
     */
    @Bean
    public OpenApiCustomizer serverUrlCustomizer() {
        return openApi -> {
            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .build()
                .toUriString();

            Server server = new Server()
                .url(baseUrl)
                .description("FAIDARE API");

            openApi.servers(List.of(server));
        };
    }
}
