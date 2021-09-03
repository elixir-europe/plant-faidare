package fr.inra.urgi.faidare.repository.es;

import com.google.common.collect.Sets;
import fr.inra.urgi.faidare.Application;
import fr.inra.urgi.faidare.domain.criteria.LocationCriteria;
import fr.inra.urgi.faidare.domain.data.LocationSitemapVO;
import fr.inra.urgi.faidare.domain.data.LocationVO;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmSitemapVO;
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

import java.util.Iterator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@Import({ESSetUp.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource("/test.properties")
@SpringBootTest(classes = Application.class)
class LocationRepositoryTest {

    @Autowired
    ESSetUp esSetUp;

    @BeforeAll
    void before() {
        esSetUp.initialize(LocationVO.class, 0L);
    }

    @Autowired
    LocationRepository repository;

    @Test
    void should_Get_By_Id() {
        String expectedId = "805";
        LocationVO result = repository.getById(expectedId);
        assertThat(result).isNotNull();
        assertThat(result.getLocationDbId()).isEqualTo(expectedId);
    }

    @Test
    void should_Have_Same_Name_And_LocationName() {
        // 805
        LocationVO result = repository.getById("805");
        assertThat(result).isNotNull();
        assertThat(result.getLocationName()).isNotBlank().isEqualTo(result.getName());

        // 806
        result = repository.getById("806");
        assertThat(result).isNotNull();
        assertThat(result.getLocationName()).isNotBlank().isEqualTo(result.getName());
    }

    @SuppressWarnings("deprecation")
    @Test
    void should_Have_Same_Abbreviation_And_Abreviation() {
        // 805
        LocationVO result = repository.getById("805");
        assertThat(result).isNotNull();
        assertThat(result.getAbbreviation()).isNotBlank().isEqualTo(result.getAbreviation());

        // 806
        result = repository.getById("806");
        assertThat(result).isNotNull();
        assertThat(result.getAbbreviation()).isNotBlank().isEqualTo(result.getAbreviation());
    }

    @SuppressWarnings("deprecation")
    @Test
    void should_Have_Same_InstituteAddress_And_InstituteAdress() {
        // 805
        LocationVO result = repository.getById("805");
        assertThat(result).isNotNull();
        assertThat(result.getInstituteAddress()).isNotBlank().isEqualTo(result.getInstituteAdress());

        // 806
        result = repository.getById("806");
        assertThat(result).isNotNull();
        assertThat(result.getInstituteAddress()).isNotBlank().isEqualTo(result.getInstituteAdress());
    }

    @Test
    void should_Find() {
        int pageSize = 3;
        int page = 1;
        LocationCriteria criteria = new LocationCriteria();
        criteria.setPageSize((long) pageSize);
        criteria.setPage((long) page);

        PaginatedList<LocationVO> result = repository.find(criteria);
        assertThat(result).isNotNull().hasSize(pageSize);

        assertThat(result.getPagination()).isNotNull();
        assertThat(result.getPagination().getPageSize()).isEqualTo(pageSize);
        assertThat(result.getPagination().getCurrentPage()).isEqualTo(page);
    }

    @Test
    void should_Find_By_Types() {
        Set<String> expectedTypes = Sets.newHashSet("Breeding and Evaluation site", "Origin and Breeding site");
        LocationCriteria criteria = new LocationCriteria();
        criteria.setLocationTypes(expectedTypes);

        PaginatedList<LocationVO> locations = repository.find(criteria);

        assertThat(locations).isNotNull().hasSize(3);
        assertThat(locations).extracting("locationType").hasSameElementsAs(expectedTypes);
    }

    @Test
    void shouldScrollAllForSitemap() {
        Iterator<LocationSitemapVO> list = repository.scrollAllForSitemap(100);
        assertThat(list).toIterable()
                        .isNotEmpty()
                        .allMatch(vo -> !vo.getLocationDbId().isEmpty());
    }


}
