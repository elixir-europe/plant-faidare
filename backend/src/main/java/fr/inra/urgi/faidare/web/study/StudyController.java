package fr.inra.urgi.faidare.web.study;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import fr.inra.urgi.faidare.api.NotFoundException;
import fr.inra.urgi.faidare.config.FaidareProperties;
import fr.inra.urgi.faidare.domain.criteria.GermplasmPOSTSearchCriteria;
import fr.inra.urgi.faidare.domain.data.TrialVO;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmVO;
import fr.inra.urgi.faidare.domain.data.study.StudyDetailVO;
import fr.inra.urgi.faidare.domain.data.variable.ObservationVariableVO;
import fr.inra.urgi.faidare.domain.xref.XRefDocumentVO;
import fr.inra.urgi.faidare.repository.es.GermplasmRepository;
import fr.inra.urgi.faidare.repository.es.StudyRepository;
import fr.inra.urgi.faidare.repository.es.TrialRepository;
import fr.inra.urgi.faidare.repository.es.XRefDocumentRepository;
import fr.inra.urgi.faidare.repository.file.CropOntologyRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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

    public StudyController(StudyRepository studyRepository,
                           FaidareProperties faidareProperties,
                           XRefDocumentRepository xRefDocumentRepository,
                           GermplasmRepository germplasmRepository,
                           CropOntologyRepository cropOntologyRepository,
                           TrialRepository trialRepository) {
        this.studyRepository = studyRepository;
        this.faidareProperties = faidareProperties;
        this.xRefDocumentRepository = xRefDocumentRepository;
        this.germplasmRepository = germplasmRepository;
        this.cropOntologyRepository = cropOntologyRepository;
        this.trialRepository = trialRepository;
    }

    @GetMapping("/{studyId}")
    public ModelAndView get(@PathVariable("studyId") String studyId) {
        StudyDetailVO study = studyRepository.getById(studyId);

        // List<XRefDocumentVO> crossReferences = xRefDocumentRepository.find(
        //     XRefDocumentSearchCriteria.forXRefId(site.getLocationDbId()));
        List<XRefDocumentVO> crossReferences = Arrays.asList(
            createXref("foobar"),
            createXref("bazbing")
        );

        // LocationVO site = createSite();

        if (study == null) {
            throw new NotFoundException("Study with ID " + studyId + " not found");
        }

        List<GermplasmVO> germplasms = getGermplasms(study);
        List<ObservationVariableVO>variables = getVariables(study);
        List<TrialVO> trials = getTrials(study);

        return new ModelAndView("study",
                                "model",
                                new StudyModel(
                                    study,
                                    faidareProperties.getByUri(study.getSourceUri()),
                                    germplasms,
                                    variables,
                                    trials,
                                    crossReferences
                                )
        );
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
