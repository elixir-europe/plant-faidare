package fr.inra.urgi.gpds.repository.es;

import fr.inra.urgi.gpds.Application;
import fr.inra.urgi.gpds.domain.criteria.ProgramCriteria;
import fr.inra.urgi.gpds.domain.data.impl.ProgramVO;
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

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@Import({ESSetUp.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource("/test.properties")
@SpringBootTest(classes = Application.class)
class ProgramRepositoryTest {

    @Autowired
    ProgramRepository repository;

    @Autowired
    ESSetUp esSetUp;

    @BeforeAll
    public void before() {
        esSetUp.initialize(ProgramVO.class, 0);
    }

    @Test
    void should_Get_By_Id() {
        String expectedId = "P1";
        ProgramVO result = repository.getById(expectedId);
        assertThat(result).isNotNull();
        assertThat(result).extracting("programDbId").containsOnly(expectedId);
    }

    @Test
    void should_Have_Same_Name_And_ProgramName() {
        // P1
        ProgramVO result = repository.getById("P1");
        assertThat(result).isNotNull();
        assertThat(result.getProgramName()).isNotBlank().isEqualTo(result.getName());

        // P2
        result = repository.getById("P2");
        assertThat(result).isNotNull();
        assertThat(result.getProgramName()).isNotBlank().isEqualTo(result.getName());
    }

    @Test
    void should_Find_Paginated() {
        int pageSize = 3;
        int page = 1;
        ProgramCriteria criteria = new ProgramCriteria();
        criteria.setPageSize((long) pageSize);
        criteria.setPage((long) page);

        PaginatedList<ProgramVO> result = repository.find(criteria);
        assertThat(result).isNotNull().isNotEmpty().hasSize(pageSize);

        assertThat(result.getPagination()).isNotNull();
        assertThat(result.getPagination().getPageSize()).isEqualTo(pageSize);
        assertThat(result.getPagination().getCurrentPage()).isEqualTo(page);
    }

    @Test
    void should_Find_By_Name() {
        String expectedName = "Amaizing";
        ProgramCriteria criteria = new ProgramCriteria();
        criteria.setName(expectedName);

        PaginatedList<ProgramVO> result = repository.find(criteria);

        assertThat(result).isNotNull().isNotEmpty().hasSize(1);
        assertThat(result).extracting("name").containsOnly(expectedName);
    }

    @Test
    void should_Find_By_Abbreviation() {
        String expectedAbbreviation = "ANR-12-ADAP-0009";
        ProgramCriteria criteria = new ProgramCriteria();
        criteria.setAbbreviation(expectedAbbreviation);

        PaginatedList<ProgramVO> result = repository.find(criteria);

        assertThat(result).isNotNull().isNotEmpty().hasSize(1);
        assertThat(result).extracting("abbreviation").containsOnly(expectedAbbreviation);
    }
}
