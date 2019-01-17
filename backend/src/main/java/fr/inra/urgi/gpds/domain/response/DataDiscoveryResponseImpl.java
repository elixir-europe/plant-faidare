package fr.inra.urgi.gpds.domain.response;

import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiData;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiMetadata;
import fr.inra.urgi.gpds.domain.data.impl.DataDiscoveryDocument;
import fr.inra.urgi.gpds.domain.data.impl.FacetImpl;

import java.util.List;

/**
 * @author gcornut
 *
 *
 */
class DataDiscoveryResponseImpl implements DataDiscoveryResponse {

	private final BrapiMetadata metadata;
	private final BrapiData<DataDiscoveryDocument> result;
	private final List<FacetImpl> facets;

	DataDiscoveryResponseImpl(BrapiMetadata metadata, BrapiData<DataDiscoveryDocument> result, List<FacetImpl> facets) {
		this.metadata = metadata;
		this.result = result;
		this.facets = facets;
	}

	@Override
	public BrapiMetadata getMetadata() {
		return metadata;
	}

	@Override
	public BrapiData<DataDiscoveryDocument> getResult() {
		return result;
	}

	@Override
	public List<FacetImpl> getFacets() {
		return facets;
	}

}
