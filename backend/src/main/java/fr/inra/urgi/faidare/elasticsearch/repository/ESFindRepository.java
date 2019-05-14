package fr.inra.urgi.faidare.elasticsearch.repository;

import fr.inra.urgi.faidare.domain.criteria.base.PaginationCriteria;
import fr.inra.urgi.faidare.domain.response.PaginatedList;

/**
 * Interface representing an ElasticSearch repository providing a paginated
 * document search based on a criteria.
 *
 * @author gcornut
 */
public interface ESFindRepository<C extends PaginationCriteria, VO> {

    /**
     * Find ES documents using criteria
     */
    PaginatedList<VO> find(C criteria);

}
