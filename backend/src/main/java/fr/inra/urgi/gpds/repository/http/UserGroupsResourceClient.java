package fr.inra.urgi.gpds.repository.http;

import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import fr.inra.urgi.gpds.config.GPDSProperties;
import fr.inra.urgi.gpds.filter.AuthenticationStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * User group client used to list the group ids for a user
 *
 * @author gcornut
 *
 *
 */
@Component
public class UserGroupsResourceClient {

    private static final Logger logger = LoggerFactory.getLogger(UserGroupsResourceClient.class);

    private final GPDSProperties properties;
    private final RestTemplate client;

    /**
     * 1 hour cache of user group ids
     */
    private static final Cache<String, List<Integer>> CACHE_GROUPS_FOR_USER =
        CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS).build();

    public UserGroupsResourceClient(
        GPDSProperties properties,
        RestTemplate client
    ) {
        this.client = client;
        this.properties = properties;
    }

    public List<Integer> fetchUserGroups() {
        List<Integer> groups = Lists.newArrayList(0);

        AuthenticationStore.User user = AuthenticationStore.get();
        if (user != null) {
            String userName = user.getName();

            String token = properties.getSecurityUserGroupWsToken();
            String baseUrl = properties.getSecurityUserGroupWsUrl();

            groups = CACHE_GROUPS_FOR_USER.getIfPresent(user);
            if (groups == null && !Strings.isNullOrEmpty(baseUrl)) {
                String url = baseUrl + "?token=" + token + "&userName=" + userName;
                logger.trace("Using web services URL: " + baseUrl);

                HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                headers.set("Authorization", "Basic " + user.getAuthCode());

                HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

                ResponseEntity<Integer[]> response = this.client.exchange(url, HttpMethod.GET, entity, Integer[].class);

                Integer[] groupsArray = response.getBody();

                if (groupsArray != null) {
                    groups = Arrays.asList(groupsArray);
                    CACHE_GROUPS_FOR_USER.put(userName, groups);
                }
            }
        }
        return groups;
    }

}
