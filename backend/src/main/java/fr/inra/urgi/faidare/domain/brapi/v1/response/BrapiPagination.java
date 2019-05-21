package fr.inra.urgi.faidare.domain.brapi.v1.response;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;

/**
 * @author gcornut
 */
public interface BrapiPagination {
    @JsonView(JSONView.BrapiFields.class)
    long getPageSize();

    @JsonView(JSONView.BrapiFields.class)
    long getCurrentPage();

    @JsonView(JSONView.BrapiFields.class)
    long getTotalCount();

    @JsonView(JSONView.BrapiFields.class)
    long getTotalPages();
}
