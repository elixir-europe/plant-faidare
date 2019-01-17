package fr.inra.urgi.gpds.elasticsearch.criteria.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Maps a criteria property to a value object property
 *
 * @author gcornut
 *
 *
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DocumentPath {

	/**
	 * Path into the document to the desired field
	 */
	String[] value();

	/**
	 * Extra field path leading to elasticsearch virtual field
	 */
	String virtualField() default "";

}
