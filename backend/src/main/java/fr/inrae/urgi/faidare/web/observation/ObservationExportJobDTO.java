package fr.inrae.urgi.faidare.web.observation;

import fr.inrae.urgi.faidare.web.observation.ObservationExportJob.Status;

/**
 * DTO for {@link ObservationExportJob}
 * @author JB Nizet
 */
public record ObservationExportJobDTO(String id, Status status) {
    public ObservationExportJobDTO(ObservationExportJob job) {
        this(job.getId(), job.getStatus());
    }
}
