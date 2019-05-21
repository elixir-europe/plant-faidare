package fr.inra.urgi.faidare.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;

import java.util.Set;

/**
 * @author gcornut
 * @link https://github.com/plantbreeding/API/blob/master/Specification/Calls/Calls.md
 */
public interface BrapiCall {

    @JsonView(JSONView.BrapiFields.class)
    String getCall();

    @JsonView(JSONView.BrapiFields.class)
    Set<String> getDatatypes();

    @JsonView(JSONView.BrapiFields.class)
    Set<String> getMethods();

    @JsonView(JSONView.BrapiFields.class)
    Set<String> getVersions();
}
