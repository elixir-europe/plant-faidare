package fr.inrae.urgi.faidare.web.trial;

import static fr.inrae.urgi.faidare.web.Fixtures.htmlContent;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import fr.inrae.urgi.faidare.api.brapi.v2.BrapiListResponse;
import fr.inrae.urgi.faidare.config.DataSource;
import fr.inrae.urgi.faidare.config.FaidareProperties;
import fr.inrae.urgi.faidare.dao.v1.LocationV1Dao;
import fr.inrae.urgi.faidare.dao.v2.ChoosableObservationExportCriteria;
import fr.inrae.urgi.faidare.dao.v2.GermplasmV2Dao;
import fr.inrae.urgi.faidare.dao.v2.ObservationUnitV2Dao;
import fr.inrae.urgi.faidare.dao.v2.ObservationV2Dao;
import fr.inrae.urgi.faidare.dao.v2.TrialV2Dao;
import fr.inrae.urgi.faidare.domain.LocationVO;
import fr.inrae.urgi.faidare.domain.brapi.TrialSitemapVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.GermplasmV2VO;
import fr.inrae.urgi.faidare.domain.brapi.v2.TrialV2VO;
import fr.inrae.urgi.faidare.web.Fixtures;
import fr.inrae.urgi.faidare.web.germplasm.ExportFormat;
import fr.inrae.urgi.faidare.web.observationunit.ObservationUnitExportJob;
import fr.inrae.urgi.faidare.web.observationunit.ObservationUnitExportJobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/**
 * MVC tests for {@link TrialController}
 * @author JB Nizet
 */
@WebMvcTest(TrialController.class)
public class TrialControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TrialV2Dao mockTrialRepository;

    @MockitoBean
    private FaidareProperties mockFaidareProperties;

    @MockitoBean
    private LocationV1Dao mockLocationRepository;

    @MockitoBean
    private GermplasmV2Dao mockGermplasmRepository;

    @MockitoBean
    private ObservationUnitV2Dao mockObservationUnitRepository;

    @MockitoBean
    private ObservationV2Dao mockObservationRepository;

    @MockitoBean
    private ObservationUnitExportJobService mockObservationUnitExportJobService;

    @Autowired
    private TrialController trialController;

    private TrialV2VO trial;
    private DataSource dataSource;
    private GermplasmV2VO germplasm;
    private LocationVO location;

    @BeforeEach
    void prepare() {
        trial = Fixtures.createTrialV2();
        when(mockTrialRepository.getByTrialDbId(trial.getTrialDbId())).thenReturn(trial);

        germplasm = Fixtures.createGermplasmV2ForTrial();
        when(mockGermplasmRepository.findGermplasmsByCriteria(any())).thenReturn(
            new BrapiListResponse<>(null, List.of(germplasm))
        );

        dataSource = Fixtures.createDataSource();

        when(mockFaidareProperties.getByUri(germplasm.getSourceUri())).thenReturn(dataSource);

        location = Fixtures.createSite();
        when(mockLocationRepository.getByLocationDbIdIn(anySet())).thenReturn(List.of(location));

        when(mockObservationRepository.findChoosableObservationExportCriteriaByTrialDbId(trial.getTrialDbId())).thenReturn(
            new ChoosableObservationExportCriteria(
                List.of(),
                List.of(),
                List.of()
            )
        );
    }

    @Test
    void shouldDisplayTrial() throws Exception {
        mockMvc.perform(get("/trials/{id}", trial.getTrialDbId()))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
               .andExpect(htmlContent().hasTitle("Trial Trial type 1: Trial 1"))
               .andExpect(htmlContent().containsH2s("Identification", "Genotype", "Studies", "Contact"))
               .andExpect(htmlContent().endsCorrectly());
    }

    @Test
    void shouldDisplayTrialExport() throws Exception {
        ObservationUnitExportJob job = new ObservationUnitExportJob("job1", ExportFormat.EXCEL);
        when(mockObservationUnitExportJobService.getJob(job.getId())).thenReturn(Optional.of(job));
        mockMvc.perform(get("/trials/{id}/exports/{jobId}", trial.getTrialDbId(), job.getId()))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
               .andExpect(htmlContent().hasTitle("Export for trial Trial type 1: Trial 1"))
               .andExpect(htmlContent().endsCorrectly());
    }

    @Test
    void shouldGenerateSitemap() throws Exception {
        List<TrialSitemapVO> trials = Arrays.asList(
            new TrialSitemapVO("trial1"),
            new TrialSitemapVO("trial4"),
            new TrialSitemapVO("trial54"),
            new TrialSitemapVO("trial70")
        );

        // the hashCode algorithm is specified in the javadoc, so it's guaranteed to be
        // the same everywhere
        // uncomment the following line to see which sitemap index each study has
        // trials.forEach(trial -> System.out.println(trial.getTrialDbId() + " = " + Math.floorMod(trial.getTrialDbId().hashCode(), Sitemaps.BUCKET_COUNT)));

        when(mockTrialRepository.findAllForSitemap()).thenAnswer(invocation -> trials.stream());
        testSitemap(0, "http://localhost/faidare/trials/trial4\nhttp://localhost/faidare/trials/trial70\n");
        testSitemap(8, "http://localhost/faidare/trials/trial1\nhttp://localhost/faidare/trials/trial54\n");
        testSitemap(7, "");

        mockMvc.perform(get("/faidare/trials/sitemap-17.txt")
                            .contextPath("/faidare"))
               .andExpect(status().isNotFound());
    }

    private void testSitemap(int index, String expectedContent) throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/faidare/trials/sitemap-" + index + ".txt")
                                                  .contextPath("/faidare"))
                                     .andExpect(request().asyncStarted())
                                     .andReturn();

        this.mockMvc.perform(asyncDispatch(mvcResult))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                    .andExpect(content().string(expectedContent));

    }
}
