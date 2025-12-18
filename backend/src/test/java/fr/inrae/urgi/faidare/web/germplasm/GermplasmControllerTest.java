package fr.inrae.urgi.faidare.web.germplasm;

import static fr.inrae.urgi.faidare.web.Fixtures.htmlContent;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Sets;
import fr.inrae.urgi.faidare.config.DataSource;
import fr.inrae.urgi.faidare.config.FaidareProperties;
import fr.inrae.urgi.faidare.dao.XRefDocumentDao;
import fr.inrae.urgi.faidare.dao.v1.GermplasmAttributeV1Dao;
import fr.inrae.urgi.faidare.dao.v1.GermplasmPedigreeV1Dao;
import fr.inrae.urgi.faidare.dao.v2.GermplasmMcpdDao;
import fr.inrae.urgi.faidare.dao.v2.GermplasmV2Dao;
import fr.inrae.urgi.faidare.domain.GermplasmMcpdVO;
import fr.inrae.urgi.faidare.domain.XRefDocumentVO;
import fr.inrae.urgi.faidare.domain.brapi.GermplasmSitemapVO;
import fr.inrae.urgi.faidare.domain.brapi.v1.GermplasmAttributeV1VO;
import fr.inrae.urgi.faidare.domain.brapi.v2.GermplasmV2VO;
import fr.inrae.urgi.faidare.web.Fixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.ObjectMapper;

/**
 * MVC tests for {@link GermplasmController}
 * @author JB Nizet
 */
