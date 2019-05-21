package fr.inra.urgi.faidare.domain.criteria;

import fr.inra.urgi.faidare.domain.brapi.v1.criteria.BrapiObservationVariableCriteria;
import fr.inra.urgi.faidare.domain.brapi.v1.criteria.BrapiPaginationCriteria;
import fr.inra.urgi.faidare.domain.criteria.base.PaginationCriteriaImpl;

import java.util.Set;

/**
 * @author gcornut
 */
public class ObservationVariableCriteria extends PaginationCriteriaImpl
    implements BrapiPaginationCriteria, BrapiObservationVariableCriteria {

    private String traitClass;
    private Set<String> observationVariableDbIds;

    @Override
    public String getTraitClass() {
        return traitClass;
    }

    public void setTraitClass(String traitClass) {
        this.traitClass = traitClass;
    }

    @Override
    public Set<String> getObservationVariableDbIds() {
        return observationVariableDbIds;
    }

    public void setObservationVariableDbIds(Set<String> observationVariableDbIds) {
        this.observationVariableDbIds = observationVariableDbIds;
    }

}
