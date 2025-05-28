package fr.inrae.urgi.faidare.dao.v2;

import fr.inrae.urgi.faidare.api.brapi.v2.BrapiListResponse;
import fr.inrae.urgi.faidare.config.ElasticSearchConfig;
import fr.inrae.urgi.faidare.config.FaidareProperties;
import fr.inrae.urgi.faidare.domain.CollPopVO;
import fr.inrae.urgi.faidare.domain.SynonymsVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.GermplasmV2VO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.elasticsearch.DataElasticsearchTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;

import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
@Import({ElasticSearchConfig.class})
@DataElasticsearchTest
class GermplasmV2DaoTest {

    @Autowired
    protected GermplasmV2Dao germplasmDao;

    @Autowired
    private ElasticsearchOperations elasticsearchTemplate;


/* To be implemented in july
    @Test
    void getByGermplasmDbId_Mcpd_flavor_test() {
        GermplasmMcpdVO germplasmVo =
                germplasmDao.getByGermplasmDbId("dXJuOklCRVQvNzc5OGU1N2QtNzZjYS00ZjJiLTliMDctYmI5NTI0ODZlYjFi");

        assertThat(germplasmVo).isNotNull();
        assertThat(germplasmVo).isInstanceOf(GermplasmMcpdVO.class);
        assertThat(germplasmVo.getAccessionNames()).isNull();
        if (germplasmVo.getAccessionNames() != null) {
            assertThat(germplasmVo.getAccessionNames()).isEmpty();
        }

    } */

    @Test
    void getByGermplasmDbId_should_return_one_result() {
        GermplasmV2VO germplasmVo =
                germplasmDao.getByGermplasmDbId("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0NTA1");

        assertThat(germplasmVo).isNotNull();
        assertThat(germplasmVo.getGermplasmDbId())
                .isEqualTo("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0NTA1");
    }

    @Test
    void getByGermplasmPUI_should_return_one_result() {
        GermplasmV2VO germplasmVo =
                germplasmDao.getByGermplasmPUI("https://doi.org/10.15454/WL6NIE");
        assertThat(germplasmVo).isNotNull();
        assertThat(germplasmVo.getGermplasmPUI())
                .isEqualTo("https://doi.org/10.15454/WL6NIE");
        assertThat(germplasmVo.getGermplasmDbId())
                .isEqualTo("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0MDU5");
    }

    @Test
    void custom_should_search_by_accessionNumber(){
        GermplasmV2Criteria gCrit = new GermplasmV2Criteria();
        gCrit.setAccessionNumber(List.of("EM01324"));
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isEqualTo(1);
        assertThat(germplasmVOs.getResult().getData().get(0).getAccessionNumber()).isEqualTo("EM01324");
    }

    @Test
    void custom_should_search_by_binomialNames(){
        GermplasmV2Criteria gCrit = new GermplasmV2Criteria();
        gCrit.setBinomialNames(List.of("Triticum aestivum"));
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
        assertThat(germplasmVOs.getResult().getData().get(0).getGenusSpecies()).isEqualTo("Triticum aestivum");
    }

    @Test
    void custom_should_search_by_collection(){
        GermplasmV2Criteria gCrit = new GermplasmV2Criteria();
        gCrit.setCollections((List.of("Wheat INRA collection")));
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(200);
        Predicate<CollPopVO> streamsPredicate = item -> item.getName().equals("Wheat INRA collection") ;
        assertThat(germplasmVOs.getResult().getData().get(0).getCollection().stream().filter(streamsPredicate)).isNotEmpty();
    }

    @Test
    void custom_should_search_by_panel(){
        GermplasmV2Criteria gCrit = new GermplasmV2Criteria();
        gCrit.setCollections((List.of("RIL")));
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
        Predicate<CollPopVO> streamsPredicate = item -> item.getName().equals("RIL") ;
        assertThat(germplasmVOs.getResult().getData().get(0).getPanel().stream().filter(streamsPredicate)).isNotEmpty();
    }