@WebMvcTest(GermplasmController.class)
@Import({GermplasmMcpdExportService.class, GermplasmExportService.class})
public class GermplasmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GermplasmV2Dao mockGermplasmRepository;

    @MockitoBean
    private FaidareProperties mockFaidareProperties;

    @MockitoBean
    private XRefDocumentDao mockXRefDocumentRepository;

    @MockitoBean
    private GermplasmAttributeV1Dao mockGermplasmAttributeRepository;

    @MockitoBean
    private GermplasmMcpdDao mockGermplasmMcpdRepository;

    @MockitoBean
    private GermplasmPedigreeV1Dao mockGermplasmPedigreeV1Dao;

    private GermplasmV2VO germplasm;
    private List<XRefDocumentVO> crossReferences;
    private DataSource dataSource;

    @BeforeEach
    void prepare() {
        germplasm = Fixtures.createGermplasm();
        when(mockGermplasmRepository.getByGermplasmDbId(germplasm.getGermplasmDbId())).thenReturn(germplasm);

        crossReferences = Arrays.asList(
            Fixtures.createXref("foobar"),
            Fixtures.createXref("bazbing")
        );
        when(mockXRefDocumentRepository.findByLinkedResourcesID(any()))
            .thenReturn(crossReferences);

        dataSource = Fixtures.createDataSource();

        when(mockFaidareProperties.getByUri(germplasm.getSourceUri())).thenReturn(dataSource);
        when(mockFaidareProperties.getByUri(any())).thenReturn(dataSource);

        GermplasmAttributeV1VO attribute = Fixtures.createGermplasmAttribute();
        when(mockGermplasmAttributeRepository.getByGermplasmDbId(germplasm.getGermplasmDbId())).thenReturn(attribute);
    }

    @Test
    void shouldDisplayGermplasm() throws Exception {
        mockMvc.perform(get("/germplasms/{id}", germplasm.getGermplasmDbId()))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
               .andExpect(htmlContent().hasTitle("Germplasm: BLE BARBU DU ROUSSILLON"))
               .andExpect(htmlContent().containsH2s("Identification",
                                                    "Depositary",
                                                    "Collector",
                                                    "Breeder",
                                                    "Donors",
                                                    "Distributors",
                                                    "Evaluation Data",
                                                    "Genealogy",
                                                    "Population",
                                                    "Collection",
                                                    "Panel",
                                                    "Cross references"))
               .andExpect(htmlContent().endsCorrectly());
    }

    @Test
    void shouldDisplayGermplasmWithNullCollector() throws Exception {
        germplasm.setCollector(null);

        mockMvc.perform(get("/germplasms/{id}", germplasm.getGermplasmDbId()))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
               .andExpect(htmlContent().hasTitle("Germplasm: BLE BARBU DU ROUSSILLON"))
               .andExpect(htmlContent().containsH2s("Identification",
                                                    "Depositary",
                                                    "Collector",
                                                    "Breeder",
                                                    "Donors",
                                                    "Distributors",
                                                    "Evaluation Data",
                                                    "Genealogy",
                                                    "Population",
                                                    "Collection",
                                                    "Panel",
                                                    "Cross references"))
               .andExpect(htmlContent().endsCorrectly());
    }

    @Test
    void shouldDisplayGermplasmWithIdAsParameter() throws Exception {
        mockMvc.perform(get("/germplasms").param("id", germplasm.getGermplasmDbId()))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
               .andExpect(htmlContent().hasTitle("Germplasm: BLE BARBU DU ROUSSILLON"))
               .andExpect(htmlContent().containsH2s("Identification",
                                                    "Depositary",
                                                    "Collector",
                                                    "Breeder",
                                                    "Donors",
                                                    "Distributors",
                                                    "Evaluation Data",
                                                    "Genealogy",
                                                    "Population",
                                                    "Collection",
                                                    "Panel",
                                                    "Cross references"))
               .andExpect(htmlContent().endsCorrectly());
    }

    @Test
    void shouldDisplayGermplasmWithPuiAsParameter() throws Exception {
        when(mockGermplasmRepository.getByGermplasmPUI(germplasm.getGermplasmPUI())).thenReturn(germplasm);

        mockMvc.perform(get("/germplasms").param("pui", germplasm.getGermplasmPUI()))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
               .andExpect(htmlContent().hasTitle("Germplasm: BLE BARBU DU ROUSSILLON"))
               .andExpect(htmlContent().containsH2s("Identification",
                                                    "Depositary",
                                                    "Collector",
                                                    "Breeder",
                                                    "Donors",
                                                    "Distributors",
                                                    "Evaluation Data",
                                                    "Genealogy",
                                                    "Population",
                                                    "Collection",
                                                    "Panel",
                                                    "Cross references"))
               .andExpect(htmlContent().endsCorrectly());
    }

    @Test
    void shouldSupportLegacyPath() throws Exception {
        when(mockGermplasmRepository.getByGermplasmPUI(germplasm.getGermplasmPUI())).thenReturn(germplasm);

        mockMvc.perform(get("/germplasm").param("pui", germplasm.getGermplasmPUI()))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
               .andExpect(htmlContent().hasTitle("Germplasm: BLE BARBU DU ROUSSILLON"))
               .andExpect(htmlContent().endsCorrectly());
    }

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

        when(mockGermplasmRepository.findAllForSitemap()).thenAnswer(invocation -> germplasms.stream());

        testSitemap(6, "http://localhost/faidare/germplasms/germplasm1\nhttp://localhost/faidare/germplasms/germplasm45\n");
        testSitemap(9, "http://localhost/faidare/germplasms/germplasm4\nhttp://localhost/faidare/germplasms/germplasm73\n");
        testSitemap(7, "");

        mockMvc.perform(get("/faidare/germplasms/sitemap-17.txt")
                            .contextPath("/faidare"))
               .andExpect(status().isNotFound());
    }

    @Test
    void shouldExportMcpds() throws Exception {
        List<GermplasmMcpdVO> germplasms = List.of(
            Fixtures.createGermplasmMcpd(),
            Fixtures.createGermplasmMcpd()
        );

        GermplasmMcpdExportCommand command = new GermplasmMcpdExportCommand(
            Sets.newHashSet("g1", "g2"),
            Arrays.asList(GermplasmMcpdExportableField.PUID, GermplasmMcpdExportableField.INSTCODE));

        when(mockGermplasmMcpdRepository.findByGermplasmDbIdIn(eq(command.getIds())))
            .thenAnswer(invocation -> germplasms.stream());

        MvcResult mvcResult = mockMvc.perform(post("/germplasms/exports/mcpd")
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  .content(objectMapper.writeValueAsBytes(command)))
                                     .andExpect(request().asyncStarted())
                                     .andReturn();

        this.mockMvc.perform(asyncDispatch(mvcResult))
               .andExpect(status().isOk())
               .andExpect(content().contentType("text/csv"))
               .andExpect(content().string("\"PUID\";\"INSTCODE\"\n" +
                                               "\"PUI1\";\"Inst1\"\n" +
                                               "\"PUI1\";\"Inst1\"\n"));
    }

    @Test
    void shouldExportPlantMaterials() throws Exception {
        List<GermplasmV2VO> germplasms = Arrays.asList(
            Fixtures.createGermplasm(),
            Fixtures.createGermplasm(),
            Fixtures.createGermplasmMinimal()
        );

        GermplasmExportCommand command = new GermplasmExportCommand(
            Sets.newHashSet("g1", "g2"),
            Arrays.asList(GermplasmExportableField.DOI,
                          GermplasmExportableField.ACCESSION_NUMBER,
                          GermplasmExportableField.ACCESSION_NAME));

        when(mockGermplasmRepository.findByGermplasmDbIdIn(eq(command.getIds())))
            .thenAnswer(invocation -> germplasms.stream());

        MvcResult mvcResult = mockMvc.perform(post("/germplasms/exports/plant-material")
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  .content(objectMapper.writeValueAsBytes(
                                                      command)))
                                     .andExpect(request().asyncStarted())
                                     .andReturn();

        this.mockMvc.perform(asyncDispatch(mvcResult))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/csv"))
                    .andExpect(content().string("\"DOI\";\"Accession number\";\"Accession name\"\n" +
                                                    "\"germplasmPUI\";\"1408\";\"BLE BARBU DU ROUSSILLON\"\n" +
                                                    "\"germplasmPUI\";\"1408\";\"BLE BARBU DU ROUSSILLON\"\n" +
                                                    "\"germplasmPUI mini\";\"1408-mini\";\"BLE BARBU DU ROUSSILLON mini\"\n"));
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
