package fr.inra.urgi.gpds.domain.data.impl;

import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiAdditionalInfo;
import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiLocation;
import fr.inra.urgi.gpds.domain.data.GnpISInternal;
import fr.inra.urgi.gpds.elasticsearch.document.annotation.Document;
import fr.inra.urgi.gpds.elasticsearch.document.annotation.Id;

import java.util.List;

/**
 * LocationVO extending the official BreedingAPI specs with specific fields
 *
 * @author gcornut
 *
 *
 */
@Document(type = "location")
public class LocationVO implements GnpISInternal, BrapiLocation {

	@Id
	private String locationDbId;

	private String name;
	private String locationType;
	private String abbreviation;

	private String countryCode;
	private String countryName;

	private String institutionAdress;
	private String institutionName;

	private Double altitude;
	private Double latitude;
	private Double longitude;

	private BrapiAdditionalInfo additionalInfo;

	// GnpIS specific fields
	private List<Long> speciesGroup;
	private Long groupId;

	@Override
	public String getAbbreviation() {
		return abbreviation;
	}

	@Override
	public Double getAltitude() {
		return altitude;
	}

	@Override
	public String getCountryCode() {
		return countryCode;
	}

	@Override
	public String getCountryName() {
		return countryName;
	}

	@Override
	public String getInstitutionAdress() {
		return institutionAdress;
	}

	@Override
	public String getInstitutionName() {
		return institutionName;
	}

	@Override
	public Double getLatitude() {
		return latitude;
	}

	@Override
	public String getLocationDbId() {
		return locationDbId;
	}

	@Override
	public String getLocationType() {
		return locationType;
	}

	@Override
	public Double getLongitude() {
		return longitude;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public BrapiAdditionalInfo getAdditionalInfo() {
		return additionalInfo;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLocationDbId(String locationDbId) {
		this.locationDbId = locationDbId;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public void setInstitutionAdress(String institutionAdress) {
		this.institutionAdress = institutionAdress;
	}

	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName;
	}

	public void setAltitude(Double altitude) {
		this.altitude = altitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public void setAdditionalInfo(BrapiAdditionalInfo additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	@Override
	public List<Long> getSpeciesGroup() {
		return speciesGroup;
	}

	public void setSpeciesGroup(List<Long> speciesGroup) {
		this.speciesGroup = speciesGroup;
	}

	@Override
	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

}
