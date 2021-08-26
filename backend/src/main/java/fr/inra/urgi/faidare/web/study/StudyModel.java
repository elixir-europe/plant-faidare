package fr.inra.urgi.faidare.web.study;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.inra.urgi.faidare.domain.data.LocationVO;
import fr.inra.urgi.faidare.domain.data.TrialVO;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmVO;
import fr.inra.urgi.faidare.domain.data.study.StudyDetailVO;
import fr.inra.urgi.faidare.domain.data.variable.ObservationVariableVO;
import fr.inra.urgi.faidare.domain.datadiscovery.data.DataSource;
import fr.inra.urgi.faidare.domain.xref.XRefDocumentVO;
import fr.inra.urgi.faidare.web.site.MapLocation;

/**
 * The model used by the study page
 * @author JB Nizet
 */
public final class StudyModel {
    private final StudyDetailVO study;
    private final DataSource source;
    private final List<GermplasmVO> germplasms;
    private final List<ObservationVariableVO> variables;
    private final List<TrialVO> trials;
    private final List<XRefDocumentVO> crossReferences;
    private final LocationVO location;
    private final List<Map.Entry<String, Object>> additionalInfoProperties;

    public StudyModel(StudyDetailVO study,
                      DataSource source,
                      List<GermplasmVO> germplasms,
                      List<ObservationVariableVO> variables,
                      List<TrialVO> trials,
                      List<XRefDocumentVO> crossReferences,
                      LocationVO location) {
        this.study = study;
        this.source = source;
        this.germplasms = germplasms;
        this.variables = variables;
        this.trials = trials;
        this.crossReferences = crossReferences;
        this.location = location;

        Map<String, Object> additionalInfo =
            study.getAdditionalInfo() == null ? Collections.emptyMap() : study.getAdditionalInfo().getProperties();
        this.additionalInfoProperties =
            additionalInfo.entrySet()
                          .stream()
                          .filter(entry -> entry.getValue() != null && !entry.getValue().toString().isEmpty())
                          .sorted(Map.Entry.comparingByKey())
                          .collect(Collectors.toList());
    }

    public StudyDetailVO getStudy() {
        return study;
    }

    public DataSource getSource() {
        return source;
    }

    public List<XRefDocumentVO> getCrossReferences() {
        return crossReferences;
    }

    public List<GermplasmVO> getGermplasms() {
        return germplasms;
    }

    public List<ObservationVariableVO> getVariables() {
        return variables;
    }

    public List<TrialVO> getTrials() {
        return trials;
    }

    public List<Map.Entry<String, Object>> getAdditionalInfoProperties() {
        return additionalInfoProperties;
    }

    public List<MapLocation> getMapLocations() {
        if (this.location == null) {
            return Collections.emptyList();
        }
        return MapLocation.locationsToDisplayableMapLocations(Collections.singletonList(this.location));
    }
}
