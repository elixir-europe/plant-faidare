package fr.inra.urgi.faidare.domain.brapi.v1.response;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;

import java.util.List;

/**
 * @author gcornut
 */
public interface BrapiData<T> {

    @JsonView(JSONView.BrapiFields.class)
    List<T> getData();

}
