package fr.inra.urgi.gpds.repository.es;

import com.google.common.collect.Sets;
import fr.inra.urgi.gpds.domain.criteria.LocationCriteria;
import fr.inra.urgi.gpds.domain.data.impl.LocationVO;
import fr.inra.urgi.gpds.domain.response.PaginatedList;
import fr.inra.urgi.gpds.repository.es.setup.ESSetUp;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
public class LocationRepositoryTest {

	@Autowired
	ESSetUp esSetUp;

	private static boolean dbInitialized = false;

	@BeforeAll
	public void before() {
		if (!dbInitialized) {
			dbInitialized = esSetUp.initialize(LocationVO.class, 0);
		}
	}

    @Autowired
	LocationRepository repository;

    @Test
    public void should_Get_By_Id() {
		String expectedId = "805";
		LocationVO result = repository.getById(expectedId);
		assertThat(result).isNotNull();
		assertThat(result).extracting("locationDbId").containsOnly(expectedId);
	}

	@Test
    public void should_Find() {
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
    public void should_Find_By_Types() {
		Set<String> expectedTypes = Sets.newHashSet("Breeding and Evaluation site", "Origin and Breeding site");
		LocationCriteria criteria = new LocationCriteria();
		criteria.setLocationTypes(expectedTypes);

		PaginatedList<LocationVO> locations = repository.find(criteria);

		assertThat(locations).isNotNull().hasSize(3);
		assertThat(locations).extracting("locationType").containsOnlyElementsOf(expectedTypes);
	}
}
