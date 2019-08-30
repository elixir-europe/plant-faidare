package fr.inra.urgi.faidare.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;
import fr.inra.urgi.faidare.domain.data.germplasm.DonorInfoVO;
import fr.inra.urgi.faidare.domain.data.germplasm.InstituteVO;

import java.util.List;

/**
 * @author gcornut
 */
public interface BrapiGermplasmMcpd {

    @JsonView(JSONView.BrapiFields.class)
    String getGermplasmDbId();

    @JsonView(JSONView.BrapiFields.class)
    String getGermplasmPUI();

    @JsonView(JSONView.BrapiFields.class)
    String getAccessionNumber();

    @JsonView(JSONView.BrapiFields.class)
    List<String> getAccessionNames();

    @JsonView(JSONView.BrapiFields.class)
    List<String> getAlternateIDs();

    @JsonView(JSONView.BrapiFields.class)
    String getCropName();

    @JsonView(JSONView.BrapiFields.class)
    String getGenus();

    @JsonView(JSONView.BrapiFields.class)
    String getSpecies();

    @JsonView(JSONView.BrapiFields.class)
    String getSpeciesAuthority();

    @JsonView(JSONView.BrapiFields.class)
    String getSubtaxon();

    @JsonView(JSONView.BrapiFields.class)
    String getSubtaxonAuthority();

    @JsonView(JSONView.BrapiFields.class)
    String getAncestralData();

    @JsonView(JSONView.BrapiFields.class)
    String getBiologicalStatusOfAccessionCode();

    @JsonView(JSONView.BrapiFields.class)
    String getMlsStatus();

    @JsonView(JSONView.BrapiFields.class)
    String getCountryOfOriginCode();

    @JsonView(JSONView.BrapiFields.class)
    String getInstituteCode();

    @JsonView(JSONView.BrapiFields.class)
    BrapiGermplasmCollectingInfo getCollectingInfo();

    @JsonView(JSONView.BrapiFields.class)
    String getAcquisitionDate();

    @JsonView(JSONView.BrapiFields.class)
    String getAcquisitionSourceCode();

    @JsonView(JSONView.BrapiFields.class)
    List<DonorInfoVO> getDonorInfo();

    @JsonView(JSONView.BrapiFields.class)
    List<? extends BrapiGermplasmInstitute> getBreedingInstitutes();

    @JsonView(JSONView.BrapiFields.class)
    List<? extends BrapiGermplasmInstitute> getSafetyDuplicateInstitutes();

    @JsonView(JSONView.BrapiFields.class)
    List<String> getStorageTypeCodes();

}
