package fr.inrae.urgi.faidare.domain.variable;

import java.util.List;

/**
 * @author gcornut
 */
public class TraitVO implements BrapiTrait {

    private String traitDbId;
    private String name;

    private String traitClass;

    private String description;
    private List<String> synonyms;
    private String mainAbbreviation;
    private List<String> alternativeAbbreviations;
    private String entity;
    private String attribute;
    private String status;
    private String xref;

    @Override
    public String getTraitDbId() {
        return traitDbId;
    }

    public void setTraitDbId(String traitDbId) {
        this.traitDbId = traitDbId;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getTraitClass() {
        return traitClass;
    }

    public void setTraitClass(String traitClass) {
        this.traitClass = traitClass;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public List<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    @Override
    public String getMainAbbreviation() {
        return mainAbbreviation;
    }

    public void setMainAbbreviation(String mainAbbreviation) {
        this.mainAbbreviation = mainAbbreviation;
    }

    @Override
    public List<String> getAlternativeAbbreviations() {
        return alternativeAbbreviations;
    }

    public void setAlternativeAbbreviations(List<String> alternativeAbbreviations) {
        this.alternativeAbbreviations = alternativeAbbreviations;
    }

    @Override
    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    @Override
    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getXref() {
        return xref;
    }

    public void setXref(String xref) {
        this.xref = xref;
    }

}
