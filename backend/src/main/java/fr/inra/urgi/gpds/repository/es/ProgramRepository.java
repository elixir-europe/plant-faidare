package fr.inra.urgi.gpds.repository.es;

import fr.inra.urgi.gpds.domain.criteria.ProgramCriteria;
import fr.inra.urgi.gpds.domain.data.impl.ProgramVO;
import fr.inra.urgi.gpds.domain.response.PaginatedList;
import fr.inra.urgi.gpds.elasticsearch.repository.ESFindRepository;
import fr.inra.urgi.gpds.elasticsearch.repository.ESGetByIdRepository;

/**
 * Breeding API programs
 *
 * @author gcornut
 *
 *
 */
public interface ProgramRepository
		extends ESFindRepository<ProgramCriteria, ProgramVO>,
    ESGetByIdRepository<ProgramVO> {

	@Override
	ProgramVO getById(String id);

	@Override
	PaginatedList<ProgramVO> find(ProgramCriteria criteria);

}
