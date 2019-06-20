package fr.inra.urgi.faidare.domain.datadiscovery.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;
import fr.inra.urgi.faidare.domain.jsonld.data.DataCatalog;

import java.util.List;

/**
 * Data discovery source defined as a schema.org/DataCatalog
 *
 * @author gcornut
 */
public interface DataSource extends DataCatalog {

    @Override
    @JsonView(JSONView.GnpISFields.class)
    String getUri();

    @Override
    @JsonView(JSONView.GnpISFields.class)
    String getIdentifier();

    @Override
    @JsonView(JSONView.GnpISFields.class)
    String getName();

    @Override
    @JsonView(JSONView.GnpISFields.class)
    List<String> getType();

    @Override
    @JsonView(JSONView.GnpISFields.class)
    String getUrl();

    @Override
    @JsonView(JSONView.GnpISFields.class)
    String getImage();

}
