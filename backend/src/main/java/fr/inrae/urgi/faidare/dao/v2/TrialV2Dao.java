package fr.inrae.urgi.faidare.dao.v2;

import fr.inrae.urgi.faidare.dao.DocumentDao;
import fr.inrae.urgi.faidare.domain.brapi.v2.TrialV2VO;

public interface TrialV2Dao extends DocumentDao<TrialV2VO>, TrialV2DaoCustom {
    TrialV2VO getByTrialDbId(String trialDbId);
}
