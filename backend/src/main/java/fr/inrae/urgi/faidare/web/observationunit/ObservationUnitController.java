package fr.inrae.urgi.faidare.web.observationunit;

/**
 * Controller allowing to export observation units
 * @author JB Nizet
 */

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.inrae.urgi.faidare.dao.v2.ObservationExportCriteria;
import fr.inrae.urgi.faidare.dao.v2.ObservationUnitExportCriteria;
import fr.inrae.urgi.faidare.dao.v2.ObservationUnitV2Dao;
import fr.inrae.urgi.faidare.dao.v2.ObservationV2Dao;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.ObservationUnitV2VO;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.ObservationVO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

/**
 * Controller used to display a germplasm card based on its ID.
 * Note that this controller is mapped to the /germplasm path in addition to
 * the canonical /germplasms path in order to still honor legacy URLs used
 * in external applications
 * @author JB Nizet
 */
@Controller("webObservationUnitController")
@RequestMapping({"/observation-units"})
public class ObservationUnitController {

    private final ObservationUnitV2Dao observationUnitRepository;
    private final ObservationV2Dao observationRepository;
    private final ObservationUnitExportService observationUnitExportService;

    public ObservationUnitController(ObservationUnitV2Dao observationUnitRepository, ObservationV2Dao observationRepository, ObservationUnitExportService observationUnitExportService) {
        this.observationUnitRepository = observationUnitRepository;
        this.observationRepository = observationRepository;
        this.observationUnitExportService = observationUnitExportService;
    }

    @PostMapping("/exports")
    @ResponseBody
    public ResponseEntity<StreamingResponseBody> export(@Validated @RequestBody ObservationUnitExportCommand command) {
        StreamingResponseBody body = out -> {
            try (
                Stream<ObservationUnitV2VO> observationUnits = observationUnitRepository.findByExportCriteria(
                    new ObservationUnitExportCriteria(command.trialDbId(), command.observationLevelCode())
                );
                Stream<ObservationVO> observations = observationRepository.findByExportCriteria(
                    new ObservationExportCriteria(
                        command.trialDbId(),
                        command.studyLocations(),
                        command.seasonNames(),
                        command.observationVariableNames()
                    )
                )
            ) {
                List<ExportedObservationUnit> exportedObservationUnits = join(observationUnits, observations);

                switch (command.format()) {
                    case CSV -> observationUnitExportService.exportAsCsv(out, exportedObservationUnits);
                    case EXCEL -> observationUnitExportService.exportAsExcel(out, exportedObservationUnits);
                }
            }
        };
        return ResponseEntity.ok().contentType(command.format().getMediaType()).body(body);
    }

    private List<ExportedObservationUnit> join(Stream<ObservationUnitV2VO> observationUnits, Stream<ObservationVO> observations) {
        Map<String, List<ObservationVO>> observationsByObservationUnitDbId =
            observations.collect(Collectors.groupingBy(ObservationVO::getObservationUnitDbId));
        return observationUnits.map(
            observationUnit -> new ExportedObservationUnit(
                observationUnit,
                observationsByObservationUnitDbId
                    .getOrDefault(observationUnit.getObservationUnitDbId(), List.of())
            )
        ).toList();
    }
}
