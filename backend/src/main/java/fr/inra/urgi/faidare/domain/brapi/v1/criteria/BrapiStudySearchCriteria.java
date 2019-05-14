package fr.inra.urgi.faidare.domain.brapi.v1.criteria;

import java.util.Set;

/**
 * @author gcornut
 */
public interface BrapiStudySearchCriteria extends BrapiPaginationCriteria, BrapiSortCriteria, BrapiStudyCriteria {

    Set<String> getStudyLocations();

    Set<String> getProgramNames();

    Set<String> getStudyNames();

}
