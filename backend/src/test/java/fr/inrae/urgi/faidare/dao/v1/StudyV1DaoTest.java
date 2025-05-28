package fr.inrae.urgi.faidare.dao.v1;

import fr.inrae.urgi.faidare.config.ElasticSearchConfig;
import fr.inrae.urgi.faidare.domain.brapi.StudySitemapVO;
import fr.inrae.urgi.faidare.domain.brapi.v1.StudyV1VO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.elasticsearch.DataElasticsearchTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataElasticsearchTest
@Import({ElasticSearchConfig.class})
class StudyV1DaoTest {

    @Autowired
    protected StudyV1Dao studyV1Dao;

    /**
     *     Code tested For futur implementation in Faidare cards:
     *         public ModelAndView get(@PathVariable("germplasmId") String germplasmId) {
     *         GermplasmVO germplasm = germplasmDao.getByGermplasmDbId(germplasmId);
     *
     *         @GetMapping(params = "id")
     *         public ModelAndView getById(@RequestParam("id") String germplasmId) {
     *         GermplasmVO germplasm = germplasmRepository.getByGermplasmDbId(germplasmId);
     */
    @Test
    void getByStudyDbId_should_return_one_result() {
        StudyV1VO sVo =
                studyV1Dao.getByStudyDbId("dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0xlX01vdWxvbl8yMDA0X1RFQ0g=");

        assertThat(sVo).isNotNull();
        assertThat(sVo.getStudyDbId())
                .isEqualTo("dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0xlX01vdWxvbl8yMDA0X1RFQ0g=");
    }

    @Test
    void getByStudyDbId_should_return_lastUpdate() {
        StudyV1VO sVo =
                studyV1Dao.getByStudyDbId("dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0VzdHIlQzMlQTllcy1Nb25zXzIwMDRfVEVDSA==");

        assertThat(sVo).isNotNull();
        assertThat(sVo.getStudyDbId())
                .isEqualTo("dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0VzdHIlQzMlQTllcy1Nb25zXzIwMDRfVEVDSA==");
        assertThat(sVo.getLastUpdate().getTimestamp()).isNotNull().containsSubsequence("2017-02-21");
    }

    /**
     * Present for historic reasons.
     * replacement code for
     *     public Set<String> getVariableIds(String studyDbId)
     */
    @Test
    void should_get_variables_by_study_id(){
        StudyV1VO sVo =
                studyV1Dao.getByStudyDbId("dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0VzdHIlQzMlQTllcy1Nb25zXzIwMTBfVEVDSA==");

        assertThat(sVo).isNotNull();
        assertThat(sVo.getStudyDbId())
                .isEqualTo("dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0VzdHIlQzMlQTllcy1Nb25zXzIwMTBfVEVDSA==");
        Set<String> obsVarIds = Set.copyOf(sVo.getObservationVariableDbIds());
        assertThat(obsVarIds).isNotNull().isNotEmpty().hasSizeGreaterThan(10);
        assertThat(obsVarIds).contains("CO_321:1000216");
        assertThat(obsVarIds).containsAll(List.of("CO_321:1000227",
            "CO_321:1000228",
            "CO_321:1000229",
            "CO_321:1000230",
            "CO_321:1000231",
            "CO_321:1000236",
            "CO_321:1000237",
            "CO_321:1000238",
            "CO_321:1000239",
            "CO_321:1000240"));
        assertThat(obsVarIds).doesNotContain("foo");
    }

    @Test
    void findAllForSitemap() {
        List<StudySitemapVO> list = studyV1Dao.findAllForSitemap().toList();
        assertThat(list.size()).isGreaterThan(1);
        assertThat(list.get(0)).isInstanceOf(StudySitemapVO.class);
        assertThat(list.get(0).getStudyDbId()).isNotNull();
    }
}
