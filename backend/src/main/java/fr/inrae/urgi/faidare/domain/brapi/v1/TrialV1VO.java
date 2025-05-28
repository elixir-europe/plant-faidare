package fr.inrae.urgi.faidare.domain.brapi.v1;

import fr.inrae.urgi.faidare.config.ElasticSearchConfig;
import fr.inrae.urgi.faidare.domain.ContactVO;
import org.springframework.context.annotation.Import;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.util.List;
import java.util.Objects;

@Import({ElasticSearchConfig.class})
@Document(
    indexName = "#{@faidarePropertiesBean.getAliasName('trial', 0L)}",
    createIndex = false
)

public class TrialV1VO {

    private Boolean active;

    private String additionalInfo;
    private String trialDbId;
    private String trialName;
    private Long groupId;
    private String trialType;
    private String instituteName;
    private String endDate;
    @Id
    private String _id;
    private String programDbId;
    private String programName;
    private String dataSetAuthorship;

    @Field("schema:includedInDataCatalog")
    private String sourceUri;

    private String startDate;
    private List<ContactVO> contact;
    private List<String> studyDbIds;
    private List<StudyV1miniVO> studies;
    private String documentationURL;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrialV1VO trialV1VO = (TrialV1VO) o;
        return Objects.equals(active, trialV1VO.active) && Objects.equals(additionalInfo, trialV1VO.additionalInfo) && Objects.equals(trialDbId, trialV1VO.trialDbId) && Objects.equals(trialName, trialV1VO.trialName) && Objects.equals(groupId, trialV1VO.groupId) && Objects.equals(trialType, trialV1VO.trialType) && Objects.equals(instituteName, trialV1VO.instituteName) && Objects.equals(endDate, trialV1VO.endDate) && Objects.equals(_id, trialV1VO._id) && Objects.equals(programDbId, trialV1VO.programDbId) && Objects.equals(programName, trialV1VO.programName) && Objects.equals(dataSetAuthorship, trialV1VO.dataSetAuthorship) && Objects.equals(startDate, trialV1VO.startDate) && Objects.equals(contact, trialV1VO.contact) && Objects.equals(studyDbIds, trialV1VO.studyDbIds) && Objects.equals(studies, trialV1VO.studies) && Objects.equals(documentationURL, trialV1VO.documentationURL);
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public List<ContactVO> getContact() {
        return contact;
    }

    public void setContact(List<ContactVO> contact) {
        this.contact = contact;
    }

    public String getDataSetAuthorship() {
        return dataSetAuthorship;
    }

    public void setDataSetAuthorship(String dataSetAuthorship) {
        this.dataSetAuthorship = dataSetAuthorship;
    }

    public String getDocumentationURL() {
        return documentationURL;
    }

    public void setDocumentationURL(String documentationURL) {
        this.documentationURL = documentationURL;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
    }

    public String getProgramDbId() {
        return programDbId;
    }

    public void setProgramDbId(String programDbId) {
        this.programDbId = programDbId;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getSourceUri() {
        return sourceUri;
    }

    public void setSourceUri(String sourceUri) {
        this.sourceUri = sourceUri;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public List<StudyV1miniVO> getStudies() {
        return studies;
    }

    public void setStudies(List<StudyV1miniVO> studies) {
        this.studies = studies;
    }

    public List<String> getStudyDbIds() {
        return studyDbIds;
    }

    public void setStudyDbIds(List<String> studyDbIds) {
        this.studyDbIds = studyDbIds;
    }

    public String getTrialDbId() {
        return trialDbId;
    }

    public void setTrialDbId(String trialDbId) {
        this.trialDbId = trialDbId;
    }

    public String getTrialName() {
        return trialName;
    }

    public void setTrialName(String trialName) {
        this.trialName = trialName;
    }

    public String getTrialType() {
        return trialType;
    }

    public void setTrialType(String trialType) {
        this.trialType = trialType;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(active, additionalInfo, trialDbId, trialName, groupId, trialType, instituteName, endDate, _id, programDbId, programName, dataSetAuthorship, startDate, contact, studyDbIds, studies, documentationURL);
    }

}
