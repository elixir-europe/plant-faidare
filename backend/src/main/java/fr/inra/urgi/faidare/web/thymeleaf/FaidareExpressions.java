package fr.inra.urgi.faidare.web.thymeleaf;

import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.inra.urgi.faidare.domain.data.germplasm.CollPopVO;
import fr.inra.urgi.faidare.domain.data.germplasm.TaxonSourceVO;
import fr.inra.urgi.faidare.utils.Sites;
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
            NavbarEntry.menu("URGI", Arrays.asList(
                NavbarEntry.link("Home", "https://urgi.versailles.inra.fr"),
                NavbarEntry.link("News", "https://urgi.versailles.inra.fr/About-us/News"),
                NavbarEntry.link("About us", "https://urgi.versailles.inra.fr/About-us")
            )),
            NavbarEntry.menu("More...", Arrays.asList(
                NavbarEntry.link("About", "https://urgi.versailles.inrae.fr/faidare/about"),
                NavbarEntry.link("Join us", "https://urgi.versailles.inra.fr/faidare/join"),
                NavbarEntry.link("Terms of use", "https://urgi.versailles.inra.fr/faidare/legal"),
                NavbarEntry.link("Help", "https://urgi.versailles.inra.fr/faidare/help"),
                NavbarEntry.link("News/Perspectives", "https://urgi.versailles.inra.fr/faidare/news")
            ))
        );
    }

    private final Locale locale;

    public FaidareExpressions(Locale locale) {
        this.locale = locale;
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
        return "https://urgi.versailles.inrae.fr/faidare/search";
    }

    private String collPopTitle(CollPopVO collPopVO, Function<String, String> nameTransformer) {
        if (Strings.isBlank(collPopVO.getType())) {
            return nameTransformer.apply(collPopVO.getName());
        } else {
            return nameTransformer.apply(collPopVO.getName()) + " (" + collPopVO.getType() + ")";
        }
    }
}
