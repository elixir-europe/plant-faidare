package fr.inrae.urgi.faidare.dao.v1;

import fr.inrae.urgi.faidare.dao.v2.StudyCriteria;
import fr.inrae.urgi.faidare.domain.brapi.StudySitemapVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.StudyV2VO;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.stream.Stream;


public interface StudyV1DaoCustom {

    SearchHits<StudyV2VO> findStudiesByCriteria(StudyCriteria studyCriteria);

    Stream<StudySitemapVO> findAllForSitemap();
}
