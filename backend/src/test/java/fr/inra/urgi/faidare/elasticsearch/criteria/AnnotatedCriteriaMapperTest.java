package fr.inra.urgi.faidare.elasticsearch.criteria;

import com.google.common.collect.ImmutableList;
import fr.inra.urgi.faidare.elasticsearch.criteria.annotation.CriteriaForDocument;
import fr.inra.urgi.faidare.elasticsearch.criteria.annotation.DocumentPath;
import fr.inra.urgi.faidare.elasticsearch.criteria.annotation.NoDocumentMapping;
import fr.inra.urgi.faidare.elasticsearch.criteria.fixture.*;
import fr.inra.urgi.faidare.elasticsearch.criteria.mapping.CriteriaMapping;
import fr.inra.urgi.faidare.elasticsearch.criteria.mapping.CriteriaMappingCriterion;
import fr.inra.urgi.faidare.elasticsearch.criteria.mapping.CriteriaMappingTree;
import fr.inra.urgi.faidare.elasticsearch.criteria.mapping.CriteriaMappingTreeNode;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author gcornut
 */
public class AnnotatedCriteriaMapperTest {

    /**
     * Error if the mapper encounters a criteria with no field to map
     */
    @Test
    public void should_Fail_Mapping_Empty_Criteria() {
        Assertions.assertThrows(
            AnnotatedCriteriaMapper.CriteriaMappingException.class,
            () -> AnnotatedCriteriaMapper.getMapping(IncorrectEmptyCriteria.class)
        );
    }

    /**
     * Error if the mapper encounters a criteria without {@link DocumentPath} or
     * {@link NoDocumentMapping} annotations on fields
     */
    @Test
    public void should_Fail_Mapping_Incorrectly_Annotated_Criteria_Field() {
        Assertions.assertThrows(
            AnnotatedCriteriaMapper.CriteriaMappingException.class,
            () -> AnnotatedCriteriaMapper.getMapping(IncorrectFieldAnnotationCriteria.class)
        );
    }

    /**
     * Error if the mapper encounters a criteria without
     * {@link CriteriaForDocument}
     * annotation on class
     */
    @Test
    public void should_Fail_Mapping_Incorrectly_Annotated_Criteria_Class() {
        Assertions.assertThrows(
            AnnotatedCriteriaMapper.CriteriaMappingException.class,
            () -> AnnotatedCriteriaMapper.getMapping(IncorrectClassAnnotationCriteria.class)
        );
    }

    /**
     * Error if the mapper encounters a criteria without correct getter/setter on
     * criteria fields
     */
    @Test
    public void should_Fail_Mapping_Incorrect_Bean_Property_Criteria() {
        Assertions.assertThrows(
            AnnotatedCriteriaMapper.CriteriaMappingException.class,
            () -> AnnotatedCriteriaMapper.getMapping(IncorrectBeanPropertyCriteria.class)
        );
    }

    /**
     * Error if the mapper encounters an incorrect document field path
     */
    @Test
    public void should_Fail_Mapping_Incorrect_Document_Path_Criteria() {
        Assertions.assertThrows(
            AnnotatedCriteriaMapper.CriteriaMappingException.class,
            () -> AnnotatedCriteriaMapper.getMapping(IncorrectDocumentPathCriteria.class)
        );
    }

