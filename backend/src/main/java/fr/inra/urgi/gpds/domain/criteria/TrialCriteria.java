package fr.inra.urgi.gpds.domain.criteria;

import fr.inra.urgi.gpds.domain.brapi.v1.criteria.BrapiPaginationCriteria;
import fr.inra.urgi.gpds.domain.brapi.v1.criteria.BrapiTrialCriteria;
import fr.inra.urgi.gpds.domain.criteria.base.PaginationCriteriaImpl;
import fr.inra.urgi.gpds.domain.criteria.base.SortCriteria;
import fr.inra.urgi.gpds.domain.data.impl.TrialVO;
import fr.inra.urgi.gpds.elasticsearch.criteria.annotation.CriteriaForDocument;
import fr.inra.urgi.gpds.elasticsearch.criteria.annotation.DocumentPath;
import fr.inra.urgi.gpds.elasticsearch.criteria.annotation.NoDocumentMapping;

/**
 * @author gcornut
 */
@CriteriaForDocument(TrialVO.class)
public class TrialCriteria
    extends PaginationCriteriaImpl
    implements SortCriteria, BrapiPaginationCriteria, BrapiTrialCriteria {

    @DocumentPath("programDbId")
    private String programDbId;

    // Query non nested sub field
    @DocumentPath({"studies", "locationDbId"})
    private String locationDbId;

    @DocumentPath("active")
    private Boolean active;

    // Map to nothing
    @NoDocumentMapping
    private String sortBy;

    @NoDocumentMapping
    private String sortOrder;

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
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

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
}
