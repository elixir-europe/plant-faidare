package fr.inrae.urgi.faidare.domain;

import fr.inrae.urgi.faidare.config.ElasticSearchConfig;
import org.springframework.context.annotation.Import;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Import({ElasticSearchConfig.class})
@Document(
    indexName = "#{@faidarePropertiesBean.getAliasName('germplasm', 0L)}",
    createIndex = false
)
public class CollPopVO {

    private Integer germplasmCount;

    private PuiNameValueVO germplasmRef;

    @Id
    private String collectionDbId;

    private String collectionName;

    private String collectionType;

    public CollPopVO(String collectionName, String collectionDbId) {
        this.collectionName = collectionName;
        this.collectionDbId = collectionDbId;
    }


    public Integer getGermplasmCount() {
        return germplasmCount;
    }

    public void setGermplasmCount(Integer germplasmCount) {
        this.germplasmCount = germplasmCount;
    }

    public PuiNameValueVO getGermplasmRef() {
        return germplasmRef;
    }

    public void setGermplasmRef(PuiNameValueVO germplasmRef) {
        this.germplasmRef = germplasmRef;
    }

    public String getCollectionDbId() {
        return collectionDbId;
    }

    public void setCollectionDbId(String collectionDbId) {
        this.collectionDbId = collectionDbId;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
    }
}
