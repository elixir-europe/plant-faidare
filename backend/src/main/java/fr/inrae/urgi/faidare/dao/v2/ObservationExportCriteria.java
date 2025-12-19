package fr.inrae.urgi.faidare.dao.v2;

import java.util.Set;

/**
 * Criteria used when exporting observation units and their observations
 * @author JB Nizet
 *
 * @param trialDbId (mandatory) the observations must have the given trialDbId
 *                  (<code>ObservationVO.trialDbId</code>)
 * @param studyLocations if not empty, then the observations must have one of the provided study locations
 *                       (<code>ObservationVO.studyLocation</code>)
 * @param seasonNames if not empty, then the observations must have one of the provided season names
 *                    (<code>ObservationVO.season.seasonName</code>)
 * @param observationVariableNames if not empty, then the observations must have one of the provided observation variable names
 *                                 (<code>ObservationVO.observationVariableName</code>)
 */
public record ObservationExportCriteria(
    String trialDbId,
    Set<String> studyLocations,
    Set<String> seasonNames,
    Set<String> observationVariableNames
) {
}
