package fr.inra.urgi.gpds.domain.brapi.v1.criteria;

import java.util.Set;

/**
 * @author gcornut
 */
public interface BrapiLocationCriteria extends BrapiPaginationCriteria {

    Set<String> getLocationTypes();

}
