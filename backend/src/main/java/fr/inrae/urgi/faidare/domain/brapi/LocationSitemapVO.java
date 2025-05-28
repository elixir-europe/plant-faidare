package fr.inrae.urgi.faidare.domain.brapi;

import org.springframework.data.annotation.Id;

/**
 * A minimal view of a location containing only its ID, used to generate sitemaps
 */
public class LocationSitemapVO {

    @Id
    private String _id;

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
