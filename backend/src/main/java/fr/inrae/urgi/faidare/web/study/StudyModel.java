package fr.inrae.urgi.faidare.web.study;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.inrae.urgi.faidare.config.DataSource;
import fr.inrae.urgi.faidare.domain.XRefDocumentVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.GermplasmV2VO;
import fr.inrae.urgi.faidare.domain.brapi.v2.LocationV2VO;
import fr.inrae.urgi.faidare.domain.brapi.v2.StudyV2VO;
import fr.inrae.urgi.faidare.domain.brapi.v2.TrialV2VO;
import fr.inrae.urgi.faidare.domain.variable.ObservationVariableV1VO;
import fr.inrae.urgi.faidare.web.site.MapLocation;

/**
 * The model used by the study page
 * @author JB Nizet
 */
public final class StudyModel {
    private final StudyV2VO study;
    private final DataSource source;
    private final List<GermplasmV2VO> germplasms;
    private final List<ObservationVariableV1VO> variables;
    private final List<TrialV2VO> trials;
    private final List<XRefDocumentVO> crossReferences;
    private final LocationV2VO location;
    private final List<Map.Entry<String, Object>> additionalInfoProperties;
    private final String url;
    private final String contextPath;

    public StudyModel(StudyV2VO study,
                      DataSource source,
                      List<GermplasmV2VO> germplasms,
                      List<ObservationVariableV1VO> variables,
                      List<TrialV2VO> trials,
                      List<XRefDocumentVO> crossReferences,
                      LocationV2VO location,
                      String url,
                      String contextPath) {
        this.study = study;
        this.source = source;
        this.germplasms = germplasms;
        this.variables = variables;
        this.trials = trials;
        this.crossReferences = crossReferences;
        this.location = location;
        this.url = url;
        this.contextPath = contextPath;

        Map<String, Object> additionalInfo =
            study.getAdditionalInfo() == null ? Collections.emptyMap() : study.getAdditionalInfo();
        this.additionalInfoProperties =
            additionalInfo.entrySet()
                .stream()
                .filter(entry -> entry.getValue() != null && !entry.getValue().toString().isEmpty())
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toList());
    }

    public StudyV2VO getStudy() {
        return study;
    }

    public DataSource getSource() {
        return source;
    }

    public List<XRefDocumentVO> getCrossReferences() {
        return crossReferences;
    }

    public List<GermplasmV2VO> getGermplasms() {
        return germplasms;
    }

    public List<ObservationVariableV1VO> getVariables() {
        return variables;
    }

    public List<TrialV2VO> getTrials() {
        return trials;
    }

    public String getUrl() { return url;}

    public List<Map.Entry<String, Object>> getAdditionalInfoProperties() {
        return additionalInfoProperties;
    }

    public List<MapLocation> getMapLocations() {
        if (this.location == null) {
            return Collections.emptyList();
        }
        return MapLocation.locationsToDisplayableMapLocationsV2(Collections.singletonList(this.location));
    }

    public String getContextPath() {
        return contextPath;
    }
}
