package fr.inrae.urgi.faidare.domain.brapi.v2;

import java.util.List;

public class Geometry {

    private List<String> geometry;
    private String type;

    public List<String> getGeometry() {
        return geometry;
    }

    public void setGeometry(List<String> geometry) {
        this.geometry = geometry;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
