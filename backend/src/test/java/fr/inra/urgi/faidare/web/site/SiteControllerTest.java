package fr.inra.urgi.faidare.web.site;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import fr.inra.urgi.faidare.config.FaidareProperties;
import fr.inra.urgi.faidare.domain.data.LocationSitemapVO;
import fr.inra.urgi.faidare.repository.es.LocationRepository;
import fr.inra.urgi.faidare.repository.es.XRefDocumentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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

    @Test
    void shouldGenerateSitemap() throws Exception {
        List<LocationSitemapVO> sites = Arrays.asList(
            new LocationSitemapVO("site1"),
            new LocationSitemapVO("site2")
        );
        when(mockLocationRepository.scrollAllForSitemap(anyInt())).thenReturn(sites.iterator());
        MvcResult mvcResult = mockMvc.perform(get("/faidare/sites/sitemap.txt")
                                                  .contextPath("/faidare"))
                                     .andExpect(request().asyncStarted())
                                     .andReturn();

        this.mockMvc.perform(asyncDispatch(mvcResult))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                    .andExpect(content().string("http://localhost/faidare/sites/site1\nhttp://localhost/faidare/sites/site2\n"));
    }
}
