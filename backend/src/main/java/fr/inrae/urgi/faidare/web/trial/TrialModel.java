package fr.inrae.urgi.faidare.web.trial;

import java.util.List;

import fr.inrae.urgi.faidare.config.DataSource;
import fr.inrae.urgi.faidare.domain.LocationVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.GermplasmV2VO;
import fr.inrae.urgi.faidare.domain.brapi.v2.TrialV2VO;
import fr.inrae.urgi.faidare.web.site.MapLocation;

/**
 * The model used by the trial page
 * @author JB Nizet
 */
public final class TrialModel {
    private final TrialV2VO trial;
    private final DataSource source;
    private final List<LocationVO> locations;
    private final List<GermplasmV2VO> germplasms;
    private final String contextPath;

    public TrialModel(
        TrialV2VO trial,
        DataSource source,
        List<LocationVO> locations,
        List<GermplasmV2VO> germplasms,
        String contextPath
    ) {
        this.trial = trial;
        this.source = source;
        this.locations = locations;
        this.germplasms = germplasms;
        this.contextPath = contextPath;
    }

    public TrialV2VO getTrial() {
        return trial;
    }

    public DataSource getSource() {
        return source;
    }

    public List<MapLocation> getMapLocations() {
        return MapLocation.locationsToDisplayableMapLocations(this.locations);
    }

    public List<GermplasmV2VO> getGermplasms() {
        return germplasms;
    }

    public String getContextPath() {
        return contextPath;
    }
}
