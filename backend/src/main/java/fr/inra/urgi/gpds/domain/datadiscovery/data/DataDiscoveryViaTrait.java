package fr.inra.urgi.gpds.domain.datadiscovery.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.inra.urgi.gpds.elasticsearch.document.annotation.Document;

import java.util.List;

/**
 * Properties used for search and faceting on data discovery document linked to
 * traits
 * <p>
 * These properties are not serialized in JSON but used in introspection of
 * ElasticSearch queries.
 *
 * @author gcornut
 */
@Document(type = "datadiscovery")
public interface DataDiscoveryViaTrait {

    @JsonIgnore
    TraitSection getTrait();

    interface TraitSection {
        List<String> getObservationVariableIds();
    }

}
