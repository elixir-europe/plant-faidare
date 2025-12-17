package fr.inrae.urgi.faidare.dao.v1;

import java.util.List;
import java.util.Set;

import fr.inrae.urgi.faidare.dao.DocumentDao;
import fr.inrae.urgi.faidare.domain.LocationVO;

public interface LocationV1Dao extends DocumentDao<LocationVO>, LocationV1DaoCustom {
    LocationVO getByLocationDbId(String locationDbId);
    List<LocationVO> getByLocationDbIdIn(Set<String> locationDbIds);
}
