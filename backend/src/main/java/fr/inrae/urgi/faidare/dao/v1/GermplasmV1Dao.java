package fr.inrae.urgi.faidare.dao.v1;

import fr.inrae.urgi.faidare.dao.DocumentDao;
import fr.inrae.urgi.faidare.domain.brapi.v1.GermplasmV1VO;

import java.util.Set;
import java.util.stream.Stream;

public interface GermplasmV1Dao extends DocumentDao<GermplasmV1VO>, GermplasmV1DaoCustom {


    GermplasmV1VO getByGermplasmDbId(String germplasmDbId);

    GermplasmV1VO getByGermplasmPUI(String germplasmPUI);

    Stream<GermplasmV1VO> findByGermplasmDbIdIn(Set<String> ids);
}
