package fr.inra.urgi.faidare.repository.es;

import fr.inra.urgi.faidare.domain.criteria.ProgramCriteria;
import fr.inra.urgi.faidare.domain.data.ProgramVO;
import fr.inra.urgi.faidare.domain.response.PaginatedList;
import fr.inra.urgi.faidare.elasticsearch.repository.ESFindRepository;
import fr.inra.urgi.faidare.elasticsearch.repository.ESGetByIdRepository;

/**
 * Breeding API programs
 *
 * @author gcornut
 */
public interface ProgramRepository
    extends ESFindRepository<ProgramCriteria, ProgramVO>,
    ESGetByIdRepository<ProgramVO> {

    @Override
    ProgramVO getById(String id);

    @Override
    PaginatedList<ProgramVO> find(ProgramCriteria criteria);

}
