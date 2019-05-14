package fr.inra.urgi.faidare.elasticsearch.document.fixture;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import fr.inra.urgi.faidare.elasticsearch.document.annotation.Document;
import fr.inra.urgi.faidare.elasticsearch.document.annotation.Id;
import fr.inra.urgi.faidare.elasticsearch.document.annotation.Nested;

import java.util.List;

/**
 * @author gcornut
 */
@Document(type = "dataObject4", excludedFields = {"a", "b"})
public class ComplexDocument {
    @Id(jsonName = "@id")
    String id;

    NestedObject1 nested0;

    @Nested
    NestedObject1 nested1;

    @Nested
    @JsonDeserialize(contentAs = NestedObject2.class)
    List<NestedObject2> nested2;

    @JsonDeserialize(contentAs = NestedObject2.class)
    List<NestedObject2> nested3;

    public NestedObject1 getNested1() {
        return nested1;
    }

    public List<NestedObject2> getNested2() {
        return nested2;
    }

    public class NestedObject1 {
        String id;
    }

    public class NestedObject2 implements HasJSONLDIdentifier {
        String foo;

        @JsonProperty("schema:name")
        String name;

        String identifier;

        @Override
        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }
    }

    /**
     * A Data interface providing Jackson JSON annotation
     */
    interface HasJSONLDIdentifier {

        @JsonProperty("schema:identifier")
        String getIdentifier();

    }
}
