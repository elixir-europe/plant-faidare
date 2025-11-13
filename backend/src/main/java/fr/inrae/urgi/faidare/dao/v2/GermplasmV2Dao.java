package fr.inrae.urgi.faidare.dao.v2;

import java.util.Set;
import java.util.stream.Stream;

import fr.inrae.urgi.faidare.dao.DocumentDao;
import fr.inrae.urgi.faidare.domain.brapi.GermplasmSitemapVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.GermplasmV2VO;

import java.util.Set;
import java.util.stream.Stream;

public interface GermplasmV2Dao extends DocumentDao<GermplasmV2VO>, GermplasmV2DaoCustom {


    GermplasmV2VO getByGermplasmDbId(String germplasmDbId);

    GermplasmV2VO getByGermplasmPUI(String germplasmPUI);

    Stream<GermplasmV2VO> findByGermplasmDbIdIn(Set<String> ids);
}
