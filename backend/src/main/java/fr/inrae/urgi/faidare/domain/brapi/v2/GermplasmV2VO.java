package fr.inrae.urgi.faidare.domain.brapi.v2;

import fr.inrae.urgi.faidare.config.ElasticSearchConfig;
import fr.inrae.urgi.faidare.domain.*;
import org.springframework.context.annotation.Import;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;
import java.util.Objects;

//@Import({FaidareProperties.class})

@Import({ElasticSearchConfig.class})
@Document(
        indexName = "#{@faidarePropertiesBean.getAliasName('germplasm', 0L)}",
        createIndex = false
)
public class GermplasmV2VO {


    //public static final String INDEX_NAME = "faidare_germplasm_dev-group0";

    private String accessionNumber;

    private String acquisitionDate;

    // private ... additionalInfo;

    private String biologicalStatusOfAccessionCode;

    private String biologicalStatusOfAccessionDescription;

    private GermplasmInstituteVO breeder; //GnpIS

    private String breedingMethodDbId;

    private String breedingMethodName;

    private List<GenealogyVO> children; // GnpIS
    @Field(name = "originSite")
    private SiteVO collectingSite; //GnpIS

    private List<CollPopVO> collection;

    private GermplasmInstituteVO collector; //GnpIS

    private String comment; //GnpIS

    private String commonCropName;

    private String countryOfOriginCode;

    private String defaultDisplayName;

    private List<PuiNameValueVO> descriptors; //GnpIS
    @Field(name = "distributors")
    private List<GermplasmInstituteVO> distributors; //GnpIS

    private String documentationURL;
    @Field(name = "donorInfo")
    private List<DonorVO> donors;

    private List<SiteVO> evaluationSites; //GnpIS

    private List<ExternalReferencesVO> externalReferences;

    private GenealogyVO genealogy; //GnpIS

    private String geneticNature; //GnpIS

    private String genus;

    private String genusSpecies; //GnpIS

    private String genusSpeciesSubtaxa; //GnpIS

    private String germplasmDbId;

    private String germplasmName;

    private List<GermplasmOriginVO> germplasmOrigin;

    private String germplasmPUI;

    private String germplasmPreprocessing;

    private Long groupId; //GnpIS

    private InstituteVO holdingInstitute; //GnpIS

    private InstituteVO holdingGenbank; //GnpIS

    @Id
    private String _id;

    private String instituteCode;

    private String instituteName;

    private SiteVO originSite; //GnpIS

    private List<CollPopVO> panel; //GnpIS

    private String pedigree;

    private PhotoVO photo; //GnpIS

    private List<CollPopVO> population; //GnpIS

    private String presenceStatus; //GnpIS

    private String seedSource;

    private String seedSourceDescription;

    private String source;

    @Field("schema:includedInDataCatalog")
    private String sourceUri;

    private String species;

    private String speciesAuthority;

    private List<StorageTypesVO> storageTypes;

    private List<String> studyDbIds;

    private String subtaxa;

    private String subtaxaAuthority;

    @Field(name = "synonymsV2", type = FieldType.Nested)
    private List<SynonymsVO> synonyms;

    private String taxonComment; //GnpIS

    private List<String> taxonCommonNames; //GnpIS

    private List<TaxonSourceVO> taxonIds;

    private List<String> taxonSynonyms; //GnpIS

    private List<String> typeOfGermplasmStorageCode; //GnpIS


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

    public String getBiologicalStatusOfAccessionCode() {
        return biologicalStatusOfAccessionCode;
    }

    public void setBiologicalStatusOfAccessionCode(String biologicalStatusOfAccessionCode) {
        this.biologicalStatusOfAccessionCode = biologicalStatusOfAccessionCode;
    }

    public String getBiologicalStatusOfAccessionDescription() {
        return biologicalStatusOfAccessionDescription;
    }

    public void setBiologicalStatusOfAccessionDescription(String biologicalStatusOfAccessionDescription) {
        this.biologicalStatusOfAccessionDescription = biologicalStatusOfAccessionDescription;
    }

    public GermplasmInstituteVO getBreeder() {
        return breeder;
    }

