package fr.inrae.urgi.faidare.dao.v2;

import java.util.List;

/**
 * The items that can be chosen when exporting obersvations
 * @author JB Nizet
 */
public record ChoosableObservationExportCriteria(
    List<String> seasonNames,
    List<String> studyLocations,
    List<String> observationVariableNames
) {
}
