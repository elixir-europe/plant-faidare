package fr.inrae.urgi.faidare.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ElasticSearchConfig {

    private FaidareProperties faidareProperties;

    @Bean
    public FaidareProperties faidareProperties() {
        return new FaidareProperties();
    }

    public ElasticSearchConfig(FaidareProperties faidareProperties) {
        this.faidareProperties = faidareProperties;
    }

    /**
     * Exposes the bean of type FaidareProperties under the name
     * "faidarePropertiesBean"so that "faidarePropertiesBean" can be used
     * in SpEL expressions
     */
    @Primary
    @Bean
    public FaidareProperties faidarePropertiesBean() {
        return this.faidareProperties;
    }


}
