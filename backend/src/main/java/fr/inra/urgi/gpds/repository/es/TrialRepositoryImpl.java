package fr.inra.urgi.gpds.repository.es;

import fr.inra.urgi.gpds.domain.criteria.TrialCriteria;
import fr.inra.urgi.gpds.domain.data.impl.TrialVO;
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