    @Test
    void custom_should_search_by_pop(){
        GermplasmV2Criteria gCrit = new GermplasmV2Criteria();
        gCrit.setCollections((List.of("ILN028")));
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
        Predicate<CollPopVO> streamsPredicate = item -> item.getName().equals("ILN028") ;
        assertThat(germplasmVOs.getResult().getData().get(0).getPopulation().stream().filter(streamsPredicate)).isNotEmpty();
    }

    @Test
    void custom_should_search_by_commonCropNames(){
        GermplasmV2Criteria gCrit = new GermplasmV2Criteria();
        gCrit.setCommonCropNames(List.of("Wheat"));
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
        GermplasmV2VO toto = germplasmVOs.getResult().getData().get(0);
        assertThat(toto.getCommonCropName()).isEqualTo("Wheat");
    }

    @Test
    void custom_should_search_by_genus(){
        GermplasmV2Criteria gCrit = new GermplasmV2Criteria();
        gCrit.setGenus(List.of("Populus"));
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
        assertThat(germplasmVOs.getResult().getData().get(0).getGenus()).isEqualTo("Populus");
    }

    @Test
    void custom_should_search_by_genus_pageSize1(){
        GermplasmV2Criteria gCrit = new GermplasmV2Criteria();
        gCrit.setGenus(List.of("Triticum"));
        gCrit.setPageSize(1);
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getPageSize()).isEqualTo(1);
        assertThat(germplasmVOs.getMetadata().getPagination().getCurrentPage()).isEqualTo(0);
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isEqualTo(544);
        assertThat(germplasmVOs.getResult().getData().get(0).getGenus()).isEqualTo("Triticum");
    }

    @Test
    void custom_should_search_by_germplasmDbIds(){
        GermplasmV2Criteria gCrit = new GermplasmV2Criteria();
        gCrit.setGermplasmDbId(List.of(
            "dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0MDU5",//recital
            "dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0MzI4",//soisson
            "dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0NTA1"//TREMIE
        ));
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isEqualTo(3);
        // Elasticsearch does not guarantee the order of results, so we verify the IDs regardless of their order.
        List<String> returnedDbIds = germplasmVOs.getResult().getData().stream()
            .map(GermplasmV2VO::getGermplasmDbId).toList();
        assertThat(returnedDbIds).containsExactlyInAnyOrder(
            "dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0MDU5",
            "dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0MzI4",
            "dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0NTA1"
        );
        assertThat(germplasmVOs.getResult().getData().get(0).getGermplasmDbId()).isEqualTo("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0MDU5");
        assertThat(germplasmVOs.getResult().getData().get(1).getGermplasmDbId()).isEqualTo("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0NTA1");
    }

    @Test
    void custom_should_search_by_unique_germplasmDbId(){
        GermplasmV2Criteria gCrit = new GermplasmV2Criteria();
        gCrit.setGermplasmDbId(List.of(
            "dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0MzI4"//soisson
        ));
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isEqualTo(1);
        List<String> returnedDbIds = germplasmVOs.getResult().getData().stream()
            .map(GermplasmV2VO::getGermplasmDbId).toList();
        assertThat(returnedDbIds).containsExactlyInAnyOrder(
            "dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0MzI4"
        );
        assertThat(germplasmVOs.getResult().getData().get(0).getGermplasmDbId()).isEqualTo("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0MzI4");
    }

    @Test
    void custom_should_search_by_germplasmName(){
        GermplasmV2Criteria gCrit = new GermplasmV2Criteria();
        gCrit.setGermplasmName(List.of("APACHE"));
        BrapiListResponse<GermplasmV2VO> pgVo = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(pgVo).isNotNull();
        assertThat(pgVo.getResult().getData()).isNotEmpty();
        assertThat(pgVo.getResult().getData().get(0).getGermplasmName()).isEqualTo("APACHE");
    }

    @Test
    void custom_should_search_by_germplasmPUIs(){
        GermplasmV2Criteria gCrit = new GermplasmV2Criteria();
        gCrit.setGermplasmPUIs(List.of("gnpis_pui:holding-921_taxon-4898_accession-49472"));
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isEqualTo(1);
        assertThat(germplasmVOs.getResult().getData().get(0).getGermplasmPUI()).isEqualTo("gnpis_pui:holding-921_taxon-4898_accession-49472");
    }

    @Test
    void custom_should_search_by_instituteCodes(){
        GermplasmV2Criteria gCrit = new GermplasmV2Criteria();
        gCrit.setInstituteCodes(List.of("FRA095"));
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(10);
        assertThat(germplasmVOs.getResult().getData().get(0).getInstituteCode()).isEqualTo("FRA095");
    }
    @Test
    void custom_should_search_by_species(){
        GermplasmV2Criteria gCrit = new GermplasmV2Criteria();
        gCrit.setSpecies(List.of("aestivum"));
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
        assertThat(germplasmVOs.getResult().getData().get(0).getSpecies()).isEqualTo("aestivum");
    }


    @Test
    void custom_should_search_by_studyDbIds(){
        GermplasmV2Criteria gCrit = new GermplasmV2Criteria();
        gCrit.setStudyDbIds(List.of("dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0xlX01vdWxvbl8yMDAyX1RFQ0g="));
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(10);
        assertThat(germplasmVOs.getResult().getData().get(0).getStudyDbIds()).contains("dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0xlX01vdWxvbl8yMDAyX1RFQ0g=");
    }

    @Test
    void custom_should_search_by_synonyms() {
        GermplasmV2Criteria gCrit = new GermplasmV2Criteria();

        SynonymsVO synonymsVO = new SynonymsVO();
        synonymsVO.setSynonym("DI01016");

        gCrit.setSynonyms(List.of(synonymsVO));
        BrapiListResponse<GermplasmV2VO> germplasmVOs =
            germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount())
            .isGreaterThan(0);
