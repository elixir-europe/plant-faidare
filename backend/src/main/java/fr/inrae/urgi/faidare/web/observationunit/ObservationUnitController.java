package fr.inrae.urgi.faidare.web.observationunit;

import java.io.IOException;

import fr.inrae.urgi.faidare.api.NotFoundException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller allowing to export observation units
 * @author JB Nizet
 */
@RestController
@RequestMapping({"/observation-units"})
public class ObservationUnitController {

    private final ObservationUnitExportJobService jobService;

    public ObservationUnitController(ObservationUnitExportJobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/exports")
    @ResponseStatus(HttpStatus.CREATED)
    public ObservationUnitExportJobDTO export(@Validated @RequestBody ObservationUnitExportCommand command) {
        ObservationUnitExportJob exportJob = jobService.createExportJob(command);
        return new ObservationUnitExportJobDTO(exportJob);
    }

    @GetMapping("/exports/{jobId}")
    public ObservationUnitExportJobDTO getJob(@PathVariable String jobId) {
        ObservationUnitExportJob exportJob = jobService.getJob(jobId).orElseThrow(() -> new NotFoundException("No export job with ID " + jobId));
        return new ObservationUnitExportJobDTO(exportJob);
    }

    @GetMapping("/exports/{jobId}/content")
    public ResponseEntity<Resource> getExportResult(@PathVariable String jobId) throws IOException {
        ObservationUnitExportJob exportJob = jobService.getJob(jobId).orElseThrow(() -> new NotFoundException("No export job with ID " + jobId));
        if (exportJob.getStatus() != ObservationUnitExportJob.Status.DONE) {
            throw new NotFoundException("Export job with ID " + jobId + " is not done");
        }
        return ResponseEntity.ok()
            .contentType(exportJob.getFormat().getMediaType())
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                ContentDisposition.builder("attachment")
                                  .filename(exportJob.getFile().getFileName().toString())
                                  .build()
                                  .toString())
            .body(new FileSystemResource(exportJob.getFile()));
    }
}
