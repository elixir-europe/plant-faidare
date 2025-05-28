package fr.inrae.urgi.faidare.dao.v1;

import fr.inrae.urgi.faidare.api.brapi.v2.BrapiListResponse;
import fr.inrae.urgi.faidare.dao.v1.GermplasmV1Criteria;
import fr.inrae.urgi.faidare.domain.brapi.GermplasmSitemapVO;
import fr.inrae.urgi.faidare.domain.brapi.v1.GermplasmV1VO;
import org.springframework.data.elasticsearch.core.SearchHitsIterator;

import java.util.Set;
import java.util.stream.Stream;

public interface GermplasmV1DaoCustom {
    SearchHitsIterator<GermplasmV1VO> scrollGermplasmsByGermplasmDbIds(Set<String> germplasmDbIds, int fetchSize);
    Stream<GermplasmSitemapVO> findAllForSitemap();

    BrapiListResponse<GermplasmV1VO> findGermplasmsByCriteria(GermplasmV1Criteria germplasmV2Criteria);
}
