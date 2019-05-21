package fr.inra.urgi.faidare.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;

import java.util.List;

/**
 * + * @author mbuy
 * + *
 * + *
 * +
 */

public interface BrapiProgeny {

    @JsonView(JSONView.BrapiFields.class)
    String getGermplasmDbId();

    @JsonView(JSONView.BrapiFields.class)
    String getDefaultDisplayName();

    @JsonView(JSONView.BrapiFields.class)
    List<BrapiParentProgeny> getProgeny();
}
