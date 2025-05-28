package fr.inrae.urgi.faidare.domain.brapi.v2;

import fr.inrae.urgi.faidare.config.ElasticSearchConfig;
import fr.inrae.urgi.faidare.domain.ContactVO;
import fr.inrae.urgi.faidare.domain.ExternalReferencesVO;
import org.springframework.context.annotation.Import;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.util.List;
import java.util.Objects;
import java.util.stream.LongStream;

@Import({ElasticSearchConfig.class})
@Document(
    indexName = "#{@faidarePropertiesBean.getAliasName('trial', 0L)}",
    createIndex = false
)

public class TrialV2VO {

    private Boolean active;

    //private String additionalInfo;
    private String commonCropName;
    private List<ExternalReferencesVO> externalReferences;
    private PublicationsVO publications;
    private String trialDbId;
    private String trialName;
    private String trialPUI;
    private String trialDescription;
    private Long groupId;
    private String trialType;
    private String instituteName;
    private String endDate;
    @Id
    private String _id;
    private String programDbId;
    private String programName;
    private DatasetAuthorshipsVO dataSetAuthorship;

    @Field("schema:includedInDataCatalog")
    private String sourceUri;

    private String startDate;
    private List<ContactVO> contact;
    private List<String> studyDbIds;
    private List<StudyV2miniVO> studies;
    private String documentationURL;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrialV2VO trialV2VO = (TrialV2VO) o;
        return Objects.equals(active, trialV2VO.active) && Objects.equals(commonCropName, trialV2VO.commonCropName) && Objects.equals(contact, trialV2VO.contact) && Objects.equals(externalReferences, trialV2VO.externalReferences) && Objects.equals(publications, trialV2VO.publications) && Objects.equals(trialDbId, trialV2VO.trialDbId) && Objects.equals(trialName, trialV2VO.trialName) && Objects.equals(trialPUI, trialV2VO.trialPUI) && Objects.equals(trialDescription, trialV2VO.trialDescription) && Objects.equals(groupId, trialV2VO.groupId) && Objects.equals(trialType, trialV2VO.trialType) && Objects.equals(instituteName, trialV2VO.instituteName) && Objects.equals(endDate, trialV2VO.endDate) && Objects.equals(_id, trialV2VO._id) && Objects.equals(programDbId, trialV2VO.programDbId) && Objects.equals(programName, trialV2VO.programName) && Objects.equals(dataSetAuthorship, trialV2VO.dataSetAuthorship) && Objects.equals(sourceUri, trialV2VO.sourceUri) && Objects.equals(startDate, trialV2VO.startDate) && Objects.equals(contact, trialV2VO.contact) && Objects.equals(studyDbIds, trialV2VO.studyDbIds) && Objects.equals(studies, trialV2VO.studies) && Objects.equals(documentationURL, trialV2VO.documentationURL);
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<ContactVO> getContact() {
        return contact;
    }

    public void setContact(List<ContactVO> contact) {
        this.contact = contact;
    }

    public DatasetAuthorshipsVO getDataSetAuthorship() {
        return dataSetAuthorship;
    }

    public void setDataSetAuthorship(DatasetAuthorshipsVO dataSetAuthorship) {
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

    public List<StudyV2miniVO> getStudies() {
        return studies;
    }

    public void setStudies(List<StudyV2miniVO> studies) {
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

    public String getCommonCropName() {
        return commonCropName;
    }

    public void setCommonCropName(String commonCropName) {
        this.commonCropName = commonCropName;
    }

    public List<ExternalReferencesVO> getExternalReferences() {
        return externalReferences;
    }

    public void setExternalReferences(List<ExternalReferencesVO> externalReferences) {
        this.externalReferences = externalReferences;
    }

    public PublicationsVO getPublications() {
        return publications;
    }

    public void setPublications(PublicationsVO publications) {
        this.publications = publications;
    }

    public String getTrialPUI() {
        return trialPUI;
    }

    public void setTrialPUI(String trialPUI) {
        this.trialPUI = trialPUI;
    }

    public String getTrialDescription() {
        return trialDescription;
    }

    public void setTrialDescription(String trialDescription) {
        this.trialDescription = trialDescription;
    }

    @Override
    public int hashCode() {
        return Objects.hash(active, commonCropName, contact, externalReferences, publications, trialDbId, trialName, trialPUI, trialDescription, groupId, trialType, instituteName, endDate, _id, programDbId, programName, dataSetAuthorship, sourceUri, startDate, contact, studyDbIds, studies, documentationURL);
    }
}
