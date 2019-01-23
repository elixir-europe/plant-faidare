package fr.inra.urgi.gpds.repository.es;

import com.google.common.collect.Sets;
import fr.inra.urgi.gpds.Application;
import fr.inra.urgi.gpds.domain.criteria.StudySearchCriteria;
import fr.inra.urgi.gpds.domain.data.impl.LocationVO;
import fr.inra.urgi.gpds.domain.data.impl.StudyDetailVO;
import fr.inra.urgi.gpds.domain.data.impl.StudySummaryVO;
import fr.inra.urgi.gpds.domain.response.PaginatedList;
import fr.inra.urgi.gpds.repository.es.setup.ESSetUp;
import org.assertj.core.api.Assertions;
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
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@ExtendWith(SpringExtension.class)
@Import({ESSetUp.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource("/test.properties")
@SpringBootTest(classes = Application.class)
class StudyRepositoryTest {

    @Autowired
    ESSetUp esSetUp;

    @BeforeAll
    void before() {
        esSetUp.initialize(StudyDetailVO.class, 0);
        esSetUp.initialize(LocationVO.class, 0);
    }

    @Autowired
    StudyRepository repository;

    @Test
    void should_Get_By_Id() {
        String expectedId = "BTH_Orgeval_2008_SetA2";
        StudyDetailVO result = repository.getById(expectedId);
        assertThat(result).isNotNull();
        assertThat(result).extracting("studyDbId").containsOnly(expectedId);
        assertThat(result).extracting("location").isNotEmpty();
    }


    @Test
    void should_Have_Same_Name_And_StudyName() {
        // BTH_Orgeval_2008_SetA2
        StudyDetailVO result = repository.getById("BTH_Orgeval_2008_SetA2");
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getStudyName()).isNotBlank().isEqualTo(result.getName());

        // S2
        result = repository.getById("S2");
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getStudyName()).isNotBlank().isEqualTo(result.getName());
    }

    @Test
    void should_Find_Paginated() {
        int pageSize = 3;
        int page = 2;
        StudySearchCriteria criteria = new StudySearchCriteria();
        criteria.setPageSize((long) pageSize);
        criteria.setPage((long) page);

        PaginatedList<StudySummaryVO> result = repository.find(criteria);
        assertThat(result).isNotNull().isNotEmpty().hasSize(pageSize);

        assertThat(result.getPagination()).isNotNull();
        assertThat(result.getPagination().getPageSize()).isEqualTo(pageSize);
        assertThat(result.getPagination().getCurrentPage()).isEqualTo(page);
    }

    @Test
    void should_Find_By_Locations() {
        Set<String> expectedLocations = Sets.newHashSet("Bordeaux", "Cavallermaggiore");
        StudySearchCriteria criteria = new StudySearchCriteria();
        criteria.setStudyLocations(expectedLocations);

        PaginatedList<StudySummaryVO> result = repository.find(criteria);

        assertThat(result).isNotNull().hasSize(3);
        assertThat(result).extracting("locationName")
            .containsOnlyElementsOf(expectedLocations);
    }

    @Test
    void should_Find_By_Names() {
        Set<String> expectedNames = Sets.newHashSet("BTH_Lusignan_2015_SetB1", "BTH_Le_Moulon_2006_SetA2");
        StudySearchCriteria criteria = new StudySearchCriteria();
        criteria.setStudyNames(expectedNames);

        PaginatedList<StudySummaryVO> result = repository.find(criteria);

        assertThat(result).isNotNull().isNotEmpty();
        assertThat(result).extracting("name")
            .containsOnlyElementsOf(expectedNames);
    }

    @Test
    void should_Find_All_Criteria() {
        StudySearchCriteria criteria = new StudySearchCriteria();
        Set<String> names = Sets.newHashSet(
            "Test de comparaison de provenances de chene sessile - 181000402",
            "Test de comparaison de provenances de chene sessile - 181000102");
        criteria.setStudyNames(names);
        Set<String> locations = Sets.newHashSet("Saint-Laurent - Forêt domaniale de Vierzon");
        criteria.setStudyLocations(locations);
        Set<String> programs = Sets.newHashSet("PlantaComp");
        criteria.setProgramNames(programs);
        Boolean active = true;
        criteria.setActive(active);
        Set<String> germplasm = Sets.newHashSet("ZG9pOjEwLjE1NDU0LzEuNDkyMTc4NTMwMzAyMDkxOEUxMg==", "ZG9pOjEwLjE1NDU0LzEuNDkyMTc4NjkxNTQwMjg5NkUxMg==");
        criteria.setGermplasmDbIds(germplasm);
        Set<String> variables = Sets.newHashSet("CO_357:0000011");
        criteria.setObservationVariableDbIds(variables);
        String season = "1996";
        criteria.setSeasonDbId(season);
        String type = "Provenance trial";
        criteria.setStudyType(type);

        PaginatedList<StudySummaryVO> result = repository.find(criteria);

        assertThat(result).isNotNull().isNotEmpty();
        assertThat(result).extracting("name").containsOnlyElementsOf(names);
        assertThat(result).extracting("locationName").containsOnlyElementsOf(locations);
        assertThat(result).extracting("programName").containsOnlyElementsOf(programs);
        assertThat(result).extracting("active").containsOnly(active);
        assertThat(result).flatExtracting("seasons").contains(season);
        assertThat(result).extracting("studyType").containsOnly(type);
    }

    @Test
    void should_Find_Sorted() {
        String sortField = "studyName";

        StudySearchCriteria criteria = new StudySearchCriteria();
        criteria.setSortBy(sortField);
        criteria.setSortOrder("desc");

        PaginatedList<StudySummaryVO> result = repository.find(criteria);

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
