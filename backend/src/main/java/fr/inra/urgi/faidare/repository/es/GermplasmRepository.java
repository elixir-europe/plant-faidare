package fr.inra.urgi.faidare.repository.es;

import fr.inra.urgi.faidare.domain.criteria.FaidareGermplasmPOSTShearchCriteria;
import fr.inra.urgi.faidare.domain.criteria.GermplasmSearchCriteria;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmMcpdVO;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmSitemapVO;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmVO;
import fr.inra.urgi.faidare.domain.data.germplasm.PedigreeVO;
import fr.inra.urgi.faidare.domain.data.germplasm.ProgenyVO;
import fr.inra.urgi.faidare.domain.datadiscovery.response.GermplasmSearchResponse;
import fr.inra.urgi.faidare.domain.response.PaginatedList;
import fr.inra.urgi.faidare.elasticsearch.repository.ESFindRepository;

import java.util.Iterator;
import java.util.Set;

public interface GermplasmRepository
    extends ESFindRepository<GermplasmSearchCriteria, GermplasmVO> {

    /**
     * Find germplasm by criteria with pagination.
     */
    @Override
    PaginatedList<GermplasmVO> find(GermplasmSearchCriteria criteria);

    /**
     * Find germplasm by criteria with pagination with a should query.
     */
    GermplasmSearchResponse germplasmFind(FaidareGermplasmPOSTShearchCriteria germSearCrit);

    /**
     * Get germplasm by id.
     */
    GermplasmVO getById(String germplasmDbId);

    /**
     * Scroll through all germplasms, using the given fetch size
     */
    Iterator<GermplasmSitemapVO> scrollAllForSitemap(int fetchSize);

    /**
     * Scroll through all germplasm matching the given criteria.
     */
    Iterator<GermplasmVO> scrollAll(GermplasmSearchCriteria criteria);

    /**
     * Scroll through all germplasm matching the given FAIDARE search criteria.
     */
    Iterator<GermplasmVO> scrollAllGermplasm(FaidareGermplasmPOSTShearchCriteria criteria);

    /**
     * Scroll through all germplasmMcpd matching the given FAIDARE search criteria.
     */
    Iterator<GermplasmMcpdVO> scrollAllGermplasmMcpd(FaidareGermplasmPOSTShearchCriteria criteria);

    /**
     * Scroll through all germplasmMcpd having one of the given IDs.
     */
    Iterator<GermplasmMcpdVO> scrollGermplasmMcpdsByIds(Set<String> ids, int fetchSize);

    /**
     * Scroll through all germplasm having one of the given IDs.
     */
    Iterator<GermplasmVO> scrollGermplasmsByIds(Set<String> ids, int fetchSize);

    /**
     * Find pedigree for germplasm by id.
     */
    PedigreeVO findPedigree(String germplasmDbId);

    /**
     * Find progeny for germplasm by id.
     */
    ProgenyVO findProgeny(String germplasmDbId);

    /**
     * Find germplasm mcpd by id.
     */
    GermplasmMcpdVO getAsMcpdById(String germplasmDbId);


}
