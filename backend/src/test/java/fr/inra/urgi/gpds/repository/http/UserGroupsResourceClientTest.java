package fr.inra.urgi.gpds.repository.http;

import fr.inra.urgi.gpds.config.GPDSProperties;
import fr.inra.urgi.gpds.filter.AuthenticationStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserGroupsResourceClientTest {

    @Mock
    GPDSProperties properties;

    @Mock
    RestTemplate restClient;

    @InjectMocks
    UserGroupsResourceClient client;

    @Test
    void should_Return_User_Groups_When_User_Is_Authenticated() {
        String userName = "ekimmel";
        String authorization = "<AUTH_CODE>";

        when(properties.getSecurityUserGroupWsUrl()).thenReturn("http://example.com/api");
        when(properties.getSecurityUserGroupWsToken()).thenReturn("<TOKEN>");

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Basic " + authorization);

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        Integer[] expectedGroups = {0, 34, 43, 25, 32, 8};
        ResponseEntity<Integer[]> response = new ResponseEntity<>(expectedGroups, HttpStatus.OK);
        when(restClient.exchange("http://example.com/api?token=<TOKEN>&userName=" + userName, HttpMethod.GET, entity, Integer[].class))
            .thenReturn(response);

        AuthenticationStore.setUser(userName, authorization);

        List<Integer> actualGroups = client.fetchUserGroups();

        assertThat(actualGroups).isNotNull().contains(expectedGroups);
        AuthenticationStore.reset();
    }

    @Test
    void should_Return_User_Group_0_When_No_User_Is_Authenticated() {
        List<Integer> groups = client.fetchUserGroups();
        assertThat(groups).isNotNull().isNotEmpty().containsExactly(0);
    }

}
