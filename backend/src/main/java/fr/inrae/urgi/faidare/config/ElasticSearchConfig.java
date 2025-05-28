package fr.inrae.urgi.faidare.config;

import org.apache.http.HttpHost;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.elasticsearch.client.NodeSelector;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ElasticSearchConfig {

    @Value("${data.elasticsearch.host}")
    private String esHost;

    @Value("${data.elasticsearch.port}")
    private Integer esPort;


    /**
     * Provides builder for {@link ElasticsearchRestClientAutoConfiguration}
     */
    //The RestClient.builder() is not deprecated in Elasticsearch 7.x.
    // However, in newer versions (e.g., 8.x), there may be alternative approaches
    // or native clients to consider.
    @Bean
    public RestClientBuilder restClientBuilder() {
        // if we are on CI, we use a hardcoded host, else we use the injected value
        String host = System.getenv("CI") != null ? "elasticsearch" : esHost;
        HttpHost httpHost = new HttpHost(host, esPort, HttpHost.DEFAULT_SCHEME_NAME);

        return RestClient.builder(httpHost)
            .setNodeSelector(NodeSelector.SKIP_DEDICATED_MASTERS)
            .setHttpClientConfigCallback(http -> http.setDefaultIOReactorConfig(
                IOReactorConfig.custom()
                    .setIoThreadCount(2)
                    .build())
            );
    }

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
