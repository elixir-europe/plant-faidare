package fr.inra.urgi.gpds.domain.brapi.v1.response;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;

import java.util.List;

/**
 * @author gcornut
 */
public interface BrapiData<T> {

    @JsonView(JSONView.BrapiFields.class)
    List<T> getData();

}
