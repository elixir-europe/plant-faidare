package fr.inra.urgi.faidare.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;

import java.util.List;

/**
 * @author gcornut
 */
public interface BrapiGermplasm extends HasBrapiDocumentationURL {

    @JsonView(JSONView.BrapiFields.class)
    String getGermplasmDbId();

    @JsonView(JSONView.BrapiFields.class)
    String getDefaultDisplayName();

    @JsonView(JSONView.BrapiFields.class)
    String getAccessionNumber();

    @JsonView(JSONView.BrapiFields.class)
    String getGermplasmName();

    @JsonView(JSONView.BrapiFields.class)
    String getGermplasmPUI();

    @JsonView(JSONView.BrapiFields.class)
    String getPedigree();

    @JsonView(JSONView.BrapiFields.class)
    String getSeedSource();

    @JsonView(JSONView.BrapiFields.class)
    List<String> getSynonyms();

    @JsonView(JSONView.BrapiFields.class)
    String getCommonCropName();

    @JsonView(JSONView.BrapiFields.class)
    String getInstituteCode();

    @JsonView(JSONView.BrapiFields.class)
    String getInstituteName();

    @JsonView(JSONView.BrapiFields.class)
    String getBiologicalStatusOfAccessionCode();

    @JsonView(JSONView.BrapiFields.class)
    String getCountryOfOriginCode();

    @JsonView(JSONView.BrapiFields.class)
    List<String> getTypeOfGermplasmStorageCode();

    @JsonView(JSONView.BrapiFields.class)
    String getGenus();

    @JsonView(JSONView.BrapiFields.class)
    String getSpecies();

    @JsonView(JSONView.BrapiFields.class)
    String getSpeciesAuthority();

    @JsonView(JSONView.BrapiFields.class)
    String getSubtaxa();

    @JsonView(JSONView.BrapiFields.class)
    String getSubtaxaAuthority();

    @JsonView(JSONView.BrapiFields.class)
    List<? extends BrapiGermplasmDonor> getDonors();

    @JsonView(JSONView.BrapiFields.class)
    String getAcquisitionDate();

    @JsonView(JSONView.BrapiFields.class)
    List<? extends BrapiGermplasmTaxonSource> getTaxonIds();

    @Override
    String getDocumentationURL();
}
