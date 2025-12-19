package fr.inrae.urgi.faidare.web.observationunit;

import java.util.Set;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import fr.inrae.urgi.faidare.web.germplasm.ExportFormat;

/**
 * Command sent to export observation units and observations
 * @author JB Nizet
 */
public record ObservationUnitExportCommand(
    @NotBlank String trialDbId,
    @NotBlank String observationLevelCode,
    Set<String> studyLocations,
    Set<String> seasonNames,
    Set<String> observationVariableNames,
    @NotNull ExportFormat format
) {
}
