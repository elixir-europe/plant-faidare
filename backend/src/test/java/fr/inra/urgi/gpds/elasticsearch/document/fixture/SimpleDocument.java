package fr.inra.urgi.gpds.elasticsearch.document.fixture;

import fr.inra.urgi.gpds.elasticsearch.document.annotation.Document;
import fr.inra.urgi.gpds.elasticsearch.document.annotation.Id;

/**
 * @author gcornut
 */
@Document(type = "dataObject3")
public class SimpleDocument {
	@Id
	String id;
}
