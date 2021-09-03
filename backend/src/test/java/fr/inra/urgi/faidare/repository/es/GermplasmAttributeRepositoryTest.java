package fr.inra.urgi.faidare.repository.es;

import fr.inra.urgi.faidare.Application;
import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiGermplasmAttributeValue;
import fr.inra.urgi.faidare.domain.criteria.GermplasmAttributeCriteria;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmAttributeValueListVO;
import fr.inra.urgi.faidare.domain.response.PaginatedList;
import fr.inra.urgi.faidare.repository.es.setup.ESSetUp;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@Import({ESSetUp.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource("/test.properties")
@SpringBootTest(classes = Application.class)
class GermplasmAttributeRepositoryTest {

    @Autowired
    ESSetUp esSetUp;

    @BeforeAll
    void before() {
        esSetUp.initialize(GermplasmAttributeValueListVO.class, 0L);
    }

    @Autowired
    GermplasmAttributeRepository repository;

    @Test
    void should_Find_Paginated() {
        int pageSize = 3;
        int page = 1;
        GermplasmAttributeCriteria criteria = new GermplasmAttributeCriteria();
        criteria.setPageSize((long) pageSize);
        criteria.setPage((long) page);

        PaginatedList<GermplasmAttributeValueListVO> result = repository.find(criteria);
        assertThat(result).isNotNull().isNotEmpty().hasSize(pageSize);

        assertThat(result.getPagination()).isNotNull();
        assertThat(result.getPagination().getPageSize()).isEqualTo(pageSize);
        assertThat(result.getPagination().getCurrentPage()).isEqualTo(page);
    }

    @Test
    void should_Find_Empty_By_Germplasm() {
        String expectedGermplasm = "ZG9pOjEwLjE1NDU0LzEuNDkyMTc4NTUxNDk2NzQzRTEy";
        GermplasmAttributeCriteria criteria = new GermplasmAttributeCriteria();
        criteria.setGermplasmDbId(expectedGermplasm);

        PaginatedList<GermplasmAttributeValueListVO> result = repository.find(criteria);

        assertThat(result).isNotNull().isNotEmpty().hasSize(1);

        GermplasmAttributeValueListVO attribute = result.get(0);
        assertThat(attribute.getGermplasmDbId()).isEqualTo(expectedGermplasm);
        assertThat(attribute.getData()).isNotNull().isEmpty();
    }

    @Test
    void should_Find_All_Attributes_By_Germplasm() {
        String expectedGermplasm = "ZG9pOjEwLjE1NDU0LzEuNDkyMTc4NTU1NDM3OTc1OEUxMg==";
        GermplasmAttributeCriteria criteria = new GermplasmAttributeCriteria();
        criteria.setGermplasmDbId(expectedGermplasm);

        PaginatedList<GermplasmAttributeValueListVO> result = repository.find(criteria);

        assertThat(result).isNotNull().isNotEmpty().hasSize(1);

        GermplasmAttributeValueListVO attribute = result.get(0);
        assertThat(attribute.getGermplasmDbId()).isEqualTo(expectedGermplasm);
        assertThat(attribute.getData()).isNotNull().hasSize(3);
    }

    @Test
    void should_Find_By_Germplasm_And_Attribute() {
        String expectedGermplasm = "ZG9pOjEwLjE1NDU0LzEuNDkyMTc4NTU1NDM3OTc1OEUxMg==";
        List<String> expectedAttributes = Arrays.asList("39228", "39229");
        GermplasmAttributeCriteria criteria = new GermplasmAttributeCriteria();
        criteria.setGermplasmDbId(expectedGermplasm);
        criteria.setAttributeList(expectedAttributes);

        PaginatedList<GermplasmAttributeValueListVO> result = repository.find(criteria);

        assertThat(result).isNotNull().isNotEmpty().hasSize(1);

        List<BrapiGermplasmAttributeValue> data = result.get(0).getData();
        assertThat(data).isNotNull().isNotEmpty().hasSize(2);
        assertThat(data)
            .extracting("attributeDbId")
            .isSubsetOf(expectedAttributes);
    }
}
