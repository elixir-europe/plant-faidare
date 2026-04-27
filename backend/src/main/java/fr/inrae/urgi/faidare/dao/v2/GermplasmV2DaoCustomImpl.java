package fr.inrae.urgi.faidare.dao.v2;
//https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/current/searching.html
//https://www.baeldung.com/spring-data-criteria-queries
//https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#repositories.single-repository-behavior
//https://www.baeldung.com/spring-data-elasticsearch-queries

import co.elastic.clients.elasticsearch._types.FieldValue;
import fr.inrae.urgi.faidare.api.brapi.v2.BrapiListResponse;
import fr.inrae.urgi.faidare.config.DocumentType;
import fr.inrae.urgi.faidare.config.FaidareProperties;
import fr.inrae.urgi.faidare.domain.brapi.GermplasmSitemapVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.GermplasmV2VO;
import fr.inrae.urgi.faidare.domain.brapi.v2.StudyV2miniVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.TrialV2VO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.client.elc.Queries;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.CriteriaQueryBuilder;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilterBuilder;

import java.util.*;
import java.util.stream.Stream;

public class GermplasmV2DaoCustomImpl implements GermplasmV2DaoCustom {

    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    TrialV2Dao trialV2Dao;

    @Autowired
    private FaidareProperties faidareProperties;

