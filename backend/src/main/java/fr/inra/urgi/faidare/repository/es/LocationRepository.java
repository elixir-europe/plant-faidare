package fr.inra.urgi.faidare.repository.es;

import java.util.Iterator;

import fr.inra.urgi.faidare.domain.criteria.LocationCriteria;
import fr.inra.urgi.faidare.domain.data.LocationSitemapVO;
import fr.inra.urgi.faidare.domain.data.LocationVO;
import fr.inra.urgi.faidare.domain.response.PaginatedList;
import fr.inra.urgi.faidare.elasticsearch.repository.ESFindRepository;
import fr.inra.urgi.faidare.elasticsearch.repository.ESGetByIdRepository;

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

    Iterator<LocationSitemapVO> scrollAllForSitemap(int fetchSize);
}
