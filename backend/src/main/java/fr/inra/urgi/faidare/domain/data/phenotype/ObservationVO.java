package fr.inra.urgi.faidare.domain.data.phenotype;

import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiObservation;

import java.util.Date;

/**
 * @author gcornut
 */
public class ObservationVO implements BrapiObservation {

    private String observationDbId;
    private String observationVariableDbId;
    private String observationVariableName;
    private Date observationTimeStamp;
    private String season;
    private String collector;
    private String value;

    @Override
    public String getObservationDbId() {
        return observationDbId;
    }

    public void setObservationDbId(String observationDbId) {
        this.observationDbId = observationDbId;
    }

    @Override
    public String getObservationVariableDbId() {
        return observationVariableDbId;
    }

    public void setObservationVariableDbId(String observationVariableDbId) {
        this.observationVariableDbId = observationVariableDbId;
    }

    @Override
    public String getObservationVariableName() {
        return observationVariableName;
    }

    public void setObservationVariableName(String observationVariableName) {
        this.observationVariableName = observationVariableName;
    }

    @Override
    public Date getObservationTimeStamp() {
        return observationTimeStamp;
    }

    public void setObservationTimeStamp(Date observationTimeStamp) {
        this.observationTimeStamp = observationTimeStamp;
    }

    @Override
    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    @Override
    public String getCollector() {
        return collector;
    }

    public void setCollector(String collector) {
        this.collector = collector;
    }

    @Override
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
