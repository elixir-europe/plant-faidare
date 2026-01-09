package fr.inrae.urgi.faidare.web.trial;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.servlet.http.HttpServletRequest;

import fr.inrae.urgi.faidare.api.NotFoundException;
import fr.inrae.urgi.faidare.config.DataSource;
import fr.inrae.urgi.faidare.config.FaidareProperties;
import fr.inrae.urgi.faidare.dao.v1.LocationV1Dao;
import fr.inrae.urgi.faidare.dao.v2.ChoosableObservationExportCriteria;
import fr.inrae.urgi.faidare.dao.v2.GermplasmV2Criteria;
import fr.inrae.urgi.faidare.dao.v2.GermplasmV2Dao;
import fr.inrae.urgi.faidare.dao.v2.ObservationUnitV2Dao;
import fr.inrae.urgi.faidare.dao.v2.ObservationV2Dao;
import fr.inrae.urgi.faidare.dao.v2.TrialV2Dao;
import fr.inrae.urgi.faidare.domain.LocationVO;
import fr.inrae.urgi.faidare.domain.brapi.TrialSitemapVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.GermplasmV2VO;
import fr.inrae.urgi.faidare.domain.brapi.v2.StudyV2miniVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.TrialV2VO;
import fr.inrae.urgi.faidare.utils.Sitemaps;
import fr.inrae.urgi.faidare.web.observation.ObservationExportJob;
import fr.inrae.urgi.faidare.web.observation.ObservationExportJobDTO;
import fr.inrae.urgi.faidare.web.observation.ObservationExportJobService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

/**
 * Controller used to display a trial card based on its ID.
 * @author JB Nizet
 */
@Controller("webTrialController")
@RequestMapping("/trials")
public class TrialController {

    private final TrialV2Dao trialRepository;
    private final FaidareProperties faidareProperties;
    private final LocationV1Dao locationRepository;
    private final GermplasmV2Dao germplasmRepository;
    private final ObservationUnitV2Dao observationUnitRepository;
    private final ObservationV2Dao observationRepository;
    private final ObservationExportJobService jobService;

    public TrialController(TrialV2Dao trialRepository,
                           FaidareProperties faidareProperties,
                           LocationV1Dao locationRepository,
                           GermplasmV2Dao germplasmRepository,
                           ObservationUnitV2Dao observationUnitRepository,
                           ObservationV2Dao observationRepository,
                           ObservationExportJobService jobService) {
        this.trialRepository = trialRepository;
        this.faidareProperties = faidareProperties;
        this.locationRepository = locationRepository;
        this.germplasmRepository = germplasmRepository;
        this.observationUnitRepository = observationUnitRepository;
        this.observationRepository = observationRepository;
        this.jobService = jobService;
    }

    @GetMapping("/{trialId}")
    public ModelAndView get(@PathVariable("trialId") String trialId, HttpServletRequest request) {
        TrialV2VO trial = trialRepository.getByTrialDbId(trialId);

        if (trial == null) {
            throw new NotFoundException("Trial with ID " + trialId + " not found");
        }

        DataSource source = faidareProperties.getByUri(trial.getSourceUri());
        List<LocationVO> locations = findLocations(trial);
        List<GermplasmV2VO> germplasms = findGermplasms(trial);

        // FIXME there should be variables, but there's no way to get them.
        //   The method used by the STudyController is not fixed yet, and what
        //   the spec says about getting them using the brapi v2 endpoint is not implemented either

        // DONE: need merge with master

        // FIXME the spec screenshot shows 4 columns for the studies (name, source, type, description
        //  but the text of the spec only mentions name and location
        //  Besides it talks about using a brapi v2 endpoint to get the studies (which is weird enough since we should use the DAOs directly)
        //  but the trial directly has studies embedded that have a name and a location, so we'll use that
        //  trial also has studyDbIds BTW.

        // Clarification: the spec was not fully explicit — the goal is to show the 4 fields from the screenshot (name, source, type, description) plus the location.
        // Agreed: no need to use a BrAPI endpoint — using the DAO directly is fine here.

        List<String> observationLevelCodes = observationUnitRepository.findObservationLevelCodesByTrialDbId(trialId);
        ChoosableObservationExportCriteria choosableObservationExportCriteria = observationRepository.findChoosableObservationExportCriteriaByTrialDbId(trialId);

        return new ModelAndView("trial",
                                "model",
                                new TrialModel(
                                    trial,
                                    source,
                                    locations,
                                    germplasms,
                                    new TrialChoosableExportCriteria(
                                        observationLevelCodes,
                                        choosableObservationExportCriteria.seasonNames(),
                                        choosableObservationExportCriteria.studyLocations(),
                                        choosableObservationExportCriteria.observationVariableNames()
                                    ),
                                    request.getContextPath()
                                )
        );
    }

    @GetMapping("/{trialId}/exports/{jobId}")
    public ModelAndView exportPage(
        @PathVariable("trialId") String trialId,
        @PathVariable("jobId") String jobId,
        HttpServletRequest request
    ) {
        TrialV2VO trial = trialRepository.getByTrialDbId(trialId);
        if (trial == null) {
            throw new NotFoundException("Trial with ID " + trialId + " not found");
        }
        ObservationExportJob job =
            jobService.getJob(jobId).orElseThrow(() -> new NotFoundException("no export job with ID " + jobId));
        return new ModelAndView(
            "trial-export",
            "model",
            new TrialExportModel(
                request.getContextPath(),
                trial,
                new ObservationExportJobDTO(job)
            )
        );
    }

    @GetMapping(value = "/sitemap-{index}.txt")
    @ResponseBody
    public ResponseEntity<StreamingResponseBody> sitemap(@PathVariable("index") int index) {
        if (index < 0 || index >= Sitemaps.BUCKET_COUNT) {
            throw new NotFoundException("no sitemap for this index");
        }
        StreamingResponseBody body = out -> {
            try (Stream<TrialSitemapVO> stream = trialRepository.findAllForSitemap()) {
                Sitemaps.generateSitemap(
                    "/trials/sitemap-" + index + ".txt",
                    out,
                    stream,
                    vo -> Math.floorMod(vo.getTrialDbId().hashCode(),
                                        Sitemaps.BUCKET_COUNT) == index,
                    vo -> "/trials/" + vo.getTrialDbId());
            }
        };
        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(body);
    }

    private List<LocationVO> findLocations(TrialV2VO trial) {
        Set<String> locationDbIds = trial
            .getStudies()
            .stream()
            .map(StudyV2miniVO::getLocationDbId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        return locationDbIds.isEmpty() ? List.of() : locationRepository.getByLocationDbIdIn(locationDbIds);
    }

    private List<GermplasmV2VO> findGermplasms(TrialV2VO trial) {
        GermplasmV2Criteria criteria = new GermplasmV2Criteria();
        criteria.setTrialDbId(List.of(trial.getTrialDbId()));
        criteria.setPage(0);
        // This should hopefully be sufficient, and I guess nobody cares nor will use this page
        // to find what they want to find if there are more than 1000
        criteria.setPageSize(1000);
        return germplasmRepository.findGermplasmsByCriteria(criteria).getResult().getData();
    }
}
