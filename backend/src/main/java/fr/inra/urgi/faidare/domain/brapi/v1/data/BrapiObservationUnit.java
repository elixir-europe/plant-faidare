package fr.inra.urgi.faidare.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;

import java.util.List;

/**
 * @author gcornut
 */
public interface BrapiObservationUnit {
    @JsonView(JSONView.BrapiFields.class)
    String getProgramDbId();

    @JsonView(JSONView.BrapiFields.class)
    String getObservationUnitDbId();

    @JsonView(JSONView.BrapiFields.class)
    String getObservationUnitName();

    @JsonView(JSONView.BrapiFields.class)
    String getObservationLevel();

    @JsonView(JSONView.BrapiFields.class)
    String getObservationLevels();

    @JsonView(JSONView.BrapiFields.class)
    String getPlotNumber();

    @JsonView(JSONView.BrapiFields.class)
    String getPlantNumber();

    @JsonView(JSONView.BrapiFields.class)
    String getBlockNumber();

    @JsonView(JSONView.BrapiFields.class)
    String getReplicate();

    @JsonView(JSONView.BrapiFields.class)
    String getEntryType();

    @JsonView(JSONView.BrapiFields.class)
    String getEntryNumber();

    @JsonView(JSONView.BrapiFields.class)
    String getGermplasmDbId();

    @JsonView(JSONView.BrapiFields.class)
    String getGermplasmName();

    @JsonView(JSONView.BrapiFields.class)
    String getStudyDbId();

    @JsonView(JSONView.BrapiFields.class)
    String getStudyName();

    @JsonView(JSONView.BrapiFields.class)
    String getStudyLocationDbId();

    @JsonView(JSONView.BrapiFields.class)
    String getStudyLocation();

    @JsonView(JSONView.BrapiFields.class)
    String getProgramName();

    @JsonView(JSONView.BrapiFields.class)
    @JsonProperty("X")
    String getX();

    @JsonView(JSONView.BrapiFields.class)
    @JsonProperty("Y")
    String getY();

    @JsonView(JSONView.BrapiFields.class)
    List<BrapiObservationUnitTreatment> getTreatments();

    @JsonView(JSONView.BrapiFields.class)
    List<BrapiObservation> getObservations();

    @JsonView(JSONView.BrapiFields.class)
    List<BrapiObservationUnitXRef> getObservationUnitXref();

}
