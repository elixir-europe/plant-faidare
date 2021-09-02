package fr.inra.urgi.faidare.web.germplasm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import fr.inra.urgi.faidare.config.FaidareProperties;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmSitemapVO;
import fr.inra.urgi.faidare.domain.data.study.StudySitemapVO;
import fr.inra.urgi.faidare.repository.es.GermplasmAttributeRepository;
import fr.inra.urgi.faidare.repository.es.GermplasmRepository;
import fr.inra.urgi.faidare.repository.es.XRefDocumentRepository;
import fr.inra.urgi.faidare.utils.Sitemaps;
import fr.inra.urgi.faidare.web.study.StudyController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/**
 * MVC tests for {@link GermplasmController}
 * @author JB Nizet
 */
@WebMvcTest(GermplasmController.class)
public class GermplasmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GermplasmRepository mockGermplasmRepository;

    @MockBean
    private FaidareProperties mockFaidareProperties;

    @MockBean
    private XRefDocumentRepository mockXRefDocumentRepository;

    @MockBean
    private GermplasmAttributeRepository mockGermplasmAttributeRepository;


    @Test
    void shouldGenerateSitemap() throws Exception {
        List<GermplasmSitemapVO> germplasms = Arrays.asList(
            new GermplasmSitemapVO("germplasm1"),
            new GermplasmSitemapVO("germplasm4"),
            new GermplasmSitemapVO("germplasm45"),
            new GermplasmSitemapVO("germplasm73")
        );

        // the hashCode algorithm is specified in the javadoc, so it's guaranteed to be
        // the same everywhere
        // uncomment the following line to see which sitemap index each study has
        // germplasms.forEach(germplasm -> System.out.println(germplasm.getGermplasmDbId() + " = " + Math.floorMod(germplasm.getGermplasmDbId().hashCode(), Sitemaps.BUCKET_COUNT)));

        when(mockGermplasmRepository.scrollAllForSitemap(anyInt())).thenAnswer(invocation -> germplasms.iterator());

        testSitemap(6, "http://localhost/faidare/germplasms/germplasm1\nhttp://localhost/faidare/germplasms/germplasm45\n");
        testSitemap(9, "http://localhost/faidare/germplasms/germplasm4\nhttp://localhost/faidare/germplasms/germplasm73\n");
        testSitemap(7, "");

        mockMvc.perform(get("/faidare/germplasms/sitemap-17.txt")
                            .contextPath("/faidare"))
               .andExpect(status().isNotFound());
    }

    private void testSitemap(int index, String expectedContent) throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/faidare/germplasms/sitemap-" + index + ".txt")
                                                  .contextPath("/faidare"))
                                     .andExpect(request().asyncStarted())
                                     .andReturn();

        this.mockMvc.perform(asyncDispatch(mvcResult))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                    .andExpect(content().string(expectedContent));

    }
}