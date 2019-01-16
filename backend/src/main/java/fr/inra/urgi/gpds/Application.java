package fr.inra.urgi.gpds;

import fr.inra.urgi.gpds.config.GPDSProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * The main gpds Application
 * @author gcornut
 */
@SpringBootApplication
@EnableAsync
@EnableConfigurationProperties(GPDSProperties.class)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
