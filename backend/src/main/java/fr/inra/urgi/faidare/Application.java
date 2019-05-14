package fr.inra.urgi.faidare;

import fr.inra.urgi.faidare.config.FaidareProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * The main faidare Application
 *
 * @author gcornut
 */
@SpringBootApplication
@EnableAsync
@EnableConfigurationProperties(FaidareProperties.class)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
