package fr.inrae.urgi.faidare.dao.v2;

import fr.inrae.urgi.faidare.api.brapi.v2.BrapiListResponse;
import fr.inrae.urgi.faidare.domain.CollPopVO;

public interface CollectionV2DaoCustom {
    BrapiListResponse<CollPopVO> getAllCollections();
}
