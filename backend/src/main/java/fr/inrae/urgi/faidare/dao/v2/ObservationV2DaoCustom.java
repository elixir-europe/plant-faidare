package fr.inrae.urgi.faidare.dao.v2;

import fr.inrae.urgi.faidare.api.brapi.v2.BrapiListResponse;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.ObservationVO;

public interface ObservationV2DaoCustom {
    BrapiListResponse<ObservationVO> findObservationByCriteria(ObservationV2Criteria observationCriteria);
}