//        List<SynonymsVO> synonyms = germplasmVOs.getResult().getData().get(0).getSynonyms();
//        List<String> expectedSynonyms = List.of("DI01016");
//        System.out.println(synonyms);
//        assertThat(synonyms)
//            .extracting(SynonymsVO::getSynonym)
//            .isEqualTo(expectedSynonyms);
//        assertThat(germplasmVOs.getResult().getData().get(0).getGermplasmDbId())
//            .isEqualTo("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI2ODkx");
    }
    @Test
    void custom_should_search_by_trialDbIds(){
        GermplasmV2Criteria gCrit = new GermplasmV2Criteria();
        gCrit.setTrialDbIds(List.of("dXJuOklOUkFFLVVSR0kvdHJpYWwvMjQ="));
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
        assertThat(germplasmVOs.getResult().getData()).isNotEmpty().extractingResultOf("getAccessionNumber").containsExactlyInAnyOrder("661300238", "661300585", "661300444", "661300252", "661300447", "661300540", "661300328", "661300580", "661300355", "661300534");
    }
    @Autowired
    private FaidareProperties faidareProperties;

    @Test
    public void integrationTestAliasName() {
        String indexName = faidareProperties.getAliasName("germplasm", 0L);
        assertThat(indexName).isEqualTo("faidare_germplasm_dev-group0");
    }

    @Test
    public void testFaidarePropertiesQuerying() {
        // Verify that the document has been correctly indexed in Elasticsearch
        String germplasmId = "dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI3ODA3";
        GermplasmV2VO indexedGermplasm = germplasmDao.getByGermplasmDbId(germplasmId);

        // Ensure the retrieved document is not null and validate its key fields
        assertNotNull(indexedGermplasm, "The document must not be null");
        assertThat(indexedGermplasm.getGermplasmName()).isEqualTo("DI08011");
        assertThat(indexedGermplasm.getGermplasmPUI()).isEqualTo("https://doi.org/10.15454/E8FP9Y");

        // Verify that the index name is correctly generated and exists in Elasticsearch
        String expectedIndexName = faidareProperties.getAliasName("germplasm", 0L);
        IndexCoordinates indexCoordinates = elasticsearchTemplate.getIndexCoordinatesFor(GermplasmV2VO.class);

        // Ensure the index name matches the expected name
        assertThat(indexCoordinates.getIndexName()).isEqualTo(expectedIndexName);

        // Check that the index actually exists in the Elasticsearch cluster
        IndexOperations indexOperations = elasticsearchTemplate.indexOps(indexCoordinates);
        boolean indexExists = indexOperations.exists();
        assertThat(indexExists).isTrue();
    }

}
