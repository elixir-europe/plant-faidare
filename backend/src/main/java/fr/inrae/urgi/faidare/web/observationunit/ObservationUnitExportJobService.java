package fr.inrae.urgi.faidare.web.observationunit;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalNotification;
import fr.inrae.urgi.faidare.dao.v2.ObservationExportCriteria;
import fr.inrae.urgi.faidare.dao.v2.ObservationUnitExportCriteria;
import fr.inrae.urgi.faidare.dao.v2.ObservationUnitV2Dao;
import fr.inrae.urgi.faidare.dao.v2.ObservationV2Dao;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.ObservationUnitV2VO;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.ObservationVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Service which starts observation unit export jobs in the background, and
 * caches them for one hour after access.
 * When the job expires, the file it has generated on disk (if any) is deleted.
 * @author JB Nizet
 */
@Component
public class ObservationUnitExportJobService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ObservationUnitExportJobService.class);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(
        "yyyy-MM-dd-HH-mm-ss-SSS"
    );

    private final ObservationUnitV2Dao observationUnitRepository;
    private final ObservationV2Dao observationRepository;
    private final ObservationUnitExportProperties properties;
    private final ObservationUnitExportService exportService;

    private final Cache<String, ObservationUnitExportJob> jobCache = CacheBuilder.newBuilder()
        .expireAfterAccess(Duration.ofHours(1))
        .removalListener(this::handleRemoval)
        .build();

    public ObservationUnitExportJobService(
        ObservationUnitV2Dao observationUnitRepository,
        ObservationV2Dao observationRepository,
        ObservationUnitExportProperties properties,
        ObservationUnitExportService exportService
    ) {
        this.observationUnitRepository = observationUnitRepository;
        this.observationRepository = observationRepository;
        this.properties = properties;
        this.exportService = exportService;
    }

    public ObservationUnitExportJob createExportJob(ObservationUnitExportCommand command) {
        String id = UUID.randomUUID().toString();
        ObservationUnitExportJob job = new ObservationUnitExportJob(id, command.format());
        jobCache.put(id, job);

        Thread.ofVirtual().name("observation-unit-export-" + id).start(() -> export(command, job));
        return job;
    }

    public Optional<ObservationUnitExportJob> getJob(String id) {
        return Optional.ofNullable(jobCache.getIfPresent(id));
    }

    private void export(ObservationUnitExportCommand command, ObservationUnitExportJob job) {
        String fileName = "export_" + DATE_TIME_FORMATTER.format(Instant.now().atOffset(ZoneOffset.UTC)) + "." + command.format().getExtension();
        Path file = properties.directory().resolve(fileName);
        try (
            FileOutputStream fos = new FileOutputStream(file.toFile());
            BufferedOutputStream out = new BufferedOutputStream(fos, 128 * 1024);
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
                case CSV -> exportService.exportAsCsv(out, exportedObservationUnits);
                case EXCEL -> exportService.exportAsExcel(out, exportedObservationUnits);
            }
            out.close();
            job.done(file);
        } catch (Exception e) {
            LOGGER.error("Export failed for command {}", command, e);
            job.fail();
        }
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

    private void handleRemoval(RemovalNotification<String, ObservationUnitExportJob> removal) {
        try {
            Path file = removal.getValue().getFile();
            if (file != null) {
                Files.delete(file);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
