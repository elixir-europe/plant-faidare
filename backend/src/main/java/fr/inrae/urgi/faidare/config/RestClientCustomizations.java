package fr.inrae.urgi.faidare.config;

import java.time.Duration;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.json.JsonMapper;

/**
 * Utility class to help with the customization of a RestClient
 * @author JB Nizet
 */
public final class RestClientCustomizations {

    private static final JsonMapper PERMISSIVE_MAPPER =
        JsonMapper.builder()
                  .disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
                  .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                  .disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)
                  .disable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS)
                  .disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
                  .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                  .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
                  .build();

    public static void configureWithPermissiveObjectMapper(RestClient.Builder builder) {
        builder.configureMessageConverters(
            b -> b.withJsonConverter(
                new JacksonJsonHttpMessageConverter(PERMISSIVE_MAPPER)
            )
        );
    }
}
