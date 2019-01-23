package fr.inra.urgi.gpds.domain.data.impl.germplasm;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiPedigree;
import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiSibling;
import fr.inra.urgi.gpds.elasticsearch.document.annotation.Document;

import java.io.Serializable;
import java.util.List;

/**
 * Model taken from the breeding API Germplasm MCPD call
 *
 * @author cpommier, mbuy
 */
@Document(type = "germplasmPedigree")
public class PedigreeVO implements Serializable, BrapiPedigree {

    private static final long serialVersionUID = -2037667912012821133L;
    private String germplasmDbId;
    private String defaultDisplayName;
    private String pedigree;
    private Long groupId;
    private String crossingPlan;
    private String crossingYear;
    private String familyCode;
    private String parent1DbId;
    private String parent1Name;
    private String parent1Type;
    private String parent2DbId;
    private String parent2Name;
    private String parent2Type;

    @JsonDeserialize(contentAs = SiblingVO.class)
    private List<BrapiSibling> siblings;

    @Override
    public String getPedigree() {
        return pedigree;
    }

    public void setPedigree(String pedigree) {
        this.pedigree = pedigree;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @Override
    public String getCrossingPlan() {
        return crossingPlan;
    }

    public void setCrossingPlan(String crossingPlan) {
        this.crossingPlan = crossingPlan;
    }

    @Override
    public String getCrossingYear() {
        return crossingYear;
    }

    public void setCrossingYear(String crossingYear) {
        this.crossingYear = crossingYear;
    }

    @Override
    public String getFamilyCode() {
        return familyCode;
    }

    public void setFamilyCode(String familyCode) {
        this.familyCode = familyCode;
    }

    @Override
    public String getParent1DbId() {
        return parent1DbId;
    }

    public void setParent1DbId(String parent1DbId) {
        this.parent1DbId = parent1DbId;
    }

    @Override
    public String getParent1Name() {
        return parent1Name;
    }

    public void setParent1Name(String parent1Name) {
        this.parent1Name = parent1Name;
    }

    @Override
    public String getParent1Type() {
        return parent1Type;
    }

    public void setParent1Type(String parent1Type) {
        this.parent1Type = parent1Type;
    }

    @Override
    public String getParent2DbId() {
        return parent2DbId;
    }

    public void setParent2DbId(String parent2DbId) {
        this.parent2DbId = parent2DbId;
    }

    @Override
    public String getParent2Name() {
        return parent2Name;
    }

    public void setParent2Name(String parent2Name) {
        this.parent2Name = parent2Name;
    }

    @Override
    public String getParent2Type() {
        return parent2Type;
    }

    public void setParent2Type(String parent2Type) {
        this.parent2Type = parent2Type;
    }

    @Override
    public List<BrapiSibling> getSiblings() {
        return siblings;
    }

    public void setSiblings(List<BrapiSibling> siblings) {
        this.siblings = siblings;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public String getGermplasmDbId() {
        return germplasmDbId;
    }

    public void setGermplasmDbId(String germplasmDbId) {
        this.germplasmDbId = germplasmDbId;
    }

    @Override
    public String getDefaultDisplayName() {
        return defaultDisplayName;
    }

    public void setDefaultDisplayName(String defaultDisplayName) {
        this.defaultDisplayName = defaultDisplayName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((germplasmDbId == null) ? 0 : germplasmDbId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PedigreeVO other = (PedigreeVO) obj;
        if (germplasmDbId == null) {
            if (other.germplasmDbId != null)
                return false;
        } else if (!germplasmDbId.equals(other.germplasmDbId))
            return false;

        return true;
    }

}
