package fr.inrae.urgi.faidare.domain;
import java.util.List;
import java.util.Objects;

import fr.inrae.urgi.faidare.config.ElasticSearchConfig;
import org.springframework.context.annotation.Import;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Import({ElasticSearchConfig.class})
@Document(
    indexName = "#{@faidarePropertiesBean.getAliasName('location', 0L)}",
    createIndex = false
)

public class LocationVO {

    //public static final String INDEX_NAME = "faidare_location_dev-group0";

    private String abbreviation;

    //private ... additionalInfo;

    private Double altitude;

    private String countryCode;

    private String countryName;

    private Long groupId;

    private String instituteAddress;

    private String instituteName;

    private Double latitude;

    @Id
    private String _id;
    private String locationDbId;
    private String locationName;
    private String locationType;
    private Double longitude;
    private String sourceUri;
    private List<Long> speciesGroup;
    private String uri;
    private String url;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationVO that = (LocationVO) o;
        return Objects.equals(abbreviation, that.abbreviation) && Objects.equals(altitude, that.altitude) && Objects.equals(countryCode, that.countryCode) && Objects.equals(countryName, that.countryName) && Objects.equals(groupId, that.groupId) && Objects.equals(instituteAddress, that.instituteAddress) && Objects.equals(instituteName, that.instituteName) && Objects.equals(latitude, that.latitude) && Objects.equals(_id, that._id) && Objects.equals(locationDbId, that.locationDbId) && Objects.equals(locationName, that.locationName) && Objects.equals(locationType, that.locationType) && Objects.equals(longitude, that.longitude) && Objects.equals(sourceUri, that.sourceUri) && Objects.equals(speciesGroup, that.speciesGroup) && Objects.equals(uri, that.uri) && Objects.equals(url, that.url);
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getInstituteAddress() {
        return instituteAddress;
    }

    public void setInstituteAddress(String instituteAddress) {
        this.instituteAddress = instituteAddress;
    }

    public String getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getLocationDbId() {
        return locationDbId;
    }

    public void setLocationDbId(String locationDbId) {
        this.locationDbId = locationDbId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getSourceUri() {
        return sourceUri;
    }

    public void setSourceUri(String sourceUri) {
        this.sourceUri = sourceUri;
    }

    public List<Long> getSpeciesGroup() {
        return speciesGroup;
    }

    public void setSpeciesGroup(List<Long> speciesGroup) {
        this.speciesGroup = speciesGroup;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(abbreviation, altitude, countryCode, countryName, groupId, instituteAddress, instituteName, latitude, _id, locationDbId, locationName, locationType, longitude, sourceUri, speciesGroup, uri, url);
    }
}
