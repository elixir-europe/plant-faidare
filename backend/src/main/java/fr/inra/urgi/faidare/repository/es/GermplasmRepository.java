package fr.inra.urgi.faidare.repository.es;

import fr.inra.urgi.faidare.domain.criteria.GermplasmSearchCriteria;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmVO;
import fr.inra.urgi.faidare.domain.data.germplasm.PedigreeVO;
import fr.inra.urgi.faidare.domain.data.germplasm.ProgenyVO;
import fr.inra.urgi.faidare.domain.response.PaginatedList;
import fr.inra.urgi.faidare.elasticsearch.repository.ESFindRepository;

import java.util.Iterator;

public interface GermplasmRepository
    extends ESFindRepository<GermplasmSearchCriteria, GermplasmVO> {

    /**
     * Find germplasm by criteria with pagination.
     */
    @Override
    PaginatedList<GermplasmVO> find(GermplasmSearchCriteria criteria);

    /**
     * Get germplasm by id.
     */
    GermplasmVO getById(String germplasmDbId);

    /**
     * Scroll through all germplasm matching the given criteria.
     */
    Iterator<GermplasmVO> scrollAll(GermplasmSearchCriteria criteria);

    /**
     * Find pedigree for germplasm by id.
     */
    PedigreeVO findPedigree(String germplasmDbId);

    /**
     * Find progeny for germplasm by id.
     */
    ProgenyVO findProgeny(String germplasmDbId);

}
