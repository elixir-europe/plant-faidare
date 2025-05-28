package fr.inrae.urgi.faidare.web.germplasm;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import jakarta.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Command sent to export a list of germplasm MCPD IDs
 * @author JB Nizet
 */
public class GermplasmMcpdExportCommand {

    @NotEmpty
    private final Set<String> ids;

    /**
     * The ordered list of fields to export. If empty, all fields are exported
     */
    private final List<GermplasmMcpdExportableField> fields;

    @JsonCreator
    public GermplasmMcpdExportCommand(@JsonProperty("ids") Set<String> ids,
                                      @JsonProperty("fields") List<GermplasmMcpdExportableField> fields) {
        this.ids = ids;
        this.fields = fields == null ? Collections.emptyList() : fields;
    }

    public Set<String> getIds() {
        return ids;
    }

    public List<GermplasmMcpdExportableField> getFields() {
        return fields;
    }
}
