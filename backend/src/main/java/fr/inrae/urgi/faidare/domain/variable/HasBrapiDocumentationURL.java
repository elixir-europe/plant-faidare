package fr.inrae.urgi.faidare.domain.variable;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inrae.urgi.faidare.domain.JSONView;

/**
 * Has BrAPI v1.3 documentationUrl field
 *
 * @author gcornut
 */
public interface HasBrapiDocumentationURL {

    @JsonView(JSONView.BrapiFields.class)
    String getDocumentationURL();

}
