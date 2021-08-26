package fr.inra.urgi.faidare.web.site;

import java.util.List;
import java.util.stream.Collectors;

import fr.inra.urgi.faidare.domain.data.LocationVO;
import fr.inra.urgi.faidare.domain.data.germplasm.SiteVO;
import fr.inra.urgi.faidare.utils.Sites;

/**
 * An object that can be serialized to JSON to serve as a map marker.
 * @author JB Nizet
 */
public final class MapLocation {
    private final String locationDbId;
    private final String locationType;
    private final String locationName;
    private final double latitude;
    private final double longitude;

    public MapLocation(String locationDbId,
                       String locationType,
                       String locationName,
                       double latitude,
                       double longitude) {
        this.locationDbId = locationDbId;
        this.locationType = locationType;
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public MapLocation(LocationVO site) {
        this(site.getLocationDbId(),
             site.getLocationType(),
             site.getLocationName(),
             site.getLatitude(),
             site.getLongitude());
    }

    public MapLocation(SiteVO site) {
        this(Sites.siteIdToLocationId(site.getSiteId()),
             site.getSiteType(),
             site.getSiteName(),
             site.getLatitude(),
             site.getLongitude());
    }

    public static List<MapLocation> locationsToDisplayableMapLocations(List<LocationVO> locations) {
        return locations.stream()
                        .filter(location -> location.getLatitude() != null && location.getLongitude() != null)
                        .map(MapLocation::new)
                        .collect(Collectors.toList());
    }

    public static List<MapLocation> sitesToDisplayableMapLocations(List<SiteVO> sites) {
        return sites.stream()
                    .filter(site -> site.getLatitude() != null && site.getLongitude() != null)
                    .map(MapLocation::new)
                    .collect(Collectors.toList());
    }

    public String getLocationDbId() {
        return locationDbId;
    }

    public String getLocationType() {
        return locationType;
    }

    public String getLocationName() {
        return locationName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
