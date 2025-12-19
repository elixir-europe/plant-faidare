package fr.inrae.urgi.faidare.web.observationunit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Set;

import fr.inrae.urgi.faidare.dao.v2.ObservationExportCriteria;
import fr.inrae.urgi.faidare.dao.v2.ObservationUnitExportCriteria;
import fr.inrae.urgi.faidare.dao.v2.ObservationUnitV2Dao;
import fr.inrae.urgi.faidare.dao.v2.ObservationV2Dao;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.ObservationLevelVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.ObservationUnitPositionVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.ObservationUnitV2VO;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.ObservationVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.TreatmentVO;
import fr.inrae.urgi.faidare.web.germplasm.ExportFormat;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import tools.jackson.databind.ObjectMapper;

/**
 * MVC tests for {@link ObservationUnitController}
 * @author JB Nizet
 */
@WebMvcTest(ObservationUnitController.class)
@Import({ObservationUnitExportService.class})
class ObservationUnitControllerTest {

    @Autowired
    private MockMvcTester mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ObservationUnitV2Dao mockObservationUnitRepository;

    @MockitoBean
    private ObservationV2Dao mockObservationRepository;

    @Test
    void shouldExportAsExcel() throws Exception {
        List<ObservationUnitV2VO> units = List.of(
            createObservationUnit()
        );

        List<ObservationVO> observations = List.of(
            createObservation(1),
            createObservation(2)
        );

        ObservationUnitExportCommand command = new ObservationUnitExportCommand(
            "trial1",
            "levelCode",
            Set.of("Verviers"),
            Set.of("2025"),
            Set.of("Variable 1", "Variable 2"),
            ExportFormat.EXCEL
        );

        when(
            mockObservationUnitRepository.findByExportCriteria(
                new ObservationUnitExportCriteria(
                    "trial1",
                    "levelCode"
                )
            )
        ).thenAnswer(invocation -> units.stream());

        when(
            mockObservationRepository.findByExportCriteria(
                new ObservationExportCriteria(
                    "trial1",
                    Set.of("Verviers"),
                    Set.of("2025"),
                    Set.of("Variable 1", "Variable 2")
                )
            )
        ).thenAnswer(invocation -> observations.stream());

        MvcTestResult result = mockMvc
            .post()
            .uri("/observation-units/exports")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(command))
            .exchange();
        assertThat(result).hasStatusOk().hasContentType(ExportFormat.EXCEL.getMediaType());

        byte[] content = result.getResponse().getContentAsByteArray();
        try (XSSFWorkbook workbook = new XSSFWorkbook(new ByteArrayInputStream(content))) {
            assertThat(workbook.getNumberOfSheets()).isEqualTo(1);
            XSSFSheet sheet = workbook.getSheetAt(0);
            assertThat(sheet.getPhysicalNumberOfRows()).isEqualTo(2); // header + 1 unit with 2 variables
        }
    }

    @Test
    void shouldExportAsCsv() throws Exception {
        List<ObservationUnitV2VO> units = List.of(
            createObservationUnit()
        );

        List<ObservationVO> observations = List.of(
            createObservation(1),
            createObservation(2)
        );

        ObservationUnitExportCommand command = new ObservationUnitExportCommand(
            "trial1",
            "levelCode",
            Set.of("Verviers"),
            Set.of("2025"),
            Set.of("Variable 1", "Variable 2"),
            ExportFormat.CSV
        );

        when(
            mockObservationUnitRepository.findByExportCriteria(
                new ObservationUnitExportCriteria(
                    "trial1",
                    "levelCode"
                )
            )
        ).thenAnswer(invocation -> units.stream());

        when(
            mockObservationRepository.findByExportCriteria(
                new ObservationExportCriteria(
                    "trial1",
                    Set.of("Verviers"),
                    Set.of("2025"),
                    Set.of("Variable 1", "Variable 2")
                )
            )
        ).thenAnswer(invocation -> observations.stream());

        MvcTestResult result = mockMvc
            .post()
            .uri("/observation-units/exports")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(command))
            .exchange();
        assertThat(result).hasStatusOk().hasContentType(ExportFormat.CSV.getMediaType());

        String content = result.getResponse().getContentAsString();
        List<String> lines = content.lines().toList();
        assertThat(lines).hasSize(2); // header + 1 unit with 2 variables
    }

    private ObservationUnitV2VO createObservationUnit() {
        ObservationUnitV2VO vo = new ObservationUnitV2VO();
        vo.setTrialDbId("trial1");
        vo.setObservationUnitDbId("unit1");
        vo.setObservationUnitName("Unit1");
        vo.setObservationUnitPosition(createObservationUnitPosition());
        vo.setGermplasmName("Germplasm1");
        vo.setGermplasmGenus("Germplasm Genus 1");
        vo.setTrialName("Trial 1");
        vo.setStudyName("Study 1");
        vo.setTreatments(List.of(createTreatment()));
        return vo;
    }

    private ObservationVO createObservation(int index) {
        ObservationVO vo = new ObservationVO();
        vo.setObservationVariableDbId("variable" + index);
        vo.setObservationVariableName("Variable " + index);
        vo.setValue("OK");
        vo.setObservationTimeStamp("2025-12-03T13:00:00Z");
        vo.setObservationUnitDbId("unit1");
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
