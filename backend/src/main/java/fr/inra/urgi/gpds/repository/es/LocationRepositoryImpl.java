package fr.inra.urgi.gpds.repository.es;

import fr.inra.urgi.gpds.domain.criteria.LocationCriteria;
import fr.inra.urgi.gpds.domain.data.impl.LocationVO;
import fr.inra.urgi.gpds.elasticsearch.ESRequestFactory;
import fr.inra.urgi.gpds.elasticsearch.ESResponseParser;
import fr.inra.urgi.gpds.elasticsearch.repository.impl.BaseESRepository;
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
