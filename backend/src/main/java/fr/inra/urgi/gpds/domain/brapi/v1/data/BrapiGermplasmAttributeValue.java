package fr.inra.urgi.gpds.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;

import java.util.Date;

/**
 * @author gcornut
 */
public interface BrapiGermplasmAttributeValue {
    @JsonView(JSONView.BrapiFields.class)
    String getAttributeDbId();

    @JsonView(JSONView.BrapiFields.class)
    String getAttributeName();

    @JsonView(JSONView.BrapiFields.class)
    String getAttributeCode();

    @JsonView(JSONView.BrapiFields.class)
    String getValue();

    @JsonView(JSONView.BrapiFields.class)
    Date getDeterminedDate();
}
