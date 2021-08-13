package fr.inra.urgi.faidare.web.site;

import fr.inra.urgi.faidare.api.NotFoundException;
import fr.inra.urgi.faidare.config.FaidareProperties;
import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiAdditionalInfo;
import fr.inra.urgi.faidare.domain.data.LocationVO;
import fr.inra.urgi.faidare.repository.es.LocationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller used to display a site card based on its ID.
 * @author JB Nizet
 */
@Controller
@RequestMapping("/sites")
public class SiteController {

    private final LocationRepository locationRepository;
    private final FaidareProperties faidareProperties;

    public SiteController(LocationRepository locationRepository,
                          FaidareProperties faidareProperties) {
        this.locationRepository = locationRepository;
        this.faidareProperties = faidareProperties;
    }

    @GetMapping("/{siteId}")
    public ModelAndView site(@PathVariable("siteId") String siteId) {
        // LocationVO site = locationRepository.getById(siteId);
        LocationVO site = createSite();

        if (site == null) {
            throw new NotFoundException("Site with ID " + siteId + " not found");
        }
        return new ModelAndView("site",
                                "model",
                                new SiteModel(site, faidareProperties.getByUri(site.getSourceUri())));
    }

    private LocationVO createSite() {
        LocationVO site = new LocationVO();
        site.setLocationName("France");
        site.setSourceUri("https://urgi.versailles.inrae.fr/gnpis");
        site.setUri("Test URI");
        site.setUrl("https://google.com");
        site.setLatitude(45.65);
        site.setLongitude(1.34);
        BrapiAdditionalInfo additionalInfo = new BrapiAdditionalInfo();
        additionalInfo.addProperty("Slope", 4.32);
        additionalInfo.addProperty("Distance to city", "3 km");
        additionalInfo.addProperty("foo", "bar");
        additionalInfo.addProperty("baz", "zing");
        additionalInfo.addProperty("blob", null);
        site.setAdditionalInfo(additionalInfo);
        return site;
    }
}
