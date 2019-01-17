package fr.inra.urgi.gpds.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;

import java.io.Serializable;

/**
 * @link https://github.com/plantbreeding/API/blob/master/Specification/Locations/ListLocations.md
 * @link https://github.com/plantbreeding/API/blob/master/Specification/Locations/LocationDetails.md
 *
 * @author gornut
 */
public interface BrapiLocation extends Serializable {

	// Location
	@JsonView(JSONView.BrapiFields.class)
	String getLocationDbId();

	@JsonView(JSONView.BrapiFields.class)
	String getLocationType();

	@JsonView(JSONView.BrapiFields.class)
	String getName();

	@JsonView(JSONView.BrapiFields.class)
	String getAbbreviation();

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
	String getInstitutionAdress();// TODO: correct typo Adress => Address (pull request API specs)

	@JsonView(JSONView.BrapiFields.class)
	String getInstitutionName();

	// Additional info
	@JsonView(JSONView.BrapiFields.class)
	BrapiAdditionalInfo getAdditionalInfo();

}
