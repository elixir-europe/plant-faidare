package fr.inra.urgi.faidare.web.sitemap;

import java.nio.charset.StandardCharsets;

import fr.inra.urgi.faidare.utils.Sitemaps;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller used to generate the sitemap index containing links to the site sitemap
 * (unique), the N sitemaps for the studies and the N sitemaps for the germplasms.
 *
 * Sitemaps for studies and germplasms are split in N buckets because sitemaps
 * can't be more than 50 MB and can't have more than 50,000 entries.
 * Splitting them in N sitemaps makes it almost sure that none of the sitemaps
 * overflows those limits.
 *
 * The sitemaps are split based on the hashCode of the ID of the document:
 * if a document's hash code modulo N is 0, then it's in the sitemap-0.txt, etc.
 *
 * @author JB Nizet
 */
@RestController
@RequestMapping("")
public class SitemapIndexController {
    @GetMapping("/sitemap.xml")
    public ResponseEntity<byte[]> sitemapIndex() {
        StringBuilder builder = new StringBuilder();
        builder
            .append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
            .append("<sitemapindex xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");

        for (int i = 0; i < Sitemaps.BUCKET_COUNT; i++) {
            appendSiteMap(builder, "/sites/sitemap-" + i + ".txt");
        }
        for (int i = 0; i < Sitemaps.BUCKET_COUNT; i++) {
            appendSiteMap(builder, "/germplasms/sitemap-" + i + ".txt");
        }
        for (int i = 0; i < Sitemaps.BUCKET_COUNT; i++) {
            appendSiteMap(builder, "/studies/sitemap-" + i + ".txt");
        }
        builder.append("</sitemapindex>");

        return ResponseEntity.ok()
                             .contentType(MediaType.TEXT_XML)
                             .body(builder.toString().getBytes(StandardCharsets.UTF_8));
    }

    public void appendSiteMap(StringBuilder builder, String path) {
        builder.append("  <sitemap>\n")
               .append("    <loc>")
               .append(Sitemaps.generateSitemapUrl(path))
               .append("</loc>\n")
               .append("  </sitemap>\n");
    }
}
