package fr.inra.urgi.faidare.repository.es;

import fr.inra.urgi.faidare.domain.criteria.ObservationUnitCriteria;
import fr.inra.urgi.faidare.domain.data.phenotype.ObservationUnitVO;
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
public class ObservationUnitRepositoryImpl
    extends BaseESRepository<ObservationUnitCriteria, ObservationUnitVO>
    implements ObservationUnitRepository {

    @Autowired
    public ObservationUnitRepositoryImpl(
        RestHighLevelClient client,
        ESRequestFactory requestFactory,
        ESResponseParser parser
    ) {
        super(client, requestFactory, ObservationUnitVO.class, parser);
    }

}
