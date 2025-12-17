package fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits;

import fr.inrae.urgi.faidare.domain.brapi.v2.Geometry;

import java.util.List;

public class ObservationUnitPositionVO {
    private String entryType;
    private String entryNumber;
    private ObservationLevelVO observationLevel;
    private List<String> observationLevelRelationships;
    private String observationUnitPositionDbId;
    private String positionCoordinateX;
    private String positionCoordinateXType;
    private String positionCoordinateY;
    private String positionCoordinateYType;

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getEntryNumber() {
        return entryNumber;
    }

    public void setEntryNumber(String entryNumber) {
        this.entryNumber = entryNumber;
    }

    public ObservationLevelVO getObservationLevel() {
        return observationLevel;
    }

    public void setObservationLevel(ObservationLevelVO observationLevel) {
        this.observationLevel = observationLevel;
    }

    public List<String> getObservationLevelRelationships() {
        return observationLevelRelationships;
    }

    public void setObservationLevelRelationships(List<String> observationLevelRelationships) {
        this.observationLevelRelationships = observationLevelRelationships;
    }

    public String getObservationUnitPositionDbId() {
        return observationUnitPositionDbId;
    }

    public void setObservationUnitPositionDbId(String observationUnitPositionDbId) {
        this.observationUnitPositionDbId = observationUnitPositionDbId;
    }

    public String getPositionCoordinateX() {
        return positionCoordinateX;
    }

    public void setPositionCoordinateX(String positionCoordinateX) {
        this.positionCoordinateX = positionCoordinateX;
    }

    public String getPositionCoordinateXType() {
        return positionCoordinateXType;
    }

    public void setPositionCoordinateXType(String positionCoordinateXType) {
        this.positionCoordinateXType = positionCoordinateXType;
    }

    public String getPositionCoordinateY() {
        return positionCoordinateY;
    }

    public void setPositionCoordinateY(String positionCoordinateY) {
        this.positionCoordinateY = positionCoordinateY;
    }

    public String getPositionCoordinateYType() {
        return positionCoordinateYType;
    }

    public void setPositionCoordinateYType(String positionCoordinateYType) {
        this.positionCoordinateYType = positionCoordinateYType;
    }
}
