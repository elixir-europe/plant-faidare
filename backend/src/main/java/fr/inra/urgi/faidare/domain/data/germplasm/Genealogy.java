package fr.inra.urgi.faidare.domain.data.germplasm;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;

import java.util.List;

/**
 * @author gcornut
 */
public interface Genealogy {
    @JsonView(JSONView.GnpISFields.class)
    String getCrossingPlan();

    @JsonView(JSONView.GnpISFields.class)
    String getCrossingYear();

    @JsonView(JSONView.GnpISFields.class)
    String getFamilyCode();

    @JsonView(JSONView.GnpISFields.class)
    String getFirstParentName();

    @JsonView(JSONView.GnpISFields.class)
    String getFirstParentPUI();

    @JsonView(JSONView.GnpISFields.class)
    String getFirstParentType();

    @JsonView(JSONView.GnpISFields.class)
    String getSecondParentName();

    @JsonView(JSONView.GnpISFields.class)
    String getSecondParentPUI();

    @JsonView(JSONView.GnpISFields.class)
    String getSecondParentType();

    @JsonView(JSONView.GnpISFields.class)
    List<PuiNameValueVO> getSibblings();
}
