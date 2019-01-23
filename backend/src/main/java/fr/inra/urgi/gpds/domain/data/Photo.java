package fr.inra.urgi.gpds.domain.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;

/**
 * @author gcornut
 */
public interface Photo {
    @JsonView(JSONView.GnpISFields.class)
    String getFileName();

    @JsonView(JSONView.GnpISFields.class)
    String getThumbnailFileName();

    @JsonView(JSONView.GnpISFields.class)
    String getPhotoName();

    @JsonView(JSONView.GnpISFields.class)
    String getDescription();

    @JsonView(JSONView.GnpISFields.class)
    String getCopyright();
}
