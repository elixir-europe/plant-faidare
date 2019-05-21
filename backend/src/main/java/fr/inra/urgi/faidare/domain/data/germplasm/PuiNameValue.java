package fr.inra.urgi.faidare.domain.data.germplasm;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;

/**
 * @author gcornut
 */
public interface PuiNameValue {
    @JsonView(JSONView.GnpISFields.class)
    String getPui();

    @JsonView(JSONView.GnpISFields.class)
    String getName();

    @JsonView(JSONView.GnpISFields.class)
    String getValue();
}
