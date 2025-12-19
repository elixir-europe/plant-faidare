package fr.inrae.urgi.faidare.dao.v2;

import java.util.Set;

/**
 * Criteria used when exporting observation units and their observations
 * @author JB Nizet
 *
 * @param trialDbId (mandatory) the observation units must have the given trialDbId (<code>ObservationUnitV2VO.trialDbId</code>)
 * @param observationLevelCode (mandatory) the observation units must have the given observation level code
 *                             (<code>ObservationUnitV2VO.observationUnitPosition.observationLevel.levelCode</code>)
 */
public record ObservationUnitExportCriteria(
    String trialDbId,
    String observationLevelCode
) {
}
