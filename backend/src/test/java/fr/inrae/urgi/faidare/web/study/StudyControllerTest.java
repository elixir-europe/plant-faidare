package fr.inrae.urgi.faidare.web.study;

import static fr.inrae.urgi.faidare.web.Fixtures.htmlContent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.inrae.urgi.faidare.config.DataSource;
import fr.inrae.urgi.faidare.config.FaidareProperties;
import fr.inrae.urgi.faidare.dao.XRefDocumentDao;
import fr.inrae.urgi.faidare.dao.file.CropOntologyRepository;
import fr.inrae.urgi.faidare.dao.v2.GermplasmV2Dao;
import fr.inrae.urgi.faidare.dao.v2.LocationV2Dao;
import fr.inrae.urgi.faidare.dao.v2.StudyV2Dao;
import fr.inrae.urgi.faidare.dao.v2.TrialV2Dao;
import fr.inrae.urgi.faidare.domain.XRefDocumentVO;
import fr.inrae.urgi.faidare.domain.brapi.StudySitemapVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.GermplasmV2VO;
import fr.inrae.urgi.faidare.domain.brapi.v2.LocationV2VO;
import fr.inrae.urgi.faidare.domain.brapi.v2.StudyV2VO;
import fr.inrae.urgi.faidare.domain.brapi.v2.TrialV2VO;
import fr.inrae.urgi.faidare.domain.variable.ObservationVariableV1VO;
import fr.inrae.urgi.faidare.web.Fixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Stream;

import static fr.inrae.urgi.faidare.web.Fixtures.htmlContent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * MVC tests for {@link StudyController}
 * @author JB Nizet
 */
