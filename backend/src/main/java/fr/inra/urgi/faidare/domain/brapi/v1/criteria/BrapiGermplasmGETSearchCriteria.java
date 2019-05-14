package fr.inra.urgi.faidare.domain.brapi.v1.criteria;

import java.util.List;

/**
 * @author gcornut
 */
public interface BrapiGermplasmGETSearchCriteria extends BrapiPaginationCriteria {
    List<String> getGermplasmPUI();

    List<String> getGermplasmDbId();

    List<String> getGermplasmName();
}
