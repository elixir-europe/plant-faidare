package fr.inra.urgi.gpds.repository.es;

import fr.inra.urgi.gpds.domain.criteria.GermplasmAttributeCriteria;
import fr.inra.urgi.gpds.domain.data.impl.germplasm.GermplasmAttributeValueListVO;
import fr.inra.urgi.gpds.domain.response.PaginatedList;
import fr.inra.urgi.gpds.elasticsearch.repository.ESFindRepository;

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
