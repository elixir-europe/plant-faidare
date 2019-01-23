package fr.inra.urgi.gpds.domain.data.impl.variable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiMethod;
import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiObservationVariable;
import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiScale;
import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiTrait;

import java.util.List;

/**
 * @author gcornut
 */
public class ObservationVariableVO implements BrapiObservationVariable {

    private String observationVariableDbId;
    private String name;
    private String ontologyDbId;
    private String ontologyName;
    private List<String> synonyms;
    private List<String> contextOfUse;
    private String growthStage;
    private String status;
    private String xref;
    private String institution;
    private String scientist;
    private String date;
    private String language;
    private String crop;
    private String defaultValue;

    @JsonDeserialize(as = TraitVO.class)
    private BrapiTrait trait;

    @JsonDeserialize(as = MethodVO.class)
    private BrapiMethod method;

    @JsonDeserialize(as = ScaleVO.class)
    private BrapiScale scale;

    private String documentationURL;

    @Override
    public String getDocumentationURL() {
        return documentationURL;
    }

    public void setDocumentationURL(String documentationURL) {
        this.documentationURL = documentationURL;
    }

    @Override
    public String getObservationVariableDbId() {
        return observationVariableDbId;
    }

    public void setObservationVariableDbId(String observationVariableDbId) {
        this.observationVariableDbId = observationVariableDbId;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getOntologyDbId() {
        return ontologyDbId;
    }

    public void setOntologyDbId(String ontologyDbId) {
        this.ontologyDbId = ontologyDbId;
    }

    @Override
    public String getOntologyName() {
        return ontologyName;
    }

    public void setOntologyName(String ontologyName) {
        this.ontologyName = ontologyName;
    }

    @Override
    public List<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    @Override
    public List<String> getContextOfUse() {
        return contextOfUse;
    }

    public void setContextOfUse(List<String> contextOfUse) {
        this.contextOfUse = contextOfUse;
    }

    @Override
    public String getGrowthStage() {
        return growthStage;
    }

    public void setGrowthStage(String growthStage) {
        this.growthStage = growthStage;
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

    @Override
    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    @Override
    public String getScientist() {
        return scientist;
    }

    public void setScientist(String scientist) {
        this.scientist = scientist;
    }

    @Override
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String getCrop() {
        return crop;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }

    @Override
    public BrapiTrait getTrait() {
        return trait;
    }

    public void setTrait(BrapiTrait trait) {
        this.trait = trait;
    }

    @Override
    public BrapiMethod getMethod() {
        return method;
    }

    public void setMethod(BrapiMethod method) {
        this.method = method;
    }

    @Override
    public BrapiScale getScale() {
        return scale;
    }

    public void setScale(BrapiScale scale) {
        this.scale = scale;
    }

    @Override
    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
