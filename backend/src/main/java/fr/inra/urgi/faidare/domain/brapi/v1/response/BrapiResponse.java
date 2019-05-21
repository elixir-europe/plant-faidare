package fr.inra.urgi.faidare.domain.brapi.v1.response;


import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;

/**
 * bean for general response structure for breeding API
 *
 * @author cpommier, gcornut
 *
 *
 * <code>
 * {
 * "metadata": {},
 * "result" : {}
 * }
 * </code>
 */

public interface BrapiResponse<T> {
    @JsonView(JSONView.BrapiFields.class)
    T getResult();

    @JsonView(JSONView.BrapiFields.class)
    BrapiMetadata getMetadata();
}
