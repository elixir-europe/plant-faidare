package fr.inra.urgi.faidare.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.MimeMappings;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

/**
 * MimeMappings configuration. Ensure that markdown files are correctly returned with the good MimeType
 * @author R. Flores
 */
@Configuration
public class MimeMappingServletCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
        mappings.add("md", "text/markdown");
        factory.setMimeMappings(mappings);
    }

}
