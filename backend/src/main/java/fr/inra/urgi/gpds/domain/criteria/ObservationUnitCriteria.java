package fr.inra.urgi.gpds.domain.criteria;

import fr.inra.urgi.gpds.domain.brapi.v1.criteria.BrapiObservationUnitCriteria;
import fr.inra.urgi.gpds.domain.criteria.base.PaginationCriteriaImpl;
import fr.inra.urgi.gpds.domain.data.impl.ObservationUnitVO;
import fr.inra.urgi.gpds.elasticsearch.criteria.annotation.CriteriaForDocument;
import fr.inra.urgi.gpds.elasticsearch.criteria.annotation.DocumentPath;
import fr.inra.urgi.gpds.elasticsearch.criteria.annotation.QueryType;
import org.elasticsearch.index.query.RangeQueryBuilder;

import java.util.List;
import java.util.Set;

/**
 * @author gcornut
 */
@CriteriaForDocument(ObservationUnitVO.class)
public class ObservationUnitCriteria
    extends PaginationCriteriaImpl
    implements BrapiObservationUnitCriteria {

    @DocumentPath("germplasmDbId")
    private Set<String> germplasmDbIds;

    @DocumentPath("studyDbId")
    private Set<String> studyDbIds;

    @DocumentPath("studyLocationDbId")
    private Set<String> locationDbIds;

    @DocumentPath("programDbId")
    private Set<String> programDbIds;

    @DocumentPath("observationLevel")
    private String observationLevel;

    // Map to nested fields
    @DocumentPath({"observations", "observationVariableDbId"})
    private Set<String> observationVariableDbIds;

    // Map to nested field
    @DocumentPath({"observations", "season"})
    private Set<String> seasonDbIds;

    // Map to nested field
    @DocumentPath({"observations", "observationTimeStamp"})
    @QueryType(RangeQueryBuilder.class)
    private List<String> observationTimeStampRange;

    @Override
    public List<String> getObservationTimeStampRange() {
        return observationTimeStampRange;
    }

    public void setObservationTimeStampRange(List<String> observationTimeStampRange) {
        this.observationTimeStampRange = observationTimeStampRange;
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
    public Set<String> getStudyDbIds() {
        return studyDbIds;
    }

    public void setStudyDbIds(Set<String> studyDbIds) {
        this.studyDbIds = studyDbIds;
    }

    @Override
    public Set<String> getLocationDbIds() {
        return locationDbIds;
    }

    public void setLocationDbIds(Set<String> locationDbIds) {
        this.locationDbIds = locationDbIds;
    }

    @Override
    public Set<String> getProgramDbIds() {
        return programDbIds;
    }

    public void setProgramDbIds(Set<String> programDbIds) {
        this.programDbIds = programDbIds;
    }

    @Override
    public Set<String> getSeasonDbIds() {
        return seasonDbIds;
    }

    public void setSeasonDbIds(Set<String> seasonDbIds) {
        this.seasonDbIds = seasonDbIds;
    }

    @Override
    public String getObservationLevel() {
        return observationLevel;
    }

    public void setObservationLevel(String observationLevel) {
        this.observationLevel = observationLevel;
    }
}
