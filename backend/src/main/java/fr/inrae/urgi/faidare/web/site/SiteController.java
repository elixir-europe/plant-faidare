package fr.inrae.urgi.faidare.web.site;

import fr.inrae.urgi.faidare.api.NotFoundException;
import fr.inrae.urgi.faidare.config.FaidareProperties;
import fr.inrae.urgi.faidare.dao.XRefDocumentDao;
import fr.inrae.urgi.faidare.dao.v1.LocationV1Dao;
import fr.inrae.urgi.faidare.dao.v2.LocationV2Dao;
import fr.inrae.urgi.faidare.domain.XRefDocumentVO;
import fr.inrae.urgi.faidare.domain.brapi.LocationSitemapVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.LocationV2VO;
import fr.inrae.urgi.faidare.utils.Sitemaps;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;
import java.util.stream.Stream;

/**
 * Controller used to display a site card based on its ID.
 * @author JB Nizet
 */
@Controller("webSiteController")
@RequestMapping("/sites")
public class SiteController {

    private final LocationV2Dao locationRepository;
    private final FaidareProperties faidareProperties;
    private final XRefDocumentDao xRefDocumentRepository;

    public SiteController(LocationV2Dao locationRepository,
                          FaidareProperties faidareProperties,
                          XRefDocumentDao xRefDocumentRepository) {
        this.locationRepository = locationRepository;
        this.faidareProperties = faidareProperties;
        this.xRefDocumentRepository = xRefDocumentRepository;
    }

    @GetMapping("/{siteId}")
    public ModelAndView get(@PathVariable("siteId") String siteId, HttpServletRequest request) {
        LocationV2VO site = locationRepository.getByLocationDbId(siteId);

        if (site == null) {
            throw new NotFoundException("Site with ID " + siteId + " not found");
        }

        List<XRefDocumentVO> crossReferences = xRefDocumentRepository.findByLinkedResourcesID(site.getLocationDbId());

        return new ModelAndView("site",
                                "model",
                                new SiteModel(
                                    site,
                                    faidareProperties.getByUri(site.getSourceUri()),
                                    crossReferences,
                                    request.getContextPath()
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
            try (Stream<LocationSitemapVO> stream = locationRepository.findAllForSitemap()) {
                Sitemaps.generateSitemap(
                    "/sites/sitemap-" + index + ".txt",
                    out,
                    stream,
                    vo -> Math.floorMod(vo.getLocationDbId().hashCode(),
                                        Sitemaps.BUCKET_COUNT) == index,
                    vo -> "/sites/" + vo.getLocationDbId()
                );
            }
        };
        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(body);
    }
}
