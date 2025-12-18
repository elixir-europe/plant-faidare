package fr.inrae.urgi.faidare.repository.http;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.List;

import fr.inrae.urgi.faidare.filter.AuthenticationStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.test.autoconfigure.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

@RestClientTest(
    value = UserGroupsResourceClient.class,
    properties = {
        "faidare.securityUserGroupWsUrl=http://example.com/api",
        "faidare.securityUserGroupWsToken=TOKEN"
    }
)
class UserGroupsResourceClientTest {

    @Autowired
    private MockRestServiceServer mockServer;

    @Autowired
    private UserGroupsResourceClient client;

    @Test
    void should_Return_User_Groups_When_User_Is_Authenticated() {
        String userName = "ekimmel";

        String json = "[0, 34, 43, 25, 32, 8]";
        String expectedUri = "http://example.com/api?token=TOKEN&userName=" + userName;
        mockServer.expect(requestTo(expectedUri)).andRespond(
            withSuccess(json, MediaType.APPLICATION_JSON)
        );

        AuthenticationStore.set(userName);

        List<Integer> actualGroups = client.getUserGroups();

        assertThat(actualGroups).containsExactly(0, 34, 43, 25, 32, 8);
        AuthenticationStore.reset();
    }

    @Test
    void should_Return_User_Group_0_When_No_User_Is_Authenticated() {
        List<Integer> groups = client.getUserGroups();
        assertThat(groups).isNotNull().isNotEmpty().containsExactly(0);
    }
}
