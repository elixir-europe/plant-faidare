package fr.inra.urgi.gpds.repository.http;

import fr.inra.urgi.gpds.filter.AuthenticationStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class UserGroupsResourceClientTest {

	@Autowired
    UserGroupsResourceClient client;

	@Test
	public void should_Return_User_Groups_When_User_Is_Authenticated() {
		AuthenticationStore.setUser("ekimmel", "ZWtpbW1lbDpla2ltbWVs");
		List<Integer> groups = client.fetchUserGroups();
		assertThat(groups).isNotNull().isNotEmpty().contains(0, 34, 43, 25, 32, 8);
	}

	@Test
	public void should_Return_User_Group_0_When_No_User_Is_Authenticated() {
		List<Integer> groups = client.fetchUserGroups();
		assertThat(groups).isNotNull().isNotEmpty().containsExactly(0);
	}

}
