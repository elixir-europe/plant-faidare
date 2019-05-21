package fr.inra.urgi.faidare.domain.datadiscovery.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;
import fr.inra.urgi.faidare.domain.jsonld.data.Dataset;
import fr.inra.urgi.faidare.elasticsearch.document.annotation.Document;

import java.util.List;

/**
 * Properties used for data discovery
 *
 * @author gcornut
 */
@Document(type = "datadiscovery")
public interface DataDiscovery extends Dataset {

    @Override
    @JsonView(JSONView.GnpISFields.class)
    String getIdentifier();

    @Override
    @JsonView(JSONView.GnpISFields.class)
    String getUri();

    @Override
    @JsonView(JSONView.GnpISFields.class)
    String getName();

    @Override
    @JsonView(JSONView.GnpISFields.class)
    String getSourceUri();

    @Override
    @JsonView(JSONView.GnpISFields.class)
    List<String> getType();

    @Override
    @JsonView(JSONView.GnpISFields.class)
    String getDescription();

    @Override
    @JsonView(JSONView.GnpISFields.class)
    String getUrl();

}
