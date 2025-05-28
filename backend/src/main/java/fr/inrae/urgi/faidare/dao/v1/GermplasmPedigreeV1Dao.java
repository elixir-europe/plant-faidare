package fr.inrae.urgi.faidare.dao.v1;

import fr.inrae.urgi.faidare.dao.DocumentDao;
import fr.inrae.urgi.faidare.domain.brapi.v1.GermplasmPedigreeV1VO;

public interface GermplasmPedigreeV1Dao extends DocumentDao<GermplasmPedigreeV1VO> {
    GermplasmPedigreeV1VO getByGermplasmDbId(String germplasmDbId);
}
