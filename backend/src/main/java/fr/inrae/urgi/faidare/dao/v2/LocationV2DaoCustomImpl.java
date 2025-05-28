package fr.inrae.urgi.faidare.dao.v2;

import fr.inrae.urgi.faidare.api.brapi.v2.BrapiListResponse;
import fr.inrae.urgi.faidare.config.ElasticSearchConfig;
import fr.inrae.urgi.faidare.domain.brapi.LocationSitemapVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.GermplasmV2VO;
import fr.inrae.urgi.faidare.domain.brapi.v2.LocationV2VO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
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

import java.util.stream.Stream;
@Import({ElasticSearchConfig.class})
public class LocationV2DaoCustomImpl implements LocationV2DaoCustom{

    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Override
    public Stream<LocationSitemapVO> findAllForSitemap() {
        NativeQueryBuilder nativeQueryBuilder = NativeQuery.builder();
        NativeQuery query = nativeQueryBuilder
            .withQuery(builder -> builder.matchAll(Queries.matchAllQuery()))
            .withSourceFilter(new FetchSourceFilterBuilder().withIncludes("locationDbId").build())
            .build();
        query.setTrackTotalHits(true);
        return esTemplate.searchForStream(query, LocationSitemapVO.class, IndexCoordinates.of("faidare_location_dev-group0"))
            .stream()
            .map(SearchHit::getContent);
    }

    @Override
    public BrapiListResponse<LocationV2VO> findLocationsByCriteria(LocationV2Criteria locationCriteria) {
        Criteria esCrit = new Criteria();
        if(locationCriteria.getCountryCode() != null
            && !locationCriteria.getCountryCode().isEmpty()){
            esCrit.and(new Criteria("countryCode").in(locationCriteria.getCountryCode()));
        }
        if(locationCriteria.getCountryName() != null
            && !locationCriteria.getCountryName().isEmpty()){
            esCrit.and(new Criteria("countryName").in(locationCriteria.getCountryName()));
        }
        if(locationCriteria.getDocumentationURL() != null
            && !locationCriteria.getDocumentationURL().isEmpty()){
            esCrit.and(new Criteria("documentationUrl").in(locationCriteria.getDocumentationURL()));
        }
        if(locationCriteria.getExposure() != null
            && !locationCriteria.getExposure().isEmpty()){
            esCrit.and(new Criteria("exposure").in(locationCriteria.getExposure()));
        }
        if(locationCriteria.getInstituteAddress() != null
            && !locationCriteria.getInstituteAddress().isEmpty()){
            esCrit.and(new Criteria("instituteAddress").in(locationCriteria.getInstituteAddress()));
        }
        if(locationCriteria.getInstituteName() != null
            && !locationCriteria.getInstituteName().isEmpty()){
            esCrit.and(new Criteria("instituteName").in(locationCriteria.getInstituteName()));
        }
        if(locationCriteria.getLocationDbId() != null
            && !locationCriteria.getLocationDbId().isEmpty()){
            esCrit.and(new Criteria("locationDbId").in(locationCriteria.getLocationDbId()));
        }
        if(locationCriteria.getLocationName() != null
            && !locationCriteria.getLocationName().isEmpty()){
            esCrit.and(new Criteria("locationName").in(locationCriteria.getLocationName()));
        }
        if(locationCriteria.getLocationType() != null
            && !locationCriteria.getLocationType().isEmpty()){
            esCrit.and(new Criteria("locationType").in(locationCriteria.getLocationType()));
        }
        if(locationCriteria.getParentLocationDbId() != null
            && !locationCriteria.getParentLocationDbId().isEmpty()){
            esCrit.and(new Criteria("parentLocationDbId").in(locationCriteria.getParentLocationDbId()));
        }
        if(locationCriteria.getParentLocationDbId() != null
            && !locationCriteria.getParentLocationDbId().isEmpty()){
            esCrit.and(new Criteria("parentLocationDbId").in(locationCriteria.getParentLocationDbId()));
        }
        if(locationCriteria.getParentLocationName() != null
            && !locationCriteria.getParentLocationName().isEmpty()){
            esCrit.and(new Criteria("parentLocationName").in(locationCriteria.getParentLocationName()));
        }
        if(locationCriteria.getSiteStatus() != null
            && !locationCriteria.getSiteStatus().isEmpty()){
            esCrit.and(new Criteria("siteStatus").in(locationCriteria.getSiteStatus()));
        }
        if(locationCriteria.getSlope() != null
            && !locationCriteria.getSlope().isEmpty()){
            esCrit.and(new Criteria("slope").in(locationCriteria.getSlope()));
        }
        if(locationCriteria.getTopography() != null
            && !locationCriteria.getTopography().isEmpty()){
            esCrit.and(new Criteria("topography").in(locationCriteria.getTopography()));
        }


        CriteriaQuery criteriaQuery = new CriteriaQueryBuilder(esCrit).build();
        criteriaQuery.setTrackTotalHits(true);


        criteriaQuery.setPageable(PageRequest.of(locationCriteria.getPage() != null ? locationCriteria.getPage() : 0,
            locationCriteria.getPageSize() != null ? locationCriteria.getPageSize() : 10));
        SearchHits<LocationV2VO> searchHits = esTemplate.search(criteriaQuery, LocationV2VO.class);
        return BrapiListResponse.brapiResponseForPageOf(searchHits, criteriaQuery.getPageable());
    }

}
