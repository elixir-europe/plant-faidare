package fr.inra.urgi.gpds.domain.datadiscovery.response;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiData;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiListResponse;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiMetadata;
import fr.inra.urgi.gpds.domain.datadiscovery.data.DataDiscoveryDocument;
import fr.inra.urgi.gpds.domain.datadiscovery.data.Facet;

import java.util.List;

/**
 * @author gcornut
 */
public interface DataDiscoveryResponse extends BrapiListResponse<DataDiscoveryDocument> {

    @Override
    @JsonView(JSONView.GnpISFields.class)
    BrapiMetadata getMetadata();

    @Override
    @JsonView(JSONView.GnpISFields.class)
    BrapiData<DataDiscoveryDocument> getResult();

    @JsonView(JSONView.GnpISFields.class)
    List<? extends Facet> getFacets();

}
