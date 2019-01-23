package fr.inra.urgi.gpds.domain.brapi.v1;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiAdditionalInfo;
import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiTrial;
import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiTrialStudy;
import fr.inra.urgi.gpds.domain.criteria.ProgramCriteria;
import fr.inra.urgi.gpds.domain.data.impl.*;
import fr.inra.urgi.gpds.domain.data.impl.germplasm.GermplasmVO;
import fr.inra.urgi.gpds.domain.data.impl.variable.OntologyVO;
import fr.inra.urgi.gpds.elasticsearch.query.impl.ESGenericQueryFactoryTest;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test mapping of Brapi data interfaces to JSON
 *
 * @author gcornut
 */
class BrapiMappingTest {

    private static ObjectMapper mapper;

    private static String additionalInfoExample;
    private static String trialExample;
    private static String programCriteriaExample;

    @BeforeAll
    static void beforeClass() {
        mapper = new ObjectMapper();

        // Making sure we always serialize json fields in the same order for
        // test purpose
        mapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        additionalInfoExample = "{" +
            "  \"prop2\":[\"value2\",\"value3\"]," +
            "  \"prop1\":\"value1\"" +
            "}";

        trialExample = "{" +
            "  \"additionalInfo\":{" +
            "    \"publications\":[\"pubMedId\",\"doi\"]" +
            "  }," +
            "  \"datasetAuthorship\":{" +
            "    \"datasetPUI\":\"datasetPUI\"," +
            "    \"license\":\"license\"" +
            "  }," +
            "  \"studies\":[{" +
            "     \"studyDbId\":\"study1\",\"studyName\":\"Study 1\"" +
            "   },{" +
            "     \"studyDbId\":\"study2\",\"studyName\":\"Study 2\"" +
            "   }]," +
            "  \"trialDbId\":\"trial\"," +
            "  \"trialName\":\"Trial\"" +
            "}";

        programCriteriaExample = "{\"page\":0,\"pageSize\":100,\"programDbId\":\"A\",\"name\":\"B\"}";
    }

    @Test
    void should_Serialize_Additional_Info() throws IOException {
        BrapiAdditionalInfo info = new BrapiAdditionalInfo();
        info.addProperty("prop1", "value1");
        info.addProperty("prop2", Arrays.asList("value2", "value3"));

        String expectedJson = additionalInfoExample;
        String actualJson = mapper.writeValueAsString(info);
        ESGenericQueryFactoryTest.assertJsonEquals(actualJson, expectedJson);
    }

    @Test
    void should_Deserialize_Additional_Info() throws IOException {
        String json = additionalInfoExample;
        BrapiAdditionalInfo info = mapper.readValue(json, BrapiAdditionalInfo.class);

        assertThat(info).isNotNull();
        assertThat(info.getProperties()).isNotNull().isNotEmpty();
        assertThat(info.getProperties().get("prop1")).isNotNull().isEqualTo("value1");
        assertThat(info.getProperties().get("prop2")).isNotNull().isInstanceOf(List.class);
        assertThat((List<String>) info.getProperties().get("prop2")).isNotEmpty().contains("value2", "value3");
    }

    /**
     * {@link BrapiTrial} is a good example of a complex Brapi object. It is an
     * interface (with separate concrete implementation) containing dynamically
     * mapped field (additional info), an interface field
     * (trial.datasetAuthorship) and a list of interfaces (trial.contacts,
     * trial.studies).
     */
    @Test
    void should_Serialize_Complex_Brapi_Object() throws IOException {
        TrialDatasetAuthorshipVO authorship = new TrialDatasetAuthorshipVO();
        authorship.setLicense("license");
        authorship.setDatasetPUI("datasetPUI");

        BrapiAdditionalInfo additionalInfo = new BrapiAdditionalInfo();
        additionalInfo.addProperty("publications", Arrays.asList("pubMedId", "doi"));

        TrialStudySummaryVO study1 = new TrialStudySummaryVO();
        study1.setStudyDbId("study1");
        study1.setStudyName("Study 1");

        TrialStudySummaryVO study2 = new TrialStudySummaryVO();
        study2.setStudyDbId("study2");
        study2.setStudyName("Study 2");

        List<BrapiTrialStudy> studies = Arrays.asList(
            ((BrapiTrialStudy) study1),
            ((BrapiTrialStudy) study2)
        );

        TrialVO trial = new TrialVO();
        trial.setTrialDbId("trial");
        trial.setTrialName("Trial");
        trial.setDatasetAuthorship(authorship);
        trial.setStudies(studies);
        trial.setAdditionalInfo(additionalInfo);

        String expectedJson = trialExample;
        String actualJson = mapper.writeValueAsString(trial);
        ESGenericQueryFactoryTest.assertJsonEquals(actualJson, expectedJson);
    }

