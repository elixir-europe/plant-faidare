package fr.inra.urgi.faidare.web.germplasm;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Command sent to export a list of germplasm IDs
 * @author JB Nizet
 */
public class GermplasmExportCommand {

    @NotEmpty
    private final Set<String> ids;

    /**
     * The ordered list of fields to export. If empty, all fields are exported
     */
    private final List<GermplasmExportableField> fields;

    @JsonCreator
    public GermplasmExportCommand(@JsonProperty("ids") Set<String> ids,
                                  @JsonProperty("fields") List<GermplasmExportableField> fields) {
        this.ids = ids;
        this.fields = fields == null ? Collections.emptyList() : fields;
    }

    public Set<String> getIds() {
        return ids;
    }

    public List<GermplasmExportableField> getFields() {
        return fields;
    }
}