    public void setBreeder(GermplasmInstituteVO breeder) {
        this.breeder = breeder;
    }

    public GermplasmInstituteVO getCollector() {
        return collector;
    }

    public void setCollector(GermplasmInstituteVO collector) {
        this.collector = collector;
    }

    public String getBreedingMethodDbId() {
        return breedingMethodDbId;
    }

    public void setBreedingMethodDbId(String breedingMethodDbId) {
        this.breedingMethodDbId = breedingMethodDbId;
    }

    public String getBreedingMethodName() {
        return breedingMethodName;
    }

    public void setBreedingMethodName(String breedingMethodName) {
        this.breedingMethodName = breedingMethodName;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public List<PuiNameValueVO> getDescriptors() {
        return descriptors;
    }

    public void setDescriptors(List<PuiNameValueVO> descriptors) {
        this.descriptors = descriptors;
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

    public List<ExternalReferencesVO> getExternalReferences() {
        return externalReferences;
    }

    public void setExternalReferences(List<ExternalReferencesVO> externalReferences) {
        this.externalReferences = externalReferences;
    }

    public GenealogyVO getGenealogy() {
        return genealogy;
    }

    public void setGenealogy(GenealogyVO genealogy) {
        this.genealogy = genealogy;
    }

    public String getGeneticNature() {
        return geneticNature;
    }

    public void setGeneticNature(String geneticNature) {
        this.geneticNature = geneticNature;
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

    public List<GermplasmOriginVO> getGermplasmOrigin() {
        return germplasmOrigin;
    }

    public void setGermplasmOrigin(List<GermplasmOriginVO> germplasmOrigin) {
        this.germplasmOrigin = germplasmOrigin;
    }

    public String getGermplasmPUI() {
        return germplasmPUI;
    }

    public void setGermplasmPUI(String germplasmPUI) {
        this.germplasmPUI = germplasmPUI;
    }

    public String getGermplasmPreprocessing() {
        return germplasmPreprocessing;
    }

    public void setGermplasmPreprocessing(String germplasmPreprocessing) {
        this.germplasmPreprocessing = germplasmPreprocessing;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public InstituteVO getHoldingInstitute() {
        return holdingInstitute;
    }

    public void setHoldingInstitute(InstituteVO holdingInstitute) {
        this.holdingInstitute = holdingInstitute;
    }

    public InstituteVO getHoldingGenbank() {
        return holdingGenbank;
    }

    public void setHoldingGenbank(InstituteVO holdingGenbank) {
        this.holdingGenbank = holdingGenbank;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public String getSeedSource() {
        return seedSource;
    }

    public void setSeedSource(String seedSource) {
        this.seedSource = seedSource;
    }

    public String getSeedSourceDescription() {
        return seedSourceDescription;
    }

    public void setSeedSourceDescription(String seedSourceDescription) {
        this.seedSourceDescription = seedSourceDescription;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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

    public List<StorageTypesVO> getStorageTypes() {
        return storageTypes;
    }

    public void setStorageTypes(List<StorageTypesVO> storageTypes) {
        this.storageTypes = storageTypes;
    }

    public List<String> getStudyDbIds() {
        return studyDbIds;
    }

    public void setStudyDbIds(List<String> studyDbIds) {
        this.studyDbIds = studyDbIds;
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

    public List<SynonymsVO> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<SynonymsVO> synonyms) {
        this.synonyms = synonyms;
    }

    public String getTaxonComment() {
        return taxonComment;
    }

    public void setTaxonComment(String taxonComment) {
        this.taxonComment = taxonComment;
    }

    public List<String> getTaxonCommonNames() {
        return taxonCommonNames;
    }

    public void setTaxonCommonNames(List<String> taxonCommonNames) {
        this.taxonCommonNames = taxonCommonNames;
    }

    public List<TaxonSourceVO> getTaxonIds() {
        return taxonIds;
    }

    public void setTaxonIds(List<TaxonSourceVO> taxonIds) {
        this.taxonIds = taxonIds;
    }

    public List<String> getTaxonSynonyms() {
        return taxonSynonyms;
    }

    public void setTaxonSynonyms(List<String> taxonSynonyms) {
        this.taxonSynonyms = taxonSynonyms;
    }

    public List<String> getTypeOfGermplasmStorageCode() {
        return typeOfGermplasmStorageCode;
    }

    public void setTypeOfGermplasmStorageCode(List<String> typeOfGermplasmStorageCode) {
        this.typeOfGermplasmStorageCode = typeOfGermplasmStorageCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GermplasmV2VO that = (GermplasmV2VO) o;
        return Objects.equals(accessionNumber, that.accessionNumber) && Objects.equals(acquisitionDate, that.acquisitionDate) && Objects.equals(biologicalStatusOfAccessionCode, that.biologicalStatusOfAccessionCode) && Objects.equals(biologicalStatusOfAccessionDescription, that.biologicalStatusOfAccessionDescription) && Objects.equals(breeder, that.breeder) && Objects.equals(breedingMethodDbId, that.breedingMethodDbId) && Objects.equals(breedingMethodName, that.breedingMethodName) && Objects.equals(children, that.children) && Objects.equals(collectingSite, that.collectingSite) && Objects.equals(collection, that.collection) && Objects.equals(collector, that.collector) && Objects.equals(comment, that.comment) && Objects.equals(commonCropName, that.commonCropName) && Objects.equals(countryOfOriginCode, that.countryOfOriginCode) && Objects.equals(defaultDisplayName, that.defaultDisplayName) && Objects.equals(descriptors, that.descriptors) && Objects.equals(distributors, that.distributors) && Objects.equals(documentationURL, that.documentationURL) && Objects.equals(donors, that.donors) && Objects.equals(evaluationSites, that.evaluationSites) && Objects.equals(externalReferences, that.externalReferences) && Objects.equals(genealogy, that.genealogy) && Objects.equals(geneticNature, that.geneticNature) && Objects.equals(genus, that.genus) && Objects.equals(genusSpecies, that.genusSpecies) && Objects.equals(genusSpeciesSubtaxa, that.genusSpeciesSubtaxa) && Objects.equals(germplasmDbId, that.germplasmDbId) && Objects.equals(germplasmName, that.germplasmName) && Objects.equals(germplasmOrigin, that.germplasmOrigin) && Objects.equals(germplasmPUI, that.germplasmPUI) && Objects.equals(germplasmPreprocessing, that.germplasmPreprocessing) && Objects.equals(groupId, that.groupId) && Objects.equals(holdingInstitute, that.holdingInstitute) && Objects.equals(holdingGenbank, that.holdingGenbank) && Objects.equals(_id, that._id) && Objects.equals(instituteCode, that.instituteCode) && Objects.equals(instituteName, that.instituteName) && Objects.equals(originSite, that.originSite) && Objects.equals(panel, that.panel) && Objects.equals(pedigree, that.pedigree) && Objects.equals(photo, that.photo) && Objects.equals(population, that.population) && Objects.equals(presenceStatus, that.presenceStatus) && Objects.equals(seedSource, that.seedSource) && Objects.equals(seedSourceDescription, that.seedSourceDescription) && Objects.equals(source, that.source) && Objects.equals(sourceUri, that.sourceUri) && Objects.equals(species, that.species) && Objects.equals(speciesAuthority, that.speciesAuthority) && Objects.equals(storageTypes, that.storageTypes) && Objects.equals(studyDbIds, that.studyDbIds) && Objects.equals(subtaxa, that.subtaxa) && Objects.equals(subtaxaAuthority, that.subtaxaAuthority) && Objects.equals(synonyms, that.synonyms) && Objects.equals(taxonComment, that.taxonComment) && Objects.equals(taxonCommonNames, that.taxonCommonNames) && Objects.equals(taxonIds, that.taxonIds) && Objects.equals(taxonSynonyms, that.taxonSynonyms) && Objects.equals(typeOfGermplasmStorageCode, that.typeOfGermplasmStorageCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessionNumber, acquisitionDate, biologicalStatusOfAccessionCode, biologicalStatusOfAccessionDescription, breeder, breedingMethodDbId, breedingMethodName, children, collectingSite, collection, collector, comment, commonCropName, countryOfOriginCode, defaultDisplayName, descriptors, distributors, documentationURL, donors, evaluationSites, externalReferences, genealogy, geneticNature, genus, genusSpecies, genusSpeciesSubtaxa, germplasmDbId, germplasmName, germplasmOrigin, germplasmPUI, germplasmPreprocessing, groupId, holdingInstitute, holdingGenbank, _id, instituteCode, instituteName, originSite, panel, pedigree, photo, population, presenceStatus, seedSource, seedSourceDescription, source, sourceUri, species, speciesAuthority, storageTypes, studyDbIds, subtaxa, subtaxaAuthority, synonyms, taxonComment, taxonCommonNames, taxonIds, taxonSynonyms, typeOfGermplasmStorageCode);
    }

    @Override
    public String toString() {
        return "GermplasmV2VO{" +
            "accessionNumber='" + accessionNumber + '\'' +
            ", acquisitionDate='" + acquisitionDate + '\'' +
            ", biologicalStatusOfAccessionCode='" + biologicalStatusOfAccessionCode + '\'' +
            ", biologicalStatusOfAccessionDescription='" + biologicalStatusOfAccessionDescription + '\'' +
            ", breeder=" + breeder +
            ", breedingMethodDbId='" + breedingMethodDbId + '\'' +
            ", breedingMethodName='" + breedingMethodName + '\'' +
            ", children=" + children +
            ", collectingSite=" + collectingSite +
            ", collection=" + collection +
            ", collector=" + collector +
            ", comment='" + comment + '\'' +
            ", commonCropName='" + commonCropName + '\'' +
            ", countryOfOriginCode='" + countryOfOriginCode + '\'' +
            ", defaultDisplayName='" + defaultDisplayName + '\'' +
            ", descriptors=" + descriptors +
            ", distributors=" + distributors +
            ", documentationURL='" + documentationURL + '\'' +
            ", donors=" + donors +
            ", evaluationSites=" + evaluationSites +
            ", externalReferences=" + externalReferences +
            ", genealogy=" + genealogy +
            ", geneticNature='" + geneticNature + '\'' +
            ", genus='" + genus + '\'' +
            ", genusSpecies='" + genusSpecies + '\'' +
            ", genusSpeciesSubtaxa='" + genusSpeciesSubtaxa + '\'' +
            ", germplasmDbId='" + germplasmDbId + '\'' +
            ", germplasmName='" + germplasmName + '\'' +
            ", germplasmOrigin=" + germplasmOrigin +
            ", germplasmPUI='" + germplasmPUI + '\'' +
            ", germplasmPreprocessing='" + germplasmPreprocessing + '\'' +
            ", groupId=" + groupId +
            ", holdingInstitute=" + holdingInstitute +
            ", holdingGenbank=" + holdingGenbank +
            ", _id='" + _id + '\'' +
            ", instituteCode='" + instituteCode + '\'' +
            ", instituteName='" + instituteName + '\'' +
            ", originSite=" + originSite +
            ", panel=" + panel +
            ", pedigree='" + pedigree + '\'' +
            ", photo=" + photo +
            ", population=" + population +
            ", presenceStatus='" + presenceStatus + '\'' +
            ", seedSource='" + seedSource + '\'' +
            ", seedSourceDescription='" + seedSourceDescription + '\'' +
            ", source='" + source + '\'' +
            ", sourceUri='" + sourceUri + '\'' +
            ", species='" + species + '\'' +
            ", speciesAuthority='" + speciesAuthority + '\'' +
            ", storageTypes=" + storageTypes +
            ", studyDbIds=" + studyDbIds +
            ", subtaxa='" + subtaxa + '\'' +
            ", subtaxaAuthority='" + subtaxaAuthority + '\'' +
            ", synonyms=" + synonyms +
            ", taxonComment='" + taxonComment + '\'' +
            ", taxonCommonNames=" + taxonCommonNames +
            ", taxonIds=" + taxonIds +
            ", taxonSynonyms=" + taxonSynonyms +
            ", typeOfGermplasmStorageCode=" + typeOfGermplasmStorageCode +
            '}';
    }

}