@WebMvcTest(StudyController.class)
public class StudyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudyV2Dao mockStudyRepository;

    @MockitoBean
    private FaidareProperties mockFaidareProperties;

    @MockitoBean
    private XRefDocumentDao mockXRefDocumentRepository;

    @MockitoBean
    private GermplasmV2Dao mockGermplasmRepository;

    @MockitoBean
    private CropOntologyRepository mockCropOntologyRepository;

    @MockitoBean
    private TrialV2Dao mockTrialRepository;

    @MockitoBean
    private LocationV2Dao mockLocationRepository;

    @Autowired
    private StudyController studyController;

    private StudyV2VO study;
    private GermplasmV2VO germplasm;
    private List<XRefDocumentVO> crossReferences;
    private DataSource dataSource;
    private LocationV2VO location;
    private TrialV2VO trial;



    @BeforeEach
    void prepare() {
        study = Fixtures.createStudy();
        when(mockStudyRepository.getByStudyDbId(study.getStudyDbId())).thenReturn(study);

        germplasm = Fixtures.createGermplasm();
        when(mockGermplasmRepository.findByGermplasmDbIdIn(any())).thenAnswer(
            invocation -> Stream.of(germplasm)
        );

        crossReferences = Arrays.asList(
            Fixtures.createXref("foobar"),
            Fixtures.createXref("bazbing")
        );
        when(mockXRefDocumentRepository.findByLinkedResourcesID(any()))
            .thenReturn(crossReferences);

        dataSource = Fixtures.createDataSource();

        when(mockFaidareProperties.getByUri(study.getSourceUri())).thenReturn(dataSource);

        location = Fixtures.createSiteV2();
        when(mockLocationRepository.getByLocationDbId(study.getLocationDbId())).thenReturn(location);

        trial = Fixtures.createTrial();
        when(mockTrialRepository.getByTrialDbId(study.getTrialDbIds().iterator().next())).thenReturn(trial);

        Set<String> variableDbIds = Collections.singleton("variable1");
        // FIXME JBN uncomment this line once StudyV1Dao has a getVariableIds() method
        // when(mockStudyRepository.getVariableIds(study.getStudyDbId())).thenReturn(variableDbIds);

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
               .andExpect(htmlContent().containsH2s("Identification", "Genotype", "Data Set", "Contact", "Cross references"))
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

        when(mockStudyRepository.findAllForSitemap()).thenAnswer(invocation -> studies.stream());
        testSitemap(6, "http://localhost/faidare/studies/study1\nhttp://localhost/faidare/studies/study72\n");
        testSitemap(9, "http://localhost/faidare/studies/study4\nhttp://localhost/faidare/studies/study51\n");
        testSitemap(7, "");

        mockMvc.perform(get("/faidare/studies/sitemap-17.txt")
                            .contextPath("/faidare"))
               .andExpect(status().isNotFound());
    }

    @Nested
    class Variables {
        @Test
        void shouldFilterVariablesByLanguageWhenRequestedLanguageIsFound() throws Exception {
            study.setObservationVariableDbIds(List.of("var1", "var2", "var3"));
            ObservationVariableV1VO variableWithEnglishLanguage = Fixtures.createVariable();
            variableWithEnglishLanguage.setLanguage("EN");
            variableWithEnglishLanguage.setObservationVariableDbId("var2");

            ObservationVariableV1VO variableWithFrenchLanguage = Fixtures.createVariable();
            variableWithFrenchLanguage.setLanguage("FRA");
            variableWithFrenchLanguage.setObservationVariableDbId("var1");


            ObservationVariableV1VO variableWithNoLanguage = Fixtures.createVariable();
            variableWithNoLanguage.setLanguage(null);
            variableWithNoLanguage.setObservationVariableDbId("var3");

            when(mockCropOntologyRepository.getVariableByIds(any())).thenReturn(
                Arrays.asList(variableWithEnglishLanguage, variableWithFrenchLanguage, variableWithNoLanguage)
            );

            ModelAndView modelAndView = mockMvc.perform(get("/studies/{id}", study.getStudyDbId())
                                                            .locale(Locale.FRENCH))
                                               .andReturn()
                                               .getModelAndView();
            StudyModel model = (StudyModel) modelAndView.getModel().get("model");
            assertThat(model.getVariables()).containsOnly(variableWithFrenchLanguage, variableWithNoLanguage);
        }

        @Test
        void shouldFilterVariablesByLanguageWhenRequestedLanguageIsNotFound() throws Exception {
            study.setObservationVariableDbIds(List.of("var1", "var2", "var3"));

            ObservationVariableV1VO variableWithEnglishLanguage = Fixtures.createVariable();
            variableWithEnglishLanguage.setLanguage("EN");
            variableWithEnglishLanguage.setObservationVariableDbId("var2");


            ObservationVariableV1VO variableWithFrenchLanguage = Fixtures.createVariable();
            variableWithFrenchLanguage.setLanguage("FRA");
            variableWithFrenchLanguage.setObservationVariableDbId("var1");


            ObservationVariableV1VO variableWithNoLanguage = Fixtures.createVariable();
            variableWithNoLanguage.setLanguage(null);
            variableWithNoLanguage.setObservationVariableDbId("var3");


            when(mockCropOntologyRepository.getVariableByIds(any())).thenReturn(
                Arrays.asList(variableWithEnglishLanguage, variableWithFrenchLanguage, variableWithNoLanguage)
            );

            ModelAndView modelAndView = mockMvc.perform(get("/studies/{id}", study.getStudyDbId())
                                                            .locale(Locale.CHINA))
                                               .andReturn()
                                               .getModelAndView();
            StudyModel model = (StudyModel) modelAndView.getModel().get("model");
            assertThat(model.getVariables()).containsOnly(variableWithEnglishLanguage, variableWithNoLanguage);
        }

        @Test
        void shouldFilterVariablesByLanguageWhenRequestedLanguageIsNotFoundAndEnglishAbsent() throws Exception {
            study.setObservationVariableDbIds(List.of("var1", "var2", "var3"));
            ObservationVariableV1VO variableWithSpanishLanguage = Fixtures.createVariable();
            variableWithSpanishLanguage.setLanguage("es");
            variableWithSpanishLanguage.setObservationVariableDbId("var2");


            ObservationVariableV1VO variableWithFrenchLanguage = Fixtures.createVariable();
            variableWithFrenchLanguage.setLanguage("FRA");
            variableWithFrenchLanguage.setObservationVariableDbId("var1");


            ObservationVariableV1VO variableWithNoLanguage = Fixtures.createVariable();
            variableWithNoLanguage.setObservationVariableDbId("var3");
            variableWithNoLanguage.setLanguage(null);


            when(mockCropOntologyRepository.getVariableByIds(any())).thenReturn(
                Arrays.asList(variableWithSpanishLanguage, variableWithFrenchLanguage, variableWithNoLanguage)
            );

            ModelAndView modelAndView = mockMvc.perform(get("/studies/{id}", study.getStudyDbId())
                                                            .locale(Locale.CHINA))
                                               .andReturn()
                                               .getModelAndView();
            StudyModel model = (StudyModel) modelAndView.getModel().get("model");
            assertThat(model.getVariables()).hasSize(2).contains(variableWithNoLanguage);
        }
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
