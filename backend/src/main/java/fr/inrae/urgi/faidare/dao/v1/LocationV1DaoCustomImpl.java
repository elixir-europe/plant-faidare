package fr.inrae.urgi.faidare.dao.v1;

import fr.inrae.urgi.faidare.config.DocumentType;
import fr.inrae.urgi.faidare.config.ElasticSearchConfig;
import fr.inrae.urgi.faidare.config.FaidareProperties;
import fr.inrae.urgi.faidare.domain.LocationVO;
import fr.inrae.urgi.faidare.domain.brapi.LocationSitemapVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.client.elc.Queries;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilterBuilder;

import java.util.stream.Stream;
@Import({ElasticSearchConfig.class})
public class LocationV1DaoCustomImpl implements LocationV1DaoCustom {

    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Autowired
    private FaidareProperties faidareProperties;

    @Override
    public Stream<LocationSitemapVO> findAllForSitemap() {
        NativeQueryBuilder nativeQueryBuilder = NativeQuery.builder();
        NativeQuery query = nativeQueryBuilder
            .withQuery(builder -> builder.matchAll(Queries.matchAllQuery()))
            .withSourceFilter(new FetchSourceFilterBuilder().withIncludes("locationDbId").build())
            .build();
        query.setTrackTotalHits(true);
        return esTemplate.searchForStream(query, LocationSitemapVO.class, IndexCoordinates.of(faidareProperties.getAliasName(DocumentType.LOCATION, 0)))
                         .stream()
                         .map(SearchHit::getContent);
    }
}
