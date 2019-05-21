package fr.inra.urgi.faidare.domain.brapi.v1.criteria;

import java.util.List;

/**
 * @author gcornut
 */
public interface BrapiGermplasmAttributeCriteria {
    List<String> getAttributeList();

    String getGermplasmDbId();
}
