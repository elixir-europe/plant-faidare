package fr.inra.urgi.gpds.repository.es;

import fr.inra.urgi.gpds.domain.criteria.ObservationUnitCriteria;
import fr.inra.urgi.gpds.domain.data.impl.ObservationUnitVO;
import fr.inra.urgi.gpds.domain.response.PaginatedList;
import fr.inra.urgi.gpds.elasticsearch.repository.ESFindRepository;

/**
 * Breeding API observation unit
 *
 * @author gcornut
 *
 *
 */
public interface ObservationUnitRepository
		extends ESFindRepository<ObservationUnitCriteria, ObservationUnitVO> {

	@Override
	PaginatedList<ObservationUnitVO> find(ObservationUnitCriteria criteria);

}
