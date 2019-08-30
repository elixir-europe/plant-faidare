package fr.inra.urgi.faidare.domain.data.germplasm;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;

/**
 * @author gcornut
 */
public interface CollPop {
    @JsonView(JSONView.BrapiFields.class)
    Long getId();

    @JsonView(JSONView.BrapiFields.class)
    String getName();

    @JsonView(JSONView.BrapiFields.class)
    String getType();

    @JsonView(JSONView.BrapiFields.class)
    PuiNameValue getGermplasmRef();

    @JsonView(JSONView.BrapiFields.class)
    Integer getGermplasmCount();
}
