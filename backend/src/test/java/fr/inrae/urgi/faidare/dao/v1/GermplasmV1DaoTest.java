package fr.inrae.urgi.faidare.dao.v1;

import fr.inrae.urgi.faidare.api.brapi.v2.BrapiListResponse;
import fr.inrae.urgi.faidare.config.ElasticSearchConfig;
import fr.inrae.urgi.faidare.domain.CollPopVO;
import fr.inrae.urgi.faidare.domain.PuiNameValueVO;
import fr.inrae.urgi.faidare.domain.brapi.GermplasmSitemapVO;
import fr.inrae.urgi.faidare.domain.brapi.v1.GermplasmV1VO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.elasticsearch.test.autoconfigure.DataElasticsearchTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.core.SearchHitsIterator;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

@DataElasticsearchTest
@Import({ElasticSearchConfig.class})
class GermplasmV1DaoTest {

    @Autowired
    protected GermplasmV1Dao germplasmDao;

    /**
          Code tested For futur implementation in Faidare cards:
              public ModelAndView get(@PathVariable("germplasmId") String germplasmId) {
              GermplasmVO germplasm = germplasmDao.getByGermplasmDbId(germplasmId);

              &#064;GetMapping(params  = "id")
              public ModelAndView getById(@RequestParam("id") String germplasmId) {
              GermplasmVO germplasm = germplasmRepository.getByGermplasmDbId(germplasmId);
     */
    @Test
    void getByGermplasmDbId_should_return_one_result() {
        GermplasmV1VO germplasmVo =
                germplasmDao.getByGermplasmDbId("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0MDU5");

        assertThat(germplasmVo).isNotNull();
        assertThat(germplasmVo.getGermplasmDbId())
                .isEqualTo("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0MDU5");
    }


    /**
     * Code tested For Faidare cards:
     * &#064;GetMapping(params  = "pui")
     *     public ModelAndView getByPui(@RequestParam("pui") String pui) {
     *     GermplasmV1VO germplasmVo =
     *                 germplasmDao.getByGermplasmPUI(pui);
     */
    @Test
    void getByGermplasmPUI_should_return_one_result() {
        GermplasmV1VO germplasmVo =
                germplasmDao.getByGermplasmPUI("https://doi.org/10.15454/4NCDUP");
        assertThat(germplasmVo).isNotNull();
        assertThat(germplasmVo.getGermplasmPUI())
                .isEqualTo("https://doi.org/10.15454/4NCDUP");
        assertThat(germplasmVo.getGermplasmDbId())
                    .isEqualTo("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI2MDAy");
    }

// test :     Iterator<GermplasmVO> scrollGermplasmsByIds(Set<String> ids, int fetchSize);

    /**
     * Should be usable for backend/src/main/java/fr/inra/urgi/faidare/web/germplasm/GermplasmController.java

     &#064;PostMapping("/exports/plant-material")
     &#064;ResponseBody
     public ResponseEntity<StreamingResponseBody> export(@Validated @RequestBody GermplasmExportCommand command) {
     List<GermplasmExportableField> fields = getFieldsToExport(command);

     StreamingResponseBody body = out -> {
     Iterator<GermplasmVO> iterator = germplasmRepository.scrollGermplasmsByIds(command.getIds(), 1000);
     germplasmExportService.export(out, iterator, fields);

     By using germplasmDao.scrollGermplasmsByGermplasmDbIds instead of germplasmRepository.scrollGermplasmsByIds
     */
    @Test
    void should_get_germplasms_by_id1(){
        Set<String> dbIds = Set.of("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0MDU5",//recital
                "dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0MzI4",//soisson
                "dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0NTA1");//TREMIE
        SearchHitsIterator<GermplasmV1VO> gVoIter = germplasmDao.scrollGermplasmsByGermplasmDbIds(dbIds, 10);
        assertThat(gVoIter).isNotNull();
        assertThat(gVoIter.getTotalHits()).isEqualTo(3);
        GermplasmV1VO gVo = Objects.requireNonNull(gVoIter.stream()
                .filter(gVoHit -> "dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0MzI4".equals(gVoHit.getContent().getGermplasmDbId()))
                .findAny().orElse(null)).getContent();
        assertThat(gVo).isNotNull();
        assertThat(gVo.getGermplasmName()).isEqualTo("SOISSONS");
    }

