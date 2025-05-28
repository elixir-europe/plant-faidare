package fr.inrae.urgi.faidare.repository.http;

import fr.inrae.urgi.faidare.config.FaidareProperties;
import fr.inrae.urgi.faidare.filter.AuthenticationStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserGroupsResourceClientTest {

    @Mock
    FaidareProperties properties;

    @Mock
    RestTemplate restClient;

    @InjectMocks
    UserGroupsResourceClient client;

    @Test
    void should_Return_User_Groups_When_User_Is_Authenticated() {
        String userName = "ekimmel";

        when(properties.getSecurityUserGroupWsUrl()).thenReturn("http://example.com/api");
        when(properties.getSecurityUserGroupWsToken()).thenReturn("<TOKEN>");

        Integer[] expectedGroups = {0, 34, 43, 25, 32, 8};
        ResponseEntity<Integer[]> response = new ResponseEntity<>(expectedGroups, HttpStatus.OK);
        when(restClient.getForEntity("http://example.com/api?token=<TOKEN>&userName=" + userName, Integer[].class))
            .thenReturn(response);

        AuthenticationStore.set(userName);

        List<Integer> actualGroups = client.getUserGroups();

        assertThat(actualGroups).isNotNull().contains(expectedGroups);
        AuthenticationStore.reset();
    }

    @Test
    void should_Return_User_Group_0_When_No_User_Is_Authenticated() {
        List<Integer> groups = client.getUserGroups();
        assertThat(groups).isNotNull().isNotEmpty().containsExactly(0);
    }

}
