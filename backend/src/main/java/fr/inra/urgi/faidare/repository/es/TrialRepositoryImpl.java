package fr.inra.urgi.faidare.repository.es;

import fr.inra.urgi.faidare.domain.criteria.TrialCriteria;
import fr.inra.urgi.faidare.domain.data.TrialVO;
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
public class TrialRepositoryImpl
    extends BaseESRepository<TrialCriteria, TrialVO>
    implements TrialRepository {

    @Autowired
    public TrialRepositoryImpl(
        RestHighLevelClient client,
        ESRequestFactory requestFactory,
        ESResponseParser parser
    ) {
        super(client, requestFactory, TrialVO.class, parser);
    }

}
