package fr.inra.urgi.faidare.domain.response;

import fr.inra.urgi.faidare.domain.brapi.v1.response.BrapiMetadata;
import fr.inra.urgi.faidare.domain.datadiscovery.data.DataDiscoveryDocument;
import fr.inra.urgi.faidare.domain.datadiscovery.data.Facet;
import fr.inra.urgi.faidare.domain.datadiscovery.data.FacetImpl;
import fr.inra.urgi.faidare.domain.datadiscovery.response.DataDiscoveryResponse;

import java.util.List;

/**
 * @author gcornut
 */
public class DataDiscoveryResponseImpl extends ApiListResponseImpl<DataDiscoveryDocument> implements DataDiscoveryResponse {

    private final List<FacetImpl> facets;

    public DataDiscoveryResponseImpl(BrapiMetadata metadata, List<DataDiscoveryDocument> result, List<FacetImpl> facets) {
        super(metadata, result);
        this.facets = facets;
    }

    @Override
    public List<? extends Facet> getFacets() {
        return facets;
    }


}
