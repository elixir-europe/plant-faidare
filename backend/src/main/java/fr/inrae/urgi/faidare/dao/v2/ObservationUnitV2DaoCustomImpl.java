package fr.inrae.urgi.faidare.dao.v2;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import fr.inrae.urgi.faidare.api.brapi.v2.BrapiListResponse;
import fr.inrae.urgi.faidare.api.brapi.v2.BrapiSingleResponse;
import fr.inrae.urgi.faidare.domain.brapi.GermplasmSitemapVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.ObservationLevelVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.ObservationUnitV2VO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregation;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.client.elc.Queries;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchScrollHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.CriteriaQueryBuilder;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilterBuilder;
import org.springframework.data.elasticsearch.core.query.SqlQuery;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class ObservationUnitV2DaoCustomImpl implements ObservationUnitV2DaoCustom{

    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Override
    public BrapiListResponse<ObservationUnitV2VO> findObservationUnitByCriteria(ObservationUnitV2Criteria observationUnitCriteria){

        Criteria esCrit = new Criteria();
        if(observationUnitCriteria.getObservationUnitDbId() != null
            && !observationUnitCriteria.getObservationUnitDbId().isEmpty()){
            esCrit.and(new Criteria("observationUnitDbId").in(observationUnitCriteria.getObservationUnitDbId().get(0)));
        }
        if(observationUnitCriteria.getObservationUnitName() != null
            && !observationUnitCriteria.getObservationUnitName().isEmpty()){
            esCrit.and(new Criteria("observationUnitName").in(observationUnitCriteria.getObservationUnitName()));
        }
        if(observationUnitCriteria.getGermplasmDbId() != null
            && !observationUnitCriteria.getGermplasmDbId().isEmpty()){
            esCrit.and(new Criteria("germplasmDbId").in(observationUnitCriteria.getGermplasmDbId()));
        }
        if(observationUnitCriteria.getGermplasmName() != null
            && !observationUnitCriteria.getGermplasmName().isEmpty()) {
            esCrit.and(new Criteria("germplasmName").in(observationUnitCriteria.getGermplasmName()));
        }
//        if(observationUnitCriteria.getObservationUnitPosition() != null
//            && !observationUnitCriteria.getObservationUnitPositions().isEmpty()){
//            esCrit.and(new Criteria("observationUnitPosition").in(observationUnitCriteria.getObservationUnitPositions()));
//        }
        if(observationUnitCriteria.getBlockNumber() != null
            && !observationUnitCriteria.getBlockNumber().isEmpty()){
            esCrit.and(new Criteria("blockNumber").in(observationUnitCriteria.getBlockNumber()));
        }
        if(observationUnitCriteria.getAccessionNumber() != null
            && !observationUnitCriteria.getAccessionNumber().isEmpty()){
            esCrit.and(new Criteria("accessionNumber").in(observationUnitCriteria.getAccessionNumber()));
        }
        if(observationUnitCriteria.getPlantNumber() != null
            && !observationUnitCriteria.getPlantNumber().isEmpty()){
            esCrit.and(new Criteria("plantNumber").in(observationUnitCriteria.getPlantNumber()));
        }
        if(observationUnitCriteria.getPlotNumber() != null
            && !observationUnitCriteria.getPlotNumber().isEmpty()){
            esCrit.and(new Criteria("plotNumber").in(observationUnitCriteria.getPlotNumber()));
        }
        if(observationUnitCriteria.getReplicate() != null
            && !observationUnitCriteria.getReplicate().isEmpty()){
            esCrit.and(new Criteria("replicate").in(observationUnitCriteria.getReplicate()));
        }
        if(observationUnitCriteria.getLocationDbId() != null
            && !observationUnitCriteria.getLocationDbId().isEmpty()){
            esCrit.and(new Criteria("studyLocationDbId").in(observationUnitCriteria.getLocationDbId()));
        }
        if(observationUnitCriteria.getLocationName() != null
            && !observationUnitCriteria.getLocationName().isEmpty()){
            esCrit.and(new Criteria("studyLocation").in(observationUnitCriteria.getLocationName()));
        }
        if(observationUnitCriteria.getProgramDbId() != null
            && !observationUnitCriteria.getProgramDbId().isEmpty()){
            esCrit.and(new Criteria("programDbId").in(observationUnitCriteria.getProgramDbId()));
        }
        if(observationUnitCriteria.getProgramName() != null
            && !observationUnitCriteria.getProgramName().isEmpty()){
            esCrit.and(new Criteria("programName").in(observationUnitCriteria.getProgramName()));
        }
        if(observationUnitCriteria.getSeedLotDbId() != null
            && !observationUnitCriteria.getSeedLotDbId().isEmpty()){
            esCrit.and(new Criteria("seedLotDbId").in(observationUnitCriteria.getSeedLotDbId()));
        }
        if(observationUnitCriteria.getSeedLotName() != null
            && !observationUnitCriteria.getSeedLotName().isEmpty()){
            esCrit.and(new Criteria("seedLotName").in(observationUnitCriteria.getSeedLotName()));
        }
        if(observationUnitCriteria.getStudyDbId() != null
            && !observationUnitCriteria.getStudyDbId().isEmpty()){
            esCrit.and(new Criteria("studyDbId").in(observationUnitCriteria.getStudyDbId()));
        }
        if(observationUnitCriteria.getStudyName() != null
            && !observationUnitCriteria.getStudyName().isEmpty()){
            esCrit.and(new Criteria("studyName").in(observationUnitCriteria.getStudyName()));
        }
        if(observationUnitCriteria.getTreatments() != null
            && !observationUnitCriteria.getTreatments().isEmpty()){
            esCrit.and(new Criteria("treatment").in(observationUnitCriteria.getTreatments()));
        }
        if(observationUnitCriteria.getTaxonScientificName() != null
            && !observationUnitCriteria.getTaxonScientificName().isEmpty()){
            esCrit.and(new Criteria("taxonScientificName").in(observationUnitCriteria.getTaxonScientificName()));
        }
        if(observationUnitCriteria.getGermplasmGenus() != null
            && !observationUnitCriteria.getGermplasmGenus().isEmpty()){
            esCrit.and(new Criteria("germplasmGenus").in(observationUnitCriteria.getGermplasmGenus()));
        }
        if(observationUnitCriteria.getGermplasmCollection() != null
            && !observationUnitCriteria.getGermplasmCollection().isEmpty()){
            esCrit.and(new Criteria("germplasmCollection").in(observationUnitCriteria.getGermplasmCollection()));
        }
        if(observationUnitCriteria.getGermplasmPanel() != null
            && !observationUnitCriteria.getGermplasmPanel().isEmpty()){
            esCrit.and(new Criteria("germplasmPanel").in(observationUnitCriteria.getGermplasmPanel()));
        }
        if(observationUnitCriteria.getTrialDbId() != null
            && !observationUnitCriteria.getTrialDbId().isEmpty()){
            esCrit.and(new Criteria("trialDbId").in(observationUnitCriteria.getTrialDbId()));
        }
        if(observationUnitCriteria.getTrialName() != null
            && !observationUnitCriteria.getTrialName().isEmpty()){
            esCrit.and(new Criteria("trialName").in(observationUnitCriteria.getTrialName()));
        }
        CriteriaQuery criteriaQuery = new CriteriaQueryBuilder(esCrit).build();
        criteriaQuery.setTrackTotalHits(true);


        criteriaQuery.setPageable(PageRequest.of(observationUnitCriteria.getPage() != null ? observationUnitCriteria.getPage() : 0,
            observationUnitCriteria.getPageSize() != null ? observationUnitCriteria.getPageSize() : 10));
        SearchHits<ObservationUnitV2VO> searchHits = esTemplate.search(criteriaQuery, ObservationUnitV2VO.class);
        return BrapiListResponse.brapiResponseForPageOf(searchHits, criteriaQuery.getPageable());
    }

    @Override
    public BrapiListResponse<ObservationLevelVO> findObservationLevels(ObservationUnitV2Criteria criteria) {

        Criteria esCrit = new Criteria();

        if (criteria.getProgramDbId() != null && !criteria.getProgramDbId().isEmpty()) {
            esCrit.and(new Criteria("programDbId").in(criteria.getProgramDbId()));
        }
        if (criteria.getTrialDbId() != null && !criteria.getTrialDbId().isEmpty()) {
            esCrit.and(new Criteria("trialDbId").in(criteria.getTrialDbId()));
        }
        if (criteria.getStudyDbId() != null && !criteria.getStudyDbId().isEmpty()) {
            esCrit.and(new Criteria("studyDbId").in(criteria.getStudyDbId()));
        }

        CriteriaQuery query = new CriteriaQueryBuilder(esCrit).build();
        query.setTrackTotalHits(true);

        query.setPageable(PageRequest.of(
            criteria.getPage() != null ? criteria.getPage() : 0,
            criteria.getPageSize() != null ? criteria.getPageSize() : 20
        ));

        query.addSourceFilter(new FetchSourceFilter( true,
            new String[]{"observationUnitPosition.observationLevel"}, null
        ));

        SearchHits<Map> hits = esTemplate.search(query, Map.class);

        List<ObservationLevelVO> levels = hits.stream()
            .map(hit -> {
                Map<String, Object> source = hit.getContent();
                Map<String, Object> position = (Map<String, Object>) source.get("observationUnitPosition");

                if (position != null) {
                    Map<String, Object> level = (Map<String, Object>) position.get("observationLevel");
                    if (level != null) {
                        ObservationLevelVO vo = new ObservationLevelVO();
                        vo.setLevelOrder((String) level.get("levelCode"));
                        vo.setLevelName((String) level.get("levelName"));
                        return vo;
                    }
                }
                return null;
            })
            .filter(Objects::nonNull)
            .distinct()
            .toList();

        return BrapiListResponse.brapiResponseForPageOf(
            levels,
            query.getPageable(),
            Math.toIntExact(hits.getTotalHits())
        );
    }

    @Override
    public Stream<ObservationUnitV2VO> findByExportCriteria(ObservationUnitExportCriteria exportCriteria) {
        Criteria criteria = Criteria
            .where("trialDbId").is(exportCriteria.trialDbId())
            .and(Criteria.where("observationUnitPosition.observationLevel.levelCode").is(exportCriteria.observationLevelCode()));
        if (!exportCriteria.studyLocations().isEmpty()) {
            criteria.and(Criteria.where("studyLocation").in(exportCriteria.studyLocations()));
        }
        return esTemplate.searchForStream(new CriteriaQuery(criteria), ObservationUnitV2VO.class)
            .stream()
            .map(SearchHit::getContent);
    }

    @Override
    public List<String> findObservationLevelCodesByTrialDbId(String trialDbId) {
        NativeQueryBuilder nativeQueryBuilder = NativeQuery.builder();
        NativeQuery query = nativeQueryBuilder
            .withQuery(new CriteriaQuery(Criteria.where("trialDbId").is(trialDbId)))
            .withAggregation(
                "code",
                Aggregation.of(
                    aggregationBuilder ->
                        aggregationBuilder.terms(
                            termsBuilder ->
                                termsBuilder
                                    .field("observationUnitPosition.observationLevel.levelCode")
                                    .size(1000)
                        )
                )
            )
            .withMaxResults(0)
            .build();
        SearchHits<?> searchHits = esTemplate.search(query, ObservationUnitV2VO.class);

        ElasticsearchAggregations aggregations =
            (ElasticsearchAggregations) searchHits.getAggregations();
        ElasticsearchAggregation codeAggregation = aggregations.get("code");
        List<String> codes = codeAggregation
            .aggregation()
            .getAggregate()
            .sterms()
            .buckets()
            .array()
            .stream()
            .map(bucket -> bucket.key().stringValue())
            .sorted()
            .toList();
        return codes;
    }
}
