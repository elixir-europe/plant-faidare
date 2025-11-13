package fr.inrae.urgi.faidare.web.germplasm;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Command sent to export a list of germplasm IDs in the MIAPPE format
 * @author JB Nizet
 */
public record GermplasmMiappeExportCommand(
    @NotEmpty Set<String> ids,
    @NotNull ExportFormat format
) {
}
