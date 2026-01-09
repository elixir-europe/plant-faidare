package fr.inrae.urgi.faidare.web.observation.json;

import java.util.List;

/**
 * A record matching the structure of a JSON observation export
 * (see https://brapiphenotyping21.docs.apiary.io/#/reference/observations/get-observations-table/get-observations-table)
 * @author JB Nizet
 */
public record JsonObservationExport(
    List<String> headerRow,
    List<JsonObservationVariable> observationVariables,
    List<List<String>> data
) {
}
