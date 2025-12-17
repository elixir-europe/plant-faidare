package fr.inrae.urgi.faidare.web.study;

import fr.inrae.urgi.faidare.api.NotFoundException;
import fr.inrae.urgi.faidare.config.FaidareProperties;
import fr.inrae.urgi.faidare.dao.XRefDocumentDao;
import fr.inrae.urgi.faidare.dao.file.CropOntologyRepository;
import fr.inrae.urgi.faidare.dao.v2.LocationV2Dao;
import fr.inrae.urgi.faidare.dao.v2.GermplasmV2Dao;
import fr.inrae.urgi.faidare.dao.v2.StudyV2Dao;
import fr.inrae.urgi.faidare.dao.v2.TrialV2Dao;
import fr.inrae.urgi.faidare.domain.XRefDocumentVO;
import fr.inrae.urgi.faidare.domain.brapi.StudySitemapVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.GermplasmV2VO;
import fr.inrae.urgi.faidare.domain.brapi.v2.LocationV2VO;
import fr.inrae.urgi.faidare.domain.brapi.v2.StudyV2VO;
import fr.inrae.urgi.faidare.domain.brapi.v2.TrialV2VO;
import fr.inrae.urgi.faidare.domain.variable.ObservationVariableV1VO;
import fr.inrae.urgi.faidare.utils.Sitemaps;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Controller used to display a study card based on its ID.
 *
 * @author JB Nizet
 */
@Controller("webStudyController")
@RequestMapping("/studies")
public class StudyController {

    private final StudyV2Dao studyRepository;
    private final FaidareProperties faidareProperties;
    private final XRefDocumentDao xRefDocumentRepository;
    private final GermplasmV2Dao germplasmRepository;
    private final CropOntologyRepository cropOntologyRepository;
    private final TrialV2Dao trialRepository;
    private final LocationV2Dao locationRepository;

    public StudyController(StudyV2Dao studyRepository,
                           FaidareProperties faidareProperties,
                           XRefDocumentDao xRefDocumentRepository,
                           GermplasmV2Dao germplasmRepository,
                           CropOntologyRepository cropOntologyRepository,
                           TrialV2Dao trialRepository,
                           LocationV2Dao locationRepository) {
        this.studyRepository = studyRepository;
        this.faidareProperties = faidareProperties;
        this.xRefDocumentRepository = xRefDocumentRepository;
        this.germplasmRepository = germplasmRepository;
        this.cropOntologyRepository = cropOntologyRepository;
        this.trialRepository = trialRepository;
        this.locationRepository = locationRepository;
    }

