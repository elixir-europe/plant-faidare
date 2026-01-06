package fr.inrae.urgi.faidare.web.observationunit;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.file.Files;
import java.nio.file.Path;
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
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.SeasonVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.TreatmentVO;
import fr.inrae.urgi.faidare.web.germplasm.ExportFormat;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Unit tests for {@link ObservationUnitExportJobService}
 * @author JB Nizet
 */
class ObservationUnitExportJobServiceTest {

    @TempDir
    private Path exportDirectory;

    private ObservationUnitV2Dao mockObservationUnitRepository;
    private ObservationV2Dao mockObservationRepository;
    private ObservationUnitExportJobService jobService;

    @BeforeEach
    void prepare() {
        mockObservationUnitRepository = mock(ObservationUnitV2Dao.class);
        mockObservationRepository = mock(ObservationV2Dao.class);
        jobService = new ObservationUnitExportJobService(
            mockObservationUnitRepository,
            mockObservationRepository,
            new ObservationUnitExportProperties(
                exportDirectory
            ),
            new ObservationUnitExportService()
        );
    }

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
                    "levelCode",
                    Set.of("Verviers")
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

        ObservationUnitExportJob exportJob = jobService.createExportJob(command);
        await().atMost(5, SECONDS).until(() -> exportJob.getStatus() != ObservationUnitExportJob.Status.RUNNING);

        assertThat(exportJob.getStatus()).isEqualTo(ObservationUnitExportJob.Status.DONE);

        try (XSSFWorkbook workbook = new XSSFWorkbook(exportJob.getFile().toFile())) {
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
                    "levelCode",
                    Set.of("Verviers")
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

        ObservationUnitExportJob exportJob = jobService.createExportJob(command);
        await().atMost(5, SECONDS).until(() -> exportJob.getStatus() != ObservationUnitExportJob.Status.RUNNING);

        assertThat(exportJob.getStatus()).isEqualTo(ObservationUnitExportJob.Status.DONE);

        String content = Files.readString(exportJob.getFile());
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
        vo.setStudyLocation("Gaillac");
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
        SeasonVO season = new SeasonVO();
        season.setSeasonName("2025");
        vo.setSeason(season);
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
