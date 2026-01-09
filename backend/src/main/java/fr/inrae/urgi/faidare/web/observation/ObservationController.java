package fr.inrae.urgi.faidare.web.observation;

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
 * Controller allowing to export observations
 * @author JB Nizet
 */
@RestController
@RequestMapping({"/observations"})
public class ObservationController {

    private final ObservationExportJobService jobService;

    public ObservationController(ObservationExportJobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/exports")
    @ResponseStatus(HttpStatus.CREATED)
    public ObservationExportJobDTO export(@Validated @RequestBody ObservationExportCommand command) {
        ObservationExportJob exportJob = jobService.createExportJob(command);
        return new ObservationExportJobDTO(exportJob);
    }

    @GetMapping("/exports/{jobId}")
    public ObservationExportJobDTO getJob(@PathVariable String jobId) {
        ObservationExportJob exportJob = jobService.getJob(jobId).orElseThrow(() -> new NotFoundException("No export job with ID " + jobId));
        return new ObservationExportJobDTO(exportJob);
    }

    @GetMapping("/exports/{jobId}/content")
    public ResponseEntity<Resource> getExportResult(@PathVariable String jobId) throws IOException {
        ObservationExportJob exportJob = jobService.getJob(jobId).orElseThrow(() -> new NotFoundException("No export job with ID " + jobId));
        if (exportJob.getStatus() != ObservationExportJob.Status.DONE) {
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
