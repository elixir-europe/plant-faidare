package fr.inrae.urgi.faidare.web.observationunit;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
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
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ObservationUnitExportService}
 * @author JB Nizet
 */
class ObservationUnitExportServiceTest {
    @Test
    void shouldGenerateCorrectColumnsAndRows() throws IOException, CsvException {
        ObservationUnitExportService service = new ObservationUnitExportService();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        ExportedObservationUnit unit1 = new ExportedObservationUnit(
            createObservationUnit(1),
            List.of(
                createObservation("2015", 1), // row 1
                createObservation("2015", 2), // row 1 (same year, different variable)
                createObservation("2015", 3), // row 1 (same year, different variable)
                createObservation("2015", 1), // row 2 (same year, same variable)
                createObservation("2016", 1), // row 3 (different year)
                createObservation("2016", 2) // row 3  (different year, different variable)
            )
        );
        ExportedObservationUnit unit2 = new ExportedObservationUnit(
            createObservationUnit(2),
            List.of(
                createObservation("2015", 1), // row 4
                createObservation("2015", 2)  // row 4 (same year, different variable)
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

        assertThat(lines).hasSize(5);
        String[] header = lines.getFirst();
        assertThat(header).containsExactly(
            "Observation Unit ID",
            "Observation Unit Name",
            "Observation Level",
            "Germplasm Name",
            "Germplasm Genus",
            "Trial Name",
            "Study Name",
            "Study Location",
            "Treatments",
            "Season",
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
            "Germplasm1",
            "Germplasm Genus 1",
            "Trial 1",
            "Study 1",
            "Location 1",
            "Modality",
            "2015",
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
            "Germplasm1",
            "Germplasm Genus 1",
            "Trial 1",
            "Study 1",
            "Location 1",
            "Modality",
            "2015",
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
            "Germplasm1",
            "Germplasm Genus 1",
            "Trial 1",
            "Study 1",
            "Location 1",
            "Modality",
            "2016",
            "v1",
            "2016-01-01T01:00:00Z",
            "v2",
            "2016-01-01T02:00:00Z",
            "",
            ""
        );
        assertThat(lines.get(4)).containsExactly(
            "unit2",
            "Unit2",
            "levelCode",
            "Germplasm2",
            "Germplasm Genus 2",
            "Trial 2",
            "Study 2",
            "Location 2",
            "Modality",
            "2015",
            "v1",
            "2015-01-01T01:00:00Z",
            "v2",
            "2015-01-01T02:00:00Z",
            "",
            ""
        );
    }

    private ObservationUnitV2VO createObservationUnit(int index) {
        ObservationUnitV2VO vo = new ObservationUnitV2VO();
        vo.setTrialDbId("trial" + index);
        vo.setObservationUnitDbId("unit" + index);
        vo.setObservationUnitName("Unit" + index);
        vo.setObservationUnitPosition(createObservationUnitPosition());
        vo.setGermplasmName("Germplasm" + index);
        vo.setGermplasmGenus("Germplasm Genus " + index);
        vo.setTrialName("Trial " + index);
        vo.setStudyName("Study " + index);
        vo.setStudyLocation("Location " + index);
        vo.setTreatments(List.of(createTreatment()));
        return vo;
    }

    private ObservationVO createObservation(String year, int index) {
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
        return vo;
    }

    private TreatmentVO createTreatment() {
        TreatmentVO vo = new TreatmentVO();
        vo.setFactor("Factor");
        vo.setModality("Modality");
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
}
