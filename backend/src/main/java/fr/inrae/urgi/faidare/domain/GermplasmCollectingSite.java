package fr.inrae.urgi.faidare.domain;

public interface GermplasmCollectingSite {
    
    String getCoordinateUncertainty();

    
    String getElevation();

    
    String getGeoreferencingMethod();

    
    String getLatitudeDecimal();

    
    String getLatitudeDegrees();

    
    String getLocationDescription();

    
    String getLongitudeDecimal();

    
    String getLongitudeDegrees();

    
    String getSpatialReferenceSystem();
}
