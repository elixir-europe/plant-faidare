package fr.inrae.urgi.faidare.dao.v1;

import fr.inrae.urgi.faidare.dao.DocumentDao;
import fr.inrae.urgi.faidare.domain.LocationVO;

public interface LocationV1Dao extends DocumentDao<LocationVO>, LocationV1DaoCustom {
    LocationVO getByLocationDbId(String locationDbId);
}
