package fr.inra.urgi.faidare.service.es;

import fr.inra.urgi.faidare.domain.criteria.GermplasmSearchCriteria;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmVO;
import fr.inra.urgi.faidare.domain.data.germplasm.PedigreeVO;
import fr.inra.urgi.faidare.domain.data.germplasm.ProgenyVO;
import fr.inra.urgi.faidare.domain.response.PaginatedList;

import java.io.File;


public interface GermplasmService {

    GermplasmVO getById(String germplasmDbId);

    PaginatedList<GermplasmVO> find(GermplasmSearchCriteria criteria);

    File exportCSV(GermplasmSearchCriteria criteria);

    PedigreeVO getPedigree(String germplasmDbId);

    ProgenyVO getProgeny(String germplasmDbId);

}
