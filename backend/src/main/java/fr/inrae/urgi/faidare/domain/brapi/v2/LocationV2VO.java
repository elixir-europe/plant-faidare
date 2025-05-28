package fr.inrae.urgi.faidare.domain.brapi.v2;

import fr.inrae.urgi.faidare.config.ElasticSearchConfig;
import fr.inrae.urgi.faidare.domain.CoordinatesVO;
import fr.inrae.urgi.faidare.domain.ExternalReferencesVO;
import org.springframework.context.annotation.Import;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Import({ElasticSearchConfig.class})
@Document(
    indexName = "#{@faidarePropertiesBean.getAliasName('location', 0L)}",
    createIndex = false
)
public class LocationV2VO {
    private String abbreviation;
    private Map<String, Object> additionalInfo;
    private String coordinateDescription;
    private String coordinateUncertainty;
    private CoordinatesVO coordinates;
    private String countryCode;
    private String countryName;
    private String documentationURL;
    private String environmentType;
    private String exposure;
    private List<ExternalReferencesVO> externalReferences;
    private String instituteAddress;
    private String instituteName;
    private String locationDbId;
    private String locationName;
    private String locationType;
    private String parentLocationDbId;
    private String parentLocationName;
    private String siteStatus;
    private String slope;
    private String topography;
    private Double altitude;
    private Double latitude;
    private Double longitude;
    @Field("schema:includedInDataCatalog")
    private String sourceUri;
    private String uri;
    private String url;
    @Id
    private String _id;

    public Map<String, Object> getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(Map<String, Object> additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getCoordinateDescription() {
        return coordinateDescription;
    }

    public void setCoordinateDescription(String coordinateDescription) {
        this.coordinateDescription = coordinateDescription;
    }

    public CoordinatesVO getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(CoordinatesVO coordinates) {
        this.coordinates = coordinates;
    }

    public String getCoordinateUncertainty() {
        return coordinateUncertainty;
    }

    public void setCoordinateUncertainty(String coordinateUncertainty) {
        this.coordinateUncertainty = coordinateUncertainty;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getDocumentationURL() {
        return documentationURL;
    }

    public void setDocumentationURL(String documentationURL) {
        this.documentationURL = documentationURL;
    }

    public String getEnvironmentType() {
        return environmentType;
    }

    public void setEnvironmentType(String environmentType) {
        this.environmentType = environmentType;
    }

    public String getExposure() {
        return exposure;
    }

    public void setExposure(String exposure) {
        this.exposure = exposure;
    }

    public List<ExternalReferencesVO> getExternalReferences() {
        return externalReferences;
    }

    public void setExternalReferences(List<ExternalReferencesVO> externalReferences) {
        this.externalReferences = externalReferences;
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

    public String getParentLocationDbId() {
        return parentLocationDbId;
    }

    public void setParentLocationDbId(String parentLocationDbId) {
        this.parentLocationDbId = parentLocationDbId;
    }

    public String getParentLocationName() {
        return parentLocationName;
    }

    public void setParentLocationName(String parentLocationName) {
        this.parentLocationName = parentLocationName;
    }

    public String getSiteStatus() {
        return siteStatus;
    }

    public void setSiteStatus(String siteStatus) {
        this.siteStatus = siteStatus;
    }

    public String getSlope() {
        return slope;
    }

    public void setSlope(String slope) {
        this.slope = slope;
    }

    public String getTopography() {
        return topography;
    }

    public void setTopography(String topography) {
        this.topography = topography;
    }

    public String getSourceUri() {
        return sourceUri;
    }

    public void setSourceUri(String sourceUri) {
        this.sourceUri = sourceUri;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationV2VO that = (LocationV2VO) o;
        return Objects.equals(abbreviation, that.abbreviation) && Objects.equals(additionalInfo, that.additionalInfo) && Objects.equals(coordinateDescription, that.coordinateDescription) && Objects.equals(coordinateUncertainty, that.coordinateUncertainty) && Objects.equals(coordinates, that.coordinates) && Objects.equals(countryCode, that.countryCode) && Objects.equals(countryName, that.countryName) && Objects.equals(documentationURL, that.documentationURL) && Objects.equals(environmentType, that.environmentType) && Objects.equals(exposure, that.exposure) && Objects.equals(externalReferences, that.externalReferences) && Objects.equals(instituteAddress, that.instituteAddress) && Objects.equals(instituteName, that.instituteName) && Objects.equals(locationDbId, that.locationDbId) && Objects.equals(locationName, that.locationName) && Objects.equals(locationType, that.locationType) && Objects.equals(parentLocationDbId, that.parentLocationDbId) && Objects.equals(parentLocationName, that.parentLocationName) && Objects.equals(siteStatus, that.siteStatus) && Objects.equals(slope, that.slope) && Objects.equals(topography, that.topography) && Objects.equals(altitude, that.altitude) && Objects.equals(latitude, that.latitude) && Objects.equals(longitude, that.longitude) && Objects.equals(sourceUri, that.sourceUri) && Objects.equals(_id, that._id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(abbreviation, additionalInfo, coordinateDescription, coordinateUncertainty, coordinates, countryCode, countryName, documentationURL, environmentType, exposure, externalReferences, instituteAddress, instituteName, locationDbId, locationName, locationType, parentLocationDbId, parentLocationName, siteStatus, slope, topography, altitude, latitude, longitude, sourceUri, _id);
    }

    @Override
    public String toString() {
        return "LocationV2VO{" +
            "_id='" + _id + '\'' +
            ", abbreviation='" + abbreviation + '\'' +
            ", additionalInfo=" + additionalInfo +
            ", coordinateDescription='" + coordinateDescription + '\'' +
            ", coordinateUncertainty='" + coordinateUncertainty + '\'' +
            ", coordinates=" + coordinates +
            ", countryCode='" + countryCode + '\'' +
            ", countryName='" + countryName + '\'' +
            ", documentationURL='" + documentationURL + '\'' +
            ", environmentType='" + environmentType + '\'' +
            ", exposure='" + exposure + '\'' +
            ", externalReferences=" + externalReferences +
            ", instituteAddress='" + instituteAddress + '\'' +
            ", instituteName='" + instituteName + '\'' +
            ", locationDbId='" + locationDbId + '\'' +
            ", locationName='" + locationName + '\'' +
            ", locationType='" + locationType + '\'' +
            ", parentLocationDbId='" + parentLocationDbId + '\'' +
            ", parentLocationName='" + parentLocationName + '\'' +
            ", siteStatus='" + siteStatus + '\'' +
            ", slope='" + slope + '\'' +
            ", topography='" + topography + '\'' +
            ", altitude=" + altitude +
            ", latitude=" + latitude +
            ", longitude=" + longitude +
            ", sourceUri='" + sourceUri + '\'' +
            '}';
    }
}
