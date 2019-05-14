package fr.inra.urgi.faidare.repository.es;

import fr.inra.urgi.faidare.domain.criteria.TrialCriteria;
import fr.inra.urgi.faidare.domain.data.TrialVO;
import fr.inra.urgi.faidare.domain.response.PaginatedList;
import fr.inra.urgi.faidare.elasticsearch.repository.ESFindRepository;
import fr.inra.urgi.faidare.elasticsearch.repository.ESGetByIdRepository;

/**
 * Breeding API trial
 *
 * @author gcornut
 */
public interface TrialRepository
    extends ESFindRepository<TrialCriteria, TrialVO>,
    ESGetByIdRepository<TrialVO> {

    @Override
    TrialVO getById(String id);

    @Override
    PaginatedList<TrialVO> find(TrialCriteria criteria);

}
