package fr.inra.urgi.faidare.repository.es;

import fr.inra.urgi.faidare.domain.criteria.ObservationUnitCriteria;
import fr.inra.urgi.faidare.domain.data.phenotype.ObservationUnitVO;
import fr.inra.urgi.faidare.domain.response.PaginatedList;
import fr.inra.urgi.faidare.elasticsearch.repository.ESFindRepository;

/**
 * Breeding API observation unit
 *
 * @author gcornut
 */
public interface ObservationUnitRepository
    extends ESFindRepository<ObservationUnitCriteria, ObservationUnitVO> {

    @Override
    PaginatedList<ObservationUnitVO> find(ObservationUnitCriteria criteria);

}
