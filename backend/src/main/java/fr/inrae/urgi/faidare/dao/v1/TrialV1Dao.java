package fr.inrae.urgi.faidare.dao.v1;

import fr.inrae.urgi.faidare.dao.DocumentDao;
import fr.inrae.urgi.faidare.domain.brapi.v1.TrialV1VO;

public interface TrialV1Dao extends DocumentDao<TrialV1VO> {
    TrialV1VO getByTrialDbId(String trialDbId);
}
