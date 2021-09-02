package fr.inra.urgi.faidare.domain.data.germplasm;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiGermplasm;
import fr.inra.urgi.faidare.domain.jsonld.data.HasURI;
import fr.inra.urgi.faidare.domain.jsonld.data.HasURL;
import fr.inra.urgi.faidare.domain.jsonld.data.IncludedInDataCatalog;
import fr.inra.urgi.faidare.elasticsearch.document.annotation.Document;
import fr.inra.urgi.faidare.elasticsearch.document.annotation.Id;

/**
 * A minimal view of a germplasm, containing only its ID, used for sitemaps
 */
@Document(type = "germplasm", includedFields = "germplasmDbId")
public class GermplasmSitemapVO {

    @Id
    private String germplasmDbId;

    public GermplasmSitemapVO() {
    }

    public GermplasmSitemapVO(String germplasmDbId) {
        this.germplasmDbId = germplasmDbId;
    }

    public String getGermplasmDbId() {
        return germplasmDbId;
    }

    public void setGermplasmDbId(String germplasmDbId) {
        this.germplasmDbId = germplasmDbId;
    }
}
