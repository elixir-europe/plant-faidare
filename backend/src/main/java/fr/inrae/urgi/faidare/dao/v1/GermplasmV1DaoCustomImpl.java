package fr.inrae.urgi.faidare.dao.v1;

import fr.inrae.urgi.faidare.api.brapi.v2.BrapiListResponse;
import fr.inrae.urgi.faidare.config.ElasticSearchConfig;
import fr.inrae.urgi.faidare.dao.v1.GermplasmV1Criteria;
import fr.inrae.urgi.faidare.domain.SynonymsVO;
import fr.inrae.urgi.faidare.domain.brapi.GermplasmSitemapVO;
import fr.inrae.urgi.faidare.domain.brapi.v1.GermplasmV1VO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
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
        query.setTrackTotalHits(true);
        return elasticsearchOperations.searchForStream(query, GermplasmSitemapVO.class, IndexCoordinates.of("faidare_germplasm_dev-group0"))
                .stream()
                .map(SearchHit::getContent);
    }

    @Override
    public BrapiListResponse<GermplasmV1VO> findGermplasmsByCriteria(GermplasmV1Criteria germplasmV2Criteria) {

        Criteria esCrit = new Criteria();
        Map<String, List<String>> fieldMappings = new HashMap<>();
        fieldMappings.put("accessionNumber", germplasmV2Criteria.getAccessionNumber());
        fieldMappings.put("genusSpecies", germplasmV2Criteria.getBinomialNames());
        fieldMappings.put("commonCropName", germplasmV2Criteria.getCommonCropNames());
        fieldMappings.put("externalReferenceIDs", germplasmV2Criteria.getExternalReferenceIDs());
        fieldMappings.put("externalReferenceIds", germplasmV2Criteria.getExternalReferenceIds());
        fieldMappings.put("externalReferenceSources", germplasmV2Criteria.getExternalReferenceSources());
        fieldMappings.put("familyCodes", germplasmV2Criteria.getFamilyCodes());
        fieldMappings.put("genus", germplasmV2Criteria.getGenus());
        fieldMappings.put("germplasmDbId", germplasmV2Criteria.getGermplasmDbIds());
        fieldMappings.put("germplasmName", germplasmV2Criteria.getGermplasmName());
        fieldMappings.put("germplasmPUI", germplasmV2Criteria.getGermplasmPUIs());
        fieldMappings.put("instituteCode", germplasmV2Criteria.getInstituteCodes());
        fieldMappings.put("parentDbIds", germplasmV2Criteria.getParentDbIds());
        fieldMappings.put("progenyDbIds", germplasmV2Criteria.getProgenyDbIds());
        fieldMappings.put("programDbIds", germplasmV2Criteria.getProgramDbIds());
        fieldMappings.put("programNames", germplasmV2Criteria.getProgramNames());
        fieldMappings.put("species", germplasmV2Criteria.getSpecies());
        fieldMappings.put("studyDbIds", germplasmV2Criteria.getStudyDbIds());
        fieldMappings.put("studyNames", germplasmV2Criteria.getStudyNames());
        fieldMappings.put("trialDbIds", germplasmV2Criteria.getTrialDbIds());
        fieldMappings.put("synonyms", germplasmV2Criteria.getSynonyms());

        fieldMappings.forEach((key, value) ->
            Optional.ofNullable(value)
                .filter(v -> !v.isEmpty())
                .ifPresent(v -> esCrit.and(new Criteria(key).in(v)))
        );

        Optional.ofNullable(germplasmV2Criteria.getCollections())
            .filter(c -> !c.isEmpty())
            .ifPresent(c -> {
                Criteria panelCrit = new Criteria("panel.name").in(c);
                Criteria popCrit = new Criteria("population.name").in(c);
                Criteria collCrit = new Criteria("collection.name").in(c);
                esCrit.subCriteria(popCrit.or(panelCrit).or(collCrit));
            });


        CriteriaQuery criteriaQuery = new CriteriaQueryBuilder(esCrit).build();
        criteriaQuery.setPageable(PageRequest.of(
            Optional.ofNullable(germplasmV2Criteria.getPage()).orElse(0),
            Optional.ofNullable(germplasmV2Criteria.getPageSize()).orElse(10)
        ));

        criteriaQuery.setTrackTotalHits(true);

        SearchHits<GermplasmV1VO> searchHits = esTemplate.search(criteriaQuery, GermplasmV1VO.class);
        return BrapiListResponse.brapiResponseForPageOf(searchHits, criteriaQuery.getPageable());

    }
}
