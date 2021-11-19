package fr.inra.urgi.faidare.web.germplasm;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import fr.inra.urgi.faidare.api.NotFoundException;
import fr.inra.urgi.faidare.config.FaidareProperties;
import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiGermplasmAttributeValue;
import fr.inra.urgi.faidare.domain.criteria.GermplasmAttributeCriteria;
import fr.inra.urgi.faidare.domain.criteria.GermplasmGETSearchCriteria;
import fr.inra.urgi.faidare.domain.data.germplasm.CollPopVO;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmMcpdVO;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmSitemapVO;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmVO;
import fr.inra.urgi.faidare.domain.data.germplasm.PedigreeVO;
import fr.inra.urgi.faidare.domain.xref.XRefDocumentSearchCriteria;
import fr.inra.urgi.faidare.domain.xref.XRefDocumentVO;
import fr.inra.urgi.faidare.repository.es.GermplasmAttributeRepository;
import fr.inra.urgi.faidare.repository.es.GermplasmRepository;
import fr.inra.urgi.faidare.repository.es.XRefDocumentRepository;
import fr.inra.urgi.faidare.utils.Sitemaps;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

/**
 * Controller used to display a germplasm card based on its ID.
 * Note that this controller is mapped to the /germplasm path in addition to
 * the canonical /germplasms path in order to still honor legacy URLs used
 * in external applications
 * @author JB Nizet
 */
@Controller("webGermplasmController")
@RequestMapping({"/germplasms", "/germplasm"})
public class GermplasmController {

    private final GermplasmRepository germplasmRepository;
    private final FaidareProperties faidareProperties;
    private final XRefDocumentRepository xRefDocumentRepository;
    private final GermplasmAttributeRepository germplasmAttributeRepository;
    private final GermplasmMcpdExportService germplasmMcpdExportService;
    private final GermplasmExportService germplasmExportService;

    public GermplasmController(GermplasmRepository germplasmRepository,
                               FaidareProperties faidareProperties,
                               XRefDocumentRepository xRefDocumentRepository,
                               GermplasmAttributeRepository germplasmAttributeRepository,
                               GermplasmMcpdExportService germplasmMcpdExportService,
                               GermplasmExportService germplasmExportService) {
        this.germplasmRepository = germplasmRepository;
        this.faidareProperties = faidareProperties;
        this.xRefDocumentRepository = xRefDocumentRepository;
        this.germplasmAttributeRepository = germplasmAttributeRepository;
        this.germplasmMcpdExportService = germplasmMcpdExportService;
        this.germplasmExportService = germplasmExportService;
    }

    @GetMapping("/{germplasmId}")
    public ModelAndView get(@PathVariable("germplasmId") String germplasmId) {
        GermplasmVO germplasm = germplasmRepository.getById(germplasmId);

        if (germplasm == null) {
            throw new NotFoundException("Germplasm with ID " + germplasmId + " not found");
        }

        return toModelAndView(germplasm);
    }

    @GetMapping(params = "id")
    public ModelAndView getById(@RequestParam("id") String germplasmId) {
        GermplasmVO germplasm = germplasmRepository.getById(germplasmId);

        if (germplasm == null) {
            throw new NotFoundException("Germplasm with ID " + germplasmId + " not found");
        }

        return toModelAndView(germplasm);
    }

    @GetMapping(params = "pui")
    public ModelAndView getByPui(@RequestParam("pui") String pui) {
        GermplasmGETSearchCriteria criteria = new GermplasmGETSearchCriteria();
        criteria.setGermplasmPUI(Collections.singletonList(pui));
        List<GermplasmVO> germplasms = germplasmRepository.find(criteria);
        if (germplasms.size() != 1) {
            throw new NotFoundException("Germplasm with PUI " + pui + " not found");
        }

        return toModelAndView(germplasms.get(0));
    }

