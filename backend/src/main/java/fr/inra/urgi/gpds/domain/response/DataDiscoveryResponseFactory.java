package fr.inra.urgi.gpds.domain.response;

import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiData;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiMetadata;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiResponseFactory;
import fr.inra.urgi.gpds.domain.data.impl.DataDiscoveryDocument;
import fr.inra.urgi.gpds.domain.data.impl.FacetImpl;

import java.util.List;

/**
 * @author gcornut
 */
public class DataDiscoveryResponseFactory {

    public static DataDiscoveryResponse create(Pagination pagination, List<DataDiscoveryDocument> results, List<FacetImpl> facets) {
        BrapiMetadata metadata = BrapiResponseFactory.createMetadata(pagination, null);
        BrapiData<DataDiscoveryDocument> data = BrapiResponseFactory.createData(results);
        return new DataDiscoveryResponseImpl(metadata, data, facets);
    }

}
