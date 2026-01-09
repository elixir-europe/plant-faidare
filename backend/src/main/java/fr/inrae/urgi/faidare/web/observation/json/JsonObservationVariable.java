package fr.inrae.urgi.faidare.web.observation.json;

/**
 * An observation variable used in the `observationVariables` property of
 * {@link JsonObservationExport}
 * @author JB Nizet
 */
public record JsonObservationVariable(
    String observationVariableDbId,
    String observationVariableName
) {
}
