package fr.inra.urgi.faidare.elasticsearch.document.fixture;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import fr.inra.urgi.faidare.elasticsearch.document.annotation.Document;
import fr.inra.urgi.faidare.elasticsearch.document.annotation.Nested;

import java.util.List;

/**
 * Incorrectly annotated document object missing annotation on the type of
 * object in the nestedObjects field.
 * <p>
 * This should be remedied using the {@link JsonDeserialize} annotation with the
 * "contentAs" attribute.
 *
 * @author gcornut
 */
@Document(type = "dataObject1")
public class IncorrectNestedAnnotationDocument {
    @Nested
    List<Object> nestedObjects;
}
