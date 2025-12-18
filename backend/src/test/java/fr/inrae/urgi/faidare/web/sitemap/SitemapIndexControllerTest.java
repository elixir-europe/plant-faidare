package fr.inrae.urgi.faidare.web.sitemap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.inrae.urgi.faidare.utils.Sitemaps;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * MVC tests for {@link SitemapIndexController}
 * @author JB Nizet
 */
@WebMvcTest(SitemapIndexController.class)
class SitemapIndexControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldGenerateSitemapIndex() throws Exception {
        mockMvc.perform(get("/faidare/sitemap.xml").contextPath("/faidare"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.TEXT_XML))
            .andExpect(xpath("/sitemapindex/sitemap[1]/loc").string("http://localhost/faidare/sites/sitemap-0.txt"))
            .andExpect(xpath("/sitemapindex/sitemap[2]/loc").string("http://localhost/faidare/sites/sitemap-1.txt"))
            .andExpect(xpath("/sitemapindex/sitemap[" + (Sitemaps.BUCKET_COUNT + 1) + "]/loc").string("http://localhost/faidare/germplasms/sitemap-0.txt"))
            .andExpect(xpath("/sitemapindex/sitemap[" + (Sitemaps.BUCKET_COUNT + 2) + "]/loc").string("http://localhost/faidare/germplasms/sitemap-1.txt"))
            .andExpect(xpath("/sitemapindex/sitemap[" + (Sitemaps.BUCKET_COUNT * 2 + 1) + "]/loc").string("http://localhost/faidare/studies/sitemap-0.txt"))
            .andExpect(xpath("/sitemapindex/sitemap[" + (Sitemaps.BUCKET_COUNT * 2 + 2) + "]/loc").string("http://localhost/faidare/studies/sitemap-1.txt"));
    }
}
