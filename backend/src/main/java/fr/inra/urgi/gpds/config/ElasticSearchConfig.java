package fr.inra.urgi.gpds.config;

import org.apache.http.HttpHost;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.elasticsearch.client.NodeSelector;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.elasticsearch.rest.RestClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {

    @Value("${data.elasticsearch.cluster-name}")
    private String esClusterName;

    @Value("${data.elasticsearch.host}")
    private String esHost;

    @Value("${data.elasticsearch.port}")
    private Integer esPort;

    /**
     * Provides builder for {@link RestClientAutoConfiguration}
     */
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

}
