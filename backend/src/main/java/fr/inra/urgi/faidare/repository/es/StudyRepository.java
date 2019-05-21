package fr.inra.urgi.faidare.repository.es;

import fr.inra.urgi.faidare.domain.criteria.StudyCriteria;
import fr.inra.urgi.faidare.domain.data.study.StudyDetailVO;
import fr.inra.urgi.faidare.domain.data.study.StudySummaryVO;
import fr.inra.urgi.faidare.domain.response.PaginatedList;
import fr.inra.urgi.faidare.elasticsearch.repository.ESFindRepository;
import fr.inra.urgi.faidare.elasticsearch.repository.ESGetByIdRepository;

import java.util.Set;

/**
 * Breeding API study
 *
 * @author gcornut
 */
public interface StudyRepository
    extends ESGetByIdRepository<StudyDetailVO>,
    ESFindRepository<StudyCriteria, StudySummaryVO> {

    @Override
    StudyDetailVO getById(String id);

    @Override
    PaginatedList<StudySummaryVO> find(StudyCriteria criteria);

    /**
     * List study observation variable ids
     */
    Set<String> getVariableIds(String studyDbId);

}
