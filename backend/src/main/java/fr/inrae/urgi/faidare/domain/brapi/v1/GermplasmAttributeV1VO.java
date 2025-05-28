package fr.inrae.urgi.faidare.domain.brapi.v1;

import fr.inrae.urgi.faidare.config.ElasticSearchConfig;
import org.springframework.context.annotation.Import;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;
import java.util.Objects;

@Import({ElasticSearchConfig.class})
@Document(
    indexName = "#{@faidarePropertiesBean.getAliasName('germplasm-attribute', 0L)}",
    createIndex = false
)
public class GermplasmAttributeV1VO {

    @Id
    private String _id;

    private String germplasmAttributeDbId;
    private String germplasmDbId;
    private String germplasmURI;
    private String germplasmAttributeURI;
    private List<GermplasmAttributeValueV1VO> data;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GermplasmAttributeV1VO that = (GermplasmAttributeV1VO) o;
        return Objects.equals(_id, that._id) && Objects.equals(germplasmAttributeDbId, that.germplasmAttributeDbId) && Objects.equals(germplasmDbId, that.germplasmDbId) && Objects.equals(germplasmURI, that.germplasmURI);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, germplasmAttributeDbId, germplasmDbId, germplasmURI);
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getGermplasmAttributeDbId() {
        return germplasmAttributeDbId;
    }

    public void setGermplasmAttributeDbId(String germplasmAttributeDbId) {
        this.germplasmAttributeDbId = germplasmAttributeDbId;
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

    public String getGermplasmAttributeURI() {
        return germplasmAttributeURI;
    }

    public void setGermplasmAttributeURI(String germplasmAttributeURI) {
        this.germplasmAttributeURI = germplasmAttributeURI;
    }

    public List<GermplasmAttributeValueV1VO> getData() {
        return data;
    }

    public void setData(List<GermplasmAttributeValueV1VO> data) {
        this.data = data;
    }
}