    /**
     * Code to use in controllers for
     * germplasmCriteria.setGermplasmDbIds(Lists.newArrayList(study.getGermplasmDbIds()));
     *             return germplasmRepository.find(germplasmCriteria)
     *
     */
    @Test
    void should_get_germplasms_by_id2(){
        Set<String> dbIds = Set.of("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0MDU5",//recital
            "dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0MzI4",//soisson
            "dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0NTA1",//TREMIE
                "dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI1NTg1",//ISENGRAIN
                "dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI1NjEy",//APACHE
                "dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI1ODk1",//CF00193
                "dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI1OTEz");//CAPHORN
        SearchHitsIterator<GermplasmV1VO> gVoIter = germplasmDao.scrollGermplasmsByGermplasmDbIds(dbIds, 3);
        assertThat(gVoIter).isNotNull();
        assertThat(gVoIter.getTotalHits()).isEqualTo(7);
        Set<String> resultSet = new HashSet<>();
        while (gVoIter.hasNext()){
            GermplasmV1VO v1VO = gVoIter.next().getContent();
            resultSet.add( v1VO.getGermplasmDbId());
        }
        assertThat(resultSet.size()).isEqualTo(7);
        assertThat(resultSet).isEqualTo(dbIds);
    }

    @Test
    void findByGermplasmDbIdIn() {
        String id = "dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0MDU5";
        List<GermplasmV1VO> list = germplasmDao.findByGermplasmDbIdIn(Set.of(id)).toList();
        assertThat(list).extracting(GermplasmV1VO::getGermplasmDbId).containsOnly(id);
    }

    @Test
    void findAllForSitemap() {
        List<GermplasmSitemapVO> list = germplasmDao.findAllForSitemap().toList();
        assertThat(list.size()).isGreaterThan(1);
        assertThat(list.get(0)).isInstanceOf(GermplasmSitemapVO.class);
        assertThat(list.get(0).getGermplasmDbId()).isNotNull();
    }

    @Test
    void should_find_by_germplasmId(){
        GermplasmV1VO gVo = germplasmDao.getByGermplasmDbId("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0MDU5");
        assertThat(gVo).isNotNull();
        assertThat(gVo.getGermplasmDbId()).isEqualTo("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0MDU5");
        assertThat(gVo.getGermplasmPUI()).isEqualTo("https://doi.org/10.15454/WL6NIE");
        assertThat(gVo.getGermplasmName()).isEqualTo("RECITAL");
        assertThat(gVo.getAccessionNames()).contains("RECITAL");
    }

    @Test
    void should_find_by_germplasmId_with_collecting_site2(){
        GermplasmV1VO gVo = germplasmDao.getByGermplasmDbId("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI2ODU5");
        assertThat(gVo).isNotNull();
        assertThat(gVo.getGermplasmDbId()).isEqualTo("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI2ODU5");
        assertThat(gVo.getCollectingSite()).isNotNull();
        assertThat(gVo.getCollectingSite().getSiteId()).isEqualTo("1626");
        assertThat(gVo.getCollectingSite().getSiteName()).isEqualTo("France");
        assertThat(gVo.getCollectingSite().getLatitude()).isEqualTo(47.428085);
        assertThat(gVo.getCollectingSite().getLongitude()).isEqualTo(2.680664);
        assertThat(gVo.getCollectingSite().getSiteType()).isEqualTo("Origin, Breeding and Collecting site");
    }

    @Test
    void should_get_by_germplasm_id_and_have_collector(){
        GermplasmV1VO gVo = germplasmDao.getByGermplasmDbId("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI2ODU3");
        assertThat(gVo).isNotNull();
        assertThat(gVo.getGermplasmDbId()).isEqualTo("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI2ODU3");
        //assertThat(gVo.getCollector()).isNotNull();
        //assertThat(gVo.getCollector().getInstitute().getAcronym()).isEqualTo("INRAE_UMR_GDEC");
        //assertThat(gVo.getCollector().getMaterialType()).isEqualTo("Cutting"); is null in this case
        //assertThat(gVo.getCollector().getAccessionCreationDate()).isEqualTo(20091200); null also
    }

