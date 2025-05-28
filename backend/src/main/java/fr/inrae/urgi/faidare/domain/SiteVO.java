package fr.inrae.urgi.faidare.domain;

import java.util.Objects;

public class SiteVO {

    private String locationDbId;
    private String coordinateUncertainty;
    private String elevation;
    private String georeferencingMethod;
    private Double latitude;

    private Double longitude;
    private String latitudeDecimal;
    private String latitudeDegrees;
    private String locationDescription;
    private String longitudeDecimal;
    private String longitudeDegrees;
    private String spatialReferenceSystem;
    private String siteId;
    private String siteName;
    private String siteType;

    public SiteVO(String siteId, double latitude, double longitude, String siteName, String siteType) {
    }

    public SiteVO() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SiteVO siteVO = (SiteVO) o;
        return Objects.equals(locationDbId, siteVO.locationDbId) && Objects.equals(coordinateUncertainty, siteVO.coordinateUncertainty) && Objects.equals(elevation, siteVO.elevation) && Objects.equals(georeferencingMethod, siteVO.georeferencingMethod) && Objects.equals(latitude, siteVO.latitude) && Objects.equals(longitude, siteVO.longitude) && Objects.equals(latitudeDecimal, siteVO.latitudeDecimal) && Objects.equals(latitudeDegrees, siteVO.latitudeDegrees) && Objects.equals(locationDescription, siteVO.locationDescription) && Objects.equals(longitudeDecimal, siteVO.longitudeDecimal) && Objects.equals(longitudeDegrees, siteVO.longitudeDegrees) && Objects.equals(spatialReferenceSystem, siteVO.spatialReferenceSystem) && Objects.equals(siteId, siteVO.siteId) && Objects.equals(siteName, siteVO.siteName) && Objects.equals(siteType, siteVO.siteType);
    }

    public String getCoordinateUncertainty() {
        return coordinateUncertainty;
    }

    public void setCoordinateUncertainty(String coordinateUncertainty) {
        this.coordinateUncertainty = coordinateUncertainty;
    }

    public String getElevation() {
        return elevation;
    }

    public void setElevation(String elevation) {
        this.elevation = elevation;
    }

    public String getGeoreferencingMethod() {
        return georeferencingMethod;
    }

    public void setGeoreferencingMethod(String georeferencingMethod) {
        this.georeferencingMethod = georeferencingMethod;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getLatitudeDecimal() {
        return latitudeDecimal;
    }

    public void setLatitudeDecimal(String latitudeDecimal) {
        this.latitudeDecimal = latitudeDecimal;
    }

    public String getLatitudeDegrees() {
        return latitudeDegrees;
    }

    public void setLatitudeDegrees(String latitudeDegrees) {
        this.latitudeDegrees = latitudeDegrees;
    }

    public String getLocationDbId() {
        return locationDbId;
    }

    public void setLocationDbId(String locationDbId) {
        this.locationDbId = locationDbId;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getLongitudeDecimal() {
        return longitudeDecimal;
    }

    public void setLongitudeDecimal(String longitudeDecimal) {
        this.longitudeDecimal = longitudeDecimal;
    }

    public String getLongitudeDegrees() {
        return longitudeDegrees;
    }

    public void setLongitudeDegrees(String longitudeDegrees) {
        this.longitudeDegrees = longitudeDegrees;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteType() {
        return siteType;
    }

    public void setSiteType(String siteType) {
        this.siteType = siteType;
    }

    public String getSpatialReferenceSystem() {
        return spatialReferenceSystem;
    }

    public void setSpatialReferenceSystem(String spatialReferenceSystem) {
        this.spatialReferenceSystem = spatialReferenceSystem;
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationDbId, coordinateUncertainty, elevation, georeferencingMethod, latitude, longitude, latitudeDecimal, latitudeDegrees, locationDescription, longitudeDecimal, longitudeDegrees, spatialReferenceSystem, siteId, siteName, siteType);
    }

}
