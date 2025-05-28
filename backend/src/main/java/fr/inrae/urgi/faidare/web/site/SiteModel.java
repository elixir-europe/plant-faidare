package fr.inrae.urgi.faidare.web.site;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import fr.inrae.urgi.faidare.config.DataSource;
import fr.inrae.urgi.faidare.domain.XRefDocumentVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.LocationV2VO;

/**
 * The model used by the site page
 * @author JB Nizet
 */
public final class SiteModel {
    private static final Set<String> IGNORED_PROPERTIES =
        new HashSet<>(Arrays.asList("Site status",
                                    "Coordinates precision",
                                    "Slope",
                                    "Exposure",
                                    "Geographical location",
                                    "Distance to city",
                                    "Direction from city",
                                    "Environment type",
                                    "Topography",
                                    "Comment"));

    private final LocationV2VO site;
    private final DataSource source;
    private final Map<String, Object> additionalInfo;
    private final List<XRefDocumentVO> crossReferences;
    private final List<Map.Entry<String, Object>> additionalInfoProperties;
    private final String contextPath;

    public SiteModel(LocationV2VO site,
                     DataSource source,
                     List<XRefDocumentVO> crossReferences,
                     String contextPath) {
        this.site = site;
        this.source = source;
        // FIXME JBN uncomment once LocationVO has additionalInfo
        this.additionalInfo = site.getAdditionalInfo() == null ? Collections.emptyMap() : site.getAdditionalInfo();
        //this.additionalInfo = Collections.emptyMap();
        this.crossReferences = crossReferences;
        this.additionalInfoProperties =
            this.additionalInfo
                .entrySet()
                .stream()
                .filter(entry -> !IGNORED_PROPERTIES.contains(entry.getKey()))
                .filter(entry -> entry.getValue() != null && !entry.getValue().toString().isEmpty())
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toList());
        this.contextPath = contextPath;
    }

    public LocationV2VO getSite() {
        return site;
    }

    public DataSource getSource() {
        return source;
    }

    public Map<String, Object> getAdditionalInfo() {
        return this.additionalInfo;
    }

    public Object getSiteStatus() {
        return this.additionalInfo.get("Site status");
    }

    public Object getCoordinatesPrecision() {
        return this.additionalInfo.get("Coordinates precision");
    }

    public Object getGeographicalLocation() {
        return this.additionalInfo.get("Geographical location");
    }

    public Object getSlope() {
        return this.additionalInfo.get("Slope");
    }

    public Object getExposure() {
        return this.additionalInfo.get("Exposure");
    }

    public Object getTopography() {
        return this.additionalInfo.get("Topography");
    }

    public Object getEnvironmentType() {
        return this.additionalInfo.get("Environment type");
    }

    public Object getDistanceToCity() {
        return this.additionalInfo.get("Distance to city");
    }

    public Object getDirectionFromCity() {
        return this.additionalInfo.get("Direction from city");
    }

    public Object getComment() {
        return this.additionalInfo.get("Comment");
    }

    public List<Map.Entry<String, Object>> getAdditionalInfoProperties() {
        return additionalInfoProperties;
    }

    public List<XRefDocumentVO> getCrossReferences() {
        return crossReferences;
    }

    public List<MapLocation> getMapLocations() {
        return MapLocation.locationsToDisplayableMapLocationsV2(Collections.singletonList(this.site));
    }

    public String getContextPath() {
        return contextPath;
    }
}