    @PostMapping("/exports/mcpd")
    @ResponseBody
    public ResponseEntity<StreamingResponseBody> export(@Validated @RequestBody GermplasmMcpdExportCommand command) {
        List<GermplasmMcpdExportableField> fields = getFieldsToExport(command);

        StreamingResponseBody body = out -> {
            Iterator<GermplasmMcpdVO> iterator = germplasmRepository.scrollGermplasmMcpdsByIds(command.getIds(), 1000);
            germplasmMcpdExportService.export(out, iterator, fields);
        };
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("text/csv")).body(body);
    }

    @PostMapping("/exports/plant-material")
    @ResponseBody
    public ResponseEntity<StreamingResponseBody> export(@Validated @RequestBody GermplasmExportCommand command) {
        List<GermplasmExportableField> fields = getFieldsToExport(command);

        StreamingResponseBody body = out -> {
            Iterator<GermplasmVO> iterator = germplasmRepository.scrollGermplasmsByIds(command.getIds(), 1000);
            germplasmExportService.export(out, iterator, fields);
        };
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("text/csv")).body(body);
    }

    @GetMapping("/sitemap-{index}.txt")
    @ResponseBody
    public ResponseEntity<StreamingResponseBody> sitemap(@PathVariable("index") int index) {
        if (index < 0 || index >= Sitemaps.BUCKET_COUNT) {
            throw new NotFoundException("no sitemap for this index");
        }
        StreamingResponseBody body = out -> {
            Iterator<GermplasmSitemapVO> iterator = germplasmRepository.scrollAllForSitemap(1000);
            Sitemaps.generateSitemap(
                "/germplasms/sitemap-" + index + ".txt",
                out,
                iterator,
                vo -> Math.floorMod(vo.getGermplasmDbId().hashCode(), Sitemaps.BUCKET_COUNT) == index,
                vo -> "/germplasms/" + vo.getGermplasmDbId()
            );
        };
        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(body);
    }

    private ModelAndView toModelAndView(GermplasmVO germplasm) {
        List<BrapiGermplasmAttributeValue> attributes = getAttributes(germplasm);
        PedigreeVO pedigree = getPedigree(germplasm);

        List<XRefDocumentVO> crossReferences = xRefDocumentRepository.find(
            XRefDocumentSearchCriteria.forXRefId(germplasm.getGermplasmDbId())
        );

        sortDonors(germplasm);
        sortPopulations(germplasm);
        sortCollections(germplasm);
        sortPanels(germplasm);
        return new ModelAndView("germplasm",
                                "model",
                                new GermplasmModel(
                                    germplasm,
                                    faidareProperties.getByUri(germplasm.getSourceUri()),
                                    attributes,
                                    pedigree,
                                    crossReferences)
        );
    }

    private void sortPopulations(GermplasmVO germplasm) {
        if (germplasm.getPopulation() != null) {
            germplasm.setPopulation(germplasm.getPopulation()
                                             .stream()
                                             .sorted(Comparator.comparing(
                                                 CollPopVO::getName))
                                             .collect(Collectors.toList()));
        }
    }

    private void sortCollections(GermplasmVO germplasm) {
        if (germplasm.getCollection() != null) {
            germplasm.setCollection(germplasm.getCollection()
                                             .stream()
                                             .sorted(Comparator.comparing(CollPopVO::getName))
                                             .collect(Collectors.toList()));
        }
    }

    private void sortPanels(GermplasmVO germplasm) {
        if (germplasm.getPanel() != null) {
            germplasm.setPanel(germplasm.getPanel()
                                        .stream()
                                        .sorted(Comparator.comparing(CollPopVO::getName))
                                        .collect(Collectors.toList()));
        }
    }

    private void sortDonors(GermplasmVO germplasm) {
        if (germplasm.getDonors() != null) {
            germplasm.setDonors(germplasm.getDonors()
                                         .stream()
                                         .sorted(Comparator.comparing(donor -> donor.getDonorInstitute()
                                                                                    .getInstituteName()))
                                         .collect(Collectors.toList()));
        }
    }

    private List<BrapiGermplasmAttributeValue> getAttributes(GermplasmVO germplasm) {
        GermplasmAttributeCriteria criteria = new GermplasmAttributeCriteria();
        criteria.setGermplasmDbId(germplasm.getGermplasmDbId());
        return germplasmAttributeRepository.find(criteria)
            .stream()
            .flatMap(vo -> vo.getData().stream())
            .sorted(Comparator.comparing(BrapiGermplasmAttributeValue::getAttributeName))
            .collect(Collectors.toList());
    }

    private PedigreeVO getPedigree(GermplasmVO germplasm) {
        return germplasmRepository.findPedigree(germplasm.getGermplasmDbId());
    }

    private List<GermplasmMcpdExportableField> getFieldsToExport(
        GermplasmMcpdExportCommand command) {
        List<GermplasmMcpdExportableField> fields = command.getFields();
        if (fields.isEmpty()) {
            fields = Arrays.asList(GermplasmMcpdExportableField.values());
        }
        return fields;
    }

    private List<GermplasmExportableField> getFieldsToExport(
        GermplasmExportCommand command) {
        List<GermplasmExportableField> fields = command.getFields();
        if (fields.isEmpty()) {
            fields = Arrays.asList(GermplasmExportableField.values());
        }
        return fields;
    }
}
