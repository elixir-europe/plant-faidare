package fr.inrae.urgi.faidare.domain;


import fr.inrae.urgi.faidare.domain.brapi.v2.Geometry;

public class CoordinatesVO {

    private Geometry geometry;  // A geometry as defined by GeoJSON

    private String type;


    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
