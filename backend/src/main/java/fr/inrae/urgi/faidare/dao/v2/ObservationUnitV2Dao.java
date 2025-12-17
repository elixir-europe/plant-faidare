package fr.inrae.urgi.faidare.dao.v2;

import fr.inrae.urgi.faidare.dao.DocumentDao;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.ObservationUnitV2VO;

public interface ObservationUnitV2Dao extends DocumentDao<ObservationUnitV2VO>, ObservationUnitV2DaoCustom {
    ObservationUnitV2VO getByObservationUnitDbId(String observationUnitDbId);
}
