package fr.inrae.urgi.faidare.web.observation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

import fr.inrae.urgi.faidare.web.germplasm.ExportFormat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import tools.jackson.databind.ObjectMapper;

/**
 * MVC tests for {@link ObservationController}
 * @author JB Nizet
 */
@WebMvcTest(ObservationController.class)
@Import({ObservationExportService.class})
class ObservationControllerTest {

    @Autowired
    private MockMvcTester mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ObservationExportJobService mockJobService;

    @Test
    void shouldCreateExportJob() {

        ObservationExportCommand command = new ObservationExportCommand(
            "trial1",
            "levelCode",
            Set.of("Verviers"),
            Set.of("2025"),
            Set.of("Variable 1", "Variable 2"),
            ExportFormat.EXCEL
        );

        ObservationExportJob job = new ObservationExportJob("job1", ExportFormat.EXCEL, Path.of("/tmp/export.xlsx"));
        when(mockJobService.createExportJob(command)).thenReturn(job);

        MvcTestResult result = mockMvc
            .post()
            .uri("/observations/exports")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(command))
            .exchange();
        assertThat(result)
            .hasStatus(HttpStatus.CREATED)
            .hasContentType(MediaType.APPLICATION_JSON)
            .bodyJson().isEqualTo(
                //language=json
                """
                {
                  "id": "job1",
                  "status": "RUNNING"
                }
              """
            );
    }

    @Test
    void shouldGetExportJob() {
        ObservationExportJob job = new ObservationExportJob("job1", ExportFormat.EXCEL, Path.of("/tmp/export.xlsx"));
        job.done();
        when(mockJobService.getJob(job.getId())).thenReturn(Optional.of(job));

        MvcTestResult result = mockMvc
            .get()
            .uri("/observations/exports/{jobId}", job.getId())
            .exchange();
        assertThat(result)
            .hasStatus(HttpStatus.OK)
            .hasContentType(MediaType.APPLICATION_JSON)
            .bodyJson().isEqualTo(
                //language=json
                """
                {
                  "id": "job1",
                  "status": "DONE"
                }
              """
            );
    }

    @Test
    void shouldGetExportJobContent() throws IOException {
        Path file = Files.createTempFile("foo", ".csv");
        Files.writeString(file, "hello");
        ObservationExportJob job = new ObservationExportJob("job1", ExportFormat.CSV, file);
        job.done();
        when(mockJobService.getJob(job.getId())).thenReturn(Optional.of(job));

        MvcTestResult result = mockMvc
            .get()
            .uri("/observations/exports/{jobId}/content", job.getId())
            .exchange();
        assertThat(result)
            .hasStatus(HttpStatus.OK)
            .hasContentType(ExportFormat.CSV.getMediaType())
            .bodyText().isEqualTo("hello");
        ContentDisposition disposition = ContentDisposition.parse(result.getResponse().getHeader(HttpHeaders.CONTENT_DISPOSITION));
        assertThat(disposition.isAttachment()).isTrue();
        assertThat(disposition.getFilename().endsWith(".csv")).isTrue();
    }
}