    @GetMapping("/{studyId}")
    public ModelAndView get(@PathVariable("studyId") String studyId, Locale locale, HttpServletRequest request) {
        StudyV2VO study = studyRepository.getByStudyDbId(studyId);

        if (study == null) {
            throw new NotFoundException("Study with ID " + studyId + " not found");
        }

        List<XRefDocumentVO> crossReferences = xRefDocumentRepository.findByLinkedResourcesID(study.getStudyDbId());

        List<GermplasmV2VO> germplasms = getGermplasms(study);
        List<ObservationVariableV1VO> variables = getVariables(study, locale);
        List<TrialV2VO> trials = getTrials(study);
        LocationV2VO location = getLocation(study);

        // This code parses the 'startDate' and 'endDate' from the study object (which are strings)
        // into LocalDate objects using a DateTimeFormatter
        String dateRange = null;
        DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            if (study.getStartDate() != null && study.getEndDate() != null) {
                LocalDate start = LocalDate.parse(study.getStartDate(), inputFormatter);
                LocalDate end = LocalDate.parse(study.getEndDate(), inputFormatter);
                dateRange = "From " + outputFormatter.format(start) + " to " + outputFormatter.format(end);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("model", new StudyModel(
            study,
            faidareProperties.getByUri(study.getSourceUri()),
            germplasms,
            variables,
            trials,
            crossReferences,
            location,
            study.getUrl(),
            request.getContextPath()
        ));
        modelMap.put("dateRange", dateRange);

        return new ModelAndView("study", modelMap);
    }

    @GetMapping(value = "/sitemap-{index}.txt")
    @ResponseBody
    public ResponseEntity<StreamingResponseBody> sitemap(@PathVariable("index") int index) {
        if (index < 0 || index >= Sitemaps.BUCKET_COUNT) {
            throw new NotFoundException("no sitemap for this index");
        }
        StreamingResponseBody body = out -> {
            try (Stream<StudySitemapVO> stream = studyRepository.findAllForSitemap()) {
                Sitemaps.generateSitemap(
                    "/studies/sitemap-" + index + ".txt",
                    out,
                    stream,
                    vo -> Math.floorMod(vo.getStudyDbId().hashCode(),
                        Sitemaps.BUCKET_COUNT) == index,
                    vo -> "/studies/" + vo.getStudyDbId());
            }

        };
        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(body);
    }

    private LocationV2VO getLocation(StudyV2VO study) {
        if (Strings.isBlank(study.getLocationDbId())) {
            return null;
        }
        return locationRepository.getByLocationDbId(study.getLocationDbId());
    }

    private List<GermplasmV2VO> getGermplasms(StudyV2VO study) {
        if (study.getGermplasmDbIds() == null || study.getGermplasmDbIds().isEmpty()) {
            return Collections.emptyList();
        } else {
            return germplasmRepository.findByGermplasmDbIdIn(Set.copyOf(study.getGermplasmDbIds()))
                .sorted(Comparator.comparing(GermplasmV2VO::getGermplasmName))
                .collect(Collectors.toList());
        }
    }

    private List<ObservationVariableV1VO> getVariables(StudyV2VO study, Locale locale) {
        // FIXME JBN uncomment this line once StudyV1Dao has a getVariableIds() method
        // Set<String> variableIds = studyRepository.getVariableIds(study.getStudyDbId());
        if (study.getObservationVariableDbIds() == null || study.getObservationVariableDbIds().isEmpty()) {
            return Collections.emptyList();
        } else {
            Set<String> variableIds = new HashSet<>(study.getObservationVariableDbIds());
            List<ObservationVariableV1VO> variables = cropOntologyRepository.getVariableByIds(variableIds);
            return filterVariablesForLocale(variables, locale)
                .sorted(Comparator.comparing(ObservationVariableV1VO::getObservationVariableDbId))
                .collect(Collectors.toList());
        }
    }

    /**
     * Filter the variables by language. The principles are the following. First, the languages of the variables
     * are normalized (to transform FRA into fr for example).
     * Then, several cases are possible.
     * <p>
     * If there is no variable with the requested language, then we find the reference language.
     * The reference language is en if there is at least one variable with that language.
     * The reference is the first non null language found if there is no variable with the en language.
     * Then, we keep all the variables with the reference language (if any), and all the variables without language.
     * <p>
     * If there is at least one variable with the requested language, then we keep all the variables
     * with the requested language, and all the variables without language.
     */
    private Stream<ObservationVariableV1VO> filterVariablesForLocale(List<ObservationVariableV1VO> variables, Locale locale) {
        if (variables.isEmpty()) {
            return Stream.empty();
        }

        String requestedLanguage = locale.getLanguage();
        String referenceLanguage = findReferenceLanguage(requestedLanguage, variables);

        return variables.stream()
            .filter(variable ->
                referenceLanguage == null
                    || !StringUtils.hasText(variable.getLanguage())
                    || normalizeLanguage(variable.getLanguage()).equals(referenceLanguage));
    }

    private String findReferenceLanguage(String requestedLanguage, List<ObservationVariableV1VO> variables) {
        Set<String> normalizedVariableLanguages =
            variables.stream()
                .map(ObservationVariableV1VO::getLanguage)
                .filter(StringUtils::hasText)
                .map(this::normalizeLanguage)
                .collect(Collectors.toSet());

        String referenceLanguage = null;
        if (normalizedVariableLanguages.contains(requestedLanguage)) {
            referenceLanguage = requestedLanguage;
        } else if (normalizedVariableLanguages.contains("en")) {
            referenceLanguage = "en";
        } else if (!normalizedVariableLanguages.isEmpty()) {
            referenceLanguage = normalizedVariableLanguages.iterator().next();
        }
        return referenceLanguage;
    }

    private String normalizeLanguage(String language) {
        // this is a hack trying to accomodate for languages not bein standard in the data
        String languageInLowerCase = language.toLowerCase();
        if (languageInLowerCase.length() == 3) {
            switch (languageInLowerCase) {
                case "fra":
                    return "fr";
                case "esp":
                case "spa":
                    return "es";
                case "eng":
                    return "en";
            }
        }
        return languageInLowerCase;
    }

    private List<TrialV2VO> getTrials(StudyV2VO study) {
        if (study.getTrialDbIds() == null || study.getTrialDbIds().isEmpty()) {
            return Collections.emptyList();
        }

        return study.getTrialDbIds()
            .stream()
            .sorted(Comparator.naturalOrder())
            .map(trialRepository::getByTrialDbId)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

    }


}
