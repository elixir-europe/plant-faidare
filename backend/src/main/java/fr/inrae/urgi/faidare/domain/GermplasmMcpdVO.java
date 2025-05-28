package fr.inrae.urgi.faidare.domain;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

//indexName = "#{@dataDiscoveryProperties.getElasticsearchPrefix}resource-alias"
@Document(
        indexName = "faidare_germplasm_dev-group0",
        //indexName = "faidare_germplasm_beta-group0",
        createIndex = false
)
//@Mapping(mappingPath = "fr/inra/urgi/datadiscovery/domain/faidare/FaidareGeneticResource.mapping.json")
public final class GermplasmMcpdVO {

    private List<String> accessionNames;

    private String accessionNumber;

    private String acquisitionDate;

    private String acquisitionSourceCode;

    private List<String> alternateIDs;



    @Field(name="germplasmName") //for POC validation, totally fake
    private String ancestralData;

    @Field(name="germplasmPUI")
    private String PUID;

    //private String biologicalStatusOfAccessionCode;

    //private List<GermplasmInstitute> breedingInstitutes;

    //private GermplasmCollectingInfo collectingInfo;

    //private String commonCropName;

    private String countryOfOriginCode;

    //private String defaultDisplayName;

    private String documentationURL;

    //private List<DonorInfoVO> donorInfo;

    //private List<GermplasmDonor> donors;

    private String genus;


    //@Field(type=FieldType.Text)
    //@Id
    private String germplasmDbId;

    @JsonProperty("@id")
    @Field(name="germplasmURI")
    private String id;

    @Id
    private String _id;
    //private String germplasmName;

    //private String germplasmPUI;

    private String instituteCode;

    private String instituteName;

    private String mlsStatus;

    private String pedigree;

    private String remarks;

    //private List<GermplasmInstitute> safetyDuplicateInstitutes;

    //private String seedSource;

    private String species;

    private String speciesAuthority;

    //private List<String> storageTypeCodes;

    //To demonstrate that an existing field in ES can safely be ignored in the VO
   // private String subtaxa;

    //private String subtaxaAuthority;

   // private String subtaxon;

    //private String subtaxonAuthority;

    //private List<String> synonyms;

    //private List<TaxonSource> taxonIds;

