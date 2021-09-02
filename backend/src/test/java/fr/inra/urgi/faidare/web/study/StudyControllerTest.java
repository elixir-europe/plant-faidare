package fr.inra.urgi.faidare.web.study;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import fr.inra.urgi.faidare.config.FaidareProperties;
import fr.inra.urgi.faidare.domain.data.LocationSitemapVO;
import fr.inra.urgi.faidare.domain.data.study.StudySitemapVO;
import fr.inra.urgi.faidare.repository.es.GermplasmRepository;
import fr.inra.urgi.faidare.repository.es.LocationRepository;
import fr.inra.urgi.faidare.repository.es.StudyRepository;
import fr.inra.urgi.faidare.repository.es.TrialRepository;
import fr.inra.urgi.faidare.repository.es.XRefDocumentRepository;
import fr.inra.urgi.faidare.repository.file.CropOntologyRepository;
import fr.inra.urgi.faidare.utils.Sitemaps;
import fr.inra.urgi.faidare.web.site.SiteController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/**
 * MVC tests for {@link StudyController}
 * @author JB Nizet
 */
@WebMvcTest(StudyController.class)
public class StudyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudyRepository mockStudyRepository;

    @MockBean
    private FaidareProperties mockFaidareProperties;

    @MockBean
    private XRefDocumentRepository mockXRefDocumentRepository;

    @MockBean
    private GermplasmRepository mockGermplasmRepository;

    @MockBean
    private CropOntologyRepository mockCropOntologyRepository;

    @MockBean
    private TrialRepository mockTrialRepository;

    @MockBean
    private LocationRepository mockLocationRepository;

    @Test
    void shouldGenerateSitemap() throws Exception {
        List<StudySitemapVO> studies = Arrays.asList(
            new StudySitemapVO("study1"),
            new StudySitemapVO("study4"),
            new StudySitemapVO("study51"),
            new StudySitemapVO("study72")
        );

        // the hashCode algorithm is specified in the javadoc, so it's guaranteed to be
        // the same everywhere
        // uncomment the following line to see which sitemap index each study has
        // studies.forEach(study -> System.out.println(study.getStudyDbId() + " = " + Math.floorMod(study.getStudyDbId().hashCode(), Sitemaps.BUCKET_COUNT)));

        when(mockStudyRepository.scrollAllForSitemap(anyInt())).thenAnswer(invocation -> studies.iterator());
        testSitemap(6, "http://localhost/faidare/studies/study1\nhttp://localhost/faidare/studies/study72\n");
        testSitemap(9, "http://localhost/faidare/studies/study4\nhttp://localhost/faidare/studies/study51\n");
        testSitemap(7, "");

        mockMvc.perform(get("/faidare/studies/sitemap-17.txt")
                            .contextPath("/faidare"))
               .andExpect(status().isNotFound());
    }

    private void testSitemap(int index, String expectedContent) throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/faidare/studies/sitemap-" + index + ".txt")
                                                  .contextPath("/faidare"))
                                     .andExpect(request().asyncStarted())
                                     .andReturn();

        this.mockMvc.perform(asyncDispatch(mvcResult))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                    .andExpect(content().string(expectedContent));

    }
}
