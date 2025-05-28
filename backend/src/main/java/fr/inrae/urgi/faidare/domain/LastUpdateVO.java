package fr.inrae.urgi.faidare.domain;

public class LastUpdateVO {

    private String timestamp;

    private String version;


    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