    //private List<String> typeOfGermplasmStorageCode;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GermplasmMcpdVO that = (GermplasmMcpdVO) o;
        return Objects.equals(accessionNames, that.accessionNames) && Objects.equals(accessionNumber, that.accessionNumber) && Objects.equals(acquisitionDate, that.acquisitionDate) && Objects.equals(acquisitionSourceCode, that.acquisitionSourceCode) && Objects.equals(alternateIDs, that.alternateIDs) && Objects.equals(ancestralData, that.ancestralData) && PUID.equals(that.PUID) && Objects.equals(countryOfOriginCode, that.countryOfOriginCode) && Objects.equals(documentationURL, that.documentationURL) && Objects.equals(genus, that.genus) && germplasmDbId.equals(that.germplasmDbId) && Objects.equals(instituteCode, that.instituteCode) && Objects.equals(instituteName, that.instituteName) && Objects.equals(mlsStatus, that.mlsStatus) && Objects.equals(pedigree, that.pedigree) && Objects.equals(remarks, that.remarks) && Objects.equals(species, that.species) && Objects.equals(speciesAuthority, that.speciesAuthority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessionNames, accessionNumber, acquisitionDate, acquisitionSourceCode, alternateIDs, ancestralData, PUID, countryOfOriginCode, documentationURL, genus, germplasmDbId, instituteCode, instituteName, mlsStatus, pedigree, remarks, species, speciesAuthority);
    }

    public List<String> getAccessionNames() {
        return accessionNames;
    }

    public void setAccessionNames(List<String> accessionNames) {
        this.accessionNames = accessionNames;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccessionNumber() {
        return accessionNumber;
    }

    public void setAccessionNumber(String accessionNumber) {
        this.accessionNumber = accessionNumber;
    }

    public String getAcquisitionDate() {
        return acquisitionDate;
    }

    public void setAcquisitionDate(String acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public String getAcquisitionSourceCode() {
        return acquisitionSourceCode;
    }

    public void setAcquisitionSourceCode(String acquisitionSourceCode) {
        this.acquisitionSourceCode = acquisitionSourceCode;
    }

    public List<String> getAlternateIDs() {
        return alternateIDs;
    }

    public void setAlternateIDs(List<String> alternateIDs) {
        this.alternateIDs = alternateIDs;
    }

    public String getAncestralData() {
        return ancestralData;
    }

    public void setAncestralData(String ancestralData) {
        this.ancestralData = ancestralData;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getPUID() {
        return PUID;
    }

    public void setPUID(String PUID) {
        this.PUID = PUID;
    }

    public String getCountryOfOriginCode() {
        return countryOfOriginCode;
    }

    public void setCountryOfOriginCode(String countryOfOriginCode) {
        this.countryOfOriginCode = countryOfOriginCode;
    }

    public String getDocumentationURL() {
        return documentationURL;
    }

    public void setDocumentationURL(String documentationURL) {
        this.documentationURL = documentationURL;
    }

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }

    public String getGermplasmDbId() {
        return germplasmDbId;
    }

    public void setGermplasmDbId(String germplasmDbId) {
        this.germplasmDbId = germplasmDbId;
    }

    public String getInstituteCode() {
        return instituteCode;
    }

    public void setInstituteCode(String instituteCode) {
        this.instituteCode = instituteCode;
    }

    public String getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
    }

    public String getMlsStatus() {
        return mlsStatus;
    }

    public void setMlsStatus(String mlsStatus) {
        this.mlsStatus = mlsStatus;
    }

    public String getPedigree() {
        return pedigree;
    }

    public void setPedigree(String pedigree) {
        this.pedigree = pedigree;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getSpeciesAuthority() {
        return speciesAuthority;
    }

    public void setSpeciesAuthority(String speciesAuthority) {
        this.speciesAuthority = speciesAuthority;
    }

    @Override
    public String toString() {
        return "GermplasmMcpdVO{" +
                "accessionNames=" + accessionNames +
                ", accessionNumber='" + accessionNumber + '\'' +
                ", acquisitionDate='" + acquisitionDate + '\'' +
                ", acquisitionSourceCode='" + acquisitionSourceCode + '\'' +
                ", alternateIDs=" + alternateIDs +
                ", ancestralData='" + ancestralData + '\'' +
                ", PUID='" + PUID + '\'' +
                ", countryOfOriginCode='" + countryOfOriginCode + '\'' +
                ", documentationURL='" + documentationURL + '\'' +
                ", genus='" + genus + '\'' +
                ", germplasmDbId='" + germplasmDbId + '\'' +
                ", instituteCode='" + instituteCode + '\'' +
                ", instituteName='" + instituteName + '\'' +
                ", mlsStatus='" + mlsStatus + '\'' +
                ", pedigree='" + pedigree + '\'' +
                ", remarks='" + remarks + '\'' +
                ", species='" + species + '\'' +
                ", speciesAuthority='" + speciesAuthority + '\'' +
                '}';
    }
}


/*
List<String> getAccessionNames();

    String getAccessionNumber();

    String getAcquisitionDate();

    String getAcquisitionSourceCode();

    List<String> getAlternateIDs();

    String getAncestralData();

    String getBiologicalStatusOfAccessionCode();

    List<GermplasmInstitute> getBreedingInstitutes();

    GermplasmCollectingInfo getCollectingInfo();

    String getCommonCropName();

    String getCountryOfOriginCode();

    String getDefaultDisplayName();

    String getDocumentationURL();

    List<DonorInfoVO> getDonorInfo();

    List<GermplasmDonor> getDonors();

    String getGenus();

    String getGermplasmDbId();

    String getGermplasmName();

    String getGermplasmPUI();

    String getInstituteCode();

    String getInstituteName();

    String getMlsStatus();

    String getPedigree();

    String getRemarks();

    List<GermplasmInstitute> getSafetyDuplicateInstitutes();

    String getSeedSource();

    String getSpecies();

    String getSpeciesAuthority();

    List<String> getStorageTypeCodes();

    String getSubtaxa();

    String getSubtaxaAuthority();

    String getSubtaxon();

    String getSubtaxonAuthority();

    List<String> getSynonyms();

    List<TaxonSource> getTaxonIds();

    List<String> getTypeOfGermplasmStorageCode();

*/
