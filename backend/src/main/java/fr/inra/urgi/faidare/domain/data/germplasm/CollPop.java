package fr.inra.urgi.faidare.domain.data.germplasm;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;

/**
 * @author gcornut
 */
public interface CollPop {
    @JsonView(JSONView.GnpISFields.class)
    Long getId();

    @JsonView(JSONView.GnpISFields.class)
    String getName();

    @JsonView(JSONView.GnpISFields.class)
    String getType();

    @JsonView(JSONView.GnpISFields.class)
    PuiNameValue getGermplasmRef();

    @JsonView(JSONView.GnpISFields.class)
    Integer getGermplasmCount();
}
