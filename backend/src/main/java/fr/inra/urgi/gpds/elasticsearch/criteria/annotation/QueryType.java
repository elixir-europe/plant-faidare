package fr.inra.urgi.gpds.elasticsearch.criteria.annotation;


import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotated a criteria field to specify which kind of ES query it maps to
 *
 * @author gcornut
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryType {
    Class<? extends QueryBuilder> value() default TermQueryBuilder.class;
}
