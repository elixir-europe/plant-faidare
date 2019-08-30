package fr.inra.urgi.faidare.domain.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;

import java.io.Serializable;
import java.util.List;

/**
 * Properties used internally which should not be exposed
 *
 * @author gcornut
 */
public interface GnpISInternal extends Serializable {

    /**
     * Restricted group DB identifier from which the VO belong
     */
    @JsonView(JSONView.Internal.class)
    Long getGroupId();

}
