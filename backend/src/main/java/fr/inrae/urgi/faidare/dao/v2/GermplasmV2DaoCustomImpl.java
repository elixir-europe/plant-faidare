package fr.inrae.urgi.faidare.dao.v2;
//https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/current/searching.html
//https://www.baeldung.com/spring-data-criteria-queries
//https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#repositories.single-repository-behavior
//https://www.baeldung.com/spring-data-elasticsearch-queries

import fr.inrae.urgi.faidare.api.brapi.v2.BrapiListResponse;
import fr.inrae.urgi.faidare.domain.SynonymsVO;
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

    @Override
    public BrapiListResponse<GermplasmV2VO> findGermplasmsByCriteria(GermplasmV2Criteria germplasmCriteria) {

        Criteria esCrit = new Criteria();

        if(germplasmCriteria.getAccessionNumber() != null
                && !germplasmCriteria.getAccessionNumber().isEmpty()){
            esCrit.and(new Criteria("accessionNumber").in(germplasmCriteria.getAccessionNumber()));
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
            Criteria collCrit = new Criteria("collection.name").in(germplasmCriteria.getCollections());
            collPopCrit.or("panel.name").in(germplasmCriteria.getCollections());
            esCrit.subCriteria(popCrit.or(panelCrit).or(collCrit));
        }

        if (germplasmCriteria.getCommonCropNames() != null
            && !germplasmCriteria.getCommonCropNames().isEmpty()) {
            esCrit.and(new Criteria("commonCropName").in(germplasmCriteria.getCommonCropNames()));
        }

        if (germplasmCriteria.getExternalReferenceIDs() != null
            && !germplasmCriteria.getExternalReferenceIDs().isEmpty()) {
            esCrit.and(new Criteria("externalReferenceIDs").in(germplasmCriteria.getExternalReferenceIDs()));
        }

        if (germplasmCriteria.getExternalReferenceIds() != null
            && !germplasmCriteria.getExternalReferenceIds().isEmpty()) {
            esCrit.and(new Criteria("externalReferenceIds").in(germplasmCriteria.getExternalReferenceIds()));
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

        if(germplasmCriteria.getGermplasmDbId() != null
                && !germplasmCriteria.getGermplasmDbId().isEmpty()){
                esCrit.and(new Criteria("germplasmDbId").in(germplasmCriteria.getGermplasmDbId()));
        }

        if (germplasmCriteria.getGermplasmName() != null
            && !germplasmCriteria.getGermplasmName().isEmpty()) {
            esCrit.and(new Criteria("germplasmName").in(germplasmCriteria.getGermplasmName()));
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
            List<String> synonymValues = germplasmCriteria.getSynonyms()
                .stream()
                .map(SynonymsVO::getSynonym)
                .toList();
            esCrit.and(new Criteria("synonymsV2").subCriteria(
                new Criteria("synonymsV2.synonym").in(synonymValues)));
            //esCrit.and(new Criteria("synonyms").in(germplasmCriteria.getSynonyms()));
        }

        if (germplasmCriteria.getTrialDbIds() != null
            && !germplasmCriteria.getTrialDbIds().isEmpty()) {
            TrialCriteria tCrit = new TrialCriteria();
            tCrit.setTrialDbId(germplasmCriteria.getTrialDbIds()); // Use this instead of trialV2Dao.getByTrialDbId because the latter takes only a single String as a parameter, whereas this one takes a List<String>, allowing searches with multiple trialDbIds
            List<TrialV2VO> trials = trialV2Dao.findTrialsByCriteria(tCrit).getResult().getData();
            List<String> studyDbIds = trials.stream()
                .flatMap(trial -> trial.getStudies().stream())
                .map(StudyV2miniVO::getStudyDbId)
                .toList();
            esCrit.and(new Criteria("studyDbIds").in(studyDbIds));
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
        return elasticsearchOperations.searchForStream(query, GermplasmSitemapVO.class, IndexCoordinates.of("#{@faidarePropertiesBean.getAliasName('germplasm', 0L)}"))
            .stream()
            .map(SearchHit::getContent);
    }


}
