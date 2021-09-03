package fr.inra.urgi.faidare.web.study;

import static fr.inra.urgi.faidare.web.Fixtures.createSite;
import static fr.inra.urgi.faidare.web.Fixtures.htmlContent;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import fr.inra.urgi.faidare.config.FaidareProperties;
import fr.inra.urgi.faidare.domain.data.LocationSitemapVO;
import fr.inra.urgi.faidare.domain.data.LocationVO;
import fr.inra.urgi.faidare.domain.data.TrialVO;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmVO;
import fr.inra.urgi.faidare.domain.data.study.StudyDetailVO;
import fr.inra.urgi.faidare.domain.data.study.StudySitemapVO;
import fr.inra.urgi.faidare.domain.datadiscovery.data.DataSource;
import fr.inra.urgi.faidare.domain.response.PaginatedList;
import fr.inra.urgi.faidare.domain.xref.XRefDocumentSearchCriteria;
import fr.inra.urgi.faidare.domain.xref.XRefDocumentVO;
import fr.inra.urgi.faidare.repository.es.GermplasmRepository;
import fr.inra.urgi.faidare.repository.es.LocationRepository;
import fr.inra.urgi.faidare.repository.es.StudyRepository;
import fr.inra.urgi.faidare.repository.es.TrialRepository;
import fr.inra.urgi.faidare.repository.es.XRefDocumentRepository;
import fr.inra.urgi.faidare.repository.file.CropOntologyRepository;
import fr.inra.urgi.faidare.utils.Sitemaps;
import fr.inra.urgi.faidare.web.Fixtures;
import fr.inra.urgi.faidare.web.site.SiteController;
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

    private StudyDetailVO study;
    private GermplasmVO germplasm;
    private List<XRefDocumentVO> crossReferences;
    private DataSource dataSource;
    private LocationVO location;
    private TrialVO trial;

    @BeforeEach
    void prepare() {
        study = Fixtures.createStudy();
        when(mockStudyRepository.getById(study.getStudyDbId())).thenReturn(study);

        germplasm = Fixtures.createGermplasm();
        when(mockGermplasmRepository.find(any())).thenReturn(new PaginatedList<>(null, Arrays.asList(germplasm)));

        crossReferences = Arrays.asList(
            Fixtures.createXref("foobar"),
            Fixtures.createXref("bazbing")
        );
        when(mockXRefDocumentRepository.find(any()))
            .thenReturn(new PaginatedList<>(null, crossReferences));

        dataSource = Fixtures.createDataSource();
        when(mockFaidareProperties.getByUri(study.getSourceUri())).thenReturn(dataSource);

        location = Fixtures.createSite();
        when(mockLocationRepository.getById(study.getLocationDbId())).thenReturn(location);

        trial = Fixtures.createTrial();
        when(mockTrialRepository.getById(study.getTrialDbIds().iterator().next())).thenReturn(trial);

        Set<String> variableDbIds = Collections.singleton("variable1");
        when(mockStudyRepository.getVariableIds(study.getStudyDbId())).thenReturn(variableDbIds);
        when(mockCropOntologyRepository.getVariableByIds(variableDbIds)).thenReturn(
            Arrays.asList(Fixtures.createVariable())
        );
    }

    @Test
    void shouldDisplayStudy() throws Exception {
        mockMvc.perform(get("/studies/{id}", study.getStudyDbId()))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
               .andExpect(htmlContent().hasTitle("Study Doability: Study 1"))
               .andExpect(htmlContent().containsH2s("Identification", "Genotype", "Variables", "Data Set", "Contact", "Additional information", "Cross references"))
               .andExpect(htmlContent().endsCorrectly());
    }

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
