package fr.inrae.urgi.faidare.web.trial;

import java.util.List;

/**
 * The criteria that can be chosen in order to do an export
 * @author JB Nizet
 */
public record TrialChoosableExportCriteria(
    List<String> observationLevelCodes,
    List<String> seasonNames,
    List<String> studyLocations,
    List<String> obervationVariableNames
) {
}
