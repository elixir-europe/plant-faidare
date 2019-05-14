package fr.inra.urgi.faidare.domain;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.elasticsearch.ESResponseParser;

/**
 * Collection of Jack JSON views used to annotate Java bean properties using {@link JsonView}
 *
 * @author gcornut
 */
public interface JSONView {

    /**
     * JSON View for BrAPI fields
     */
    interface BrapiFields {
    }

    /**
     * JSON View for GnpIS fields that can be exposed
     */
    interface GnpISFields {
    }

    /**
     * JSON View on JSON-LD fields
     */
    interface JSONLDFields {
    }

    /**
     * JSON view for GnpIS public API (combine fields from BrAPI and GnpIS)
     */
    interface BrapiWithJSONLD extends BrapiFields, JSONLDFields {
    }

    /**
     * JSON view for GnpIS public API (combine fields from BrAPI and GnpIS)
     */
    interface GnpISAPI extends JSONLDFields, GnpISFields, BrapiFields {
    }

    /**
     * Jackson json view used for internal properties that should not be exposed.
     * Used in {@link ESResponseParser} to deserialize internal properties from Elasticsearch.
     */
    interface Internal extends GnpISAPI {
    }

}
