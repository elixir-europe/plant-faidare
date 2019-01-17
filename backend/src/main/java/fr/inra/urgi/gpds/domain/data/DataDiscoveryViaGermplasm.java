package fr.inra.urgi.gpds.domain.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.inra.urgi.gpds.elasticsearch.document.annotation.Document;

import java.util.List;

/**
 *
 * Properties used for search and faceting on data discovery document linked to germplasm.
 *
 * These properties are not serialized in JSON but used in introspection of ElasticSearch queries.
 *
 * @author gcornut
 *
 *
 */
@Document(type = "datadiscovery")
public interface DataDiscoveryViaGermplasm {

	@JsonIgnore
	GermplasmSection getGermplasm();

	interface GermplasmSection {
		List<String> getCropName();

		List<String> getGermplasmList();

		List<String> getAccession();
	}

}
