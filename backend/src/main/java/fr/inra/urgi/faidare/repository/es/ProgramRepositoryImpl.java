package fr.inra.urgi.faidare.repository.es;

import fr.inra.urgi.faidare.domain.criteria.ProgramCriteria;
import fr.inra.urgi.faidare.domain.data.ProgramVO;
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
public class ProgramRepositoryImpl extends BaseESRepository<ProgramCriteria, ProgramVO>
    implements ProgramRepository {

    @Autowired
    public ProgramRepositoryImpl(
        RestHighLevelClient client,
        ESRequestFactory requestFactory,
        ESResponseParser parser
    ) {
        super(client, requestFactory, ProgramVO.class, parser);
    }

}
