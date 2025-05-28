package fr.inrae.urgi.faidare.domain.brapi.v2;

import java.util.List;

public class Geometry {

    private List<String> coordinates;
    private String type;

    public List<String> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<String> coordinates) {
        this.coordinates = coordinates;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
