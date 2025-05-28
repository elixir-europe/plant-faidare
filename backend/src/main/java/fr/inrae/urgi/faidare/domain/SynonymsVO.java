package fr.inrae.urgi.faidare.domain;

public class SynonymsVO {
    private String type;
    private String synonym;

    public SynonymsVO() {
        this.type = null;
    }

    public SynonymsVO(String synonym) {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSynonym() {
        return synonym;
    }

    public void setSynonym(String synonym) {
        this.synonym = synonym;
    }

    @Override
    public String toString() {
        return "{" +
            "type='" + type + '\'' +
            ", synonym='" + synonym + '\'' +
            '}';
    }
}
