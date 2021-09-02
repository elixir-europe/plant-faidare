package fr.inra.urgi.faidare.elasticsearch.document;

import com.google.common.collect.ImmutableList;
import fr.inra.urgi.faidare.elasticsearch.document.fixture.ComplexDocument;
import fr.inra.urgi.faidare.elasticsearch.document.fixture.IncorrectMissingAnnotationDocument;
import fr.inra.urgi.faidare.elasticsearch.document.fixture.IncorrectNestedAnnotationDocument;
import fr.inra.urgi.faidare.elasticsearch.document.fixture.SimpleDocument;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author gcornut
 */
class DocumentAnnotationUtilTest {

    @Test
    void should_Fail_Missing_Doc_Annotation() {
        Assertions.assertThrows(
            DocumentAnnotationUtil.ValueObjectMetadataException.class,
            () -> DocumentAnnotationUtil.getDocumentObjectMetadata(IncorrectMissingAnnotationDocument.class)
        );
    }

    @Test
    void should_Fail_Missing_Collection_Content_Annotation() {
        Assertions.assertThrows(
            DocumentAnnotationUtil.ValueObjectMetadataException.class,
            () -> DocumentAnnotationUtil.getDocumentObjectMetadata(IncorrectNestedAnnotationDocument.class)
        );
    }

    @Test()
    void should_Generate_Simple_Metadata() {
        DocumentMetadata<SimpleDocument> metadata = DocumentAnnotationUtil
            .getDocumentObjectMetadata(SimpleDocument.class);
        assertThat(metadata).isNotNull();
        assertThat(metadata.getDocumentType()).isEqualTo("dataObject3");
        assertThat(metadata.getIdField()).isEqualTo("id");
        assertThat(metadata.getDocumentClass()).isEqualTo(SimpleDocument.class);

        assertThat(metadata.getExcludedFields()).isEmpty();
        assertThat(metadata.getFieldsByName()).hasSize(1);
    }

    @Test()
    void should_Generate_Complex_Metadata() {
        DocumentMetadata<ComplexDocument> metadata = DocumentAnnotationUtil
            .getDocumentObjectMetadata(ComplexDocument.class);
        assertThat(metadata).isNotNull();
        assertThat(metadata.getDocumentType()).isEqualTo("dataObject4");
        assertThat(metadata.getIncludedFields()).containsExactly("id", "nested0");
        assertThat(metadata.getExcludedFields()).containsExactly("a", "b");

        assertThat(metadata.getIdField()).isEqualTo("@id");
        assertThat(metadata.getDocumentClass()).isEqualTo(ComplexDocument.class);

        assertThat(metadata.getFieldsByName()).hasSize(5);

        DocumentMetadata.Field nested1 = metadata.getFieldsByName().get("nested1");
        assertThat(nested1.getDescriptor()).isNotNull();
        assertThat(nested1.getPath()).isEqualTo(ImmutableList.of("nested1"));
        assertThat(nested1.isNestedObject()).isTrue();
        assertThat(nested1.getFieldClass()).isEqualTo(ComplexDocument.NestedObject1.class);

        DocumentMetadata.Field id = metadata.getFieldsByName().get("id");
        assertThat(id.getDescriptor()).isNull();
        assertThat(id.getPath()).isEqualTo(ImmutableList.of("id"));
        assertThat(id.isNestedObject()).isFalse();
        assertThat(id.getFieldClass()).isEqualTo(String.class);

        DocumentMetadata.Field nested2 = metadata.getFieldsByName().get("nested2");
        assertThat(nested2.getDescriptor()).isNotNull();
        assertThat(nested2.getPath()).isEqualTo(ImmutableList.of("nested2"));
        assertThat(nested2.isNestedObject()).isTrue();
        assertThat(nested2.getFieldClass()).isEqualTo(ComplexDocument.NestedObject2.class);
    }

    @Test
    void should_Get_Field_By_Path() {
        DocumentMetadata<ComplexDocument> metadata = DocumentAnnotationUtil
            .getDocumentObjectMetadata(ComplexDocument.class);

        DocumentMetadata.Field field;
        field = metadata.getByPath(ImmutableList.of("nested2", "foo"));
        assertThat(field).isNotNull();

        field = metadata.getByPath(ImmutableList.of("nested3", "foo"));
        assertThat(field).isNotNull();

        field = metadata.getByPath(ImmutableList.of("nested0", "id"));
        assertThat(field).isNotNull();
    }

    @Test
    void should_Get_Field_By_JSON_Path_Or_Java_Path() {
        DocumentMetadata<ComplexDocument> metadata = DocumentAnnotationUtil
            .getDocumentObjectMetadata(ComplexDocument.class);

        DocumentMetadata.Field field;

        // Get by java path
        field = metadata.getByPath(ImmutableList.of("nested2", "name"));
        assertThat(field).isNotNull();
        // Get by json path (set with direct Jackson JSON annotation on field)
        field = metadata.getByPath(ImmutableList.of("nested2", "schema:name"));
        assertThat(field).isNotNull();

        // Get by java path
        field = metadata.getByPath(ImmutableList.of("nested2", "identifier"));
        assertThat(field).isNotNull();
        // Get by json path (set with indirect Jackson JSON annotation on getter method of parent interface)
        field = metadata.getByPath(ImmutableList.of("nested2", "schema:identifier"));
        assertThat(field).isNotNull();
    }
}
