package fr.inrae.urgi.faidare.domain.brapi.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.inrae.urgi.faidare.config.ElasticSearchConfig;
import org.springframework.context.annotation.Import;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.util.List;
import java.util.Objects;

//indexName = "#{@dataDiscoveryProperties.getElasticsearchPrefix}resource-alias"
@Import({ElasticSearchConfig.class})
@Document(
    indexName = "#{@faidarePropertiesBean.getAliasName('germplasm-pedigree', 0L)}",
    createIndex = false
)
//@Mapping(mappingPath = "fr/inra/urgi/datadiscovery/domain/faidare/FaidareGeneticResource.mapping.json")
public final class GermplasmPedigreeV1VO {

    @Id
    private String _id;
    @JsonProperty("@id")
    @Field(name="germplasmURI")
    private String id;
    private String germplasmDbId;

    private String germplasmPedigreeDbId;
    private String defaultDisplayName;
    private String pedigree;
    private Long groupId;
    private String crossingPlan;
    private String crossingYear;
    private String familyCode;
    private String parent1DbId;
    private String parent1Name;
    private String parent1Type;
    private String parent2DbId;
    private String parent2Name;
    private String parent2Type;
    private List<SiblingV1VO> siblings;
    private String germplasmURI;

    private String germplasmPedigreeURI;
    private String parent1URI;
    private String parent2URI;
    @JsonProperty("@type")
    private String type = "germplasmPedigree";
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GermplasmPedigreeV1VO that = (GermplasmPedigreeV1VO) o;
        return Objects.equals(_id, that._id) && Objects.equals(germplasmDbId, that.germplasmDbId) && Objects.equals(defaultDisplayName, that.defaultDisplayName) && Objects.equals(pedigree, that.pedigree) && Objects.equals(groupId, that.groupId) && Objects.equals(crossingPlan, that.crossingPlan) && Objects.equals(crossingYear, that.crossingYear) && Objects.equals(familyCode, that.familyCode) && Objects.equals(parent1DbId, that.parent1DbId) && Objects.equals(parent1Name, that.parent1Name) && Objects.equals(parent1Type, that.parent1Type) && Objects.equals(parent2DbId, that.parent2DbId) && Objects.equals(parent2Name, that.parent2Name) && Objects.equals(parent2Type, that.parent2Type) && Objects.equals(siblings, that.siblings) && Objects.equals(germplasmURI, that.germplasmURI) && Objects.equals(parent1URI, that.parent1URI) && Objects.equals(parent2URI, that.parent2URI);
    }

    public String getCrossingPlan() {
        return crossingPlan;
    }

    public String getGermplasmPedigreeURI() {
        return germplasmPedigreeURI;
    }

    public void setGermplasmPedigreeURI(String germplasmPedigreeURI) {
        this.germplasmPedigreeURI = germplasmPedigreeURI;
    }

    public void setCrossingPlan(String crossingPlan) {
        this.crossingPlan = crossingPlan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGermplasmPedigreeDbId() {
        return germplasmPedigreeDbId;
    }

    public void setGermplasmPedigreeDbId(String germplasmPedigreeDbId) {
        this.germplasmPedigreeDbId = germplasmPedigreeDbId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCrossingYear() {
        return crossingYear;
    }

    public void setCrossingYear(String crossingYear) {
        this.crossingYear = crossingYear;
    }

    public String getDefaultDisplayName() {
        return defaultDisplayName;
    }

    public void setDefaultDisplayName(String defaultDisplayName) {
        this.defaultDisplayName = defaultDisplayName;
    }

    public String getFamilyCode() {
        return familyCode;
    }

    public void setFamilyCode(String familyCode) {
        this.familyCode = familyCode;
    }

    public String getGermplasmDbId() {
        return germplasmDbId;
    }

    public void setGermplasmDbId(String germplasmDbId) {
        this.germplasmDbId = germplasmDbId;
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

    public String getParent1DbId() {
        return parent1DbId;
    }

    public void setParent1DbId(String parent1DbId) {
        this.parent1DbId = parent1DbId;
    }

    public String getParent1Name() {
        return parent1Name;
    }

    public void setParent1Name(String parent1Name) {
        this.parent1Name = parent1Name;
    }

    public String getParent1Type() {
        return parent1Type;
    }

    public void setParent1Type(String parent1Type) {
        this.parent1Type = parent1Type;
    }

    public String getParent1URI() {
        return parent1URI;
    }

    public void setParent1URI(String parent1URI) {
        this.parent1URI = parent1URI;
    }

    public String getParent2DbId() {
        return parent2DbId;
    }

    public void setParent2DbId(String parent2DbId) {
        this.parent2DbId = parent2DbId;
    }

    public String getParent2Name() {
        return parent2Name;
    }

    public void setParent2Name(String parent2Name) {
        this.parent2Name = parent2Name;
    }

    public String getParent2Type() {
        return parent2Type;
    }

    public void setParent2Type(String parent2Type) {
        this.parent2Type = parent2Type;
    }

    public String getParent2URI() {
        return parent2URI;
    }

    public void setParent2URI(String parent2URI) {
        this.parent2URI = parent2URI;
    }

    public String getPedigree() {
        return pedigree;
    }

    public void setPedigree(String pedigree) {
        this.pedigree = pedigree;
    }

    public List<SiblingV1VO> getSiblings() {
        return siblings;
    }

    public void setSiblings(List<SiblingV1VO> siblings) {
        this.siblings = siblings;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, germplasmDbId, defaultDisplayName, pedigree, groupId, crossingPlan, crossingYear, familyCode, parent1DbId, parent1Name, parent1Type, parent2DbId, parent2Name, parent2Type, siblings, germplasmURI, parent1URI, parent2URI);
    }

}
