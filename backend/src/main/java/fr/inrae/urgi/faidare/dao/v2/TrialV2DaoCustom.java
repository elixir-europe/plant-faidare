package fr.inrae.urgi.faidare.dao.v2;

import fr.inrae.urgi.faidare.api.brapi.v2.BrapiListResponse;
import fr.inrae.urgi.faidare.domain.brapi.v2.TrialV2VO;

public interface TrialV2DaoCustom {
    BrapiListResponse<TrialV2VO> findTrialsByCriteria(TrialCriteria trialCriteria);
}
