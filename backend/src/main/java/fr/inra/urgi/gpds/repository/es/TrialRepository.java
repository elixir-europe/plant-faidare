package fr.inra.urgi.gpds.repository.es;

import fr.inra.urgi.gpds.domain.criteria.TrialCriteria;
import fr.inra.urgi.gpds.domain.data.impl.TrialVO;
import fr.inra.urgi.gpds.domain.response.PaginatedList;
import fr.inra.urgi.gpds.elasticsearch.repository.ESFindRepository;
import fr.inra.urgi.gpds.elasticsearch.repository.ESGetByIdRepository;

/**
 * Breeding API trial
 *
 * @author gcornut
 *
 *
 */
public interface TrialRepository
		extends ESFindRepository<TrialCriteria, TrialVO>,
    ESGetByIdRepository<TrialVO> {

	@Override
	TrialVO getById(String id);

	@Override
	PaginatedList<TrialVO> find(TrialCriteria criteria);

}
