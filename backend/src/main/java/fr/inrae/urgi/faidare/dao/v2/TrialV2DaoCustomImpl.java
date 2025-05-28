package fr.inrae.urgi.faidare.dao.v2;

import fr.inrae.urgi.faidare.api.brapi.v2.BrapiListResponse;
import fr.inrae.urgi.faidare.dao.v1.LocationV1Dao;
import fr.inrae.urgi.faidare.domain.brapi.v2.TrialV2VO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.CriteriaQueryBuilder;



public class TrialV2DaoCustomImpl implements TrialV2DaoCustom{

    @Autowired
    private  StudyV2Dao studyDao;
    @Autowired
    private  LocationV1Dao locationV1Dao;
    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Override
    public BrapiListResponse<TrialV2VO> findTrialsByCriteria(TrialCriteria trialCriteria){

        Criteria esCrit = new Criteria();

        if (trialCriteria.getTrialDbId() != null
            && !trialCriteria.getTrialDbId().isEmpty()) {
            esCrit.and(new Criteria("trialDbId").in(trialCriteria.getTrialDbId()));
        }

        if (trialCriteria.getTrialName() != null
            && !trialCriteria.getTrialName().isEmpty()) {
            esCrit.and(new Criteria("trialName").in(trialCriteria.getTrialName()));
        }

        if (trialCriteria.getTrialType() != null
            && !trialCriteria.getTrialType().isEmpty()) {
            esCrit.and(new Criteria("trialType").in(trialCriteria.getTrialType()));
        }

        if (trialCriteria.getInstituteName() != null
            && !trialCriteria.getInstituteName().isEmpty()) {
            esCrit.and(new Criteria("contacts.instituteName").in(trialCriteria.getInstituteName()));
        }

        if (trialCriteria.getProgramDbId() != null
            && !trialCriteria.getProgramDbId().isEmpty()) {
            esCrit.and(new Criteria("programDbId").in(trialCriteria.getProgramDbId()));
        }

        if (trialCriteria.getProgramNames() != null
            && !trialCriteria.getProgramNames().isEmpty()) {
            esCrit.and(new Criteria("programName").in(trialCriteria.getProgramNames()));
        }

        if (trialCriteria.getStudyDbIds() != null
            && !trialCriteria.getStudyDbIds().isEmpty()) {
            esCrit.and(new Criteria("studies.studyDbId").in(trialCriteria.getStudyDbIds()));
        }

        CriteriaQuery criteriaQuery = new CriteriaQueryBuilder(esCrit).build();
        SearchHits<TrialV2VO> searchHits = esTemplate.search(criteriaQuery, TrialV2VO.class);
        return BrapiListResponse.brapiResponseForPageOf(searchHits, criteriaQuery.getPageable());



    }
}
