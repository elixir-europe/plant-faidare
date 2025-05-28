package fr.inrae.urgi.faidare.dao.v1;
import fr.inrae.urgi.faidare.dao.DocumentDao;
import fr.inrae.urgi.faidare.domain.brapi.v1.StudyV1VO;

public interface StudyV1Dao extends DocumentDao<StudyV1VO>, StudyV1DaoCustom {

    StudyV1VO getByStudyDbId(String studyDbId);

}
