package fr.inrae.urgi.faidare.dao.v1;

import fr.inrae.urgi.faidare.dao.DocumentDao;
import fr.inrae.urgi.faidare.domain.brapi.v1.GermplasmAttributeV1VO;
import fr.inrae.urgi.faidare.domain.brapi.v1.GermplasmAttributeValueV1VO;

import java.util.List;

public interface GermplasmAttributeV1Dao extends DocumentDao<GermplasmAttributeV1VO> {
    GermplasmAttributeV1VO getByGermplasmDbId(String germplasmDbId);
}
