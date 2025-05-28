package fr.inrae.urgi.faidare.dao.v2;

import fr.inrae.urgi.faidare.api.brapi.v2.BrapiListResponse;
import fr.inrae.urgi.faidare.domain.brapi.StudySitemapVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.StudyV2VO;
import fr.inrae.urgi.faidare.domain.variable.ObservationVariableVO;

import java.util.Set;
import java.util.stream.Stream;

public interface StudyV2DaoCustom {

    BrapiListResponse<StudyV2VO> findStudiesByCriteria(StudyCriteria studyCriteria);

    Stream<StudySitemapVO> findAllForSitemap();

}
