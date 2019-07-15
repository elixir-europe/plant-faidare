package fr.inra.urgi.faidare.elasticsearch;

import com.fasterxml.jackson.databind.util.ArrayIterator;
import fr.inra.urgi.faidare.config.FaidareProperties;
import fr.inra.urgi.faidare.repository.http.UserGroupsResourceClient;
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
class ESRequestFactoryTest {

    @InjectMocks
    ESRequestFactory requestFactory;

    @Mock
    FaidareProperties properties;

    @Mock
    UserGroupsResourceClient userGroupsResourceClient;

    @Test
    void should_Generate_Aliases_Single_Group_No_Source() {
        String documentType = "doc1";
        List<Integer> groups = Collections.singletonList(0);
        String index0 = "index_group0";

        when(properties.getAliasName(documentType, 0)).thenReturn(index0);

        when(userGroupsResourceClient.getUserGroups()).thenReturn(groups);

        String[] aliases = requestFactory.getAliases(documentType);
        assertThat(aliases).containsExactly(index0);
    }

    @Test
    void should_Generate_Aliases_Multiple_Groups_Multiple_Sources() {
        String documentType = "doc1";
        List<Integer> groups = Arrays.asList(0, 1, 2);
        List<String> sources = Arrays.asList("a", "b", "c");
        String[] indices = {
            "index_sourcea_group1",
            "index_sourcea_group2",
            "index_sourcea_group0",
        };

        when(properties.getAliasName(documentType, 1)).thenReturn(indices[0]);
        when(properties.getAliasName(documentType, 2)).thenReturn(indices[1]);
        when(properties.getAliasName(documentType, 0)).thenReturn(indices[2]);

        when(userGroupsResourceClient.getUserGroups()).thenReturn(groups);

        String[] aliases = requestFactory.getAliases(documentType, sources);
        assertThat(aliases).containsOnlyElementsOf(new ArrayIterator<>(indices));
    }

}
