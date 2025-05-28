package fr.inrae.urgi.faidare.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * @author gcornut
 */
@Configuration
public class JSONConfig {

    @Bean
    public MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(permissiveMapper());
        return converter;
    }

    @Bean
    public ObjectMapper permissiveMapper() {
        return JsonMapper.builder()
                         .disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
                         .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                         .disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)
                         .disable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS)
                         .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                         .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
                         .build();
    }
}
