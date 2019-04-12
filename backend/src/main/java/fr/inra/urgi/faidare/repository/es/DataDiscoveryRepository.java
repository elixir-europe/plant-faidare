package fr.inra.urgi.faidare.repository.es;

import fr.inra.urgi.faidare.domain.datadiscovery.criteria.DataDiscoveryCriteria;
import fr.inra.urgi.faidare.domain.datadiscovery.response.DataDiscoveryResponse;
import fr.inra.urgi.faidare.elasticsearch.repository.ESSuggestRepository;

import java.util.LinkedHashSet;

/**
 * @author gcornut
 */
public interface DataDiscoveryRepository
    extends ESSuggestRepository<DataDiscoveryCriteria> {

    /**
     * Suggest term values contained in field
     *
     * @param field      field name from the DataDiscoveryCriteria
     * @param searchText the search text used to filter terms
     * @param fetchSize  size of the suggestion list
     * @param criteria   the criteria used to pre-filter terms
     * @return
     */
    @Override
    LinkedHashSet<String> suggest(String field, String searchText, Integer fetchSize, DataDiscoveryCriteria criteria);

    DataDiscoveryResponse find(DataDiscoveryCriteria criteria);
}
