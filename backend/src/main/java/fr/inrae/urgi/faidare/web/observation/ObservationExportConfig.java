package fr.inrae.urgi.faidare.web.observation;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for observation export
 * @author JB Nizet
 */
@Configuration
@EnableConfigurationProperties(ObservationExportProperties.class)
public class ObservationExportConfig {
}