    @Override
    public BrapiListResponse<GermplasmV2VO> findGermplasmsByCriteria(GermplasmV2Criteria germplasmCriteria) {

        Criteria esCrit = new Criteria();

        if(germplasmCriteria.getAccessionNumbers() != null
                && !germplasmCriteria.getAccessionNumbers().isEmpty()){
            esCrit.and(new Criteria("accessionNumber").in(germplasmCriteria.getAccessionNumbers()));
        }

        if (germplasmCriteria.getBinomialNames() != null
            && !germplasmCriteria.getBinomialNames().isEmpty()) {
            esCrit.and(new Criteria("genusSpecies").in(germplasmCriteria.getBinomialNames()));
        }

        if (germplasmCriteria.getCollections() != null
            && !germplasmCriteria.getCollections().isEmpty()) {
            Criteria collPopCrit = new Criteria();
            Criteria panelCrit = new Criteria("panel.name").in(germplasmCriteria.getCollections());
            Criteria popCrit = new Criteria("population.name").in(germplasmCriteria.getCollections());
            Criteria collCrit = new Criteria("collection.collectionName").in(germplasmCriteria.getCollections());
            collPopCrit.or("panel.name").in(germplasmCriteria.getCollections());
            esCrit.subCriteria(popCrit.or(panelCrit).or(collCrit));
        }

        if (germplasmCriteria.getCommonCropNames() != null
            && !germplasmCriteria.getCommonCropNames().isEmpty()) {
            esCrit.and(new Criteria("commonCropName").in(germplasmCriteria.getCommonCropNames()));
        }

        if (germplasmCriteria.getExternalReferenceIds() != null
            && !germplasmCriteria.getExternalReferenceIds().isEmpty()) {
            esCrit.and(new Criteria("externalReferenceIDs").in(germplasmCriteria.getExternalReferenceIds()));
        }

        if (germplasmCriteria.getExternalReferenceSources() != null
            && !germplasmCriteria.getExternalReferenceSources().isEmpty()) {
            esCrit.and(new Criteria("externalReferenceSources").in(germplasmCriteria.getExternalReferenceSources()));
        }

        if (germplasmCriteria.getFamilyCodes() != null
            && !germplasmCriteria.getFamilyCodes().isEmpty()) {
            esCrit.and(new Criteria("familyCodes").in(germplasmCriteria.getFamilyCodes()));
        }

        if (germplasmCriteria.getGenus() != null
            && !germplasmCriteria.getGenus().isEmpty()) {
            esCrit.and(new Criteria("genus").in(germplasmCriteria.getGenus()));
        }

        if(germplasmCriteria.getGermplasmDbIds() != null
                && !germplasmCriteria.getGermplasmDbIds().isEmpty()){
            esCrit.and(new Criteria("germplasmDbId").in(germplasmCriteria.getGermplasmDbIds()));
        }

        if (germplasmCriteria.getGermplasmNames() != null
            && !germplasmCriteria.getGermplasmNames().isEmpty()) {
            esCrit.and(new Criteria("germplasmName").in(germplasmCriteria.getGermplasmNames()));
        }

        if (germplasmCriteria.getGermplasmPUIs() != null
            && !germplasmCriteria.getGermplasmPUIs().isEmpty()) {
            esCrit.and(new Criteria("germplasmPUI").in(germplasmCriteria.getGermplasmPUIs()));
        }

        if (germplasmCriteria.getInstituteCodes() != null
            && !germplasmCriteria.getInstituteCodes().isEmpty()) {
            esCrit.and(new Criteria("instituteCode").in(germplasmCriteria.getInstituteCodes()));
        }

        if (germplasmCriteria.getParentDbIds() != null
            && !germplasmCriteria.getParentDbIds().isEmpty()) {
            esCrit.and(new Criteria("parentDbIds").in(germplasmCriteria.getParentDbIds()));
        }

        if (germplasmCriteria.getProgenyDbIds() != null
            && !germplasmCriteria.getProgenyDbIds().isEmpty()) {
            esCrit.and(new Criteria("progenyDbIds").in(germplasmCriteria.getProgenyDbIds()));
        }

        if (germplasmCriteria.getProgramDbIds() != null
            && !germplasmCriteria.getProgramDbIds().isEmpty()) {
            esCrit.and(new Criteria("programDbIds").in(germplasmCriteria.getProgramDbIds()));
        }

        if (germplasmCriteria.getProgramNames() != null
            && !germplasmCriteria.getProgramNames().isEmpty()) {
            esCrit.and(new Criteria("programNames").in(germplasmCriteria.getProgramNames()));
        }

        if (germplasmCriteria.getSpecies() != null
            && !germplasmCriteria.getSpecies().isEmpty()) {
            esCrit.and(new Criteria("species").in(germplasmCriteria.getSpecies()));
        }

        if (germplasmCriteria.getStudyDbIds() != null
            && !germplasmCriteria.getStudyDbIds().isEmpty()) {
            esCrit.and(new Criteria("studyDbIds").in(germplasmCriteria.getStudyDbIds()));
        }

        if (germplasmCriteria.getStudyNames() != null
            && !germplasmCriteria.getStudyNames().isEmpty()) {
            esCrit.and(new Criteria("studyNames").in(germplasmCriteria.getStudyNames()));
        }

        if (germplasmCriteria.getSynonyms() != null
            && !germplasmCriteria.getSynonyms().isEmpty()) {
            NativeQuery query = new NativeQueryBuilder()
                .withQuery(q -> q
                    .nested(n -> n
                        .path("synonymsV2")
                        .query(nq -> nq
                            .terms(t -> t
                                .field("synonymsV2.synonym")
                                .terms(v -> v.value(
                                    germplasmCriteria.getSynonyms()
                                        .stream()
                                        .map(FieldValue::of)
                                        .toList()
                                ))
                            )
                        )
                    )
                )
                .withPageable(PageRequest.of(
                    germplasmCriteria.getPage() != null ? germplasmCriteria.getPage() : 0,
                    germplasmCriteria.getPageSize() != null ? germplasmCriteria.getPageSize() : 10
                ))
                .build();

            //Waiting to merge with the spring bot migration to use Crtieria.nested instead of this
            SearchHits<GermplasmV2VO> searchHits = elasticsearchOperations.search(
                query,
                GermplasmV2VO.class,
                IndexCoordinates.of(faidareProperties.getAliasName("germplasm", 0L)) // remplace par ton alias réel
            );
            return BrapiListResponse.brapiResponseForPageOf(searchHits, query.getPageable());
        }

        if (germplasmCriteria.getTrialDbIds() != null
            && !germplasmCriteria.getTrialDbIds().isEmpty()) {

            TrialCriteria tCrit = new TrialCriteria();
            tCrit.setTrialDbId(germplasmCriteria.getTrialDbIds());

            List<TrialV2VO> trials = trialV2Dao.findTrialsByCriteria(tCrit).getResult().getData();

            List<String> studyDbIds = trials.stream()
                .flatMap(trial -> trial.getStudies() == null ? Stream.empty() : trial.getStudies().stream())
                .map(StudyV2miniVO::getStudyDbId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

            int batchSize = 500;
            Map<String, GermplasmV2VO> resultById = new LinkedHashMap<>();

            for (int i = 0; i < studyDbIds.size(); i += batchSize) {
                List<String> batch = studyDbIds.subList(i, Math.min(i + batchSize, studyDbIds.size()));

                Criteria batchCrit = new Criteria();
                batchCrit.subCriteria(esCrit);
                batchCrit.and(new Criteria("studyDbIds").in(batch));

                CriteriaQuery batchQuery = new CriteriaQueryBuilder(batchCrit).build();
                batchQuery.setTrackTotalHits(true);
                batchQuery.setPageable(PageRequest.of(
                    germplasmCriteria.getPage() != null ? germplasmCriteria.getPage() : 0,
                    germplasmCriteria.getPageSize() != null ? germplasmCriteria.getPageSize() : 10
                ));

                SearchHits<GermplasmV2VO> batchHits = esTemplate.search(batchQuery, GermplasmV2VO.class);

                batchHits.stream()
                    .map(SearchHit::getContent)
                    .forEach(germplasm -> resultById.putIfAbsent(germplasm.getGermplasmDbId(), germplasm));
            }

            return BrapiListResponse.brapiResponseForPageOf(new ArrayList<>(resultById.values()));
        }

        if (germplasmCriteria.getTrialNames() != null
            && !germplasmCriteria.getTrialNames().isEmpty()) {
            esCrit.and(new Criteria("trialNames").in(germplasmCriteria.getTrialNames()));
        }


        CriteriaQuery criteriaQuery = new CriteriaQueryBuilder(esCrit).build();
        criteriaQuery.setTrackTotalHits(true);


        criteriaQuery.setPageable(PageRequest.of(germplasmCriteria.getPage() != null ? germplasmCriteria.getPage() : 0,
            germplasmCriteria.getPageSize() != null ? germplasmCriteria.getPageSize() : 10));
        SearchHits<GermplasmV2VO> searchHits = esTemplate.search(criteriaQuery, GermplasmV2VO.class);
        return BrapiListResponse.brapiResponseForPageOf(searchHits, criteriaQuery.getPageable());

    }
    //TODO: verfiy the matchAllQuery + withIncludes
    @Override
    public Stream<GermplasmSitemapVO> findAllForSitemap() {
        NativeQueryBuilder nativeQueryBuilder = NativeQuery.builder();
        NativeQuery query = nativeQueryBuilder
            .withQuery(builder -> builder.matchAll(Queries.matchAllQuery()))
            .withSourceFilter(new FetchSourceFilterBuilder().withIncludes("germplasmDbId").build())
            .build();
        query.setTrackTotalHits(true);
        String germplasmAliasName = faidareProperties.getAliasName(DocumentType.GERMPLASM, 0L);
        return elasticsearchOperations.searchForStream(query, GermplasmSitemapVO.class, IndexCoordinates.of(germplasmAliasName))
            .stream()
            .map(SearchHit::getContent);
    }


}
