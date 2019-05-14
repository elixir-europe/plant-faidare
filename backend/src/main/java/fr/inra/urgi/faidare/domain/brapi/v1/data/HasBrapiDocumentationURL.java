package fr.inra.urgi.faidare.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;

/**
 * Has BrAPI v1.3 documentationUrl field
 *
 * @author gcornut
 */
public interface HasBrapiDocumentationURL {

    @JsonView(JSONView.BrapiFields.class)
    String getDocumentationURL();

}
