package fr.inrae.urgi.faidare.web.observation;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.List;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.ObservationLevelVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.ObservationUnitPositionVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.ObservationUnitV2VO;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.ObservationVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.SeasonVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.TreatmentVO;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import tools.jackson.databind.ObjectMapper;

/**
 * Unit tests for {@link ObservationExportService}
 * @author JB Nizet
 */
class ObservationExportServiceTest {
    @Test
    void shouldGenerateCorrectColumnsAndRows() throws IOException, CsvException {
        ObservationExportService service = new ObservationExportService(new ObjectMapper());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        ExportedObservationUnit unit1 = new ExportedObservationUnit(
            createObservationUnit(1),
            List.of(
                createObservation("2015", 1.0F, 1), // row 1
                createObservation("2015", 1.0F, 2), // row 1 (same year, same gdd, different variable)
                createObservation("2015", 1.0F, 3), // row 1 (same year, same gdd different variable)
                createObservation("2015", 1.0F, 1), // row 2 (same year, same gdd, same variable)
                createObservation("2015", 2.0F, 1), // row 3 (same year, different gdd, same variable)
                createObservation("2016", 2.0F, 1), // row 4 (different year, different gdd)
                createObservation("2016", 2.0F, 2) // row 4  (different year, different gdd, different variable)
            )
        );
        ExportedObservationUnit unit2 = new ExportedObservationUnit(
            createObservationUnit(2),
            List.of(
                createObservation("2015", 1.0F, 1), // row 5
                createObservation("2015", 1.0F, 2)  // row 5 (same year, same gdd)
            )
        );

        service.exportAsCsv(baos, List.of(unit1, unit2));

        String result = new String(baos.toByteArray(), StandardCharsets.UTF_8);

        CSVReader csvReader = new CSVReaderBuilder(new StringReader(result)).withCSVParser(
            new CSVParserBuilder().
                withSeparator(';')
                .withQuoteChar('"')
                .withEscapeChar('\\')
                .build()
        ).build();
        List<String[]> lines = csvReader.readAll();

        assertThat(lines).hasSize(6);
        String[] header = lines.getFirst();
        assertThat(header).containsExactly(
            "obsUnitId",
            "observationUnitName",
            "obsUnitType",
            "germplasmId",
            "germplasmName",
            "genus",
            "trialName",
            "studyId",
            "studyName",
            "siteName",
            "factor1",
            "factor2",
            "season",
            "gdd",
            "Variable 1(variable1)",
            "Variable 1(variable1)_date",
            "Variable 2(variable2)",
            "Variable 2(variable2)_date",
            "Variable 3(variable3)",
            "Variable 3(variable3)_date"
        );
        assertThat(lines.get(1)).containsExactly(
            "unit1",
            "Unit1",
            "levelCode",
            "germplasm1",
            "Germplasm 1",
            "Germplasm Genus 1",
            "Trial 1",
            "study1",
            "Study 1",
            "Location 1",
            "modality1",
            "modality2",
            "2015",
            "1.0",
            "v1",
            "2015-01-01T01:00:00Z",
            "v2",
            "2015-01-01T02:00:00Z",
            "v3",
            "2015-01-01T03:00:00Z"
        );
        assertThat(lines.get(2)).containsExactly(
            "unit1",
            "Unit1",
            "levelCode",
            "germplasm1",
            "Germplasm 1",
            "Germplasm Genus 1",
            "Trial 1",
            "study1",
            "Study 1",
            "Location 1",
            "modality1",
            "modality2",
            "2015",
            "1.0",
            "v1",
            "2015-01-01T01:00:00Z",
            "",
            "",
            "",
            ""
        );
        assertThat(lines.get(3)).containsExactly(
            "unit1",
            "Unit1",
            "levelCode",
            "germplasm1",
            "Germplasm 1",
            "Germplasm Genus 1",
            "Trial 1",
            "study1",
            "Study 1",
            "Location 1",
            "modality1",
            "modality2",
            "2015",
            "2.0",
            "v1",
            "2015-01-01T01:00:00Z",
            "",
            "",
            "",
            ""
        );
        assertThat(lines.get(4)).containsExactly(
            "unit1",
            "Unit1",
            "levelCode",
            "germplasm1",
            "Germplasm 1",
            "Germplasm Genus 1",
            "Trial 1",
            "study1",
            "Study 1",
            "Location 1",
            "modality1",
            "modality2",
            "2016",
            "2.0",
            "v1",
            "2016-01-01T01:00:00Z",
            "v2",
            "2016-01-01T02:00:00Z",
            "",
            ""
        );
        assertThat(lines.get(5)).containsExactly(
            "unit2",
            "Unit2",
            "levelCode",
            "germplasm2",
            "Germplasm 2",
            "Germplasm Genus 2",
            "Trial 2",
            "study2",
            "Study 2",
            "Location 2",
            "modality1",
            "modality2",
            "2015",
            "1.0",
            "v1",
            "2015-01-01T01:00:00Z",
            "v2",
            "2015-01-01T02:00:00Z",
            "",
            ""
        );
    }

