package fr.inrae.urgi.faidare.dao.v2;

import fr.inrae.urgi.faidare.dao.DocumentDao;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.ObservationVO;

public interface ObservationV2Dao extends DocumentDao<ObservationVO>, ObservationV2DaoCustom {
    ObservationVO getByObservationDbId(String observationDbId);
}
