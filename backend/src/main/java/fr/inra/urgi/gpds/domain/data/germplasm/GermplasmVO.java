package fr.inra.urgi.gpds.domain.data.germplasm;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiGermplasm;
import fr.inra.urgi.gpds.domain.jsonld.data.HasURI;
import fr.inra.urgi.gpds.domain.jsonld.data.HasURL;
import fr.inra.urgi.gpds.domain.jsonld.data.IncludedInDataCatalog;
import fr.inra.urgi.gpds.elasticsearch.document.annotation.Document;
import fr.inra.urgi.gpds.elasticsearch.document.annotation.Id;

import java.io.Serializable;
import java.util.List;

/**
 * Germplasm model based on {@link BrapiGermplasm} and extended with GnpIs specific fields (see {@link ExtendedGermplasm}
 *
 * @author C. Michotey, gcornut
 */
@Document(type = "germplasm")
public class GermplasmVO
    implements Serializable, BrapiGermplasm, ExtendedGermplasm, HasURI, HasURL, IncludedInDataCatalog {

    private static final long serialVersionUID = -7719928853974382749L;

    private Long groupId;
    private List<Long> speciesGroup;

    @Id
    private String germplasmDbId;
    private String defaultDisplayName;
    private String accessionNumber;
    private String germplasmName;
    private String pedigree;
    private String seedSource;
    private List<String> synonyms;
    private String commonCropName;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private String instituteCode = "";

    private String instituteName;
    private String biologicalStatusOfAccessionCode;
    private String countryOfOriginCode;
    private List<String> typeOfGermplasmStorageCode;
    private List<TaxonSourceVO> taxonIds;
    private List<DonorVO> donors;

    private String genus;
    private String species;
    private String speciesAuthority;
    private String subtaxa;
    private String subtaxaAuthority;
    private String acquisitionDate;

    private List<String> taxonSynonyms;
    private List<String> taxonCommonNames;
    private String taxonComment;
    private String geneticNature;
    private String comment;
    private PhotoVO photo;
    private InstituteVO holdingInstitute;
    private InstituteVO holdingGenbank;
    private String presenceStatus;

    private GenealogyVO genealogy;
    private List<GenealogyVO> children;
    private List<SimpleVO> descriptors;

    private SiteVO originSite;
    private SiteVO collectingSite;
    private List<SiteVO> evaluationSites;

    private GermplasmInstituteVO collector;
    private GermplasmInstituteVO breeder;
    private List<GermplasmInstituteVO> distributors;

    private List<CollPopVO> panel;
    private List<CollPopVO> collection;
    private List<CollPopVO> population;

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
    public String getSourceUri() {
        return this.sourceUri;
    }

    public void setSourceUri(String sourceUri) {
        this.sourceUri = sourceUri;
    }

    @Override
    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getDocumentationURL() {
        return url;
    }

    public void setDocumentationURL(String documentationURL) {
        this.url = documentationURL;
    }


    @Override
    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @Override
    public List<Long> getSpeciesGroup() {
        return speciesGroup;
    }

    public void setSpeciesGroup(List<Long> speciesGroup) {
        this.speciesGroup = speciesGroup;
    }

    @Override
    public String getGermplasmDbId() {
        return germplasmDbId;
    }

    public void setGermplasmDbId(String germplasmDbId) {
        this.germplasmDbId = germplasmDbId;
    }

    @Override
    public String getDefaultDisplayName() {
        return defaultDisplayName;
    }

    public void setDefaultDisplayName(String defaultDisplayName) {
        this.defaultDisplayName = defaultDisplayName;
    }

    @Override
    public String getAccessionNumber() {
        return accessionNumber;
    }

    public void setAccessionNumber(String accessionNumber) {
        this.accessionNumber = accessionNumber;
    }

    @Override
    public String getGermplasmName() {
        return germplasmName;
    }

    public void setGermplasmName(String germplasmName) {
        this.germplasmName = germplasmName;
    }

    @Override
    public String getGermplasmPUI() {
        return uri;
    }

    public void setGermplasmPUI(String germplasmPUI) {
        this.uri = germplasmPUI;
    }

    @Override
    public String getPedigree() {
        return pedigree;
    }

    public void setPedigree(String pedigree) {
        this.pedigree = pedigree;
    }

    @Override
    public String getSeedSource() {
        return seedSource;
    }

    public void setSeedSource(String seedSource) {
        this.seedSource = seedSource;
    }

    @Override
    public List<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    @Override
    public String getCommonCropName() {
        return commonCropName;
    }

    public void setCommonCropName(String commonCropName) {
        this.commonCropName = commonCropName;
    }

    @Override
    public String getInstituteCode() {
        return instituteCode;
    }

    public void setInstituteCode(String instituteCode) {
        this.instituteCode = instituteCode;
    }

    @Override
    public String getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
    }

    @Override
    public String getBiologicalStatusOfAccessionCode() {
        return biologicalStatusOfAccessionCode;
    }

    public void setBiologicalStatusOfAccessionCode(String biologicalStatusOfAccessionCode) {
        this.biologicalStatusOfAccessionCode = biologicalStatusOfAccessionCode;
    }

    @Override
    public String getCountryOfOriginCode() {
        return countryOfOriginCode;
    }

    public void setCountryOfOriginCode(String countryOfOriginCode) {
        this.countryOfOriginCode = countryOfOriginCode;
    }

    @Override
    public List<String> getTypeOfGermplasmStorageCode() {
        return typeOfGermplasmStorageCode;
    }

    public void setTypeOfGermplasmStorageCode(List<String> typeOfGermplasmStorageCode) {
        this.typeOfGermplasmStorageCode = typeOfGermplasmStorageCode;
    }

    @Override
    public List<TaxonSourceVO> getTaxonIds() {
        return taxonIds;
    }

    public void setTaxonIds(List<TaxonSourceVO> taxonIds) {
        this.taxonIds = taxonIds;
    }

    @Override
    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }

    @Override
    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    @Override
    public String getSpeciesAuthority() {
        return speciesAuthority;
    }

    public void setSpeciesAuthority(String speciesAuthority) {
        this.speciesAuthority = speciesAuthority;
    }

    @Override
    public String getSubtaxa() {
        return subtaxa;
    }

    public void setSubtaxa(String subtaxa) {
        this.subtaxa = subtaxa;
    }

    @Override
    public String getSubtaxaAuthority() {
        return subtaxaAuthority;
    }

    public void setSubtaxaAuthority(String subtaxaAuthority) {
        this.subtaxaAuthority = subtaxaAuthority;
    }

    @Override
    public List<DonorVO> getDonors() {
        return donors;
    }

    public void setDonors(List<DonorVO> donors) {
        this.donors = donors;
    }

    @Override
    public String getAcquisitionDate() {
        return acquisitionDate;
    }

    public void setAcquisitionDate(String acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    @Override
    public List<String> getTaxonSynonyms() {
        return taxonSynonyms;
    }

    public void setTaxonSynonyms(List<String> taxonSynonyms) {
        this.taxonSynonyms = taxonSynonyms;
    }

    @Override
    public List<String> getTaxonCommonNames() {
        return taxonCommonNames;
    }

    public void setTaxonCommonNames(List<String> taxonCommonNames) {
        this.taxonCommonNames = taxonCommonNames;
    }

    @Override
    public String getTaxonComment() {
        return taxonComment;
    }

    public void setTaxonComment(String taxonComment) {
        this.taxonComment = taxonComment;
    }

    @Override
    public String getGeneticNature() {
        return geneticNature;
    }

    public void setGeneticNature(String geneticNature) {
        this.geneticNature = geneticNature;
    }

    @Override
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public PhotoVO getPhoto() {
        return photo;
    }

    public void setPhoto(PhotoVO photo) {
        this.photo = photo;
    }

    @Override
    public InstituteVO getHoldingInstitute() {
        return holdingInstitute;
    }

    public void setHoldingInstitute(InstituteVO holdingInstitute) {
        this.holdingInstitute = holdingInstitute;
    }

    @Override
    public InstituteVO getHoldingGenbank() {
        return holdingGenbank;
    }

    public void setHoldingGenbank(InstituteVO holdingGenbank) {
        this.holdingGenbank = holdingGenbank;
    }

    @Override
    public String getPresenceStatus() {
        return presenceStatus;
    }

    public void setPresenceStatus(String presenceStatus) {
        this.presenceStatus = presenceStatus;
    }

    @Override
    public GenealogyVO getGenealogy() {
        return genealogy;
    }

    public void setGenealogy(GenealogyVO genealogy) {
        this.genealogy = genealogy;
    }

    @Override
    public List<GenealogyVO> getChildren() {
        return children;
    }

    public void setChildren(List<GenealogyVO> children) {
        this.children = children;
    }

    @Override
    public List<SimpleVO> getDescriptors() {
        return descriptors;
    }

    public void setDescriptors(List<SimpleVO> descriptors) {
        this.descriptors = descriptors;
    }

    @Override
    public SiteVO getOriginSite() {
        return originSite;
    }

    public void setOriginSite(SiteVO originSite) {
        this.originSite = originSite;
    }

    @Override
    public SiteVO getCollectingSite() {
        return collectingSite;
    }

    public void setCollectingSite(SiteVO collectingSite) {
        this.collectingSite = collectingSite;
    }

    @Override
    public List<SiteVO> getEvaluationSites() {
        return evaluationSites;
    }

    public void setEvaluationSites(List<SiteVO> evaluationSites) {
        this.evaluationSites = evaluationSites;
    }

    @Override
    public GermplasmInstituteVO getCollector() {
        return collector;
    }

    public void setCollector(GermplasmInstituteVO collector) {
        this.collector = collector;
    }

    @Override
    public GermplasmInstituteVO getBreeder() {
        return breeder;
    }

    public void setBreeder(GermplasmInstituteVO breeder) {
        this.breeder = breeder;
    }

    @Override
    public List<GermplasmInstituteVO> getDistributors() {
        return distributors;
    }

    public void setDistributors(List<GermplasmInstituteVO> distributors) {
        this.distributors = distributors;
    }

    @Override
    public List<CollPopVO> getPanel() {
        return panel;
    }

    public void setPanel(List<CollPopVO> panel) {
        this.panel = panel;
    }

    @Override
    public List<CollPopVO> getCollection() {
        return collection;
    }

    public void setCollection(List<CollPopVO> collection) {
        this.collection = collection;
    }

    @Override
    public List<CollPopVO> getPopulation() {
        return population;
    }

    public void setPopulation(List<CollPopVO> population) {
        this.population = population;
    }

}
