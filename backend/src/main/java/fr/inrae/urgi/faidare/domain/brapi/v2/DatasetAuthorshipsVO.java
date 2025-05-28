package fr.inrae.urgi.faidare.domain.brapi.v2;

import java.util.Objects;

public class DatasetAuthorshipsVO {

    private String datasetPUI;
    private String license;
    private String publicReleaseDate;
    private String submissionDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DatasetAuthorshipsVO that = (DatasetAuthorshipsVO) o;
        return Objects.equals(datasetPUI, that.datasetPUI) && Objects.equals(license, that.license) && Objects.equals(publicReleaseDate, that.publicReleaseDate) && Objects.equals(submissionDate, that.submissionDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(datasetPUI, license, publicReleaseDate, submissionDate);
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getDatasetPUI() {
        return datasetPUI;
    }

    public void setDatasetPUI(String datasetPUI) {
        this.datasetPUI = datasetPUI;
    }

    public String getPublicReleaseDate() {
        return publicReleaseDate;
    }

    public void setPublicReleaseDate(String publicReleaseDate) {
        this.publicReleaseDate = publicReleaseDate;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }
}
