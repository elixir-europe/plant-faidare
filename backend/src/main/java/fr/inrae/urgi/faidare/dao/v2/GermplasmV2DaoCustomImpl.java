package fr.inrae.urgi.faidare.dao.v2;
//https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/current/searching.html
//https://www.baeldung.com/spring-data-criteria-queries
//https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#repositories.single-repository-behavior
//https://www.baeldung.com/spring-data-elasticsearch-queries

import co.elastic.clients.elasticsearch._types.FieldValue;
import fr.inrae.urgi.faidare.api.brapi.v2.BrapiListResponse;
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

import java.util.List;
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

        if(germplasmCriteria.getAccessionNumber() != null
                && !germplasmCriteria.getAccessionNumber().isEmpty()){
            esCrit.and(new Criteria("accessionNumber").in(germplasmCriteria.getAccessionNumber()));
        }

        if (germplasmCriteria.getBinomialName() != null
            && !germplasmCriteria.getBinomialName().isEmpty()) {
            esCrit.and(new Criteria("genusSpecies").in(germplasmCriteria.getBinomialName()));
        }

        if (germplasmCriteria.getCollection() != null
            && !germplasmCriteria.getCollection().isEmpty()) {
            Criteria collPopCrit = new Criteria();
            Criteria panelCrit = new Criteria("panel.name").in(germplasmCriteria.getCollection());
            Criteria popCrit = new Criteria("population.name").in(germplasmCriteria.getCollection());
            Criteria collCrit = new Criteria("collection.name").in(germplasmCriteria.getCollection());
            collPopCrit.or("panel.name").in(germplasmCriteria.getCollection());
            esCrit.subCriteria(popCrit.or(panelCrit).or(collCrit));
        }

        if (germplasmCriteria.getCommonCropName() != null
            && !germplasmCriteria.getCommonCropName().isEmpty()) {
            esCrit.and(new Criteria("commonCropName").in(germplasmCriteria.getCommonCropName()));
        }

        if (germplasmCriteria.getExternalReferenceId() != null
            && !germplasmCriteria.getExternalReferenceId().isEmpty()) {
            esCrit.and(new Criteria("externalReferenceIDs").in(germplasmCriteria.getExternalReferenceId()));
        }

        if (germplasmCriteria.getExternalReferenceSource() != null
            && !germplasmCriteria.getExternalReferenceSource().isEmpty()) {
            esCrit.and(new Criteria("externalReferenceSources").in(germplasmCriteria.getExternalReferenceSource()));
        }

        if (germplasmCriteria.getFamilyCode() != null
            && !germplasmCriteria.getFamilyCode().isEmpty()) {
            esCrit.and(new Criteria("familyCodes").in(germplasmCriteria.getFamilyCode()));
        }

        if (germplasmCriteria.getGenus() != null
            && !germplasmCriteria.getGenus().isEmpty()) {
            esCrit.and(new Criteria("genus").in(germplasmCriteria.getGenus()));
        }

        if(germplasmCriteria.getGermplasmDbId() != null
                && !germplasmCriteria.getGermplasmDbId().isEmpty()){
            esCrit.and(new Criteria("germplasmDbId").in(germplasmCriteria.getGermplasmDbId()));
        }

        if (germplasmCriteria.getGermplasmName() != null
            && !germplasmCriteria.getGermplasmName().isEmpty()) {
            esCrit.and(new Criteria("germplasmName").in(germplasmCriteria.getGermplasmName()));
        }

        if (germplasmCriteria.getGermplasmPUI() != null
            && !germplasmCriteria.getGermplasmPUI().isEmpty()) {
            esCrit.and(new Criteria("germplasmPUI").in(germplasmCriteria.getGermplasmPUI()));
        }

        if (germplasmCriteria.getInstituteCode() != null
            && !germplasmCriteria.getInstituteCode().isEmpty()) {
            esCrit.and(new Criteria("instituteCode").in(germplasmCriteria.getInstituteCode()));
        }

        if (germplasmCriteria.getParentDbId() != null
            && !germplasmCriteria.getParentDbId().isEmpty()) {
            esCrit.and(new Criteria("parentDbIds").in(germplasmCriteria.getParentDbId()));
        }

        if (germplasmCriteria.getProgenyDbId() != null
            && !germplasmCriteria.getProgenyDbId().isEmpty()) {
            esCrit.and(new Criteria("progenyDbIds").in(germplasmCriteria.getProgenyDbId()));
        }

        if (germplasmCriteria.getProgramDbId() != null
            && !germplasmCriteria.getProgramDbId().isEmpty()) {
            esCrit.and(new Criteria("programDbIds").in(germplasmCriteria.getProgramDbId()));
        }

        if (germplasmCriteria.getProgramName() != null
            && !germplasmCriteria.getProgramName().isEmpty()) {
            esCrit.and(new Criteria("programNames").in(germplasmCriteria.getProgramName()));
        }

        if (germplasmCriteria.getSpecies() != null
            && !germplasmCriteria.getSpecies().isEmpty()) {
            esCrit.and(new Criteria("species").in(germplasmCriteria.getSpecies()));
        }

        if (germplasmCriteria.getStudyDbId() != null
            && !germplasmCriteria.getStudyDbId().isEmpty()) {
            esCrit.and(new Criteria("studyDbIds").in(germplasmCriteria.getStudyDbId()));
        }

        if (germplasmCriteria.getStudyName() != null
            && !germplasmCriteria.getStudyName().isEmpty()) {
            esCrit.and(new Criteria("studyNames").in(germplasmCriteria.getStudyName()));
        }

        if (germplasmCriteria.getSynonym() != null
            && !germplasmCriteria.getSynonym().isEmpty()) {
            NativeQuery query = new NativeQueryBuilder()
                .withQuery(q -> q
                    .nested(n -> n
                        .path("synonymsV2")
                        .query(nq -> nq
                            .terms(t -> t
                                .field("synonymsV2.synonym")
                                .terms(v -> v.value(
                                    germplasmCriteria.getSynonym()
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

        if (germplasmCriteria.getTrialDbId() != null
            && !germplasmCriteria.getTrialDbId().isEmpty()) {
            TrialCriteria tCrit = new TrialCriteria();
            tCrit.setTrialDbId(germplasmCriteria.getTrialDbId()); // Use this instead of trialV2Dao.getByTrialDbId because the latter takes only a single String as a parameter, whereas this one takes a List<String>, allowing searches with multiple trialDbIds
            List<TrialV2VO> trials = trialV2Dao.findTrialsByCriteria(tCrit).getResult().getData();
            List<String> studyDbIds = trials.stream()
                .flatMap(trial -> trial.getStudies().stream())
                .map(StudyV2miniVO::getStudyDbId)
                .toList();
            esCrit.and(new Criteria("studyDbIds").in(studyDbIds));
        }

        if (germplasmCriteria.getTrialName() != null
            && !germplasmCriteria.getTrialName().isEmpty()) {
            esCrit.and(new Criteria("trialNames").in(germplasmCriteria.getTrialName()));
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
        return elasticsearchOperations.searchForStream(query, GermplasmSitemapVO.class, IndexCoordinates.of("#{@faidarePropertiesBean.getAliasName('germplasm', 0L)}"))
            .stream()
            .map(SearchHit::getContent);
    }


}
