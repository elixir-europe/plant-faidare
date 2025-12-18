package fr.inrae.urgi.faidare.web.site;

import fr.inrae.urgi.faidare.config.DataSource;
import fr.inrae.urgi.faidare.config.FaidareProperties;
import fr.inrae.urgi.faidare.dao.XRefDocumentDao;
import fr.inrae.urgi.faidare.dao.v2.LocationV2Dao;
import fr.inrae.urgi.faidare.domain.XRefDocumentVO;
import fr.inrae.urgi.faidare.domain.brapi.LocationSitemapVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.LocationV2VO;
import fr.inrae.urgi.faidare.web.Fixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static fr.inrae.urgi.faidare.web.Fixtures.htmlContent;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * MVC tests for {@link SiteController}
 * @author JB Nizet
 */
@WebMvcTest(SiteController.class)
public class SiteControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LocationV2Dao mockLocationRepository;

    @MockitoBean
    private XRefDocumentDao mockXRefDocumentRepository;

    @MockitoBean
    private FaidareProperties mockFaidareProperties;

    private LocationV2VO site;
    private List<XRefDocumentVO> crossReferences;
    private DataSource dataSource;

    @BeforeEach
    void prepare() {
        site = Fixtures.createSiteV2();
        when(mockLocationRepository.getByLocationDbId(site.getLocationDbId())).thenReturn(site);

        crossReferences = Arrays.asList(
            Fixtures.createXref("foobar"),
            Fixtures.createXref("bazbing")
        );
        when(mockXRefDocumentRepository.findByLinkedResourcesID(any()))
            .thenReturn(crossReferences);

        dataSource = Fixtures.createDataSource();
        when(mockFaidareProperties.getByUri(site.getSourceUri())).thenReturn(dataSource);
    }

    @Test
    void shouldDisplaySite() throws Exception {
        mockMvc.perform(get("/sites/{id}", site.getLocationDbId()))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(htmlContent().hasTitle("Site France"))
            .andExpect(htmlContent().containsH2s("Details", "Cross references"))
            .andExpect(htmlContent().endsCorrectly());

    }

    @Test
    void shouldGenerateSitemap() throws Exception {
        List<LocationSitemapVO> sites = Arrays.asList(
            new LocationSitemapVO("site1"),
            new LocationSitemapVO("site4"),
            new LocationSitemapVO("site53"),
            new LocationSitemapVO("site68")
        );

        // the hashCode algorithm is specified in the javadoc, so it's guaranteed to be
        // the same everywhere
        // uncomment the following line to see which sitemap index each study has
        // sites.forEach(site -> System.out.println(site.getLocationDbId() + " = " + Math.floorMod(site.getLocationDbId().hashCode(), Sitemaps.BUCKET_COUNT)));

        when(mockLocationRepository.findAllForSitemap()).thenAnswer(invocation -> sites.stream());
        testSitemap(2, "http://localhost/faidare/sites/site1\nhttp://localhost/faidare/sites/site53\n");
        testSitemap(5, "http://localhost/faidare/sites/site4\nhttp://localhost/faidare/sites/site68\n");
        testSitemap(7, "");

        mockMvc.perform(get("/faidare/sites/sitemap-17.txt")
                            .contextPath("/faidare"))
               .andExpect(status().isNotFound());
    }

    private void testSitemap(int index, String expectedContent) throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/faidare/sites/sitemap-" + index + ".txt")
                                                  .contextPath("/faidare"))
                                     .andExpect(request().asyncStarted())
                                     .andReturn();

        this.mockMvc.perform(asyncDispatch(mvcResult))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                    .andExpect(content().string(expectedContent));

    }
}
