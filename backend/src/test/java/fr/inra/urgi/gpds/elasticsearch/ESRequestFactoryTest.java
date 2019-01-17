package fr.inra.urgi.gpds.elasticsearch;

import com.fasterxml.jackson.databind.util.ArrayIterator;
import fr.inra.urgi.gpds.config.GPDSProperties;
import fr.inra.urgi.gpds.repository.http.UserGroupsResourceClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author gcornut
 */
@ExtendWith(SpringExtension.class)
public class ESRequestFactoryTest {

	@InjectMocks
    ESRequestFactory requestFactory;

	@Mock
    GPDSProperties properties;

	@Mock
    UserGroupsResourceClient userGroupsResourceClient;

	@Test
	public void should_Generate_Aliases_Single_Group_No_Source() {
		String documentType = "doc1";
		List<Integer> groups = Collections.singletonList(0);
		String index0 = "index_group0";

		when(properties.getIndexName("*", documentType, 0)).thenReturn(index0);

		when(userGroupsResourceClient.fetchUserGroups()).thenReturn(groups);

		String[] aliases = requestFactory.getAliases(documentType);
		assertThat(aliases).containsExactly(index0);
	}

	@Test
	public void should_Generate_Aliases_Multiple_Groups_Multiple_Sources() {
		String documentType = "doc1";
		List<Integer> groups = Arrays.asList(0, 1, 2);
		List<String> sources = Arrays.asList("a", "b", "c");
		String[] indices = {
				"index_sourcea_group1",
				"index_sourcea_group2",
				"index_sourcea_group0",
				"index_sourceb_group0",
				"index_sourceb_group1",
				"index_sourceb_group2",
				"index_sourcec_group0",
				"index_sourcec_group1",
				"index_sourcec_group2",
		};

		when(properties.getIndexName("a", documentType, 1)).thenReturn(indices[0]);
		when(properties.getIndexName("a", documentType, 2)).thenReturn(indices[1]);
		when(properties.getIndexName("a", documentType, 0)).thenReturn(indices[2]);
		when(properties.getIndexName("b", documentType, 0)).thenReturn(indices[3]);
		when(properties.getIndexName("b", documentType, 1)).thenReturn(indices[4]);
		when(properties.getIndexName("b", documentType, 2)).thenReturn(indices[5]);
		when(properties.getIndexName("c", documentType, 0)).thenReturn(indices[6]);
		when(properties.getIndexName("c", documentType, 1)).thenReturn(indices[7]);
		when(properties.getIndexName("c", documentType, 2)).thenReturn(indices[8]);

		when(userGroupsResourceClient.fetchUserGroups()).thenReturn(groups);

		String[] aliases = requestFactory.getAliases(documentType, sources);
		assertThat(aliases).containsOnlyElementsOf(new ArrayIterator<>(indices));
	}

}
