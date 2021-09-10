package fr.inra.urgi.faidare.web.site;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import fr.inra.urgi.faidare.api.NotFoundException;
import fr.inra.urgi.faidare.config.FaidareProperties;
import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiAdditionalInfo;
import fr.inra.urgi.faidare.domain.data.LocationSitemapVO;
import fr.inra.urgi.faidare.domain.data.LocationVO;
import fr.inra.urgi.faidare.domain.xref.XRefDocumentSearchCriteria;
import fr.inra.urgi.faidare.domain.xref.XRefDocumentVO;
import fr.inra.urgi.faidare.repository.es.LocationRepository;
import fr.inra.urgi.faidare.repository.es.XRefDocumentRepository;
import fr.inra.urgi.faidare.utils.Sitemaps;
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
 * Controller used to display a site card based on its ID.
 * @author JB Nizet
 */
@Controller("webSiteController")
@RequestMapping("/sites")
public class SiteController {

    private final LocationRepository locationRepository;
    private final FaidareProperties faidareProperties;
    private final XRefDocumentRepository xRefDocumentRepository;

    public SiteController(LocationRepository locationRepository,
                          FaidareProperties faidareProperties,
                          XRefDocumentRepository xRefDocumentRepository) {
        this.locationRepository = locationRepository;
        this.faidareProperties = faidareProperties;
        this.xRefDocumentRepository = xRefDocumentRepository;
    }

    @GetMapping("/{siteId}")
    public ModelAndView get(@PathVariable("siteId") String siteId) {
        LocationVO site = locationRepository.getById(siteId);

        if (site == null) {
            throw new NotFoundException("Site with ID " + siteId + " not found");
        }

        // TODO uncomment this and remove the hard-coded xrefs
        // List<XRefDocumentVO> crossReferences = xRefDocumentRepository.find(
        //     XRefDocumentSearchCriteria.forXRefId(site.getLocationDbId()));
        List<XRefDocumentVO> crossReferences = Arrays.asList(
            createXref("foobar"),
            createXref("bazbing")
        );

        return new ModelAndView("site",
                                "model",
                                new SiteModel(
                                    site,
                                    faidareProperties.getByUri(site.getSourceUri()),
                                    crossReferences
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
            Iterator<LocationSitemapVO> iterator = locationRepository.scrollAllForSitemap(1000);
            Sitemaps.generateSitemap(
                "/sites/sitemap-" + index + ".txt",
                out,
                iterator,
                vo -> Math.floorMod(vo.getLocationDbId().hashCode(), Sitemaps.BUCKET_COUNT) == index,
                vo -> "/sites/" + vo.getLocationDbId()
            );
        };
        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(body);
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
