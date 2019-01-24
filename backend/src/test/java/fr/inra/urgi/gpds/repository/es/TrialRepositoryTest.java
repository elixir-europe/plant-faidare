package fr.inra.urgi.gpds.repository.es;

import fr.inra.urgi.gpds.Application;
import fr.inra.urgi.gpds.domain.criteria.TrialCriteria;
import fr.inra.urgi.gpds.domain.data.TrialVO;
import fr.inra.urgi.gpds.domain.response.PaginatedList;
import fr.inra.urgi.gpds.repository.es.setup.ESSetUp;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@Import({ESSetUp.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource("/test.properties")
@SpringBootTest(classes = Application.class)
public class TrialRepositoryTest {

    @Autowired
    ESSetUp esSetUp;

    @BeforeAll
    public void before() {
        esSetUp.initialize(TrialVO.class, 0);
    }

    @Autowired
    TrialRepository repository;

    @Test
    void should_Get_By_Id() {
        String expectedId = "T1";
        TrialVO result = repository.getById(expectedId);
        assertThat(result).isNotNull();
        assertThat(result).extracting("trialDbId").containsOnly(expectedId);
    }

    @Test
    void should_Find_Paginated() {
        int pageSize = 3;
        int page = 1;
        TrialCriteria criteria = new TrialCriteria();
        criteria.setPageSize((long) pageSize);
        criteria.setPage((long) page);

        PaginatedList<TrialVO> result = repository.find(criteria);
        assertThat(result).isNotNull().isNotEmpty().hasSize(pageSize);

        assertThat(result.getPagination()).isNotNull();
        assertThat(result.getPagination().getPageSize()).isEqualTo(pageSize);
        assertThat(result.getPagination().getCurrentPage()).isEqualTo(page);
    }

    @Test
    void should_Find_By_Location() {
        String expectedLocationDbId = "37497";
        TrialCriteria criteria = new TrialCriteria();
        criteria.setLocationDbId(expectedLocationDbId);

        PaginatedList<TrialVO> result = repository.find(criteria);

        assertThat(result).isNotNull().isNotEmpty();
        TrialVO trialVO = result.get(0);
        assertThat(trialVO.getStudies()).extracting("locationDbId")
            .contains(expectedLocationDbId);
    }

    @Test
    void should_Find_Sorted() {
        String sortField = "trialName";

        TrialCriteria criteria = new TrialCriteria();
        criteria.setSortBy(sortField);
        criteria.setSortOrder("desc");

        PaginatedList<TrialVO> result = repository.find(criteria);

        assertThat(result).isNotNull().isNotEmpty();
        assertThat(result).extracting(sortField).isSortedAccordingTo(new DescendingOrder());
    }

    private class DescendingOrder implements Comparator<Object> {
        @Override
        public int compare(Object o1, Object o2) {
            return ((String) o2).compareTo(((String) o1));
        }
    }
}
