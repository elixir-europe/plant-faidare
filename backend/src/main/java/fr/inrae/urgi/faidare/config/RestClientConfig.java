package fr.inrae.urgi.faidare.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * @author gcornut
 */
@Configuration
public class RestClientConfig {

    private final MappingJackson2HttpMessageConverter messageConverter;

    public RestClientConfig(
        @Qualifier("mappingJacksonHttpMessageConverter") MappingJackson2HttpMessageConverter messageConverter
    ) {
        this.messageConverter = messageConverter;
    }

    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory factory
            = new HttpComponentsClientHttpRequestFactory();
        // two second timeout
        factory.setConnectTimeout(2000);

        RestTemplate restTemplate = new RestTemplate(factory);

        restTemplate.getMessageConverters().removeIf(m -> m.getClass().getName().equals(MappingJackson2HttpMessageConverter.class.getName()));
        restTemplate.getMessageConverters().add(messageConverter);
        return restTemplate;
    }
}