    @Test
    public void should_Map_Complex_Annotated_Criteria() throws InvocationTargetException, IllegalAccessException {
        ComplexAnnotatedCriteria complexAnnotatedCriteria = new ComplexAnnotatedCriteria();
        List<String> value1 = ImmutableList.of("value1");
        complexAnnotatedCriteria.setCriteria1(value1);
        String value2 = "value2";
        complexAnnotatedCriteria.setCriteria2(value2);

        CriteriaMapping mapping = AnnotatedCriteriaMapper.getMapping(ComplexAnnotatedCriteria.class);

        Map<List<String>, CriteriaMappingTree> mappingTree = mapping.getMappingTree();
        assertThat(mappingTree).isNotNull().isNotEmpty().hasSize(2);

        List<String> fieldPath1 = ImmutableList.of("field1");
        CriteriaMappingCriterion criterion1 = mapping.getMapping(CriteriaMappingCriterion.class, fieldPath1);
        assertThat(criterion1).isNotNull();
        assertThat(criterion1.getValue(complexAnnotatedCriteria)).isEqualTo(value1);
        assertThat(criterion1.getName()).isEqualTo("criteria1");
        assertThat(criterion1.getDocumentFieldName()).isEqualTo("field1");
        assertThat(criterion1.getDocumentFieldPath()).isEqualTo(fieldPath1);
        assertThat(criterion1.getQueryType()).isEqualTo(RangeQueryBuilder.class);

        ImmutableList<String> fieldPath2 = ImmutableList.of("field2");
        CriteriaMappingTreeNode node = mapping.getMapping(CriteriaMappingTreeNode.class, fieldPath2);
        assertThat(node).isNotNull();
        assertThat(node.getDocumentFieldName()).isEqualTo("field2");
        assertThat(node.getDocumentFieldPath()).isEqualTo(fieldPath2);

        List<String> fieldPath3 = ImmutableList.of("field2", "field3");
        CriteriaMappingCriterion criterion3 = mapping.getMapping(CriteriaMappingCriterion.class, fieldPath3);
        assertThat(criterion3).isNotNull();
        assertThat(criterion3.getValue(complexAnnotatedCriteria)).isEqualTo(value2);
        assertThat(criterion3.getName()).isEqualTo("criteria2");
        assertThat(criterion3.getDocumentFieldName()).isEqualTo("field3");
        assertThat(criterion3.getDocumentFieldPath()).isEqualTo(fieldPath3);
        assertThat(criterion3.getQueryType()).isEqualTo(TermQueryBuilder.class);
    }

    @Test
    public void should_Get_Document_Path_From_Criterion() {
        CriteriaMapping mapping = AnnotatedCriteriaMapper.getMapping(ComplexAnnotatedCriteria.class);

        String documentPath1 = mapping.getDocumentPath("criteria1", false);
        assertThat(documentPath1).isEqualTo("field1");

        String documentPathWithVirtual = mapping.getDocumentPath("criteria3", false);
        assertThat(documentPathWithVirtual).isEqualTo("field2.field4.subField");

        String documentPathWithoutVirtual = mapping.getDocumentPath("criteria3", true);
        assertThat(documentPathWithoutVirtual).isEqualTo("field2.field4");
    }

    @Test
    public void should_Use_JsonProperty_Annotation() {
        CriteriaMapping mapping = AnnotatedCriteriaMapper.getMapping(ComplexAnnotatedCriteria.class);

        /*
         * Should find actual JSON name by direct Field annotation
         */
        String documentPath2 = mapping.getDocumentPath("criteria5", false);
        assertThat(documentPath2).isEqualTo("field2.schema:name");

        /*
         * Should find actual JSON name by in-direct getter annotation on a parent Interface
         */
        String documentPath = mapping.getDocumentPath("criteria4", false);
        assertThat(documentPath).isEqualTo("field2.schema:identifier");
    }


    @Test
    public void should_Enable_Criteria_Set_Value() throws InvocationTargetException, IllegalAccessException {
        ComplexAnnotatedCriteria complexAnnotatedCriteria = new ComplexAnnotatedCriteria();
        String value2 = "value2";
        complexAnnotatedCriteria.setCriteria2(value2);

        CriteriaMapping mapping = AnnotatedCriteriaMapper.getMapping(ComplexAnnotatedCriteria.class);

        List<String> fieldPath = ImmutableList.of("field2", "field3");
        CriteriaMappingCriterion criterion = mapping.getMapping(CriteriaMappingCriterion.class, fieldPath);
        assertThat(criterion).isNotNull();
        assertThat(criterion.getValue(complexAnnotatedCriteria)).isEqualTo(value2);
        criterion.setValue(complexAnnotatedCriteria, null);
        assertThat(criterion.getValue(complexAnnotatedCriteria)).isNull();
    }
}
