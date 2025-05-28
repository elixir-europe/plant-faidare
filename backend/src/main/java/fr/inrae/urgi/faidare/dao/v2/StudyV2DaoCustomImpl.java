package fr.inrae.urgi.faidare.dao.v2;

import fr.inrae.urgi.faidare.api.brapi.v2.BrapiListResponse;
import fr.inrae.urgi.faidare.domain.brapi.StudySitemapVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.StudyV2VO;
import fr.inrae.urgi.faidare.domain.variable.ObservationVariableVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.client.elc.Queries;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.CriteriaQueryBuilder;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilterBuilder;

import java.util.Set;
import java.util.stream.Stream;

public class StudyV2DaoCustomImpl implements StudyV2DaoCustom {

    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Override
    public BrapiListResponse<StudyV2VO> findStudiesByCriteria(StudyCriteria studyCriteria) {

        Criteria esCrit = new Criteria();

        if (studyCriteria.getCommonCropName() != null
                && !studyCriteria.getCommonCropName().isEmpty()) {
            esCrit.and(new Criteria("commonCropName").in(studyCriteria.getCommonCropName()));
        }

        if (studyCriteria.getExternalReferenceID() != null
                && !studyCriteria.getExternalReferenceID().isEmpty()) {
            esCrit.and(new Criteria("externalReferenceID").in(studyCriteria.getExternalReferenceID()));
        }

        if (studyCriteria.getExternalReferenceSource() != null
                && !studyCriteria.getExternalReferenceSource().isEmpty()) {
            esCrit.and(new Criteria("externalReferenceSources").in(studyCriteria.getExternalReferenceSource()));
        }

        if (studyCriteria.getGermplasmDbIds() != null
                && !studyCriteria.getGermplasmDbIds().isEmpty()) {
            esCrit.and(new Criteria("germplasmDbIds").in(studyCriteria.getGermplasmDbIds()));
        }

        if (studyCriteria.getGermplasmNames() != null
                && !studyCriteria.getGermplasmNames().isEmpty()) {
            esCrit.and(new Criteria("germplasmNames").in(studyCriteria.getGermplasmNames()));
        }

        if (studyCriteria.getLocationDbId() != null
                && !studyCriteria.getLocationDbId().isEmpty()) {
            esCrit.and(new Criteria("locationDbId").in(studyCriteria.getLocationDbId()));
        }

        if (studyCriteria.getLocationName() != null
                && !studyCriteria.getLocationName().isEmpty()) {
            esCrit.and(new Criteria("locationName").in(studyCriteria.getLocationName()));
        }

        if (studyCriteria.getObservationVariableDbId() != null
                && !studyCriteria.getObservationVariableDbId().isEmpty()) {
            esCrit.and(new Criteria("observationVariableDbIds").in(studyCriteria.getObservationVariableDbId()));
        }

        if (studyCriteria.getObservationVariableName() != null
                && !studyCriteria.getObservationVariableName().isEmpty()) {
            esCrit.and(new Criteria("observationVariableNames").in(studyCriteria.getObservationVariableName()));
        }

        if (studyCriteria.getObservationVariablePUI() != null
                && !studyCriteria.getObservationVariablePUI().isEmpty()) {
            esCrit.and(new Criteria("observationVariablePUIs").in(studyCriteria.getObservationVariablePUI()));
        }

        if (studyCriteria.getProgramDbId() != null
                && !studyCriteria.getProgramDbId().isEmpty()) {
            esCrit.and(new Criteria("programDbId").in(studyCriteria.getProgramDbId()));
        }

        if (studyCriteria.getProgramName() != null
                && !studyCriteria.getProgramName().isEmpty()) {
            esCrit.and(new Criteria("programName").in(studyCriteria.getProgramName()));
        }

        if (studyCriteria.getSeasonDbId() != null
                && !studyCriteria.getSeasonDbId().isEmpty()) {
            esCrit.and(new Criteria("seasonDbIds").in(studyCriteria.getSeasonDbId()));
        }

        if (studyCriteria.getSortBy() != null
                && !studyCriteria.getSortBy().isEmpty()) {
            esCrit.and(new Criteria("sortBy").in(studyCriteria.getSortBy()));
        }

        if (studyCriteria.getSortOrder() != null
                && !studyCriteria.getSortOrder().isEmpty()) {
            esCrit.and(new Criteria("sortOrder").in(studyCriteria.getSortOrder()));
        }

        if (studyCriteria.getStudyCode() != null
                && !studyCriteria.getStudyCode().isEmpty()) {
            esCrit.and(new Criteria("studyCode").in(studyCriteria.getStudyCode()));
        }

        if (studyCriteria.getStudyDbId() != null
                && !studyCriteria.getStudyDbId().isEmpty()) {
            esCrit.and(new Criteria("studyDbId").in(studyCriteria.getStudyDbId()));
        }

        if (studyCriteria.getStudyName() != null
                && !studyCriteria.getStudyName().isEmpty()) {
            esCrit.and(new Criteria("studyName").in(studyCriteria.getStudyName()));
        }

        if (studyCriteria.getStudyPUI() != null
                && !studyCriteria.getStudyPUI().isEmpty()) {
            esCrit.and(new Criteria("studyPUI").in(studyCriteria.getStudyPUI()));
        }

        if (studyCriteria.getStudyType() != null
                && !studyCriteria.getStudyType().isEmpty()) {
            esCrit.and(new Criteria("studyType").in(studyCriteria.getStudyType()));
        }

        if (studyCriteria.getTrialDbIds() != null
                && !studyCriteria.getTrialDbIds().isEmpty()) {
            esCrit.and(new Criteria("trialDbId").in(studyCriteria.getTrialDbIds()));
        }

        if (studyCriteria.getTrialNames() != null
                && !studyCriteria.getTrialNames().isEmpty()) {
            esCrit.and(new Criteria("trialName").in(studyCriteria.getTrialNames()));
        }

        CriteriaQuery criteriaQuery = new CriteriaQueryBuilder(esCrit).build();
        criteriaQuery.setTrackTotalHits(true);
        SearchHits<StudyV2VO> searchHits= esTemplate.search(criteriaQuery, StudyV2VO.class);
        return BrapiListResponse.brapiResponseForPageOf(searchHits, criteriaQuery.getPageable());
    }

    @Override
    public Stream<StudySitemapVO> findAllForSitemap() {
        NativeQueryBuilder nativeQueryBuilder = NativeQuery.builder();
        NativeQuery query = nativeQueryBuilder
            .withQuery(builder -> builder.matchAll(Queries.matchAllQuery()))
            .withSourceFilter(new FetchSourceFilterBuilder().withIncludes("studyDbId").build())
            .build();
        query.setTrackTotalHits(true);
        return esTemplate.searchForStream(query, StudySitemapVO.class, IndexCoordinates.of("faidare_study_dev-group0"))
            .stream()
            .map(SearchHit::getContent);
    }

}
