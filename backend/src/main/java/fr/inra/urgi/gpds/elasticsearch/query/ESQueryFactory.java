package fr.inra.urgi.gpds.elasticsearch.query;

import org.elasticsearch.index.query.QueryBuilder;

/**
 * @author gcornut
 */
public interface ESQueryFactory<C> {

    /**
     * Create query from the criteria
     */
    QueryBuilder createQuery(C criteria);

}
