package fr.inra.urgi.faidare.domain.criteria;

import fr.inra.urgi.faidare.domain.criteria.base.SortCriteria;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmVO;
import fr.inra.urgi.faidare.elasticsearch.criteria.annotation.CriteriaForDocument;
import fr.inra.urgi.faidare.elasticsearch.criteria.annotation.DocumentPath;
import fr.inra.urgi.faidare.elasticsearch.criteria.annotation.NoDocumentMapping;

import java.util.List;

/**
 * @author jdestin
 */
@CriteriaForDocument(GermplasmVO.class)
public class FaidareGermplasmPOSTShearchCriteria extends GermplasmPOSTSearchCriteria implements SortCriteria {

    @DocumentPath(value = "synonyms")
    private List<String> synonyms;

    /*@DocumentPath(value = {"panel", "name"}, objectsValue = CollPopVO.class)
    private List<String> panel;

    @DocumentPath(value = {"collection", "name"}, objectsValue = CollPopVO.class)
    private List<String> collection;

    @DocumentPath(value = {"population", "name"}, objectsValue = CollPopVO.class)
    private List<String> population;*/

    @DocumentPath(value = "commonCropName")
    private List<String> commonCropName;

    @DocumentPath(value = "species")
    private List<String> species;

    @DocumentPath(value = "genus")
    private List<String> genus;

    @DocumentPath(value = "genusSpecies")
    private List<String> genusSpecies;

    @DocumentPath(value = "subtaxa")
    private List<String> subtaxa;

    @DocumentPath(value = "genusSpeciesSubtaxa")
    private List<String> genusSpeciesSubtaxa;

    @DocumentPath("taxonSynonyms")
    private List<String> taxonSynonyms;

    @DocumentPath("taxonCommonNames")
    private List<String> taxonCommonNames;

    @DocumentPath(value = {"holdingInstitute", "organisation"})
    private List<String> holdingInstitute;

    @DocumentPath("sourceUri")
    private List<String> sources;

    @DocumentPath("biologicalStatusOfAccessionCode")
    private List<String> biologicalStatus;

    @DocumentPath("geneticNature")
    private List<String> geneticNature;

    @DocumentPath("countryOfOriginCode")
    private List<String> country;

    @NoDocumentMapping
    private List<String> facetFields;

    @NoDocumentMapping
    private String sortBy = null;// = "schema:name";

    @NoDocumentMapping
    private String sortOrder = null;// = SortOrder.ASC.name();




    /*public List<String> getPanel() { return panel; }

    public void setPanel(List<String> panel) { this.panel = panel; }

    public List<String> getCollection() { return collection; }

    public void setCollection(List<String> collection) { this.collection = collection; }

    public List<String> getPopulation() { return population; }

    public void setPopulation(List<String> population) { this.population = population; }*/

    public List<String> getCommonCropName() { return commonCropName; }

    public void setCommonCropName(List<String> commonCropName) {
        this.commonCropName = commonCropName;
    }

    public List<String> getSpecies() { return species; }

    public void setSpecies(List<String> species) { this.species = species; }

    public List<String> getGenus() { return genus; }

    public void setGenus(List<String> genus) { this.genus = genus; }

    public List<String> getGenusSpecies() { return genusSpecies; }

    public void setGenusSpecies(List<String> genusSpecies) {
        this.genusSpecies = genusSpecies;
    }

    public List<String> getSubtaxa() { return subtaxa; }

    public void setSubtaxa(List<String> subtaxa) { this.subtaxa = subtaxa; }

    public List<String> getGenusSpeciesSubtaxa() { return genusSpeciesSubtaxa; }

    public void setGenusSpeciesSubtaxa(List<String> genusSpeciesSubtaxa) {
        this.genusSpeciesSubtaxa = genusSpeciesSubtaxa;
    }

    public List<String> getTaxonSynonyms() { return taxonSynonyms; }

    public void setTaxonSynonyms(List<String> taxonSynonyms) {
        this.taxonSynonyms = taxonSynonyms;
    }

    public List<String> getSynonyms() { return synonyms; }

    public void setSynonyms(List<String> synonyms) { this.synonyms = synonyms; }

    public List<String> getTaxonCommonNames() {
        return taxonCommonNames;
    }

    public void setTaxonCommonNames(List<String> taxonCommonNames) {
        this.taxonCommonNames = taxonCommonNames;
    }

    public List<String> getHoldingInstitute() {
        return holdingInstitute;
    }

    public void setHoldingInstitute(List<String> holdingInstitute) {
        this.holdingInstitute = holdingInstitute;
    }

    public List<String> getSources() {
        return sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }

    public List<String> getBiologicalStatus() { return biologicalStatus; }

    public void setBiologicalStatus(List<String> biologicalStatus) {
        this.biologicalStatus = biologicalStatus;
    }

    public List<String> getGeneticNature() {
        return geneticNature;
    }

    public void setGeneticNature(List<String> geneticNature) {
        this.geneticNature = geneticNature;
    }

    public List<String> getCountry() { return country; }

    public void setCountry(List<String> country) {
        this.country = country;
    }


    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }


    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override
    public String getSortBy() {
        return sortBy;
    }

    @Override
    public String getSortOrder() {
        return sortOrder;
    }


    public List<String> getFacetFields() {
        return facetFields;
    }

    public void setFacetFields(List<String> facetFields) {
        this.facetFields = facetFields;
    }
}