    @Test
    void shouldGenerateCorrectJsonStructure() throws JSONException {
        ObservationExportService service = new ObservationExportService(new ObjectMapper());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        ExportedObservationUnit unit1 = new ExportedObservationUnit(
            createObservationUnit(1),
            List.of(
                createObservation("2015", 1.0F, 1),
                createObservation("2015", 1.0F, 2),
                createObservation("2015", 1.0F, 3),
                createObservation("2015", 1.0F, 1),
                createObservation("2015", 2.0F, 1),
                createObservation("2016", 2.0F, 1),
                createObservation("2016", 2.0F, 2)
            )
        );
        ExportedObservationUnit unit2 = new ExportedObservationUnit(
            createObservationUnit(2),
            List.of(
                createObservation("2015", 1.0F, 1),
                createObservation("2015", 1.0F, 2)
            )
        );

        service.exportAsJson(baos, List.of(unit1, unit2));

        String result = new String(baos.toByteArray(), StandardCharsets.UTF_8);
        System.out.println(result);

        String expectedJson =
            // language=json
            """
            {
              "headerRow": [
                "observationUnitDbId",
                "observationUnitName",
                "observationLevel",
                "germplasmDbId",
                "germplasmName",
                "germplasmGenus",
                "trialName",
                "studyDbId",
                "studyName",
                "studyLocation",
                "factor1",
                "factor2",
                "year",
                "gdd",
                "observationTimeStamp"
              ],
              "observationVariables": [
                {
                  "observationVariableDbId": "variable1",
                  "observationVariableName": "Variable 1"
                },
                {
                  "observationVariableDbId": "variable2",
                  "observationVariableName": "Variable 2"
                },
                {
                  "observationVariableDbId": "variable3",
                  "observationVariableName": "Variable 3"
                }
              ],
              "data": [
                [
                  "unit1",
                  "Unit1",
                  "levelCode",
                  "germplasm1",
                  "Germplasm 1",
                  "Germplasm Genus 1",
                  "Trial 1",
                  "study1",
                  "Study 1",
                  "Location 1",
                  "modality1",
                  "modality2",
                  "2015",
                  "1.0",
                  "2015-01-01T01:00:00Z",
                  "v1",
                  null,
                  null
                ],
                [
                  "unit1",
                  "Unit1",
                  "levelCode",
                  "germplasm1",
                  "Germplasm 1",
                  "Germplasm Genus 1",
                  "Trial 1",
                  "study1",
                  "Study 1",
                  "Location 1",
                  "modality1",
                  "modality2",
                  "2015",
                  "1.0",
                  "2015-01-01T01:00:00Z",
                  "v1",
                  null,
                  null
                ],
                [
                  "unit1",
                  "Unit1",
                  "levelCode",
                  "germplasm1",
                  "Germplasm 1",
                  "Germplasm Genus 1",
                  "Trial 1",
                  "study1",
                  "Study 1",
                  "Location 1",
                  "modality1",
                  "modality2",
                  "2015",
                  "2.0",
                  "2015-01-01T01:00:00Z",
                  "v1",
                  null,
                  null
                ],
                [
                  "unit1",
                  "Unit1",
                  "levelCode",
                  "germplasm1",
                  "Germplasm 1",
                  "Germplasm Genus 1",
                  "Trial 1",
                  "study1",
                  "Study 1",
                  "Location 1",
                  "modality1",
                  "modality2",
                  "2015",
                  "1.0",
                  "2015-01-01T02:00:00Z",
                  null,
                  "v2",
                  null
                ],
                [
                  "unit1",
                  "Unit1",
                  "levelCode",
                  "germplasm1",
                  "Germplasm 1",
                  "Germplasm Genus 1",
                  "Trial 1",
                  "study1",
                  "Study 1",
                  "Location 1",
                  "modality1",
                  "modality2",
                  "2015",
                  "1.0",
                  "2015-01-01T03:00:00Z",
                  null,
                  null,
                  "v3"
                ],
                [
                  "unit1",
                  "Unit1",
                  "levelCode",
                  "germplasm1",
                  "Germplasm 1",
                  "Germplasm Genus 1",
                  "Trial 1",
                  "study1",
                  "Study 1",
                  "Location 1",
                  "modality1",
                  "modality2",
                  "2016",
                  "2.0",
                  "2016-01-01T01:00:00Z",
                  "v1",
                  null,
                  null
                ],
                [
                  "unit1",
                  "Unit1",
                  "levelCode",
                  "germplasm1",
                  "Germplasm 1",
                  "Germplasm Genus 1",
                  "Trial 1",
                  "study1",
                  "Study 1",
                  "Location 1",
                  "modality1",
                  "modality2",
                  "2016",
                  "2.0",
                  "2016-01-01T02:00:00Z",
                  null,
                  "v2",
                  null
                ],
                [
                  "unit2",
                  "Unit2",
                  "levelCode",
                  "germplasm2",
                  "Germplasm 2",
                  "Germplasm Genus 2",
                  "Trial 2",
                  "study2",
                  "Study 2",
                  "Location 2",
                  "modality1",
                  "modality2",
                  "2015",
                  "1.0",
                  "2015-01-01T01:00:00Z",
                  "v1",
                  null,
                  null
                ],
                [
                  "unit2",
                  "Unit2",
                  "levelCode",
                  "germplasm2",
                  "Germplasm 2",
                  "Germplasm Genus 2",
                  "Trial 2",
                  "study2",
                  "Study 2",
                  "Location 2",
                  "modality1",
                  "modality2",
                  "2015",
                  "1.0",
                  "2015-01-01T02:00:00Z",
                  null,
                  "v2",
                  null
                ]
              ]
            }
            """;
        JSONAssert.assertEquals(expectedJson, result, true);
    }

