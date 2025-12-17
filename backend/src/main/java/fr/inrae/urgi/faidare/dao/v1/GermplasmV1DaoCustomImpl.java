package fr.inrae.urgi.faidare.dao.v1;

import fr.inrae.urgi.faidare.api.brapi.v2.BrapiListResponse;
import fr.inrae.urgi.faidare.config.DocumentType;
import fr.inrae.urgi.faidare.config.ElasticSearchConfig;
import fr.inrae.urgi.faidare.config.FaidareProperties;
import fr.inrae.urgi.faidare.domain.brapi.GermplasmSitemapVO;
import fr.inrae.urgi.faidare.domain.brapi.v1.GermplasmV1VO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.client.elc.Queries;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchHitsIterator;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.CriteriaQueryBuilder;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilterBuilder;

import java.util.*;
import java.util.stream.Stream;

@Import({ElasticSearchConfig.class})
public class GermplasmV1DaoCustomImpl implements GermplasmV1DaoCustom{

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;
    @Autowired
    private ElasticsearchTemplate esTemplate;
    @Autowired
    private FaidareProperties faidareProperties;

    @Override
    public SearchHitsIterator<GermplasmV1VO> scrollGermplasmsByGermplasmDbIds(Set<String> germplasmDbIds, int fetchSize) {
        Criteria esCrit = new Criteria("germplasmDbId").in(germplasmDbIds);
        CriteriaQuery criteriaQuery = new CriteriaQueryBuilder(esCrit).build();
        criteriaQuery.setTrackTotalHits(true);
        SearchHitsIterator<GermplasmV1VO> stream = elasticsearchOperations.searchForStream(criteriaQuery, GermplasmV1VO.class);
        return stream;
    }

    @Override
    public Stream<GermplasmSitemapVO> findAllForSitemap() {
        NativeQueryBuilder nativeQueryBuilder = NativeQuery.builder();
        NativeQuery query = nativeQueryBuilder
                .withQuery(builder -> builder.matchAll(Queries.matchAllQuery()))
                .withSourceFilter(new FetchSourceFilterBuilder().withIncludes("germplasmDbId").build())
                .build();
        return elasticsearchOperations.searchForStream(query, GermplasmSitemapVO.class, IndexCoordinates.of(faidareProperties.getAliasName(DocumentType.GERMPLASM, 0)))
                .stream()
                .map(SearchHit::getContent);
    }

    @Override
    public BrapiListResponse<GermplasmV1VO> findGermplasmsByCriteria(GermplasmV1Criteria germplasmCriteria) {

        Criteria esCrit = new Criteria();
        Map<String, List<String>> fieldMappings = new HashMap<>();
        fieldMappings.put("accessionNumber", germplasmCriteria.getAccessionNumber());
        fieldMappings.put("genusSpecies", germplasmCriteria.getBinomialNames());
        fieldMappings.put("commonCropName", germplasmCriteria.getCommonCropNames());
        fieldMappings.put("externalReferenceIDs", germplasmCriteria.getExternalReferenceIDs());
        fieldMappings.put("externalReferenceIds", germplasmCriteria.getExternalReferenceIds());
        fieldMappings.put("externalReferenceSources", germplasmCriteria.getExternalReferenceSources());
        fieldMappings.put("familyCodes", germplasmCriteria.getFamilyCodes());
        fieldMappings.put("genus", germplasmCriteria.getGenus());
        fieldMappings.put("germplasmDbId", germplasmCriteria.getGermplasmDbIds());
        fieldMappings.put("germplasmName", germplasmCriteria.getGermplasmName());
        fieldMappings.put("germplasmPUI", germplasmCriteria.getGermplasmPUIs());
        fieldMappings.put("instituteCode", germplasmCriteria.getInstituteCodes());
        fieldMappings.put("parentDbIds", germplasmCriteria.getParentDbIds());
        fieldMappings.put("progenyDbIds", germplasmCriteria.getProgenyDbIds());
        fieldMappings.put("programDbIds", germplasmCriteria.getProgramDbIds());
        fieldMappings.put("programNames", germplasmCriteria.getProgramNames());
        fieldMappings.put("species", germplasmCriteria.getSpecies());
        fieldMappings.put("studyDbIds", germplasmCriteria.getStudyDbIds());
        fieldMappings.put("studyNames", germplasmCriteria.getStudyNames());
        fieldMappings.put("trialDbIds", germplasmCriteria.getTrialDbIds());
        fieldMappings.put("synonyms", germplasmCriteria.getSynonyms());

        fieldMappings.forEach((key, value) ->
            Optional.ofNullable(value)
                .filter(v -> !v.isEmpty())
                .ifPresent(v -> esCrit.and(new Criteria(key).in(v)))
        );

        Optional.ofNullable(germplasmCriteria.getCollections())
            .filter(c -> !c.isEmpty())
            .ifPresent(c -> {
                Criteria panelCrit = new Criteria("panel.name").in(c);
                Criteria popCrit = new Criteria("population.name").in(c);
                Criteria collCrit = new Criteria("collection.name").in(c);
                esCrit.subCriteria(popCrit.or(panelCrit).or(collCrit));
            });


        CriteriaQuery criteriaQuery = new CriteriaQueryBuilder(esCrit).build();
        criteriaQuery.setPageable(PageRequest.of(
            Optional.ofNullable(germplasmCriteria.getPage()).orElse(0),
            Optional.ofNullable(germplasmCriteria.getPageSize()).orElse(10)
        ));

        criteriaQuery.setTrackTotalHits(true);

        SearchHits<GermplasmV1VO> searchHits = esTemplate.search(criteriaQuery, GermplasmV1VO.class);
        return BrapiListResponse.brapiResponseForPageOf(searchHits, criteriaQuery.getPageable());

    }
    @Override
    public BrapiListResponse<GermplasmV1VO> findByFilters(
        String germplasmPUI,
        String germplasmDbId,
        String germplasmName,
        String commonCropName,
        Pageable pageable) {

        Criteria criteria = new Criteria();

        if (germplasmPUI != null && !germplasmPUI.isEmpty()) {
            criteria = criteria.and("germplasmPUI").is(germplasmPUI);
        }

        if (germplasmDbId != null && !germplasmDbId.isEmpty()) {
            criteria = criteria.and("germplasmDbId").is(germplasmDbId);
        }

        if (germplasmName != null && !germplasmName.isEmpty()) {
            criteria = criteria.and("germplasmName.keyword").is(germplasmName);
        }

        if (commonCropName != null && !commonCropName.isEmpty()) {
            criteria = criteria.and("commonCropName.keyword").is(commonCropName);
        }

        CriteriaQuery query = new CriteriaQuery(criteria);
        query.setPageable(pageable);
        query.setTrackTotalHits(true);

        SearchHits<GermplasmV1VO> hits = elasticsearchOperations.search(query, GermplasmV1VO.class);
        return BrapiListResponse.brapiResponseForPageOf(hits, query.getPageable());
    }
}
