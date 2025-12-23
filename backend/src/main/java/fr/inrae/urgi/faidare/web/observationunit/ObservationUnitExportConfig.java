package fr.inrae.urgi.faidare.web.observationunit;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for observation unit export
 * @author JB Nizet
 */
@Configuration
@EnableConfigurationProperties(ObservationUnitExportProperties.class)
public class ObservationUnitExportConfig {
}
