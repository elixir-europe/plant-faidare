package fr.inra.urgi.gpds.domain.data.impl;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiTrialDatasetAuthorship;

/**
 * @author gcornut
 */
public class TrialDatasetAuthorshipVO implements BrapiTrialDatasetAuthorship {

    public static final TrialDatasetAuthorshipVO EMPTY = new TrialDatasetAuthorshipVO();

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private String license = "";

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private String datasetPUI = "";

    @Override
    public String getLicense() {
        return license;
    }

    @Override
    public String getDatasetPUI() {
        return datasetPUI;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public void setDatasetPUI(String datasetPUI) {
        this.datasetPUI = datasetPUI;
    }
}
