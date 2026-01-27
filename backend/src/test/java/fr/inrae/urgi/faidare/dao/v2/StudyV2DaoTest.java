package fr.inrae.urgi.faidare.dao.v2;

import fr.inrae.urgi.faidare.api.brapi.v2.BrapiListResponse;
import fr.inrae.urgi.faidare.config.ElasticSearchConfig;
import fr.inrae.urgi.faidare.domain.brapi.GermplasmSitemapVO;
import fr.inrae.urgi.faidare.domain.brapi.StudySitemapVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.StudyV2VO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.elasticsearch.test.autoconfigure.DataElasticsearchTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Import({ElasticSearchConfig.class})
@DataElasticsearchTest
class StudyV2DaoTest {

    /**
     * StudyV1VO and StudyV2VO are compatible enough.
     * Therefore, there is no studyV1DAO, only a StudyV2DAO that serves both
     */

    @Autowired
    protected StudyV2Dao studyV2Dao;

    @Test
    void getByStudyDbId_should_return_empty_result() {
        StudyV2VO studyVO =
                studyV2Dao.getByStudyDbId("foo");
        assertThat(studyVO).isNull();
    }

    @Test
    void getByStudyDbID_studyDbId(){
        StudyV2VO studyVO = studyV2Dao.getByStudyDbId("dXJuOklOUkFFLVVSR0kvc3R1ZHkvQ2FtMTE=");
        assertThat(studyVO).isNotNull();
        assertThat(studyVO.getStudyDbId()).isEqualTo("dXJuOklOUkFFLVVSR0kvc3R1ZHkvQ2FtMTE=");
    }
//    It is commented because in the actual data test we have just null commonCropNames
//    @Test
//    void custom_should_search_by_commonCropNames(){
//        StudyCriteria sCrit = new StudyCriteria();
//        sCrit.setCommonCropNames(List.of("Wheat"));
//        SearchHits<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
//        assertThat(studyVOs).isNotNull().isNotEmpty();
//        assertThat(studyVOs.getSearchHit(0).getContent().getCommonCropName()).isEqualTo("Wheat");
//    }

    @Test
    void custom_should_search_by_germplasmDbIds(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setGermplasmDbId((List.of("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0MzI4")));
        BrapiListResponse<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull();
        assertThat(studyVOs.getResult().getData().get(0).getGermplasmDbIds()).contains("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0MzI4");
    }

    @Test
    void custom_should_search_by_locationDbIds(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setLocationDbId(List.of("dXJuOklOUkFFLVVSR0kvbG9jYXRpb24vNDA4MTk="));
        BrapiListResponse<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull();
        assertThat(studyVOs.getResult().getData().get(0).getLocationDbId()).isEqualTo("dXJuOklOUkFFLVVSR0kvbG9jYXRpb24vNDA4MTk=");
    }

    @Test
    void custom_should_search_by_locationNames(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setLocationName(List.of("Gaillac"));
        BrapiListResponse<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull();
        assertThat(studyVOs.getResult().getData().get(0).getLocationName()).isEqualTo("Gaillac");
    }

    @Test
    void custom_should_search_by_observationVariableDbIds(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setObservationVariableDbId(List.of("EIPO:0000002"));
        BrapiListResponse<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull();
        assertThat(studyVOs.getResult().getData().get(0).getObservationVariableDbIds()).contains("EIPO:0000002");
    }

    @Test
    void custom_should_search_by_programDbIds(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setProgramDbId(List.of("dXJuOklOUkFFLVVSR0kvcHJvZ3JhbS9GbGVzaGxlc3M="));
        BrapiListResponse<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull();
        assertThat(studyVOs.getResult().getData().get(0).getProgramDbId()).isEqualTo("dXJuOklOUkFFLVVSR0kvcHJvZ3JhbS9GbGVzaGxlc3M=");
    }

    @Test
    void custom_should_search_by_programNames(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setProgramName(List.of("IN_VITRO_PETRAEA_SNP"));
        BrapiListResponse<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull();
        assertThat(studyVOs.getResult().getData().get(0).getProgramName()).isEqualTo("IN_VITRO_PETRAEA_SNP");
    }

    @Test
    void custom_should_search_by_studyDbIds(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setStudyDbId(List.of("dXJuOklOUkFFLVVSR0kvc3R1ZHkvQ2FtMTM="));
        BrapiListResponse<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull();
        assertThat(studyVOs.getResult().getData().get(0).getStudyDbId()).isEqualTo("dXJuOklOUkFFLVVSR0kvc3R1ZHkvQ2FtMTM=");
    }

    @Test
    void custom_should_search_by_studyName(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setStudyName(List.of("SNPGrapeMap SNP calling analysis"));
        BrapiListResponse<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull();
        assertThat(studyVOs.getResult().getData().get(0).getStudyName()).isEqualTo("SNPGrapeMap SNP calling analysis");
    }

    @Test
    void custom_should_search_by_studyTypes(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setStudyType(List.of("Phenotyping Study"));
        BrapiListResponse<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull();
        assertThat(studyVOs.getResult().getData().get(0).getStudyType()).isEqualTo("Phenotyping Study");
    }

    @Test
    void custom_should_search_by_trialDbIds(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setTrialDbIds(List.of("dXJuOklOUkFFLVVSR0kvdHJpYWwvNDI="));
        BrapiListResponse<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull();
        assertThat(studyVOs.getResult().getData().get(0).getTrialDbId()).contains("dXJuOklOUkFFLVVSR0kvdHJpYWwvNDI=");
    }

    @Test
    void custom_should_search_by_trialNames(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setTrialNames(List.of("Drops Phenotyping Network"));
        BrapiListResponse<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull();
        assertThat(studyVOs.getResult().getData().get(0).getTrialName()).isEqualTo("Drops Phenotyping Network");
    }

    @Test
    void findAllForSitemap() {
        try (Stream<StudySitemapVO> stream = studyV2Dao.findAllForSitemap().limit(10)) {
            List<StudySitemapVO> list = stream.toList();
            assertThat(list).isNotEmpty();
            for (StudySitemapVO vo : list) {
                assertThat(vo).isInstanceOf(StudySitemapVO.class);
                assertThat(vo.getStudyDbId()).isNotBlank();
            }
        }
    }
}
