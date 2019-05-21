package fr.inra.urgi.faidare.repository.es;

import fr.inra.urgi.faidare.domain.criteria.GermplasmAttributeCriteria;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmAttributeValueListVO;
import fr.inra.urgi.faidare.domain.response.PaginatedList;
import fr.inra.urgi.faidare.elasticsearch.repository.ESFindRepository;

/**
 * Breeding API germplasm attribute
 *
 * @author gcornut
 */
public interface GermplasmAttributeRepository
    extends ESFindRepository<GermplasmAttributeCriteria, GermplasmAttributeValueListVO> {

    @Override
    PaginatedList<GermplasmAttributeValueListVO> find(GermplasmAttributeCriteria criteria);

}
