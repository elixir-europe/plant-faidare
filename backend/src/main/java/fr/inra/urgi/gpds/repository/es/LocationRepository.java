package fr.inra.urgi.gpds.repository.es;

import fr.inra.urgi.gpds.domain.criteria.LocationCriteria;
import fr.inra.urgi.gpds.domain.data.LocationVO;
import fr.inra.urgi.gpds.domain.response.PaginatedList;
import fr.inra.urgi.gpds.elasticsearch.repository.ESFindRepository;
import fr.inra.urgi.gpds.elasticsearch.repository.ESGetByIdRepository;

/**
 * Breeding API location
 *
 * @author gcornut
 */
public interface LocationRepository
    extends ESFindRepository<LocationCriteria, LocationVO>,
    ESGetByIdRepository<LocationVO> {

    @Override
    LocationVO getById(String id);

    @Override
    PaginatedList<LocationVO> find(LocationCriteria criteria);

}
