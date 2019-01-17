package fr.inra.urgi.gpds.domain.brapi.v1.criteria;

import java.util.Set;

/**
 * @author gcornut
 *
 *
 */
public interface BrapiStudyCriteria extends BrapiPaginationCriteria, BrapiSortCriteria {

	String getStudyType();

	String getProgramDbId();

	String getLocationDbId();

	String getSeasonDbId();

	Set<String> getGermplasmDbIds();

	Set<String> getObservationVariableDbIds();

	Boolean getActive();
}
