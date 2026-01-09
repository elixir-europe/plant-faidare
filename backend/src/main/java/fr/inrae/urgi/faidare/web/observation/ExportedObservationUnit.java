package fr.inrae.urgi.faidare.web.observation;

import java.util.List;

import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.ObservationUnitV2VO;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.ObservationVO;

/**
 * An exported observation unit, along with its observations matching the export criteria
 * @author JB Nizet
 */
public record ExportedObservationUnit(
    ObservationUnitV2VO observationUnit,
    List<ObservationVO> observations
) {
}
