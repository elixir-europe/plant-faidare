package fr.inra.urgi.gpds.repository.es;

import fr.inra.urgi.gpds.domain.criteria.StudyCriteria;
import fr.inra.urgi.gpds.domain.data.study.StudyDetailVO;
import fr.inra.urgi.gpds.domain.data.study.StudySummaryVO;
import fr.inra.urgi.gpds.domain.response.PaginatedList;
import fr.inra.urgi.gpds.elasticsearch.repository.ESFindRepository;
import fr.inra.urgi.gpds.elasticsearch.repository.ESGetByIdRepository;

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
