package fr.inra.urgi.gpds.domain.data.variable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiScale;
import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiScaleValidValue;

/**
 * @author gcornut
 */
public class ScaleVO implements BrapiScale {

    private String scaleDbId;
    private String name;
    private String dataType;
    private String decimalPlaces;
    private String xref;

    @JsonDeserialize(as = ScaleValidValueVO.class)
    private BrapiScaleValidValue validValues;

    @Override
    public String getScaleDbId() {
        return scaleDbId;
    }

    public void setScaleDbId(String scaleDbId) {
        this.scaleDbId = scaleDbId;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @Override
    public String getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(String decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    @Override
    public String getXref() {
        return xref;
    }

    public void setXref(String xref) {
        this.xref = xref;
    }

    @Override
    public BrapiScaleValidValue getValidValues() {
        return validValues;
    }

    public void setValidValues(BrapiScaleValidValue validValues) {
        this.validValues = ((ScaleValidValueVO) validValues);
    }

}
