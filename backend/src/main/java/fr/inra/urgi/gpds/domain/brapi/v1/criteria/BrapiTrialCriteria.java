package fr.inra.urgi.gpds.domain.brapi.v1.criteria;

/**
 * @author gcornut
 *
 *
 */
public interface BrapiTrialCriteria extends BrapiPaginationCriteria, BrapiSortCriteria {

	String getProgramDbId();

	String getLocationDbId();

	Boolean getActive();

}
