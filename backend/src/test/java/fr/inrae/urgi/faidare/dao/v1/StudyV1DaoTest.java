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
                studyV1Dao.getByStudyDbId("dXJuOklOUkFFLVVSR0kvc3R1ZHkvQm9sMTI=");

        assertThat(sVo).isNotNull();
        assertThat(sVo.getStudyDbId())
                .isEqualTo("dXJuOklOUkFFLVVSR0kvc3R1ZHkvQm9sMTI=");
    }

    @Test
    void getByStudyDbId_should_return_lastUpdate() {
        StudyV1VO sVo =
                studyV1Dao.getByStudyDbId("dXJuOklOUkFFLVVSR0kvc3R1ZHkvMw==");

        assertThat(sVo).isNotNull();
        assertThat(sVo.getStudyDbId())
                .isEqualTo("dXJuOklOUkFFLVVSR0kvc3R1ZHkvMw==");
        assertThat(sVo.getLastUpdate().getTimestamp()).isNotNull().containsSubsequence("2016-05-30T20:39:46Z");
    }

    /**
     * Present for historic reasons.
     * replacement code for
     *     public Set<String> getVariableIds(String studyDbId)
     */
    @Test
    void should_get_variables_by_study_id(){
        StudyV1VO sVo =
                studyV1Dao.getByStudyDbId("dXJuOklOUkFFLVVSR0kvc3R1ZHkvTmVyMTE=");

        assertThat(sVo).isNotNull();
        assertThat(sVo.getStudyDbId())
                .isEqualTo("dXJuOklOUkFFLVVSR0kvc3R1ZHkvTmVyMTE=");
        Set<String> obsVarIds = Set.copyOf(sVo.getObservationVariableDbIds());
        assertThat(obsVarIds).isNotNull().isNotEmpty().hasSizeGreaterThan(10);
        assertThat(obsVarIds).contains("EIPO:0000001");
        assertThat(obsVarIds).containsAll(List.of("EIPO:0000001",
            "EIPO:0000002",
            "EIPO:0000003",
            "EIPO:0000005",
            "MIPO:0000006",
            "MIPO:0000007",
            "MIPO:0000012",
            "MIPO:0000014",
            "MIPO:0000017",
            "MIPO:0000018",
            "MIPO:0000024",
            "MIPO:0000025",
            "MIPO:0000026"));
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
