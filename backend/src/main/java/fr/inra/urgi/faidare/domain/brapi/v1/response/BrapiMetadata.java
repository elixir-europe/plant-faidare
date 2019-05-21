package fr.inra.urgi.faidare.domain.brapi.v1.response;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;

import java.util.List;

/**
 * @author gcornut
 */
public interface BrapiMetadata {
    @JsonView(JSONView.BrapiFields.class)
    BrapiPagination getPagination();

    @JsonView(JSONView.BrapiFields.class)
    List<BrapiStatus> getStatus();

    @JsonView(JSONView.BrapiFields.class)
    List<String> getDatafiles();
}
