package fr.inrae.urgi.faidare.domain.brapi.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.inrae.urgi.faidare.config.ElasticSearchConfig;
import fr.inrae.urgi.faidare.domain.*;
import org.springframework.context.annotation.Import;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.util.List;
import java.util.Objects;

@Import({ElasticSearchConfig.class})
@Document(
    indexName = "#{@faidarePropertiesBean.getAliasName('germplasm', 0L)}",
    createIndex = false
)
public final class GermplasmV1VO {
    //public static final String INDEX_NAME = "faidare_germplasm_dev-group0";

    private List<String> accessionNames;
    private String accessionNumber;
    private String acquisitionDate;
    private String acquisitionSourceCode;
    private List<String> alternateIDs;
    private String ancestralData;
    private String biologicalStatusOfAccessionCode;
    private GermplasmInstituteVO breeder;
    private List<GenealogyVO> children;
    @Field(name = "originSite")
    private SiteVO collectingSite; //GnpIS
    private List<CollPopVO> collection;
    @Field(name = "collector")
    private GermplasmInstituteVO collector;
    private String commonCropName;
    private String countryOfOriginCode;
    private List<GermplasmInstituteVO> distributors;
    //private List<GermplasmInstitute> breedingInstitutes;
    //private GermplasmCollectingInfo collectingInfo;
    private String defaultDisplayName;
    @Field(name = "documentationURL")
    private String url;
    private String documentationURL;
    private List<DonorVO> donors;
    private List<SiteVO> evaluationSites; //GnpIS
    private String genus;
    private String genusSpecies; //GnpIS
    private String genusSpeciesSubtaxa; //GnpIS
    private String germplasmDbId;
    private String germplasmName;
    private String germplasmPUI;
    private Long groupId; //GnpIS
    @Field(name = "germplasmPUI")
    private String germplasmURI;
    @JsonProperty("@id")
    @Field(name = "germplasmURI")
    private String id;
    private InstituteVO holdingInstitute; //GnpIS
    private InstituteVO holdingGenbank; //GnpIS
    @Id
    private String _id;
    private String instituteCode;
    private String instituteName;
    private String mlsStatus;
    private SiteVO originSite; //GnpIS
    private List<CollPopVO> panel; //GnpIS
    private String pedigree;
    private PhotoVO photo; //GnpIS
    private List<CollPopVO> population; //GnpIS
    private String presenceStatus; //GnpIS
    private String remarks;
    @JsonProperty("schema:name")
    @Field("schema:name")
    private String schemaName;
    @JsonProperty("schema:identifier")
    @Field("schema:identifier")
    private String schemaId;
    @JsonProperty("schema:includedInDataCatalog")
    @Field("schema:includedInDataCatalog")
    private String schemaCatalog;
    private String seedSource;
    @Field("schema:includedInDataCatalog")
    private String sourceUri;
    private String species;
    //private List<GermplasmInstitute> safetyDuplicateInstitutes;
    private String speciesAuthority;
    private List<String> storageTypeCodes;
    private List<String> studyDbIds;
    private List<String> studyURIs;
    private String subtaxa;
    private String subtaxaAuthority;
    private String subtaxon;
    private List<TaxonSourceVO> taxonIds;
    private String subtaxonAuthority;
    @Field(name = "synonyms")
    private List<String> synonyms;
    private List<String> taxonCommonNames;
    private List<String> typeOfGermplasmStorageCode;
    private String taxonComment;
    private List<String> taxonSynonyms;
    private String geneticNature;
    private String comment;
    @JsonProperty("@type")
    private String type = "germplasm";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GermplasmV1VO that = (GermplasmV1VO) o;
        return Objects.equals(accessionNames, that.accessionNames) && Objects.equals(accessionNumber, that.accessionNumber) && Objects.equals(acquisitionDate, that.acquisitionDate) && Objects.equals(acquisitionSourceCode, that.acquisitionSourceCode) && Objects.equals(alternateIDs, that.alternateIDs) && Objects.equals(ancestralData, that.ancestralData) && Objects.equals(biologicalStatusOfAccessionCode, that.biologicalStatusOfAccessionCode) && Objects.equals(breeder, that.breeder) && Objects.equals(children, that.children) && Objects.equals(collectingSite, that.collectingSite) && Objects.equals(collection, that.collection) && Objects.equals(collector, that.collector) && Objects.equals(commonCropName, that.commonCropName) && Objects.equals(countryOfOriginCode, that.countryOfOriginCode) && Objects.equals(distributors, that.distributors) && Objects.equals(defaultDisplayName, that.defaultDisplayName) && Objects.equals(url, that.url) && Objects.equals(documentationURL, that.documentationURL) && Objects.equals(donors, that.donors) && Objects.equals(evaluationSites, that.evaluationSites) && Objects.equals(genus, that.genus) && Objects.equals(genusSpecies, that.genusSpecies) && Objects.equals(genusSpeciesSubtaxa, that.genusSpeciesSubtaxa) && Objects.equals(germplasmDbId, that.germplasmDbId) && Objects.equals(germplasmName, that.germplasmName) && Objects.equals(germplasmPUI, that.germplasmPUI) && Objects.equals(groupId, that.groupId) && Objects.equals(germplasmURI, that.germplasmURI) && Objects.equals(id, that.id) && Objects.equals(holdingInstitute, that.holdingInstitute) && Objects.equals(holdingGenbank, that.holdingGenbank) && Objects.equals(_id, that._id) && Objects.equals(instituteCode, that.instituteCode) && Objects.equals(instituteName, that.instituteName) && Objects.equals(mlsStatus, that.mlsStatus) && Objects.equals(originSite, that.originSite) && Objects.equals(panel, that.panel) && Objects.equals(pedigree, that.pedigree) && Objects.equals(photo, that.photo) && Objects.equals(population, that.population) && Objects.equals(presenceStatus, that.presenceStatus) && Objects.equals(remarks, that.remarks) && Objects.equals(schemaName, that.schemaName) && Objects.equals(schemaId, that.schemaId) && Objects.equals(schemaCatalog, that.schemaCatalog) && Objects.equals(seedSource, that.seedSource) && Objects.equals(sourceUri, that.sourceUri) && Objects.equals(species, that.species) && Objects.equals(speciesAuthority, that.speciesAuthority) && Objects.equals(storageTypeCodes, that.storageTypeCodes) && Objects.equals(studyDbIds, that.studyDbIds) && Objects.equals(studyURIs, that.studyURIs) && Objects.equals(subtaxa, that.subtaxa) && Objects.equals(subtaxaAuthority, that.subtaxaAuthority) && Objects.equals(subtaxon, that.subtaxon) && Objects.equals(taxonIds, that.taxonIds) && Objects.equals(subtaxonAuthority, that.subtaxonAuthority) && Objects.equals(synonyms, that.synonyms) && Objects.equals(taxonCommonNames, that.taxonCommonNames) && Objects.equals(typeOfGermplasmStorageCode, that.typeOfGermplasmStorageCode) && Objects.equals(taxonComment, that.taxonComment) && Objects.equals(taxonSynonyms, that.taxonSynonyms) && Objects.equals(geneticNature, that.geneticNature) && Objects.equals(comment, that.comment) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessionNames, accessionNumber, acquisitionDate, acquisitionSourceCode, alternateIDs, ancestralData, biologicalStatusOfAccessionCode, breeder, children, collectingSite, collection, collector, commonCropName, countryOfOriginCode, distributors, defaultDisplayName, url, documentationURL, donors, evaluationSites, genus, genusSpecies, genusSpeciesSubtaxa, germplasmDbId, germplasmName, germplasmPUI, groupId, germplasmURI, id, holdingInstitute, holdingGenbank, _id, instituteCode, instituteName, mlsStatus, originSite, panel, pedigree, photo, population, presenceStatus, remarks, schemaName, schemaId, schemaCatalog, seedSource, sourceUri, species, speciesAuthority, storageTypeCodes, studyDbIds, studyURIs, subtaxa, subtaxaAuthority, subtaxon, taxonIds, subtaxonAuthority, synonyms, taxonCommonNames, typeOfGermplasmStorageCode, taxonComment, taxonSynonyms, geneticNature, comment, type);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getGeneticNature() {
        return geneticNature;
    }

    public void setGeneticNature(String geneticNature) {
        this.geneticNature = geneticNature;
    }

    public List<String> getTaxonSynonyms() {
        return taxonSynonyms;
    }

    public void setTaxonSynonyms(List<String> taxonSynonyms) {
        this.taxonSynonyms = taxonSynonyms;
    }

    public String getTaxonComment() {
        return taxonComment;
    }

    public void setTaxonComment(String taxonComment) {
        this.taxonComment = taxonComment;
    }

    public List<TaxonSourceVO> getTaxonIds() {
        return taxonIds;
    }

    public void setTaxonIds(List<TaxonSourceVO> taxonIds) {
        this.taxonIds = taxonIds;
    }

    public List<String> getAccessionNames() {
        return accessionNames;
    }

    public void setAccessionNames(List<String> accessionNames) {
        this.accessionNames = accessionNames;
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

    public String getBiologicalStatusOfAccessionCode() {
        return biologicalStatusOfAccessionCode;
    }

    public void setBiologicalStatusOfAccessionCode(String biologicalStatusOfAccessionCode) {
        this.biologicalStatusOfAccessionCode = biologicalStatusOfAccessionCode;
    }

    public GermplasmInstituteVO getBreeder() {
        return breeder;
    }

    public void setBreeder(GermplasmInstituteVO breeder) {
        this.breeder = breeder;
    }

    public List<GenealogyVO> getChildren() {
        return children;
    }

    public void setChildren(List<GenealogyVO> children) {
        this.children = children;
    }

    public SiteVO getCollectingSite() {
        return collectingSite;
    }

    public void setCollectingSite(SiteVO collectingSite) {
        this.collectingSite = collectingSite;
    }

    public List<CollPopVO> getCollection() {
        return collection;
    }

    public void setCollection(List<CollPopVO> collection) {
        this.collection = collection;
    }

    public GermplasmInstituteVO getCollector() {
        return collector;
    }

    public void setCollector(GermplasmInstituteVO collector) {
        this.collector = collector;
    }

    public String getCommonCropName() {
        return commonCropName;
    }

    public void setCommonCropName(String commonCropName) {
        this.commonCropName = commonCropName;
    }

    public String getCountryOfOriginCode() {
        return countryOfOriginCode;
    }

    public void setCountryOfOriginCode(String countryOfOriginCode) {
        this.countryOfOriginCode = countryOfOriginCode;
    }

    public String getDefaultDisplayName() {
        return defaultDisplayName;
    }

    public void setDefaultDisplayName(String defaultDisplayName) {
        this.defaultDisplayName = defaultDisplayName;
    }

    public List<GermplasmInstituteVO> getDistributors() {
        return distributors;
    }

    public void setDistributors(List<GermplasmInstituteVO> distributors) {
        this.distributors = distributors;
    }

    public String getDocumentationURL() {
        return documentationURL;
    }

    public void setDocumentationURL(String documentationURL) {
        this.documentationURL = documentationURL;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<DonorVO> getDonors() {
        return donors;
    }

    public void setDonors(List<DonorVO> donors) {
        this.donors = donors;
    }

    public List<SiteVO> getEvaluationSites() {
        return evaluationSites;
    }

    public void setEvaluationSites(List<SiteVO> evaluationSites) {
        this.evaluationSites = evaluationSites;
    }

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }

    public String getGenusSpecies() {
        return genusSpecies;
    }

    public void setGenusSpecies(String genusSpecies) {
        this.genusSpecies = genusSpecies;
    }

    public String getGenusSpeciesSubtaxa() {
        return genusSpeciesSubtaxa;
    }

    public void setGenusSpeciesSubtaxa(String genusSpeciesSubtaxa) {
        this.genusSpeciesSubtaxa = genusSpeciesSubtaxa;
    }

    public String getGermplasmDbId() {
        return germplasmDbId;
    }

    public void setGermplasmDbId(String germplasmDbId) {
        this.germplasmDbId = germplasmDbId;
    }

    public String getGermplasmName() {
        return germplasmName;
    }

    public void setGermplasmName(String germplasmName) {
        this.germplasmName = germplasmName;
    }

    public String getGermplasmPUI() {
        return germplasmPUI;
    }

    public void setGermplasmPUI(String germplasmPUI) {
        this.germplasmPUI = germplasmPUI;
    }

    public String getGermplasmURI() {
        return germplasmURI;
    }

    public void setGermplasmURI(String germplasmURI) {
        this.germplasmURI = germplasmURI;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public InstituteVO getHoldingGenbank() {
        return holdingGenbank;
    }

    public void setHoldingGenbank(InstituteVO holdingGenbank) {
        this.holdingGenbank = holdingGenbank;
    }

    public InstituteVO getHoldingInstitute() {
        return holdingInstitute;
    }

    public void setHoldingInstitute(InstituteVO holdingInstitute) {
        this.holdingInstitute = holdingInstitute;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public SiteVO getOriginSite() {
        return originSite;
    }

    public void setOriginSite(SiteVO originSite) {
        this.originSite = originSite;
    }

    public List<CollPopVO> getPanel() {
        return panel;
    }

    public void setPanel(List<CollPopVO> panel) {
        this.panel = panel;
    }

    public String getPedigree() {
        return pedigree;
    }

    public void setPedigree(String pedigree) {
        this.pedigree = pedigree;
    }

    public PhotoVO getPhoto() {
        return photo;
    }

    public void setPhoto(PhotoVO photo) {
        this.photo = photo;
    }

    public List<CollPopVO> getPopulation() {
        return population;
    }

    public void setPopulation(List<CollPopVO> population) {
        this.population = population;
    }

    public String getPresenceStatus() {
        return presenceStatus;
    }

    public void setPresenceStatus(String presenceStatus) {
        this.presenceStatus = presenceStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSchemaCatalog() {
        return schemaCatalog;
    }

    public void setSchemaCatalog(String schemaCatalog) {
        this.schemaCatalog = schemaCatalog;
    }

    public String getSchemaId() {
        return schemaId;
    }

    public void setSchemaId(String schemaId) {
        this.schemaId = schemaId;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getSeedSource() {
        return seedSource;
    }

    public void setSeedSource(String seedSource) {
        this.seedSource = seedSource;
    }

    public String getSourceUri() {
        return sourceUri;
    }

    public void setSourceUri(String sourceUri) {
        this.sourceUri = sourceUri;
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

    public List<String> getStorageTypeCodes() {
        return storageTypeCodes;
    }

    public void setStorageTypeCodes(List<String> storageTypeCodes) {
        this.storageTypeCodes = storageTypeCodes;
    }

    public List<String> getStudyDbIds() {
        return studyDbIds;
    }

    public void setStudyDbIds(List<String> studyDbIds) {
        this.studyDbIds = studyDbIds;
    }

    public List<String> getStudyURIs() {
        return studyURIs;
    }

    public void setStudyURIs(List<String> studyURIs) {
        this.studyURIs = studyURIs;
    }

    public String getSubtaxa() {
        return subtaxa;
    }

    public void setSubtaxa(String subtaxa) {
        this.subtaxa = subtaxa;
    }

    public String getSubtaxaAuthority() {
        return subtaxaAuthority;
    }

    public void setSubtaxaAuthority(String subtaxaAuthority) {
        this.subtaxaAuthority = subtaxaAuthority;
    }

    public String getSubtaxon() {
        return subtaxon;
    }

    public void setSubtaxon(String subtaxon) {
        this.subtaxon = subtaxon;
    }

    public String getSubtaxonAuthority() {
        return subtaxonAuthority;
    }

    public void setSubtaxonAuthority(String subtaxonAuthority) {
        this.subtaxonAuthority = subtaxonAuthority;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    public List<String> getTaxonCommonNames() {
        return taxonCommonNames;
    }

    public void setTaxonCommonNames(List<String> taxonCommonNames) {
        this.taxonCommonNames = taxonCommonNames;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getTypeOfGermplasmStorageCode() {
        return typeOfGermplasmStorageCode;
    }

    public void setTypeOfGermplasmStorageCode(List<String> typeOfGermplasmStorageCode) {
        this.typeOfGermplasmStorageCode = typeOfGermplasmStorageCode;
    }

    @Override
    public String toString() {
        return "GermplasmV1VO{" +
            "accessionNames=" + accessionNames +
            ", accessionNumber='" + accessionNumber + '\'' +
            ", acquisitionDate='" + acquisitionDate + '\'' +
            ", acquisitionSourceCode='" + acquisitionSourceCode + '\'' +
            ", alternateIDs=" + alternateIDs +
            ", ancestralData='" + ancestralData + '\'' +
            ", biologicalStatusOfAccessionCode='" + biologicalStatusOfAccessionCode + '\'' +
            ", breeder=" + breeder +
            ", children=" + children +
            ", collectingSite=" + collectingSite +
            ", collection=" + collection +
            ", collector=" + collector +
            ", commonCropName='" + commonCropName + '\'' +
            ", countryOfOriginCode='" + countryOfOriginCode + '\'' +
            ", distributors=" + distributors +
            ", defaultDisplayName='" + defaultDisplayName + '\'' +
            ", url='" + url + '\'' +
            ", documentationURL='" + documentationURL + '\'' +
            ", donors=" + donors +
            ", evaluationSites=" + evaluationSites +
            ", genus='" + genus + '\'' +
            ", genusSpecies='" + genusSpecies + '\'' +
            ", genusSpeciesSubtaxa='" + genusSpeciesSubtaxa + '\'' +
            ", germplasmDbId='" + germplasmDbId + '\'' +
            ", germplasmName='" + germplasmName + '\'' +
            ", germplasmPUI='" + germplasmPUI + '\'' +
            ", groupId=" + groupId +
            ", germplasmURI='" + germplasmURI + '\'' +
            ", id='" + id + '\'' +
            ", holdingInstitute=" + holdingInstitute +
            ", holdingGenbank=" + holdingGenbank +
            ", _id='" + _id + '\'' +
            ", instituteCode='" + instituteCode + '\'' +
            ", instituteName='" + instituteName + '\'' +
            ", mlsStatus='" + mlsStatus + '\'' +
            ", originSite=" + originSite +
            ", panel=" + panel +
            ", pedigree='" + pedigree + '\'' +
            ", photo=" + photo +
            ", population=" + population +
            ", presenceStatus='" + presenceStatus + '\'' +
            ", remarks='" + remarks + '\'' +
            ", schemaName='" + schemaName + '\'' +
            ", schemaId='" + schemaId + '\'' +
            ", schemaCatalog='" + schemaCatalog + '\'' +
            ", seedSource='" + seedSource + '\'' +
            ", sourceUri='" + sourceUri + '\'' +
            ", species='" + species + '\'' +
            ", speciesAuthority='" + speciesAuthority + '\'' +
            ", storageTypeCodes=" + storageTypeCodes +
            ", studyDbIds=" + studyDbIds +
            ", studyURIs=" + studyURIs +
            ", subtaxa='" + subtaxa + '\'' +
            ", subtaxaAuthority='" + subtaxaAuthority + '\'' +
            ", subtaxon='" + subtaxon + '\'' +
            ", taxonIds=" + taxonIds +
            ", subtaxonAuthority='" + subtaxonAuthority + '\'' +
            ", synonyms=" + synonyms +
            ", taxonCommonNames=" + taxonCommonNames +
            ", typeOfGermplasmStorageCode=" + typeOfGermplasmStorageCode +
            ", taxonComment='" + taxonComment + '\'' +
            ", taxonSynonyms=" + taxonSynonyms +
            ", geneticNature='" + geneticNature + '\'' +
            ", comment='" + comment + '\'' +
            ", type='" + type + '\'' +
            '}';
    }
}
