package fr.inra.urgi.faidare.domain.criteria;

import fr.inra.urgi.faidare.domain.brapi.v1.criteria.BrapiPaginationCriteria;
import fr.inra.urgi.faidare.domain.brapi.v1.criteria.BrapiSortCriteria;
import fr.inra.urgi.faidare.domain.brapi.v1.criteria.BrapiStudySearchCriteria;
import fr.inra.urgi.faidare.domain.criteria.base.SortCriteria;
import fr.inra.urgi.faidare.domain.data.study.StudySummaryVO;
import fr.inra.urgi.faidare.elasticsearch.criteria.annotation.CriteriaForDocument;
import fr.inra.urgi.faidare.elasticsearch.criteria.annotation.DocumentPath;

import java.util.Set;

/**
 * @author gcornut
 */
@CriteriaForDocument(StudySummaryVO.class)
public class StudySearchCriteria extends StudySummaryCriteria
    implements SortCriteria, BrapiPaginationCriteria, BrapiSortCriteria, BrapiStudySearchCriteria {

    @DocumentPath("studyName")
    private Set<String> studyNames;

    @DocumentPath("programName")
    private Set<String> programNames;

    @DocumentPath("locationName")
    private Set<String> studyLocations;

    @Override
    public Set<String> getStudyLocations() {
        return studyLocations;
    }

    public void setStudyLocations(Set<String> studyLocations) {
        this.studyLocations = studyLocations;
    }

    @Override
    public Set<String> getProgramNames() {
        return programNames;
    }

    public void setProgramNames(Set<String> programNames) {
        this.programNames = programNames;
    }

    @Override
    public Set<String> getStudyNames() {
        return studyNames;
    }

    public void setStudyNames(Set<String> studyNames) {
        this.studyNames = studyNames;
    }

}
