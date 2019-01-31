package fr.inra.urgi.gpds.elasticsearch.criteria.fixture;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import fr.inra.urgi.gpds.elasticsearch.document.annotation.Document;
import fr.inra.urgi.gpds.elasticsearch.document.annotation.Id;
import fr.inra.urgi.gpds.elasticsearch.document.annotation.Nested;

import java.util.List;

/**
 * @author gcornut
 */
@Document(type = "vo")
public class DocumentObject {
    @Id
    String field1;

    @Nested
    @JsonDeserialize(contentAs = NestedObject.class)
    List<NestedObject> field2;

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public List<NestedObject> getField2() {
        return field2;
    }

    public void setField2(List<NestedObject> field2) {
        this.field2 = field2;
    }

    @Document(type = "nested")
    class NestedObject implements HasJSONLDIdentifier {
        @Id
        String field3;

        String field4;

        String identifier;

        @JsonProperty("schema:name")
        String name;

        public String getField3() {
            return field3;
        }

        public void setField3(String field3) {
            this.field3 = field3;
        }

        public String getField4() {
            return field4;
        }

        public void setField4(String field4) {
            this.field4 = field4;
        }

        @Override
        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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
