package fr.inra.urgi.gpds.elasticsearch.criteria.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares a criteria property not to be mapped to any value object property
 *
 * @author gcornut
 *
 *
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoDocumentMapping {
}
