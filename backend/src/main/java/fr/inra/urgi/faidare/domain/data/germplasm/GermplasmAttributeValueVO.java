package fr.inra.urgi.faidare.domain.data.germplasm;

import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiGermplasmAttributeValue;

import java.util.Date;

/**
 * @author gcornut
 */
public class GermplasmAttributeValueVO implements BrapiGermplasmAttributeValue {

    private String attributeDbId;
    private String attributeName;
    private String attributeCode;
    private String value;
    private Date determinedDate;

    @Override
    public String getAttributeDbId() {
        return attributeDbId;
    }

    public void setAttributeDbId(String attributeDbId) {
        this.attributeDbId = attributeDbId;
    }

    @Override
    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    @Override
    public String getAttributeCode() {
        return attributeCode;
    }

    public void setAttributeCode(String attributeCode) {
        this.attributeCode = attributeCode;
    }

    @Override
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public Date getDeterminedDate() {
        return determinedDate;
    }

    public void setDeterminedDate(Date determinedDate) {
        this.determinedDate = determinedDate;
    }

}