    @Test
    void should_get_by_germplasm_id_and_have_children(){
        GermplasmV1VO gVo = germplasmDao.getByGermplasmDbId("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzQzMTY1");
        assertThat(gVo).isNotNull();
        assertThat(gVo.getGermplasmDbId()).isEqualTo("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzQzMTY1");
        assertThat(gVo.getChildren()).isNotNull();
        assertThat(gVo.getChildren().size()).isEqualTo(11);
        assertThat(gVo.getChildren().get(0).getFirstParentPUI()).isEqualTo("https://doi.org/10.15454/SPA0QI");
        PuiNameValueVO pnv = new PuiNameValueVO();
        pnv.setPui("https://doi.org/10.15454/OMH2PC");
        assertThat(gVo.getChildren().get(0).getSibblings()).isNotNull().isNotEmpty().contains(pnv);
    }
    //TODO: criteria search, to reactivate for full BrAPIV1

    @Test
    void custom_should_search_by_accessionNumber(){
        GermplasmV1Criteria gCrit = new GermplasmV1Criteria();
        gCrit.setAccessionNumber(List.of("CF99005"));
        BrapiListResponse<GermplasmV1VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
        assertThat(germplasmVOs.getResult().getData().get(0).getAccessionNumber()).isEqualTo("CF99005");
    }

    @Test
    void custom_should_search_by_binomialNames(){
        GermplasmV1Criteria gCrit = new GermplasmV1Criteria();
        gCrit.setBinomialNames(List.of("Triticum aestivum"));
        BrapiListResponse<GermplasmV1VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
        assertThat(germplasmVOs.getResult().getData().get(0).getGenusSpecies()).isEqualTo("Triticum aestivum");
    }

    @Test
    void custom_should_search_by_collection(){
        GermplasmV1Criteria gCrit = new GermplasmV1Criteria();
        gCrit.setCollections((List.of("Wheat INRA collection")));
        BrapiListResponse<GermplasmV1VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
        Predicate<CollPopVO> streamsPredicate = item -> item.getName().equals("Wheat INRA collection") ;
        assertThat(germplasmVOs.getResult().getData().get(0).getCollection().stream().filter(streamsPredicate)).isNotEmpty();
    }

    @Test
    void custom_should_search_by_panel(){
        GermplasmV1Criteria gCrit = new GermplasmV1Criteria();
        gCrit.setCollections((List.of("RIL")));
        BrapiListResponse<GermplasmV1VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
        Predicate<CollPopVO> streamsPredicate = item -> item.getName().equals("RIL") ;
        assertThat(germplasmVOs.getResult().getData().get(0).getPanel().stream().filter(streamsPredicate)).isNotEmpty();
    }

    @Test
    void custom_should_search_by_pop(){
        GermplasmV1Criteria gCrit = new GermplasmV1Criteria();
        gCrit.setCollections((List.of("ILN028")));
        BrapiListResponse<GermplasmV1VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
        Predicate<CollPopVO> streamsPredicate = item -> item.getName().equals("ILN028") ;
        assertThat(germplasmVOs.getResult().getData().get(0).getPopulation().stream().filter(streamsPredicate)).isNotEmpty();
    }

    @Test
    void custom_should_search_by_commonCropNames(){
        GermplasmV1Criteria gCrit = new GermplasmV1Criteria();
        gCrit.setCommonCropNames(List.of("Wheat"));
        BrapiListResponse<GermplasmV1VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
        GermplasmV1VO toto = germplasmVOs.getResult().getData().get(0);
        assertThat(toto.getCommonCropName()).isEqualTo("Wheat");
    }

    @Test
    void custom_should_search_by_genus(){
        GermplasmV1Criteria gCrit = new GermplasmV1Criteria();
        gCrit.setGenus(List.of("Triticum"));
        BrapiListResponse<GermplasmV1VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
        assertThat(germplasmVOs.getResult().getData().get(0).getGenus()).isEqualTo("Triticum");
    }

