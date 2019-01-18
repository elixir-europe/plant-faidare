package fr.inra.urgi.gpds.repository.es;

import com.google.common.collect.Sets;
import fr.inra.urgi.gpds.Application;
import fr.inra.urgi.gpds.domain.criteria.ObservationUnitCriteria;
import fr.inra.urgi.gpds.domain.data.impl.ObservationUnitVO;
import fr.inra.urgi.gpds.domain.data.impl.ObservationVO;
import fr.inra.urgi.gpds.domain.response.PaginatedList;
import fr.inra.urgi.gpds.repository.es.setup.ESSetUp;
import org.assertj.core.api.Condition;
import org.joda.time.DateTime;
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
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@Import({ESSetUp.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource("/test.properties")
@SpringBootTest(classes = Application.class)
class ObservationUnitRepositoryTest {

	@Autowired
    ESSetUp esSetUp;

	@BeforeAll
	void before() {
	    esSetUp.initialize(ObservationUnitVO.class, 0);
	}

    @Autowired
	ObservationUnitRepository repository;

	@Test
    void should_Find_Paginated() {
		int pageSize = 3;
		int page = 1;
		ObservationUnitCriteria criteria = new ObservationUnitCriteria();
		criteria.setPageSize((long) pageSize);
		criteria.setPage((long) page);

		PaginatedList<ObservationUnitVO> result = repository.find(criteria);
		assertThat(result).isNotNull().isNotEmpty().hasSize(pageSize);

		assertThat(result.getPagination()).isNotNull();
		assertThat(result.getPagination().getPageSize()).isEqualTo(pageSize);
		assertThat(result.getPagination().getCurrentPage()).isEqualTo(page);
	}

	@Test
    void should_Find_By_Level() {
		String expectedLevel = "BLOCK";
		ObservationUnitCriteria criteria = new ObservationUnitCriteria();
		criteria.setObservationLevel(expectedLevel);

		PaginatedList<ObservationUnitVO> result = repository.find(criteria);

		assertThat(result).isNotNull().hasSize(5);
		assertThat(result).extracting("observationLevel").containsOnly(expectedLevel);
	}

	@Test
    void should_Find_By_Timestamp_Range() {
		ObservationUnitCriteria criteria = new ObservationUnitCriteria();

		String from = "2001-12-31T00:00:00Z";
		String to = "2005-12-31T01:00:00Z";
		final Date fromDate = DateTime.parse(from).toDate();
		final Date toDate = DateTime.parse(to).toDate();
		List<String> timestampRange = Arrays.asList(from, to);
		criteria.setObservationTimeStampRange(timestampRange);

		PaginatedList<ObservationUnitVO> result = repository.find(criteria);

		assertThat(result).isNotNull().hasSize(4);
		assertThat(result)
				.flatExtracting("observations")
				.are(new Condition<Object>() {
					@Override
                    public boolean matches(Object data) {
						if (data instanceof ObservationVO) {
							ObservationVO observation = (ObservationVO) data;
							Date timeStamp = observation.getObservationTimeStamp();
							assertThat(timeStamp).isAfter(fromDate);
							assertThat(timeStamp).isBefore(toDate);
							return true;
						}
						return false;
					}
				});
	}

	@Test
	void should_Fail_Find_Invalid_Timestamp_Range() {
		ObservationUnitCriteria criteria = new ObservationUnitCriteria();

		List<String> timestampRange = Arrays.asList("foo", "bar", "baz");
		criteria.setObservationTimeStampRange(timestampRange);

		assertThrows(
		    RuntimeException.class,
            () -> repository.find(criteria)
        );
	}

	@Test
	void should_Find_By_Variables() {
		ObservationUnitCriteria criteria = new ObservationUnitCriteria();

		String variableId1 = "CO_357:0000088";
		String variableId2 = "CO_357:0000089";
		Set<String> variableIds = Sets.newHashSet(variableId1, variableId2);
		criteria.setObservationVariableDbIds(variableIds);

		PaginatedList<ObservationUnitVO> result = repository.find(criteria);

		assertThat(result).isNotNull().isNotEmpty();
		assertThat(result)
				.flatExtracting("observations")
				.extracting("observationVariableDbId")
				.containsOnlyElementsOf(variableIds);
	}

	@Test
	void should_Find_By_Variables_And_Timestamp() {
		ObservationUnitCriteria criteria = new ObservationUnitCriteria();

		String variableId1 = "CO_357:0000088";
		String variableId2 = "CO_357:0000089";
		Set<String> variableIds = Sets.newHashSet(variableId1, variableId2);
		criteria.setObservationVariableDbIds(variableIds);

		String from = "2002-07-03T00:00:00Z";
		String to = "2005-11-14T00:00:00Z";
		final Date fromDate = DateTime.parse(from).toDate();
		final Date toDate = DateTime.parse(to).toDate();
		List<String> timestampRange = Arrays.asList(from, to);
		criteria.setObservationTimeStampRange(timestampRange);

		PaginatedList<ObservationUnitVO> result = repository.find(criteria);

		assertThat(result).isNotNull().isNotEmpty();

		assertThat(result)
				.flatExtracting("observations")
				.extracting("observationVariableDbId")
				.containsOnlyElementsOf(variableIds);

		assertThat(result)
				.flatExtracting("observations")
				.are(new ObservationsInTimeRange(fromDate, toDate));
	}


	@Test
	void should_Find_By_Invalid_Variables() {
		ObservationUnitCriteria criteria = new ObservationUnitCriteria();

		String variableId1 = "FOO:BAR";
		Set<String> variableIds = Sets.newHashSet(variableId1);
		criteria.setObservationVariableDbIds(variableIds);

		PaginatedList<ObservationUnitVO> result = repository.find(criteria);

		assertThat(result).isNotNull().isEmpty();
	}


	@Test
	void should_Find_By_All_Criteria() {
		ObservationUnitCriteria criteria = new ObservationUnitCriteria();

		String variableId1 = "CO_357:0000088";
		String variableId2 = "CO_357:0000089";
		Set<String> variableIds = Sets.newHashSet(variableId1, variableId2);
		criteria.setObservationVariableDbIds(variableIds);

		String from = "2002-07-03T00:00:00Z";
		String to = "2005-11-14T00:00:00Z";
		final Date fromDate = DateTime.parse(from).toDate();
		final Date toDate = DateTime.parse(to).toDate();
		List<String> timestampRange = Arrays.asList(from, to);
		criteria.setObservationTimeStampRange(timestampRange);

		Set<String> programIds = Sets.newHashSet("P1", "P2");
		criteria.setProgramDbIds(programIds);

		Set<String> germplasmIds = Sets.newHashSet("G1", "G2");
		criteria.setGermplasmDbIds(germplasmIds);

		Set<String> locationIds = Sets.newHashSet("34069");
		criteria.setLocationDbIds(locationIds);

		Set<String> seasons = Sets.newHashSet("2002");
		criteria.setSeasonDbIds(seasons);

		String observationLevel = "BLOCK";
		criteria.setObservationLevel(observationLevel);

		Set<String> studyIds = Sets.newHashSet("POP2-Orleans-chancre");
		criteria.setStudyDbIds(studyIds);

		PaginatedList<ObservationUnitVO> result = repository.find(criteria);

		assertThat(result).isNotNull().isNotEmpty();

		assertThat(result)
				.extracting("studyDbId")
				.containsOnlyElementsOf(studyIds);

		assertThat(result)
				.extracting("observationLevel")
				.containsOnly(observationLevel);

		assertThat(result)
				.extracting("studyLocationDbId")
				.containsOnlyElementsOf(locationIds);

		assertThat(result)
				.extracting("germplasmDbId")
				.containsOnlyElementsOf(germplasmIds);

		assertThat(result)
				.flatExtracting("observations")
				.extracting("season")
				.containsOnlyElementsOf(seasons);

		assertThat(result)
				.flatExtracting("observations")
				.extracting("observationVariableDbId")
				.containsOnlyElementsOf(variableIds);

		assertThat(result)
				.flatExtracting("observations")
				.are(new ObservationsInTimeRange(fromDate, toDate));
	}

	private static class ObservationsInTimeRange extends Condition<Object> {
		private final Date fromDate;
		private final Date toDate;

		ObservationsInTimeRange(Date fromDate, Date toDate) {
			this.fromDate = fromDate;
			this.toDate = toDate;
		}

		@Override
        public boolean matches(Object data) {
			if (data instanceof ObservationVO) {
				ObservationVO observation = (ObservationVO) data;
				Date timeStamp = observation.getObservationTimeStamp();
				assertThat(timeStamp).isAfter(fromDate);
				assertThat(timeStamp).isBefore(toDate);
				return true;
			}
			return false;
		}
	}
}