    @Test
    void should_Deserialize_Complex_Brapi_Object() throws IOException {
        BrapiTrial trial = mapper.readValue(trialExample, TrialVO.class);

        assertThat(trial).isNotNull();
        assertThat(trial.getTrialDbId()).isEqualTo("trial");
        assertThat(trial.getTrialName()).isEqualTo("Trial");

        assertThat(trial.getDatasetAuthorship()).isNotNull();
        assertThat(trial.getDatasetAuthorship().getLicense()).isEqualTo("license");
        assertThat(trial.getDatasetAuthorship().getDatasetPUI()).isEqualTo("datasetPUI");

        assertThat(trial.getStudies()).isNotNull().isNotEmpty();
        assertThat(trial.getStudies()).are(new Condition<BrapiTrialStudy>() {
            @Override
            public boolean matches(BrapiTrialStudy value) {
                String id = value.getStudyDbId();
                String name = value.getStudyName();

                return ("study1".equals(id) && "Study 1".equals(name)) ||
                    ("study2".equals(id) && "Study 2".equals(name));
            }
        });

        assertThat(trial.getAdditionalInfo()).isNotNull();
        Map<String, Object> properties = trial.getAdditionalInfo().getProperties();
        assertThat(properties)
            .isNotNull().isNotEmpty();
        Object publications = properties.get("publications");
        assertThat(publications)
            .isNotNull()
            .isInstanceOf(List.class);
        assertThat((List<String>) publications)
            .isNotNull()
            .contains("pubMedId", "doi");
    }

    @Test
    void should_Serialize_Program_Criteria() throws IOException {
        ProgramCriteria criteria = new ProgramCriteria();
        criteria.setProgramDbId("A");
        criteria.setName("B");

        String expectedJson = programCriteriaExample;
        String actualJson = mapper.writeValueAsString(criteria);
        ESGenericQueryFactoryTest.assertJsonEquals(actualJson, expectedJson);
    }

    /**
     * Some BrAPI fields should never be null so they must be serialized as empty strings...
     */
    @Test
    void should_Serialize_Some_Empty_Fields_As_Empty_Strings() throws IOException {
        TrialVO trialVO = new TrialVO();

        String actualJson = mapper.writeValueAsString(trialVO);
        String expectedJson = "{\"datasetAuthorship\":{\"license\":\"\",\"datasetPUI\":\"\"}}";
        ESGenericQueryFactoryTest.assertJsonEquals(actualJson, expectedJson);

        ProgramVO programVO = new ProgramVO();

        actualJson = mapper.writeValueAsString(programVO);
        expectedJson = "{\"objective\":\"\",\"leadPerson\":\"\"}";
        ESGenericQueryFactoryTest.assertJsonEquals(actualJson, expectedJson);

        ObservationUnitVO observationUnitVO = new ObservationUnitVO();

        actualJson = mapper.writeValueAsString(observationUnitVO);
        expectedJson = "{\"entryType\":\"\",\"entryNumber\":\"\"}";
        ESGenericQueryFactoryTest.assertJsonEquals(actualJson, expectedJson);

        ObservationUnitXRefVO observationUnitXRefVO = new ObservationUnitXRefVO();

        actualJson = mapper.writeValueAsString(observationUnitXRefVO);
        expectedJson = "{\"id\":\"\"}";
        ESGenericQueryFactoryTest.assertJsonEquals(actualJson, expectedJson);

        GermplasmVO germplasmVO = new GermplasmVO();

        actualJson = mapper.writeValueAsString(germplasmVO);
        expectedJson = "{\"instituteCode\":\"\"}";
        ESGenericQueryFactoryTest.assertJsonEquals(actualJson, expectedJson);

        OntologyVO ontologyVO = new OntologyVO();

        actualJson = mapper.writeValueAsString(ontologyVO);
        expectedJson = "{\"authors\":\"\",\"licence\":\"\",\"copyright\":\"\",\"version\":\"\"}";
        ESGenericQueryFactoryTest.assertJsonEquals(actualJson, expectedJson);
    }
}
