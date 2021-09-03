package fr.inra.urgi.faidare.web.site;

import static fr.inra.urgi.faidare.web.Fixtures.htmlContent;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import fr.inra.urgi.faidare.config.FaidareProperties;
import fr.inra.urgi.faidare.domain.data.LocationSitemapVO;
import fr.inra.urgi.faidare.domain.data.LocationVO;
import fr.inra.urgi.faidare.domain.data.study.StudySitemapVO;
import fr.inra.urgi.faidare.domain.datadiscovery.data.DataSource;
import fr.inra.urgi.faidare.domain.response.PaginatedList;
import fr.inra.urgi.faidare.domain.xref.XRefDocumentSearchCriteria;
import fr.inra.urgi.faidare.domain.xref.XRefDocumentVO;
import fr.inra.urgi.faidare.repository.es.LocationRepository;
import fr.inra.urgi.faidare.repository.es.XRefDocumentRepository;
import fr.inra.urgi.faidare.utils.Sitemaps;
import fr.inra.urgi.faidare.web.Fixtures;
import fr.inra.urgi.faidare.web.thymeleaf.CoordinatesDialect;
import fr.inra.urgi.faidare.web.thymeleaf.FaidareDialect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/**
 * MVC tests for {@link SiteController}
 * @author JB Nizet
 */
@WebMvcTest(SiteController.class)
public class SiteControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocationRepository mockLocationRepository;

    @MockBean
    private XRefDocumentRepository mockXRefDocumentRepository;

    @MockBean
    private FaidareProperties mockFaidareProperties;

    private LocationVO site;
    private List<XRefDocumentVO> crossReferences;
    private DataSource dataSource;

    @BeforeEach
    void prepare() {
        site = Fixtures.createSite();
        when(mockLocationRepository.getById(site.getLocationDbId())).thenReturn(site);

        crossReferences = Arrays.asList(
            Fixtures.createXref("foobar"),
            Fixtures.createXref("bazbing")
        );
        when(mockXRefDocumentRepository.find(any()))
            .thenReturn(new PaginatedList<>(null, crossReferences));

        dataSource = Fixtures.createDataSource();
        when(mockFaidareProperties.getByUri(site.getSourceUri())).thenReturn(dataSource);
    }

    @Test
    void shouldDisplaySite() throws Exception {
        mockMvc.perform(get("/sites/{id}", site.getLocationDbId()))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(htmlContent().hasTitle("Site France"))
            .andExpect(htmlContent().containsH2s("Details", "Additional info", "Cross references"))
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

        when(mockLocationRepository.scrollAllForSitemap(anyInt())).thenAnswer(invocation -> sites.iterator());
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
