package fr.inrae.urgi.faidare.domain.brapi.v2;

import fr.inrae.urgi.faidare.config.ElasticSearchConfig;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.annotations.Document;

@Import({ElasticSearchConfig.class})
@Document(
    indexName = "#{@faidarePropertiesBean.getAliasName('location', 0L)}",
    createIndex = false
)
public class LocationV2miniVO {

    private Double longitude;
    private Double latitude;

    public LocationV2miniVO(Double latitude, Double longitude) {
        this.longitude = longitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

}
