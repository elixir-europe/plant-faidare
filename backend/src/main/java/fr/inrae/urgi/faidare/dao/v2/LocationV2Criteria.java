package fr.inrae.urgi.faidare.dao.v2;



import java.util.List;

public class LocationV2Criteria {

    private List<String> abbreviation;
    private List<String> countryCode;
    private List<String> countryName;
    private List<String> documentationURL;
    private List<String> exposure;
    private List<String> externalReferenceId;
    private List<String> instituteAddress;
    private List<String> instituteName;
    private List<String> locationDbId;
    private List<String> locationName;
    private List<String> locationType;
    private List<String> parentLocationDbId;
    private List<String> parentLocationName;
    private List<String> siteStatus;
    private List<String> slope;
    private List<String> topography;
    private Integer page;
    private Integer pageSize;


    public List<String> getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(List<String> abbreviation) {
        this.abbreviation = abbreviation;
    }

    public List<String> getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(List<String> countryCode) {
        this.countryCode = countryCode;
    }

    public List<String> getCountryName() {
        return countryName;
    }

    public void setCountryName(List<String> countryName) {
        this.countryName = countryName;
    }

    public List<String> getDocumentationURL() {
        return documentationURL;
    }

    public void setDocumentationURL(List<String> documentationURL) {
        this.documentationURL = documentationURL;
    }

    public List<String> getExposure() {
        return exposure;
    }

    public void setExposure(List<String> exposure) {
        this.exposure = exposure;
    }

    public List<String> getExternalReferenceId() {
        return externalReferenceId;
    }

    public void setExternalReferenceId(List<String> externalReferenceId) {
        this.externalReferenceId = externalReferenceId;
    }

    public List<String> getInstituteAddress() {
        return instituteAddress;
    }

    public void setInstituteAddress(List<String> instituteAddress) {
        this.instituteAddress = instituteAddress;
    }

    public List<String> getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(List<String> instituteName) {
        this.instituteName = instituteName;
    }

    public List<String> getLocationDbId() {
        return locationDbId;
    }

    public void setLocationDbId(List<String> locationDbId) {
        this.locationDbId = locationDbId;
    }

    public List<String> getLocationName() {
        return locationName;
    }

    public void setLocationName(List<String> locationName) {
        this.locationName = locationName;
    }

    public List<String> getLocationType() {
        return locationType;
    }

    public void setLocationType(List<String> locationType) {
        this.locationType = locationType;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<String> getParentLocationDbId() {
        return parentLocationDbId;
    }

    public void setParentLocationDbId(List<String> parentLocationDbId) {
        this.parentLocationDbId = parentLocationDbId;
    }

    public List<String> getParentLocationName() {
        return parentLocationName;
    }

    public void setParentLocationName(List<String> parentLocationName) {
        this.parentLocationName = parentLocationName;
    }

    public List<String> getSiteStatus() {
        return siteStatus;
    }

    public void setSiteStatus(List<String> siteStatus) {
        this.siteStatus = siteStatus;
    }

    public List<String> getSlope() {
        return slope;
    }

    public void setSlope(List<String> slope) {
        this.slope = slope;
    }

    public List<String> getTopography() {
        return topography;
    }

    public void setTopography(List<String> topography) {
        this.topography = topography;
    }
}
