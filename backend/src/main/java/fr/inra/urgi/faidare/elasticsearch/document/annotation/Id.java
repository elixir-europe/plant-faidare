package fr.inra.urgi.faidare.elasticsearch.document.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author gcornut
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {

    /**
     * Override the id field name
     */
    String jsonName() default "";
}
