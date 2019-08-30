package fr.inra.urgi.faidare.domain.data.germplasm;

import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiGermplasmCollectingSite;

public class CollectingSiteVO implements BrapiGermplasmCollectingSite, ExtendedCollectingSite {

    private String locationDbId;
    private String siteName;
    private String siteType;
    private String coordinateUncertainty;
    private String elevation;
    private String georeferencingMethod;
    private String latitudeDecimal;
    private String latitudeDegrees;
    private String locationDescription;
    private String longitudeDecimal;
    private String longitudeDegrees;
    private String spatialReferenceSystem;

    @Override
    public String getLocationDbId() {
        return locationDbId;
    }

    public void setLocationDbId(String locationDbId) {
        this.locationDbId = locationDbId;
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

    @Override
    public String getCoordinateUncertainty() {
        return coordinateUncertainty;
    }

    @Override
    public String getElevation() {
        return elevation;
    }

    @Override
    public String getGeoreferencingMethod() {
        return georeferencingMethod;
    }

    @Override
    public String getLatitudeDecimal() {
        return latitudeDecimal;
    }

    @Override
    public String getLatitudeDegrees() {
        return latitudeDegrees;
    }

    @Override
    public String getLocationDescription() {
        return locationDescription;
    }

    @Override
    public String getLongitudeDecimal() {
        return longitudeDecimal;
    }

    @Override
    public String getLongitudeDegrees() {
        return longitudeDegrees;
    }

    @Override
    public String getSpatialReferenceSystem() {
        return spatialReferenceSystem;
    }


    public void setCoordinateUncertainty(String coordinateUncertainty) {
        this.coordinateUncertainty = coordinateUncertainty;
    }

    public void setElevation(String elevation) {
        this.elevation = elevation;
    }

    public void setGeoreferencingMethod(String georeferencingMethod) {
        this.georeferencingMethod = georeferencingMethod;
    }

    public void setLatitudeDecimal(String latitudeDecimal) {
        this.latitudeDecimal = latitudeDecimal;
    }

    public void setLatitudeDegrees(String latitudeDegrees) {
        this.latitudeDegrees = latitudeDegrees;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public void setLongitudeDecimal(String longitudeDecimal) {
        this.longitudeDecimal = longitudeDecimal;
    }

    public void setLongitudeDegrees(String longitudeDegrees) {
        this.longitudeDegrees = longitudeDegrees;
    }

    public void setSpatialReferenceSystem(String spatialReferenceSystem) {
        this.spatialReferenceSystem = spatialReferenceSystem;
    }
}
