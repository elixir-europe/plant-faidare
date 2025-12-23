package fr.inrae.urgi.faidare.web.trial;

import fr.inrae.urgi.faidare.domain.brapi.v2.TrialV2VO;
import fr.inrae.urgi.faidare.web.observationunit.ObservationUnitExportJobDTO;

/**
 * The model for the trial export page
 * @author JB Nizet
 */
public record TrialExportModel(
    String contextPath,
    TrialV2VO trial,
    ObservationUnitExportJobDTO job
) {}
