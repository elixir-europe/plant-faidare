package fr.inra.urgi.faidare.elasticsearch.document.fixture;

import fr.inra.urgi.faidare.elasticsearch.document.annotation.Document;
import fr.inra.urgi.faidare.elasticsearch.document.annotation.Id;

/**
 * @author gcornut
 */
@Document(type = "dataObject3")
public class SimpleDocument {
    @Id
    String id;
}
