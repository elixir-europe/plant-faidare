package fr.inra.urgi.gpds.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties class holding the properties of the application (typically stored in application.yml)
 *
 * @author gcornut
 */
@ConfigurationProperties(prefix = "gpds")
public class GPDSProperties {
}
