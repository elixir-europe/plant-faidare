package fr.inrae.urgi.faidare.dao.v2;

import java.util.List;
import java.util.stream.Stream;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import fr.inrae.urgi.faidare.api.brapi.v2.BrapiListResponse;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.ObservationUnitV2VO;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.ObservationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregation;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.CriteriaQueryBuilder;


public class ObservationV2DaoCustomImpl implements ObservationV2DaoCustom{
    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Override
    public BrapiListResponse<ObservationVO> findObservationByCriteria(ObservationV2Criteria observationCriteria) {
        Criteria esCrit = new Criteria();
        if(observationCriteria.getObservationDbId() != null
            && !observationCriteria.getObservationDbId().isEmpty()){
            esCrit.and(new Criteria("observationDbId").in(observationCriteria.getObservationDbId()));
        }
        if(observationCriteria.getCollector() != null
            && !observationCriteria.getCollector().isEmpty()){
            esCrit.and(new Criteria("collector").in(observationCriteria.getCollector()));
        }
        if(observationCriteria.getGermplasmDbId() != null
            && !observationCriteria.getGermplasmDbId().isEmpty()){
            esCrit.and(new Criteria("germplasmDbId").in(observationCriteria.getGermplasmDbId()));
        }
        if(observationCriteria.getGermplasmName() != null
            && !observationCriteria.getGermplasmName().isEmpty()){
            esCrit.and(new Criteria("germplasmName").in(observationCriteria.getGermplasmName()));
        }
        if(observationCriteria.getObservationTimeStampRangeStart() != null
            && !observationCriteria.getObservationTimeStampRangeStart().isEmpty()){
            esCrit.and(new Criteria("observationTimeStamp").in(observationCriteria.getObservationTimeStampRangeStart()));
        }
        if(observationCriteria.getGdd() != null
            && !observationCriteria.getGdd().isEmpty()){
            esCrit.and(new Criteria("gdd").in(observationCriteria.getGdd()));
        }
        if(observationCriteria.getObservationUnitDbId() != null
            && !observationCriteria.getObservationUnitDbId().isEmpty()){
            esCrit.and(new Criteria("observationUnitDbId").in(observationCriteria.getObservationUnitDbId()));
        }
        if(observationCriteria.getObservationUnitName() != null
            && !observationCriteria.getObservationUnitName().isEmpty()){
            esCrit.and(new Criteria("observationUnitName").in(observationCriteria.getObservationUnitName()));
        }
        if(observationCriteria.getObservationVariableDbId() != null
            && !observationCriteria.getObservationVariableDbId().isEmpty()){
            esCrit.and(new Criteria("observationVariableDbId").in(observationCriteria.getObservationVariableDbId()));
        }
        if(observationCriteria.getObservationVariableName() != null
            && !observationCriteria.getObservationVariableName().isEmpty()){
            esCrit.and(new Criteria("observationVariableName").in(observationCriteria.getObservationVariableName()));
        }
        if(observationCriteria.getSeasonDbId() != null
            && !observationCriteria.getSeasonDbId().isEmpty()){
            esCrit.and(new Criteria("season").in(observationCriteria.getSeasonDbId()));
        }
        if(observationCriteria.getLocationDbId() != null
            && !observationCriteria.getLocationDbId().isEmpty()){
            esCrit.and(new Criteria("studyLocationDbId").in(observationCriteria.getLocationDbId()));
        }
        if(observationCriteria.getStudyDbId() != null
            && !observationCriteria.getStudyDbId().isEmpty()){
            esCrit.and(new Criteria("studyDbId").in(observationCriteria.getStudyDbId()));
        }
        if(observationCriteria.getStudyName() != null
            && !observationCriteria.getStudyName().isEmpty()){
            esCrit.and(new Criteria("studyName").in(observationCriteria.getStudyName()));
        }
        if(observationCriteria.getLocationName() != null
            && !observationCriteria.getLocationName().isEmpty()){
            esCrit.and(new Criteria("studyLocation").in(observationCriteria.getLocationName()));
        }
        if(observationCriteria.getTrialDbId() != null
            && !observationCriteria.getTrialDbId().isEmpty()){
            esCrit.and(new Criteria("trialDbId").in(observationCriteria.getTrialDbId()));
        }
        if(observationCriteria.getTrialName() != null
            && !observationCriteria.getTrialName().isEmpty()){
            esCrit.and(new Criteria("trialName").in(observationCriteria.getTrialName()));
        }
        if(observationCriteria.getValue() != null
            && !observationCriteria.getValue().isEmpty()){
            esCrit.and(new Criteria("value").in(observationCriteria.getValue()));
        }

        CriteriaQuery criteriaQuery = new CriteriaQueryBuilder(esCrit).build();
        criteriaQuery.setTrackTotalHits(true);


        criteriaQuery.setPageable(PageRequest.of(observationCriteria.getPage() != null ? observationCriteria.getPage() : 0,
            observationCriteria.getPageSize() != null ? observationCriteria.getPageSize() : 10));
        SearchHits<ObservationVO> searchHits = esTemplate.search(criteriaQuery, ObservationVO.class);
        return BrapiListResponse.brapiResponseForPageOf(searchHits, criteriaQuery.getPageable());
    }

