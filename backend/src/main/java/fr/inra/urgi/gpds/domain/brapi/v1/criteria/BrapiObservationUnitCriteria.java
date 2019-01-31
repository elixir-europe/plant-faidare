package fr.inra.urgi.gpds.domain.brapi.v1.criteria;

import java.util.List;
import java.util.Set;

/**
 * @author gcornut
 */
public interface BrapiObservationUnitCriteria extends BrapiPaginationCriteria {
    List<String> getObservationTimeStampRange();

    Set<String> getGermplasmDbIds();

    Set<String> getObservationVariableDbIds();

    Set<String> getStudyDbIds();

    Set<String> getLocationDbIds();

    Set<String> getProgramDbIds();

    Set<String> getSeasonDbIds();

    String getObservationLevel();
}
