package fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits;

public class SeasonVO {
    private String seasonDbId; // Required
    private String seasonName; // Deprecated: 'season' in BrAPI v2.1
    private Integer year;

    public String getSeasonDbId() {
        return seasonDbId;
    }

    public void setSeasonDbId(String seasonDbId) {
        this.seasonDbId = seasonDbId;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