    @Override
    public Stream<ObservationVO> findByExportCriteria(ObservationExportCriteria exportCriteria) {
        Criteria criteria = Criteria
            .where("trialDbId").is(exportCriteria.trialDbId());
        if (!exportCriteria.studyLocations().isEmpty()) {
            criteria.and(Criteria.where("studyLocation").in(exportCriteria.studyLocations()));
        }
        if (!exportCriteria.seasonNames().isEmpty()) {
            criteria.and(Criteria.where("season.seasonName").in(exportCriteria.seasonNames()));
        }
        if (!exportCriteria.observationVariableNames().isEmpty()) {
            criteria.and(Criteria.where("observationVariableName").in(exportCriteria.observationVariableNames()));
        }

        return esTemplate
            .searchForStream(new CriteriaQuery(criteria), ObservationVO.class)
            .stream()
            .map(SearchHit::getContent);
    }

    @Override
    public ChoosableObservationExportCriteria findChoosableObservationExportCriteriaByTrialDbId(String trialDbId) {
        NativeQueryBuilder nativeQueryBuilder = NativeQuery.builder();
        NativeQuery query = nativeQueryBuilder
            .withQuery(new CriteriaQuery(Criteria.where("trialDbId").is(trialDbId)))
            .withAggregation("seasonNames", fieldAggregation("season.seasonName"))
            .withAggregation("studyLocations", fieldAggregation("studyLocation"))
            .withAggregation("observationVariableNames", fieldAggregation("observationVariableName"))
            .withMaxResults(0)
            .build();
        SearchHits<?> searchHits = esTemplate.search(query, ObservationVO.class);

        ElasticsearchAggregations aggregations =
            (ElasticsearchAggregations) searchHits.getAggregations();
        return new ChoosableObservationExportCriteria(
            choosableValues(aggregations.get("seasonNames")),
            choosableValues(aggregations.get("studyLocations")),
            choosableValues(aggregations.get("observationVariableNames"))
        );
    }

    private Aggregation fieldAggregation(String field) {
        return Aggregation.of(
            aggregationBuilder ->
                aggregationBuilder.terms(
                    termsBuilder ->
                        termsBuilder
                            .field(field)
                            .size(1000)
                )
        );
    }

    private List<String> choosableValues(ElasticsearchAggregation aggregation) {
        return aggregation
            .aggregation()
            .getAggregate()
            .sterms()
            .buckets()
            .array()
            .stream()
            .map(bucket -> bucket.key().stringValue())
            .sorted()
            .toList();
    }
}
