package fr.inra.urgi.faidare.web.thymeleaf;

import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

import fr.inra.urgi.faidare.domain.data.germplasm.CollPopVO;
import fr.inra.urgi.faidare.domain.data.germplasm.TaxonSourceVO;
import org.apache.logging.log4j.util.Strings;

/**
 * The actual object offering Faidare helper methods to thymeleaf
 * @author JB Nizet
 */
public class FaidareExpressions {

    private static final Map<String, Function<String, String>> TAXON_ID_URL_FACTORIES_BY_SOURCE_NAME =
        createTaxonIdUrlFactories();

    private static Map<String, Function<String, String>> createTaxonIdUrlFactories() {
        Map<String, Function<String, String>> result = new HashMap<>();
        result.put("NCBI", s -> "https://www.ncbi.nlm.nih.gov/Taxonomy/Browser/wwwtax.cgi?mode=Info&id=" + s);
        result.put("ThePlantList", s -> "http://www.theplantlist.org/tpl1.1/record/" + s);
        result.put("TAXREF", s -> "https://inpn.mnhn.fr/espece/cd_nom/" + s);
        result.put("CatalogueOfLife", s -> "http://www.catalogueoflife.org/col/details/species/id/" + s);
        return Collections.unmodifiableMap(result);
    }

    private final Locale locale;

    public FaidareExpressions(Locale locale) {
        this.locale = locale;
    }

    public String toSiteParam(String siteId) {
        return Base64.getUrlEncoder().encodeToString(("urn:URGI/location/" + siteId).getBytes(StandardCharsets.US_ASCII));
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

    private String collPopTitle(CollPopVO collPopVO, Function<String, String> nameTransformer) {
        if (Strings.isBlank(collPopVO.getType())) {
            return nameTransformer.apply(collPopVO.getName());
        } else {
            return nameTransformer.apply(collPopVO.getName()) + " (" + collPopVO.getType() + ")";
        }
    }
}
