package fr.inrae.urgi.faidare.dao.v2;

import fr.inrae.urgi.faidare.api.brapi.v2.BrapiListResponse;
import fr.inrae.urgi.faidare.domain.brapi.LocationSitemapVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.LocationV2VO;

import java.util.stream.Stream;

public interface LocationV2DaoCustom {
    Stream<LocationSitemapVO> findAllForSitemap();

    BrapiListResponse<LocationV2VO> findLocationsByCriteria(LocationV2Criteria lCrit);
}
