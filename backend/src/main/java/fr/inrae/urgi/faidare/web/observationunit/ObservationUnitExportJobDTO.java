package fr.inrae.urgi.faidare.web.observationunit;

import fr.inrae.urgi.faidare.web.observationunit.ObservationUnitExportJob.Status;

/**
 * DTO for {@link ObservationUnitExportJob}
 * @author JB Nizet
 */
public record ObservationUnitExportJobDTO(String id, Status status) {
    public ObservationUnitExportJobDTO(ObservationUnitExportJob job) {
        this(job.getId(), job.getStatus());
    }
}
