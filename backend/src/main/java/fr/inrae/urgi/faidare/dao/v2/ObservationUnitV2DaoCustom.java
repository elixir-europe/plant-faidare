package fr.inrae.urgi.faidare.dao.v2;

import fr.inrae.urgi.faidare.api.brapi.v2.BrapiListResponse;
import fr.inrae.urgi.faidare.api.brapi.v2.BrapiSingleResponse;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.ObservationLevelVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.ObservationUnitV2VO;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.List;
import java.util.stream.Stream;


public interface ObservationUnitV2DaoCustom {
    BrapiListResponse<ObservationUnitV2VO> findObservationUnitByCriteria(ObservationUnitV2Criteria observationUnitCriteria);

    BrapiListResponse<ObservationLevelVO> findObservationLevels(ObservationUnitV2Criteria criteria);

    /**
     * Returns a stream of all the observation units matching the given criteria.
     * This stream must be closed after consumption.
     */
    Stream<ObservationUnitV2VO> findByExportCriteria(ObservationUnitExportCriteria criteria);
}
