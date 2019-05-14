package fr.inra.urgi.faidare.domain.brapi.v1.criteria;

/**
 * @author gcornut
 */
public interface BrapiTrialCriteria extends BrapiPaginationCriteria, BrapiSortCriteria {

    String getProgramDbId();

    String getLocationDbId();

    Boolean getActive();

}
