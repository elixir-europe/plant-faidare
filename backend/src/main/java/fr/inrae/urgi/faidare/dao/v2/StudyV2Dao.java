package fr.inrae.urgi.faidare.dao.v2;
import fr.inrae.urgi.faidare.dao.DocumentDao;
import fr.inrae.urgi.faidare.domain.brapi.v2.StudyV2VO;

public interface StudyV2Dao extends DocumentDao<StudyV2VO>, StudyV2DaoCustom {

    StudyV2VO getByStudyDbId(String studyDbId);

}
