package fr.inrae.urgi.faidare.dao.v1;

import fr.inrae.urgi.faidare.domain.brapi.LocationSitemapVO;

import java.util.stream.Stream;

/**
 * Custom methods of {@link LocationV1Dao}
 * @author JB Nizet
 */
public interface LocationV1DaoCustom {
    Stream<LocationSitemapVO> findAllForSitemap();
}