    @Test
    void custom_should_search_by_genus_pageSize1(){
        GermplasmV1Criteria gCrit = new GermplasmV1Criteria();
        gCrit.setGenus(List.of("Triticum"));
        gCrit.setPageSize(1);
        BrapiListResponse<GermplasmV1VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getPageSize()).isEqualTo(1);
        assertThat(germplasmVOs.getMetadata().getPagination().getCurrentPage()).isEqualTo(0);
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isEqualTo(544);
        assertThat(germplasmVOs.getResult().getData().get(0).getGenus()).isEqualTo("Triticum");
    }

    @Test
    void custom_should_search_by_germplasmDbIds(){
        GermplasmV1Criteria gCrit = new GermplasmV1Criteria();
        gCrit.setGermplasmDbIds(List.of("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI3ODA3", "dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI2OTc3"));
        BrapiListResponse<GermplasmV1VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isEqualTo(2);
        assertThat(germplasmVOs.getResult().getData().get(0).getGermplasmDbId()).isEqualTo("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI2OTc3");
        assertThat(germplasmVOs.getResult().getData().get(1).getGermplasmDbId()).isEqualTo("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI3ODA3");
    }

    @Test
    void custom_should_search_by_germplasmName(){
        GermplasmV1Criteria gCrit = new GermplasmV1Criteria();
        gCrit.setGermplasmName(List.of("APACHE"));
        BrapiListResponse<GermplasmV1VO> pgVo = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(pgVo).isNotNull();
        assertThat(pgVo.getResult().getData()).isNotEmpty();
        assertThat(pgVo.getResult().getData().get(0).getGermplasmName()).isEqualTo("APACHE");
    }

    @Test
    void custom_should_search_by_germplasmPUIs(){
        GermplasmV1Criteria gCrit = new GermplasmV1Criteria();
        gCrit.setGermplasmPUIs(List.of("https://doi.org/10.15454/BHQTEW"));
        BrapiListResponse<GermplasmV1VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
        assertThat(germplasmVOs.getResult().getData().get(0).getGermplasmPUI()).isEqualTo("https://doi.org/10.15454/BHQTEW");
    }

    @Test
    void custom_should_search_by_instituteCodes(){
        GermplasmV1Criteria gCrit = new GermplasmV1Criteria();
        gCrit.setInstituteCodes(List.of("FRA040"));
        BrapiListResponse<GermplasmV1VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
        assertThat(germplasmVOs.getResult().getData().get(0).getInstituteCode()).isEqualTo("FRA040");
    }

    @Test
    void custom_should_search_by_species(){
        GermplasmV1Criteria gCrit = new GermplasmV1Criteria();
        gCrit.setSpecies(List.of("aestivum"));
        BrapiListResponse<GermplasmV1VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
        assertThat(germplasmVOs.getResult().getData().get(0).getSpecies()).isEqualTo("aestivum");
    }


    @Test
    void custom_should_search_by_studyDbIds(){
        GermplasmV1Criteria gCrit = new GermplasmV1Criteria();
        gCrit.setStudyDbIds(List.of("dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0NoYXV4X2Rlc19QciVDMyVBOXNfMjAwMF9TZXRCMQ=="));
        BrapiListResponse<GermplasmV1VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
        assertThat(germplasmVOs.getResult().getData().get(0).getStudyDbIds()).contains("dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0NoYXV4X2Rlc19QciVDMyVBOXNfMjAwMF9TZXRCMQ==");
    }

    @Test
    void custom_should_search_by_synonyms(){
        GermplasmV1Criteria gCrit = new GermplasmV1Criteria();
        gCrit.setSynonyms(List.of("WW-152"));
        BrapiListResponse<GermplasmV1VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
        assertThat(germplasmVOs.getResult().getData().get(0).getSynonyms()).contains("WW-152");
    }

    void custom_should_search_by_trialDbIds(){
        GermplasmV1Criteria gCrit = new GermplasmV1Criteria();
        gCrit.setTrialDbIds(List.of(""));
        BrapiListResponse<GermplasmV1VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
//        assertThat(germplasmVOs.getSearchHits().getSearchHit(0).getContent().get()).isEqualTo("");
    }

}
