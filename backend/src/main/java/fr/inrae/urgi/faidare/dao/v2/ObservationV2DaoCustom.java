package fr.inrae.urgi.faidare.dao.v2;

import java.util.stream.Stream;

import fr.inrae.urgi.faidare.api.brapi.v2.BrapiListResponse;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.ObservationUnitV2VO;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.ObservationVO;

public interface ObservationV2DaoCustom {
    BrapiListResponse<ObservationVO> findObservationByCriteria(ObservationV2Criteria observationCriteria);

    /**
     * Returns a stream of all the observations matching the given criteria.
     * This stream must be closed after consumption.
     */
    Stream<ObservationVO> findByExportCriteria(ObservationExportCriteria criteria);
}
