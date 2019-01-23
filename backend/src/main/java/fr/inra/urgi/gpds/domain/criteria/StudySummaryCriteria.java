package fr.inra.urgi.gpds.domain.criteria;

import fr.inra.urgi.gpds.domain.brapi.v1.criteria.BrapiPaginationCriteria;
import fr.inra.urgi.gpds.domain.brapi.v1.criteria.BrapiSortCriteria;
import fr.inra.urgi.gpds.domain.brapi.v1.criteria.BrapiStudyCriteria;
import fr.inra.urgi.gpds.domain.criteria.base.PaginationCriteriaImpl;
import fr.inra.urgi.gpds.domain.criteria.base.SortCriteria;
import fr.inra.urgi.gpds.domain.data.impl.StudySummaryVO;
import fr.inra.urgi.gpds.elasticsearch.criteria.annotation.CriteriaForDocument;
import fr.inra.urgi.gpds.elasticsearch.criteria.annotation.DocumentPath;
import fr.inra.urgi.gpds.elasticsearch.criteria.annotation.NoDocumentMapping;

import java.util.Set;

/**
 * @author gcornut
 */
@CriteriaForDocument(StudySummaryVO.class)
public class StudySummaryCriteria extends PaginationCriteriaImpl
    implements SortCriteria, StudyCriteria, BrapiPaginationCriteria, BrapiSortCriteria, BrapiStudyCriteria {

    @DocumentPath("studyType")
    private String studyType;

    @DocumentPath("programDbId")
    private String programDbId;

    @DocumentPath("locationDbId")
    private String locationDbId;

    @DocumentPath("seasons")
    private String seasonDbId;

    @DocumentPath("germplasmDbIds")
    private Set<String> germplasmDbIds;

    @DocumentPath("observationVariableDbIds")
    private Set<String> observationVariableDbIds;

    @DocumentPath("active")
    private Boolean active;

    // Map to nothing (used for result sorting not as an actual criteria)
    @NoDocumentMapping
    private String sortBy;

    // Map to nothing (used for result sorting not as an actual criteria)
    @NoDocumentMapping
    private String sortOrder;

    @Override
    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    @Override
    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override
    public String getStudyType() {
        return studyType;
    }

    public void setStudyType(String studyType) {
        this.studyType = studyType;
    }

    @Override
    public String getProgramDbId() {
        return programDbId;
    }

    public void setProgramDbId(String programDbId) {
        this.programDbId = programDbId;
    }

    @Override
    public String getLocationDbId() {
        return locationDbId;
    }

    public void setLocationDbId(String locationDbId) {
        this.locationDbId = locationDbId;
    }

    @Override
    public String getSeasonDbId() {
        return seasonDbId;
    }

    public void setSeasonDbId(String seasonDbId) {
        this.seasonDbId = seasonDbId;
    }

    @Override
    public Set<String> getGermplasmDbIds() {
        return germplasmDbIds;
    }

    public void setGermplasmDbIds(Set<String> germplasmDbIds) {
        this.germplasmDbIds = germplasmDbIds;
    }

    @Override
    public Set<String> getObservationVariableDbIds() {
        return observationVariableDbIds;
    }

    public void setObservationVariableDbIds(Set<String> observationVariableDbIds) {
        this.observationVariableDbIds = observationVariableDbIds;
    }

    @Override
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
