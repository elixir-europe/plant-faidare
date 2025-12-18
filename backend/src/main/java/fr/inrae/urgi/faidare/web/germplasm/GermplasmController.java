package fr.inrae.urgi.faidare.web.germplasm;

import fr.inrae.urgi.faidare.api.NotFoundException;
import fr.inrae.urgi.faidare.config.FaidareProperties;
import fr.inrae.urgi.faidare.dao.XRefDocumentDao;
import fr.inrae.urgi.faidare.dao.v1.GermplasmAttributeV1Dao;
import fr.inrae.urgi.faidare.dao.v1.GermplasmPedigreeV1Dao;
import fr.inrae.urgi.faidare.dao.v2.GermplasmMcpdDao;
import fr.inrae.urgi.faidare.dao.v2.GermplasmV2Dao;
import fr.inrae.urgi.faidare.domain.CollPopVO;
import fr.inrae.urgi.faidare.domain.GermplasmMcpdVO;
import fr.inrae.urgi.faidare.domain.XRefDocumentVO;
import fr.inrae.urgi.faidare.domain.brapi.GermplasmSitemapVO;
import fr.inrae.urgi.faidare.domain.brapi.v1.GermplasmAttributeV1VO;
import fr.inrae.urgi.faidare.domain.brapi.v1.GermplasmAttributeValueV1VO;
import fr.inrae.urgi.faidare.domain.brapi.v1.GermplasmPedigreeV1VO;
import fr.inrae.urgi.faidare.domain.brapi.v2.GermplasmV2VO;
import fr.inrae.urgi.faidare.utils.Sitemaps;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private final GermplasmV2Dao germplasmRepository;
    private final GermplasmMcpdDao germplasmMcpdRepository;
    private final GermplasmPedigreeV1Dao germplasmPedigreeRepository;
    private final FaidareProperties faidareProperties;
    private final XRefDocumentDao xRefDocumentRepository;
    private final GermplasmAttributeV1Dao germplasmAttributeRepository;
    private final GermplasmMcpdExportService germplasmMcpdExportService;
    private final GermplasmExportService germplasmExportService;

    public GermplasmController(GermplasmV2Dao germplasmRepository,
                               GermplasmMcpdDao germplasmMcpdRepository,
                               GermplasmPedigreeV1Dao germplasmPedigreeRepository,
                               FaidareProperties faidareProperties,
                               XRefDocumentDao xRefDocumentRepository,
                               GermplasmAttributeV1Dao germplasmAttributeRepository,
                               GermplasmMcpdExportService germplasmMcpdExportService,
                               GermplasmExportService germplasmExportService) {
        this.germplasmRepository = germplasmRepository;
        this.germplasmMcpdRepository = germplasmMcpdRepository;
        this.germplasmPedigreeRepository = germplasmPedigreeRepository;
        this.faidareProperties = faidareProperties;
        this.xRefDocumentRepository = xRefDocumentRepository;
        this.germplasmAttributeRepository = germplasmAttributeRepository;
        this.germplasmMcpdExportService = germplasmMcpdExportService;
        this.germplasmExportService = germplasmExportService;
    }

    @GetMapping("/{germplasmId}")
    public ModelAndView get(@PathVariable("germplasmId") String germplasmId, HttpServletRequest request) {
        GermplasmV2VO germplasm = germplasmRepository.getByGermplasmDbId(germplasmId);

        if (germplasm == null) {
            throw new NotFoundException("Germplasm with ID " + germplasmId + " not found");
        }

        return toModelAndView(germplasm, request);
    }

    @GetMapping(params = "id")
    public ModelAndView getById(@RequestParam("id") String germplasmId, HttpServletRequest request) {
        GermplasmV2VO germplasm = germplasmRepository.getByGermplasmDbId(germplasmId);

        if (germplasm == null) {
            throw new NotFoundException("Germplasm with ID " + germplasmId + " not found");
        }

        return toModelAndView(germplasm, request);
    }

    @GetMapping(params = "pui")
    public ModelAndView getByPui(@RequestParam("pui") String pui, HttpServletRequest request) {
        GermplasmV2VO germplasm = germplasmRepository.getByGermplasmPUI(pui);

        if (germplasm == null) {
            throw new NotFoundException("Germplasm with PUI " + pui + " not found");
        }

        return toModelAndView(germplasm, request);
    }

    @PostMapping("/exports/mcpd")
    @ResponseBody
    public ResponseEntity<StreamingResponseBody> export(@Validated @RequestBody GermplasmMcpdExportCommand command) {
        List<GermplasmMcpdExportableField> fields = getFieldsToExport(command);

        StreamingResponseBody body = out -> {
            try (Stream<GermplasmMcpdVO> stream = germplasmMcpdRepository.findByGermplasmDbIdIn(command.getIds())) {
                germplasmMcpdExportService.export(out, stream, fields);
            }
        };
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("text/csv")).body(body);
    }

    @PostMapping("/exports/plant-material")
    @ResponseBody
    public ResponseEntity<StreamingResponseBody> export(@Validated @RequestBody GermplasmExportCommand command) {
        List<GermplasmExportableField> fields = getFieldsToExport(command);

        StreamingResponseBody body = out -> {
            try (Stream<GermplasmV2VO> stream = germplasmRepository.findByGermplasmDbIdIn(command.getIds())) {
                germplasmExportService.export(out, stream, fields);
            }
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
            try (Stream<GermplasmSitemapVO> stream = germplasmRepository.findAllForSitemap()) {
                Sitemaps.generateSitemap(
                    "/germplasms/sitemap-" + index + ".txt",
                    out,
                    stream,
                    vo -> Math.floorMod(vo.getGermplasmDbId().hashCode(),
                        Sitemaps.BUCKET_COUNT) == index,
                    vo -> "/germplasms/" + vo.getGermplasmDbId()
                );
            }
        };
        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(body);
    }

    private ModelAndView toModelAndView(GermplasmV2VO germplasm, HttpServletRequest request) {
        List<GermplasmAttributeValueV1VO> attributes = getAttributes(germplasm);
        GermplasmPedigreeV1VO pedigree = getPedigree(germplasm);

        List<XRefDocumentVO> crossReferences =
            xRefDocumentRepository.findByLinkedResourcesID(germplasm.getGermplasmDbId());

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
                crossReferences,
                request.getContextPath())
        );
    }

    private void sortPopulations(GermplasmV2VO germplasm) {
        if (germplasm.getPopulation() != null) {
            germplasm.setPopulation(germplasm.getPopulation()
                .stream()
                .sorted(Comparator.comparing(
                    CollPopVO::getName))
                .collect(Collectors.toList()));
        }
    }

    private void sortCollections(GermplasmV2VO germplasm) {
        if (germplasm.getCollection() != null) {
            germplasm.setCollection(germplasm.getCollection()
                .stream()
                .sorted(Comparator.comparing(CollPopVO::getName))
                .collect(Collectors.toList()));
        }
    }

    private void sortPanels(GermplasmV2VO germplasm) {
        if (germplasm.getPanel() != null) {
            germplasm.setPanel(germplasm.getPanel()
                .stream()
                .sorted(Comparator.comparing(CollPopVO::getName))
                .collect(Collectors.toList()));
        }
    }

    private void sortDonors(GermplasmV2VO germplasm) {
        if (germplasm.getDonors() != null) {
            germplasm.setDonors(germplasm.getDonors()
                .stream()
                .sorted(Comparator.comparing(donor -> donor.getDonorInstitute()
                    .getInstituteName()))
                .collect(Collectors.toList()));
        }
    }

    private List<GermplasmAttributeValueV1VO> getAttributes(GermplasmV2VO germplasm) {
        GermplasmAttributeV1VO attribute = germplasmAttributeRepository.getByGermplasmDbId(germplasm.getGermplasmDbId());
        if (attribute == null) {
            return List.of();
        }
        return attribute
            .getData()
            .stream()
            .sorted(Comparator.comparing(GermplasmAttributeValueV1VO::getAttributeName))
            .collect(Collectors.toList());
    }

    private GermplasmPedigreeV1VO getPedigree(GermplasmV2VO germplasm) {
        return germplasmPedigreeRepository.getByGermplasmDbId(germplasm.getGermplasmDbId());
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