    private ObservationUnitV2VO createObservationUnit(int index) {
        ObservationUnitV2VO vo = new ObservationUnitV2VO();
        vo.setObservationUnitDbId(encode("unit" + index));
        vo.setObservationUnitName("Unit" + index);
        vo.setObservationUnitPosition(createObservationUnitPosition());
        vo.setGermplasmDbId(encode("germplasm" + index));
        vo.setGermplasmName("Germplasm " + index);
        vo.setGermplasmGenus("Germplasm Genus " + index);
        vo.setTrialDbId("trial" + index);
        vo.setTrialName("Trial " + index);
        vo.setStudyDbId(encode("study" + index));
        vo.setStudyName("Study " + index);
        vo.setStudyLocation("Location " + index);
        vo.setTreatments(List.of(createTreatment(1), createTreatment(2)));
        return vo;
    }

    private ObservationVO createObservation(String year, Float gdd, int index) {
        ObservationVO vo = new ObservationVO();
        vo.setObservationVariableDbId("variable" + index);
        vo.setObservationVariableName("Variable " + index);
        vo.setValue("v" + index);
        vo.setObservationTimeStamp("2025-12-03T13:00:00Z");
        vo.setObservationUnitDbId("unit1");
        SeasonVO season = new SeasonVO();
        season.setSeasonName(year);
        vo.setSeason(season);
        vo.setObservationTimeStamp(Instant.parse(year + "-01-01T0" + index + ":00:00Z").toString());
        vo.setGdd(gdd);
        return vo;
    }

    private TreatmentVO createTreatment(int index) {
        TreatmentVO vo = new TreatmentVO();
        vo.setFactor("factor" + index);
        vo.setModality("modality" + index);
        return vo;
    }

    private ObservationUnitPositionVO createObservationUnitPosition() {
        ObservationUnitPositionVO vo = new ObservationUnitPositionVO();
        ObservationLevelVO observationLevel = new ObservationLevelVO();
        observationLevel.setLevelName("levelName");
        observationLevel.setLevelOrder("levelCode");
        vo.setObservationLevel(observationLevel);
        return vo;
    }

    private String encode(String value) {
        if (value == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }
}
