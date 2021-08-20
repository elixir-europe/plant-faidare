package fr.inra.urgi.faidare.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;

import java.io.Serializable;

public interface BrapiGermplasmCollectingSite extends Serializable {

    @JsonView(JSONView.BrapiFields.class)
    String getCoordinateUncertainty();

    @JsonView(JSONView.BrapiFields.class)
    String getElevation();

    @JsonView(JSONView.BrapiFields.class)
    String getGeoreferencingMethod();

    @JsonView(JSONView.BrapiFields.class)
    String getLatitudeDecimal();

    @JsonView(JSONView.BrapiFields.class)
    String getLatitudeDegrees();

    @JsonView(JSONView.BrapiFields.class)
    String getLocationDescription();

    @JsonView(JSONView.BrapiFields.class)
    String getLongitudeDecimal();

    @JsonView(JSONView.BrapiFields.class)
    String getLongitudeDegrees();

    @JsonView(JSONView.BrapiFields.class)
    String getSpatialReferenceSystem();
}
