package fr.inra.urgi.gpds.domain.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;

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
