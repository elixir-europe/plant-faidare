package fr.inrae.urgi.faidare.web.thymeleaf;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

import fr.inrae.urgi.faidare.domain.CollPopVO;
import fr.inrae.urgi.faidare.domain.TaxonSourceVO;
import fr.inrae.urgi.faidare.utils.Sites;
import org.apache.logging.log4j.util.Strings;

/**
 * The actual object offering Faidare helper methods to thymeleaf
 * @author JB Nizet
 */
public class FaidareExpressions {

    private static final Map<String, Function<String, String>> TAXON_ID_URL_FACTORIES_BY_SOURCE_NAME =
        createTaxonIdUrlFactories();

    private static final List<NavbarEntry> NAVBAR_ENTRIES =
        createNavbarEntries();

    private static Map<String, Function<String, String>> createTaxonIdUrlFactories() {
        Map<String, Function<String, String>> result = new HashMap<>();
        result.put("NCBI", s -> "https://www.ncbi.nlm.nih.gov/Taxonomy/Browser/wwwtax.cgi?mode=Info&id=" + s);
        result.put("ThePlantList", s -> "http://www.theplantlist.org/tpl1.1/record/" + s);
        result.put("TAXREF", s -> "https://inpn.mnhn.fr/espece/cd_nom/" + s);
        result.put("CatalogueOfLife", s -> "http://www.catalogueoflife.org/col/details/species/id/" + s);
        return Collections.unmodifiableMap(result);
    }

    private static List<NavbarEntry> createNavbarEntries() {
        return Arrays.asList(
            NavbarEntry.menu("navbar.urgi", Arrays.asList(
                NavbarEntry.link("navbar.urgi.home", "https://urgi.versailles.inrae.fr"),
                NavbarEntry.link("navbar.urgi.news", "https://urgi.versailles.inrae.fr/About-us/News"),
                NavbarEntry.link("navbar.urgi.about-us", "https://urgi.versailles.inrae.fr/About-us")
            )),
            NavbarEntry.link("navbar.about", "https://urgi.versailles.inrae.fr/faidare/about"),
            NavbarEntry.link("navbar.join-us", "https://urgi.versailles.inrae.fr/faidare/join"),
            NavbarEntry.link("navbar.terms-of-use", "https://urgi.versailles.inrae.fr/faidare/legal"),
            NavbarEntry.link("navbar.help", "https://urgi.versailles.inrae.fr/faidare/help"),
            NavbarEntry.link("navbar.news-perspectives", "https://urgi.versailles.inrae.fr/faidare/news"),
            NavbarEntry.link("navbar.web-services", "https://urgi.versailles.inrae.fr/faidare/swagger-ui/index.html")
        );
    }

    private final Locale locale;
    private final String searchUrl;

    public FaidareExpressions(Locale locale, String searchUrl) {
        this.locale = locale;
        this.searchUrl = searchUrl;
    }

    public String toSiteParam(String siteId) {
        return Sites.siteIdToLocationId(siteId);
    }

    public String collPopTitle(CollPopVO collPopVO) {
        return collPopTitle(collPopVO, Function.identity());
    }

    public String collPopTitleWithoutUnderscores(CollPopVO collPopVO) {
        return collPopTitle(collPopVO, s -> s.replace('_', ' '));
    }

    public String taxonIdUrl(TaxonSourceVO taxonSource) {
        Function<String, String> urlFactory =
            TAXON_ID_URL_FACTORIES_BY_SOURCE_NAME.get(taxonSource.getSourceName());
        return urlFactory != null ? urlFactory.apply(taxonSource.getTaxonId()) : null;
    }

    public List<NavbarEntry> navbarEntries() {
        return NAVBAR_ENTRIES;
    }

    public String searchUrl() {
        return searchUrl;
    }

    private String collPopTitle(CollPopVO collPopVO, Function<String, String> nameTransformer) {
        if (Strings.isBlank(collPopVO.getType())) {
            return nameTransformer.apply(collPopVO.getName());
        } else {
            return nameTransformer.apply(collPopVO.getName()) + " (" + collPopVO.getType() + ")";
        }
    }
}
