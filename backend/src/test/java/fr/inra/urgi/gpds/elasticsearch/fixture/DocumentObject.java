package fr.inra.urgi.gpds.elasticsearch.fixture;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import fr.inra.urgi.gpds.elasticsearch.document.annotation.Document;
import fr.inra.urgi.gpds.elasticsearch.document.annotation.Id;

import java.util.List;

/**
 * Data object class for test purpose only
 */
@Document(type = "dataObject")
public class DocumentObject {
    @Id
    public Long id;
    public String name;

    @JsonDeserialize(contentAs = Nested.class)
    public List<Nested> nested;

    @Override
    public boolean equals(Object obj) {
        DocumentObject obj1 = (DocumentObject) obj;
        return obj1.id.equals(id) && obj1.name.equals(name);
    }

    public static class Nested {
        public String id;
    }
}
