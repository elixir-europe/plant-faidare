package fr.inra.urgi.gpds.domain.brapi.v1.criteria;

import java.util.Set;

/**
 * @author gcornut
 *
 *
 */
public interface BrapiObservationVariableCriteria extends BrapiPaginationCriteria {

	String getTraitClass();

	Set<String> getObservationVariableDbIds();

}
