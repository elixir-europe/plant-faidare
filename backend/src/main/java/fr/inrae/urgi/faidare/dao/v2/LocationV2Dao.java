package fr.inrae.urgi.faidare.dao.v2;

import fr.inrae.urgi.faidare.dao.DocumentDao;
import fr.inrae.urgi.faidare.domain.brapi.v2.LocationV2VO;

public interface LocationV2Dao extends DocumentDao<LocationV2VO>, LocationV2DaoCustom {
    LocationV2VO getByLocationDbId(String locationDbId);
}
