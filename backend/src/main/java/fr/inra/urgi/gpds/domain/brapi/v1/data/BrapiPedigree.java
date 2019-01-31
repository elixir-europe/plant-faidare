package fr.inra.urgi.gpds.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;

import java.util.List;

/**
 * @author cpommier, mbuy
 */
public interface BrapiPedigree {

    @JsonView(JSONView.BrapiFields.class)
    String getPedigree();

    @JsonView(JSONView.BrapiFields.class)
    String getCrossingPlan();

    @JsonView(JSONView.BrapiFields.class)
    String getCrossingYear();

    @JsonView(JSONView.BrapiFields.class)
    String getFamilyCode();

    @JsonView(JSONView.BrapiFields.class)
    String getParent1DbId();

    @JsonView(JSONView.BrapiFields.class)
    String getParent1Name();

    @JsonView(JSONView.BrapiFields.class)
    String getParent1Type();

    @JsonView(JSONView.BrapiFields.class)
    String getParent2DbId();

    @JsonView(JSONView.BrapiFields.class)
    String getParent2Name();

    @JsonView(JSONView.BrapiFields.class)
    String getParent2Type();

    @JsonView(JSONView.BrapiFields.class)
    String getGermplasmDbId();

    @JsonView(JSONView.BrapiFields.class)
    String getDefaultDisplayName();

    @JsonView(JSONView.BrapiFields.class)
    List<BrapiSibling> getSiblings();


}
