package fr.inra.urgi.faidare.repository.es;

import java.util.Iterator;

import fr.inra.urgi.faidare.domain.criteria.LocationCriteria;
import fr.inra.urgi.faidare.domain.data.LocationSitemapVO;
import fr.inra.urgi.faidare.domain.data.LocationVO;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmSitemapVO;
import fr.inra.urgi.faidare.elasticsearch.ESRequestFactory;
import fr.inra.urgi.faidare.elasticsearch.ESResponseParser;
import fr.inra.urgi.faidare.elasticsearch.ESScrollIterator;
import fr.inra.urgi.faidare.elasticsearch.repository.impl.BaseESRepository;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author gcornut
 */
@Repository
public class LocationRepositoryImpl
    extends BaseESRepository<LocationCriteria, LocationVO>
    implements LocationRepository {

    private final RestHighLevelClient client;
    private final ESRequestFactory requestFactory;
    private final ESResponseParser parser;

    @Autowired
    public LocationRepositoryImpl(
        RestHighLevelClient client,
        ESRequestFactory requestFactory,
        ESResponseParser parser
    ) {
        super(client, requestFactory, LocationVO.class, parser);
        this.client = client;
        this.requestFactory = requestFactory;
        this.parser = parser;
    }

    @Override
    public Iterator<LocationSitemapVO> scrollAllForSitemap(int fetchSize) {
        QueryBuilder query = QueryBuilders.matchAllQuery();
        return new ESScrollIterator<>(client, requestFactory, parser, LocationSitemapVO.class, query, fetchSize);
    }
}
