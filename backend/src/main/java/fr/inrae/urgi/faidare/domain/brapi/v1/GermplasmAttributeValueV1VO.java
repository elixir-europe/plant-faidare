package fr.inrae.urgi.faidare.domain.brapi.v1;

import java.util.Date;
import java.util.Objects;


public class GermplasmAttributeValueV1VO {


    private String attributeDbId;
    private String attributeName;
    private String attributeCode;
    private String value;
    private Date determinedDate;

    public String getAttributeCode() {
        return attributeCode;
    }

    public void setAttributeCode(String attributeCode) {
        this.attributeCode = attributeCode;
    }

    public String getAttributeDbId() {
        return attributeDbId;
    }

    public void setAttributeDbId(String attributeDbId) {
        this.attributeDbId = attributeDbId;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public Date getDeterminedDate() {
        return determinedDate;
    }

    public void setDeterminedDate(Date determinedDate) {
        this.determinedDate = determinedDate;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GermplasmAttributeValueV1VO that = (GermplasmAttributeValueV1VO) o;
        return Objects.equals(attributeDbId, that.attributeDbId) && Objects.equals(attributeName, that.attributeName) && Objects.equals(attributeCode, that.attributeCode) && Objects.equals(value, that.value) && Objects.equals(determinedDate, that.determinedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attributeDbId, attributeName, attributeCode, value, determinedDate);
    }
}
