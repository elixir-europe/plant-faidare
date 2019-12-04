package fr.inra.urgi.faidare.service.es;

import fr.inra.urgi.faidare.domain.criteria.FaidareGermplasmPOSTShearchCriteria;
import fr.inra.urgi.faidare.domain.criteria.GermplasmSearchCriteria;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmVO;
import fr.inra.urgi.faidare.domain.data.germplasm.PedigreeVO;
import fr.inra.urgi.faidare.domain.data.germplasm.ProgenyVO;
import fr.inra.urgi.faidare.domain.datadiscovery.response.GermplasmSearchResponse;
import fr.inra.urgi.faidare.domain.response.PaginatedList;

import java.io.File;
import java.util.LinkedHashSet;


public interface GermplasmService {

    GermplasmVO getById(String germplasmDbId);

    PaginatedList<GermplasmVO> find(GermplasmSearchCriteria criteria);

    GermplasmSearchResponse esShouldFind(FaidareGermplasmPOSTShearchCriteria criteria);

    LinkedHashSet<String> suggest(
        String criteriaField, String searchText, Integer fetchSize, FaidareGermplasmPOSTShearchCriteria criteria
    );

    File exportCSV(GermplasmSearchCriteria criteria);

    File exportListGermplasmCSV(FaidareGermplasmPOSTShearchCriteria criteria);

    PedigreeVO getPedigree(String germplasmDbId);

    ProgenyVO getProgeny(String germplasmDbId);

}
