package fr.inra.urgi.gpds.elasticsearch.repository;

import fr.inra.urgi.gpds.domain.criteria.base.PaginationCriteria;
import fr.inra.urgi.gpds.domain.response.PaginatedList;

/**
 * Interface representing an ElasticSearch repository providing a paginated
 * document search based on a criteria.
 *
 * @author gcornut
 *
 *
 */
public interface ESFindRepository<C extends PaginationCriteria, VO> {

	/**
	 * Find ES documents using criteria
	 */
	PaginatedList<VO> find(C criteria);

}
