package fr.inra.urgi.faidare.domain.response;

import fr.inra.urgi.faidare.domain.brapi.v1.response.BrapiMetadata;
import fr.inra.urgi.faidare.domain.brapi.v1.response.BrapiResponse;
import fr.inra.urgi.faidare.domain.jsonld.data.Context;
import fr.inra.urgi.faidare.domain.jsonld.data.HasContext;

/**
 * @author gcornut
 */
public class ApiResponseImpl<T> implements BrapiResponse<T>, HasContext {

    /**
     * Default JSON-LD bringing context to the API response JSON so that a JSON-LD parser can extract RDF
     */
    private static final Context JSONLD_CONTEXT = new Context();
    static {
        JSONLD_CONTEXT.addProperty("schema", "http://schema.org/");
        JSONLD_CONTEXT.addProperty("result", "http://brapi.org/rdf/result");
        JSONLD_CONTEXT.addProperty("data", "http://brapi.org/rdf/resultData");
    }

    private final BrapiMetadata metadata;

    private final T result;

    ApiResponseImpl(BrapiMetadata metadata, T result) {
        this.metadata = metadata;
        this.result = result;
    }

    @Override
    public T getResult() {
        return result;
    }

    @Override
    public BrapiMetadata getMetadata() {
        return metadata;
    }

    @Override
    public Context getContext() {
        return JSONLD_CONTEXT;
    }
}
