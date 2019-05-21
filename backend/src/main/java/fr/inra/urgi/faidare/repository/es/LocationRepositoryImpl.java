package fr.inra.urgi.faidare.repository.es;

import fr.inra.urgi.faidare.domain.criteria.LocationCriteria;
import fr.inra.urgi.faidare.domain.data.LocationVO;
import fr.inra.urgi.faidare.elasticsearch.ESRequestFactory;
import fr.inra.urgi.faidare.elasticsearch.ESResponseParser;
import fr.inra.urgi.faidare.elasticsearch.repository.impl.BaseESRepository;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author gcornut
 */
@Repository
public class LocationRepositoryImpl
    extends BaseESRepository<LocationCriteria, LocationVO>
    implements LocationRepository {

    @Autowired
    public LocationRepositoryImpl(
        RestHighLevelClient client,
        ESRequestFactory requestFactory,
        ESResponseParser parser
    ) {
        super(client, requestFactory, LocationVO.class, parser);
    }

}
