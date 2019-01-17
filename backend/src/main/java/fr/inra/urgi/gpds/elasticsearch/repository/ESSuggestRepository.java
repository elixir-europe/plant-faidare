package fr.inra.urgi.gpds.elasticsearch.repository;

import fr.inra.urgi.gpds.domain.criteria.base.PaginationCriteria;

import java.util.LinkedHashSet;

/**
 * Interface representing an ElasticSearch repository providing suggestion on a field values.
 *
 * @author gcornut
 *
 *
 */
public interface ESSuggestRepository<C extends PaginationCriteria> {

	/**
	 * Suggest field values
	 */
	LinkedHashSet<String> suggest(String field, String searchText, Long fetchSize, C criteria);

}
