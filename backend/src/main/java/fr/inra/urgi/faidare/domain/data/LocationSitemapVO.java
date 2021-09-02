package fr.inra.urgi.faidare.domain.data;

import java.util.List;

import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiAdditionalInfo;
import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiLocation;
import fr.inra.urgi.faidare.domain.jsonld.data.HasURI;
import fr.inra.urgi.faidare.domain.jsonld.data.HasURL;
import fr.inra.urgi.faidare.domain.jsonld.data.IncludedInDataCatalog;
import fr.inra.urgi.faidare.elasticsearch.document.annotation.Document;
import fr.inra.urgi.faidare.elasticsearch.document.annotation.Id;

/**
 * A minimal view of a location containing only its ID, used to generate sitemaps
 */
@Document(type = "location", includedFields = "locationDbId")
public class LocationSitemapVO {

    @Id
    private String locationDbId;

    public LocationSitemapVO() {
    }

    public LocationSitemapVO(String locationDbId) {
        this.locationDbId = locationDbId;
    }

    public String getLocationDbId() {
        return locationDbId;
    }

    public void setLocationDbId(String locationDbId) {
        this.locationDbId = locationDbId;
    }
}
