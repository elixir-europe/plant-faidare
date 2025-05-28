package fr.inrae.urgi.faidare.domain.brapi.v2;

import fr.inrae.urgi.faidare.config.ElasticSearchConfig;
import org.springframework.context.annotation.Import;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Objects;


@Import({ElasticSearchConfig.class})
@Document(
    indexName = "#{@faidarePropertiesBean.getAliasName('study', 0L)}",
    createIndex = false
)
public final class StudyV2miniVO {



    private String locationDbId;

    private String locationName;
    @Id
    private String _id;
    private String studyDbId;
    private String studyName;
    private String studyPUI;
    private String studyURI;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudyV2miniVO that = (StudyV2miniVO) o;
        return Objects.equals(locationDbId, that.locationDbId) && Objects.equals(locationName, that.locationName) && Objects.equals(_id, that._id) && Objects.equals(studyDbId, that.studyDbId) && Objects.equals(studyName, that.studyName) && Objects.equals(studyPUI, that.studyPUI) && Objects.equals(studyURI, that.studyURI);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationDbId, locationName, _id, studyDbId, studyName, studyPUI, studyURI);
    }

    public String getLocationDbId() {
        return locationDbId;
    }

    public void setLocationDbId(String locationDbId) {
        this.locationDbId = locationDbId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getStudyDbId() {
        return studyDbId;
    }

    public void setStudyDbId(String studyDbId) {
        this.studyDbId = studyDbId;
    }

    public String getStudyName() {
        return studyName;
    }

    public void setStudyName(String studyName) {
        this.studyName = studyName;
    }

    public String getStudyPUI() {
        return studyPUI;
    }

    public void setStudyPUI(String studyPUI) {
        this.studyPUI = studyPUI;
    }

    public String getStudyURI() {
        return studyURI;
    }

    public void setStudyURI(String studyURI) {
        this.studyURI = studyURI;
    }
}
