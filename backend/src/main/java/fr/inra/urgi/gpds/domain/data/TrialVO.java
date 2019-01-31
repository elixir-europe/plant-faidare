package fr.inra.urgi.gpds.domain.data;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import fr.inra.urgi.gpds.domain.brapi.v1.data.*;
import fr.inra.urgi.gpds.domain.jsonld.data.HasURI;
import fr.inra.urgi.gpds.domain.jsonld.data.HasURL;
import fr.inra.urgi.gpds.domain.jsonld.data.IncludedInDataCatalog;
import fr.inra.urgi.gpds.elasticsearch.document.annotation.Document;
import fr.inra.urgi.gpds.elasticsearch.document.annotation.Id;

import java.util.Date;
import java.util.List;

/**
 * @author gcornut
 */
@Document(type = "trial")
public class TrialVO implements GnpISInternal, BrapiTrial, HasURI, HasURL, IncludedInDataCatalog {

    @Id
    private String trialDbId;

    private String trialName;
    private String trialType;
    private Date endDate;
    private Date startDate;
    private Boolean active;

    private String programDbId;
    private String programName;

    @JsonSetter(contentNulls = Nulls.AS_EMPTY)
    @JsonDeserialize(as = TrialDatasetAuthorshipVO.class)
    private BrapiTrialDatasetAuthorship datasetAuthorship = TrialDatasetAuthorshipVO.EMPTY;

    @JsonDeserialize(contentAs = ContactVO.class)
    private List<BrapiContact> contacts;

    @JsonDeserialize(contentAs = TrialStudySummaryVO.class)
    private List<BrapiTrialStudy> studies;

    private BrapiAdditionalInfo additionalInfo;

    // GnpIS specific fields
    private List<Long> speciesGroup;
    private Long groupId;

    // JSON-LD fields
    private String uri;
    private String url;
    private String sourceUri;

    @Override
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getSourceUri() {
        return sourceUri;
    }

    public void setSourceUri(String sourceUri) {
        this.sourceUri = sourceUri;
    }

    @Override
    public String getTrialDbId() {
        return trialDbId;
    }

    @Override
    public String getTrialName() {
        return trialName;
    }

    @Override
    public String getTrialType() {
        return trialType;
    }

    @Override
    public Date getEndDate() {
        return endDate;
    }

    @Override
    public Date getStartDate() {
        return startDate;
    }

    @Override
    public Boolean getActive() {
        return active;
    }

    @Override
    public String getProgramDbId() {
        return programDbId;
    }

    public void setProgramDbId(String programDbId) {
        this.programDbId = programDbId;
    }

    @Override
    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    @Override
    public BrapiTrialDatasetAuthorship getDatasetAuthorship() {
        return datasetAuthorship;
    }

    @Override
    public List<BrapiContact> getContacts() {
        return contacts;
    }

    @Override
    public List<BrapiTrialStudy> getStudies() {
        return studies;
    }

    @Override
    public BrapiAdditionalInfo getAdditionalInfo() {
        return additionalInfo;
    }

    public void setTrialDbId(String trialDbId) {
        this.trialDbId = trialDbId;
    }

    public void setTrialName(String trialName) {
        this.trialName = trialName;
    }

    public void setTrialType(String trialType) {
        this.trialType = trialType;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setDatasetAuthorship(BrapiTrialDatasetAuthorship datasetAuthorship) {
        if (datasetAuthorship != null) {
            this.datasetAuthorship = datasetAuthorship;
        }
    }

    public void setContacts(List<BrapiContact> contacts) {
        this.contacts = contacts;
    }

    public void setStudies(List<BrapiTrialStudy> studies) {
        this.studies = studies;
    }

    public void setAdditionalInfo(BrapiAdditionalInfo additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    @Override
    public List<Long> getSpeciesGroup() {
        return speciesGroup;
    }

    public void setSpeciesGroup(List<Long> speciesGroup) {
        this.speciesGroup = speciesGroup;
    }

    @Override
    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}
