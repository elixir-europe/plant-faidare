package fr.inrae.urgi.faidare.domain;

public class ObservationLevelsVO {

    private String levelName;

    private Integer levelOrder;


    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public Integer getLevelOrder() {
        return levelOrder;
    }

    public void setLevelOrder(Integer levelOrder) {
        this.levelOrder = levelOrder;
    }
}
