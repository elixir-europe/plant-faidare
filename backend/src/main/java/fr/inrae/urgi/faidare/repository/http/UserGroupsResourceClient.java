package fr.inrae.urgi.faidare.repository.http;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import fr.inrae.urgi.faidare.config.FaidareProperties;
import fr.inrae.urgi.faidare.config.RestClientCustomizations;
import fr.inrae.urgi.faidare.filter.AuthenticationStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

/**
 * User group client used to list the group ids for a user
 *
 * @author gcornut
 */
@Component
public class UserGroupsResourceClient {

    private static final Logger logger = LoggerFactory.getLogger(UserGroupsResourceClient.class);

    /**
     * 1 hour cache of user group ids
     */
    private static final Cache<String, List<Integer>> CACHE_GROUPS_FOR_USER =
        CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS).build();

    private static final List<Integer> PUBLIC_GROUPS = Collections.singletonList(0);

    private final FaidareProperties properties;
    private final RestClient client;
    private final RestClient.Builder clientBuilder;

    @Autowired
    public UserGroupsResourceClient(
        FaidareProperties properties,
        RestClient.Builder clientBuilder
    ) {
        this.clientBuilder = clientBuilder;
        this.client = clientBuilder.build();
        this.properties = properties;
    }

    public List<Integer> getUserGroups() {
        String userName = AuthenticationStore.get();
        if (userName == null) {
            return PUBLIC_GROUPS;
        }

        // From cache
        List<Integer> groups = CACHE_GROUPS_FOR_USER.getIfPresent(userName);
        if (groups != null) {
            return groups;
        }

        // From user group WS
        groups = fetchUserGroups(userName);
        if (groups != null) {
            // Add to cache
            CACHE_GROUPS_FOR_USER.put(userName, groups);
            return groups;
        }

        // Default to public groups
        return PUBLIC_GROUPS;
    }

    private List<Integer> fetchUserGroups(String userName) {
        String token = properties.getSecurityUserGroupWsToken();
        String baseUrl = properties.getSecurityUserGroupWsUrl();
        if (Strings.isNullOrEmpty(baseUrl) || Strings.isNullOrEmpty(token)) {
            logger.warn("No configured security group WS in application properties. Only public data will be shown.");
            return PUBLIC_GROUPS;
        }
        
        return this.client
            .get()
            .uri(baseUrl, uriBuilder ->
                uriBuilder
                    .queryParam("token", token)
                    .queryParam("userName", userName)
                    .build()
            )
            .retrieve()
            .toEntity(new ParameterizedTypeReference<List<Integer>>() {}).getBody();
    }

}
