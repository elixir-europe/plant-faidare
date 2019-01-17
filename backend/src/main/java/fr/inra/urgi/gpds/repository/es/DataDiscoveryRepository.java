package fr.inra.urgi.gpds.repository.es;

import fr.inra.urgi.gpds.domain.criteria.DataDiscoveryCriteria;
import fr.inra.urgi.gpds.domain.response.DataDiscoveryResponse;
import fr.inra.urgi.gpds.elasticsearch.repository.ESSuggestRepository;

import java.util.LinkedHashSet;

/**
 *
 * @author gcornut
 *
 *
 */
public interface DataDiscoveryRepository
	extends ESSuggestRepository<DataDiscoveryCriteria>
{

	/**
	 * Suggest term values contained in field
	 * @param field field name from the DataDiscoveryCriteria
	 * @param searchText the search text used to filter terms
	 * @param fetchSize size of the suggestion list
	 * @param criteria the criteria used to pre-filter terms
	 * @return
	 */
	@Override
	LinkedHashSet<String> suggest(String field, String searchText, Long fetchSize, DataDiscoveryCriteria criteria);

	DataDiscoveryResponse find(DataDiscoveryCriteria criteria);
}
