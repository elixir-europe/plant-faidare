package fr.inra.urgi.faidare.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;

/**
 * @author gornut
 * @link https://github.com/plantbreeding/API/blob/master/Specification/Locations/ListLocations.md
 * @link https://github.com/plantbreeding/API/blob/master/Specification/Locations/LocationDetails.md
 */
public interface BrapiLocation extends HasBrapiDocumentationURL {

    // Location
    @JsonView(JSONView.BrapiFields.class)
    String getLocationDbId();

    @JsonView(JSONView.BrapiFields.class)
    String getLocationType();

    @JsonView(JSONView.BrapiFields.class)
    String getLocationName();

    @Deprecated
    @JsonView(JSONView.BrapiFields.class)
    String getName();

    @JsonView(JSONView.BrapiFields.class)
    String getAbbreviation();

    @Deprecated
    @JsonView(JSONView.BrapiFields.class)
    String getAbreviation();

    // Geo
    @JsonView(JSONView.BrapiFields.class)
    Double getLatitude();

    @JsonView(JSONView.BrapiFields.class)
    Double getLongitude();

    @JsonView(JSONView.BrapiFields.class)
    Double getAltitude();

    // Country
    @JsonView(JSONView.BrapiFields.class)
    String getCountryCode();

    @JsonView(JSONView.BrapiFields.class)
    String getCountryName();

    // Institution

    @JsonView(JSONView.BrapiFields.class)
    String getInstituteAddress();

    // For backward compatibility with brapi v1
    @JsonView(JSONView.BrapiFields.class)
    @Deprecated
    String getInstituteAdress();

    @JsonView(JSONView.BrapiFields.class)
    String getInstituteName();

    // Additional info
    @JsonView(JSONView.BrapiFields.class)
    BrapiAdditionalInfo getAdditionalInfo();

    @Override
    String getDocumentationURL();
}
