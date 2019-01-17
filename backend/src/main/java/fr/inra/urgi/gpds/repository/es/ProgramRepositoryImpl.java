package fr.inra.urgi.gpds.repository.es;

import fr.inra.urgi.gpds.domain.criteria.ProgramCriteria;
import fr.inra.urgi.gpds.domain.data.impl.ProgramVO;
import fr.inra.urgi.gpds.elasticsearch.ESRequestFactory;
import fr.inra.urgi.gpds.elasticsearch.repository.impl.BaseESRepository;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author gcornut
 *
 *
 */
@Repository
public class ProgramRepositoryImpl extends BaseESRepository<ProgramCriteria, ProgramVO>
		implements ProgramRepository {

	@Autowired
	public ProgramRepositoryImpl(
        RestHighLevelClient client, ESRequestFactory requestFactory
    ) {
		super(client, requestFactory, ProgramVO.class);
	}

}
