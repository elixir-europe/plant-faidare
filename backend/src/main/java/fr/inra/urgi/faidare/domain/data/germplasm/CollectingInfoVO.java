package fr.inra.urgi.faidare.domain.data.germplasm;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiGermplasmCollectingInfo;
import fr.inra.urgi.faidare.domain.data.germplasm.InstituteVO;

import java.io.Serializable;
import java.util.List;

public class CollectingInfoVO implements BrapiGermplasmCollectingInfo, ExtendedCollectingInfo, Serializable {

    @JsonDeserialize(contentAs = InstituteVO.class)
    private List<InstituteVO> collectingInstitutes;

    private String collectingMissionIdentifier;
    private String collectingNumber;
    private String materialType;
    private String collectors;

    @JsonDeserialize(as = CollectingSiteVO.class)
    private CollectingSiteVO collectingSite;

    @Override
    public List<InstituteVO> getCollectingInstitutes() {
        return collectingInstitutes;
    }

    public void setCollectingInstitutes(List<InstituteVO> collectingInstitutes) {
        this.collectingInstitutes = collectingInstitutes;
    }

    @Override
    public String getCollectingMissionIdentifier() {
        return collectingMissionIdentifier;
    }

    public void setCollectingMissionIdentifier(String collectingMissionIdentifier) {
        this.collectingMissionIdentifier = collectingMissionIdentifier;
    }

    @Override
    public String getCollectingNumber() {
        return collectingNumber;
    }

    public void setCollectingNumber(String collectingNumber) {
        this.collectingNumber = collectingNumber;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getCollectors() {
        return collectors;
    }

    public void setCollectors(String collectors) {
        this.collectors = collectors;
    }

    @Override
    public CollectingSiteVO getCollectingSite() {
        return collectingSite;
    }

    public void setCollectingSite(CollectingSiteVO collectingSite) {
        this.collectingSite = collectingSite;
    }


}
