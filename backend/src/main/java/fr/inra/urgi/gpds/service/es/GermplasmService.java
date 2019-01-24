package fr.inra.urgi.gpds.service.es;

import fr.inra.urgi.gpds.domain.criteria.GermplasmSearchCriteria;
import fr.inra.urgi.gpds.domain.data.germplasm.GermplasmVO;
import fr.inra.urgi.gpds.domain.data.germplasm.PedigreeVO;
import fr.inra.urgi.gpds.domain.data.germplasm.ProgenyVO;
import fr.inra.urgi.gpds.domain.response.PaginatedList;

import java.io.File;


public interface GermplasmService {

    GermplasmVO getById(String germplasmDbId);

    PaginatedList<GermplasmVO> find(GermplasmSearchCriteria criteria);

    File exportCSV(GermplasmSearchCriteria criteria);

    PedigreeVO getPedigree(String germplasmDbId);

    ProgenyVO getProgeny(String germplasmDbId);

}
