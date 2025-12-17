package fr.inrae.urgi.faidare.config;

import java.util.Arrays;

public enum DocumentType {
    GERMPLASM,
    STUDY,
    TRIAL,
    LOCATION,
    CONTACT,
    PROGRAM,
    GERMPLASM_PEDIGREE,
    GERMPLASM_PROGENY,
    GERMPLASM_ATTRIBUTE,
    OBSERVATION_UNIT,
    OBSERVATION,
    OBSERVATION_VARIABLE,
    XREF;


    public String toKebabCase() {
        return name().toLowerCase().replace("_", "-");
    }

    public static DocumentType fromString(String input) {
        String normalized = input
            .replace("-", "_")
            .replaceAll("([a-z])([A-Z])", "$1_$2") // camelCase → snake_case
            .toUpperCase();

        return Arrays.stream(values())
            .filter(v -> v.name().equals(normalized))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unknown document type: " + input));
    }


}
