package fr.inra.urgi.gpds.domain.response;

import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiMetadata;
import fr.inra.urgi.gpds.domain.datadiscovery.data.DataDiscoveryDocument;
import fr.inra.urgi.gpds.domain.datadiscovery.data.Facet;
import fr.inra.urgi.gpds.domain.datadiscovery.data.FacetImpl;
import fr.inra.urgi.gpds.domain.datadiscovery.response.DataDiscoveryResponse;

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
