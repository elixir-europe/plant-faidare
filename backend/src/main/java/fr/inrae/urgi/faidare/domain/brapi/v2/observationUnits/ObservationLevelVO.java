package fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits;

public class ObservationLevelVO {

    private String levelCode;
    private String levelName;

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getLevelOrder() {
        return levelCode;
    }

    public void setLevelOrder(String levelOrder) {
        this.levelCode = levelOrder;
    }
}
