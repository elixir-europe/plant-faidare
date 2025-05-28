package fr.inrae.urgi.faidare.domain;

public class GermplasmOriginVO {

    private String coordinateUncertainty;

    private CoordinatesVO coordinates;


    public String getCoordinateUncertainty() {
        return coordinateUncertainty;
    }

    public void setCoordinateUncertainty(String coordinateUncertainty) {
        this.coordinateUncertainty = coordinateUncertainty;
    }

    public CoordinatesVO getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(CoordinatesVO coordinates) {
        this.coordinates = coordinates;
    }
}
