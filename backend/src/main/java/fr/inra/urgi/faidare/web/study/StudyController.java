package fr.inra.urgi.faidare.web.study;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import fr.inra.urgi.faidare.api.NotFoundException;
import fr.inra.urgi.faidare.config.FaidareProperties;
import fr.inra.urgi.faidare.domain.criteria.GermplasmPOSTSearchCriteria;
import fr.inra.urgi.faidare.domain.data.LocationVO;
import fr.inra.urgi.faidare.domain.data.TrialVO;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmVO;
import fr.inra.urgi.faidare.domain.data.study.StudyDetailVO;
import fr.inra.urgi.faidare.domain.data.study.StudySitemapVO;
import fr.inra.urgi.faidare.domain.data.variable.ObservationVariableVO;
import fr.inra.urgi.faidare.domain.xref.XRefDocumentSearchCriteria;
import fr.inra.urgi.faidare.domain.xref.XRefDocumentVO;
import fr.inra.urgi.faidare.repository.es.GermplasmRepository;
import fr.inra.urgi.faidare.repository.es.LocationRepository;
import fr.inra.urgi.faidare.repository.es.StudyRepository;
import fr.inra.urgi.faidare.repository.es.TrialRepository;
import fr.inra.urgi.faidare.repository.es.XRefDocumentRepository;
import fr.inra.urgi.faidare.repository.file.CropOntologyRepository;
import fr.inra.urgi.faidare.utils.Sitemaps;
import org.apache.logging.log4j.util.Strings;
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
 * Controller used to display a study card based on its ID.
 * @author JB Nizet
 */
@Controller("webStudyController")
@RequestMapping("/studies")
public class StudyController {

    private final StudyRepository studyRepository;
    private final FaidareProperties faidareProperties;
    private final XRefDocumentRepository xRefDocumentRepository;
    private final GermplasmRepository germplasmRepository;
    private final CropOntologyRepository cropOntologyRepository;
    private final TrialRepository trialRepository;
    private final LocationRepository locationRepository;

    public StudyController(StudyRepository studyRepository,
                           FaidareProperties faidareProperties,
                           XRefDocumentRepository xRefDocumentRepository,
                           GermplasmRepository germplasmRepository,
                           CropOntologyRepository cropOntologyRepository,
                           TrialRepository trialRepository,
                           LocationRepository locationRepository) {
        this.studyRepository = studyRepository;
        this.faidareProperties = faidareProperties;
        this.xRefDocumentRepository = xRefDocumentRepository;
        this.germplasmRepository = germplasmRepository;
        this.cropOntologyRepository = cropOntologyRepository;
        this.trialRepository = trialRepository;
        this.locationRepository = locationRepository;
    }

    @GetMapping("/{studyId}")
    public ModelAndView get(@PathVariable("studyId") String studyId) {
        StudyDetailVO study = studyRepository.getById(studyId);

        // TODO uncomment this
        // List<XRefDocumentVO> crossReferences = xRefDocumentRepository.find(
        //     XRefDocumentSearchCriteria.forXRefId(study.getStudyDbId()));
        List<XRefDocumentVO> crossReferences = Arrays.asList(
            createXref("foobar"),
            createXref("bazbing")
        );

        if (study == null) {
            throw new NotFoundException("Study with ID " + studyId + " not found");
        }

        List<GermplasmVO> germplasms = getGermplasms(study);
        List<ObservationVariableVO> variables = getVariables(study);
        List<TrialVO> trials = getTrials(study);
        LocationVO location = getLocation(study);

        // TODO remove this
        location.setLatitude(34.0);
        location.setLongitude(14.0);

        return new ModelAndView("study",
                                "model",
                                new StudyModel(
                                    study,
                                    faidareProperties.getByUri(study.getSourceUri()),
                                    germplasms,
                                    variables,
                                    trials,
                                    crossReferences,
                                    location
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
            Iterator<StudySitemapVO> iterator = studyRepository.scrollAllForSitemap(1000);
            Sitemaps.generateSitemap(
                "/sudies/sitemap-" + index + ".txt",
                out,
                iterator,
                vo -> Math.floorMod(vo.getStudyDbId().hashCode(), Sitemaps.BUCKET_COUNT) == index,
                vo -> "/studies/" + vo.getStudyDbId());
        };
        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(body);
    }

    private LocationVO getLocation(StudyDetailVO study) {
        if (Strings.isBlank(study.getLocationDbId())) {
            return null;
        }
        return locationRepository.getById(study.getLocationDbId());
    }

    private List<GermplasmVO> getGermplasms(StudyDetailVO study) {
        if (study.getGermplasmDbIds() == null || study.getGermplasmDbIds().isEmpty()) {
            return Collections.emptyList();
        } else {
            GermplasmPOSTSearchCriteria germplasmCriteria = new GermplasmPOSTSearchCriteria();
            germplasmCriteria.setGermplasmDbIds(Lists.newArrayList(study.getGermplasmDbIds()));
            return germplasmRepository.find(germplasmCriteria)
                .stream()
                .sorted(Comparator.comparing(GermplasmVO::getGermplasmName))
                .collect(Collectors.toList());
        }
    }

    private List<ObservationVariableVO> getVariables(StudyDetailVO study) {
        Set<String> variableIds = studyRepository.getVariableIds(study.getStudyDbId());
        return cropOntologyRepository.getVariableByIds(variableIds)
            .stream()
            .sorted(Comparator.comparing(ObservationVariableVO::getObservationVariableDbId))
            .collect(Collectors.toList());
    }

    private List<TrialVO> getTrials(StudyDetailVO study) {
        if (study.getTrialDbIds() == null || study.getTrialDbIds().isEmpty()) {
            return Collections.emptyList();
        }

        return study.getTrialDbIds()
                    .stream()
                    .sorted(Comparator.naturalOrder())
                    .map(trialRepository::getById)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
    }



    private XRefDocumentVO createXref(String name) {
        XRefDocumentVO xref = new XRefDocumentVO();
        xref.setName(name);
        xref.setDescription("A very large description for the xref " + name + " which has way more than 120 characters bla bla bla bla bla bla bla bla bla bla bla bla");
        xref.setDatabaseName("db_" + name);
        xref.setUrl("https://google.com");
        xref.setEntryType("type " + name);
        return xref;
    }
}
