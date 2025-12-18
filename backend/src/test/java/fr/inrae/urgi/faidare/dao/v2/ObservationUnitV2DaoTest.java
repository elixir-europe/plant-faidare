package fr.inrae.urgi.faidare.dao.v2;

import fr.inrae.urgi.faidare.api.brapi.v2.BrapiListResponse;
import fr.inrae.urgi.faidare.config.ElasticSearchConfig;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.ObservationUnitV2VO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.elasticsearch.test.autoconfigure.DataElasticsearchTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import({ElasticSearchConfig.class})
@DataElasticsearchTest
public class ObservationUnitV2DaoTest {
    @Autowired
    protected ObservationUnitV2Dao observationUnitDao;

    @Autowired
    private ElasticsearchOperations elasticsearchTemplate;

    @Test
    void getByObservationUnitDbId_should_return_one_result() {
        ObservationUnitV2VO observationUnitvo =
            observationUnitDao.getByObservationUnitDbId("dXJuOklOUkFFLVVSR0kvb2JzZXJ2YXRpb25Vbml0LzMzMzUwNDQtMjAxMw==");

        assertThat(observationUnitvo).isNotNull();
        assertThat(observationUnitvo.getObservationUnitDbId())
            .isEqualTo("dXJuOklOUkFFLVVSR0kvb2JzZXJ2YXRpb25Vbml0LzMzMzUwNDQtMjAxMw==");
    }
    @Test
    void custom_should_search_by_observationUnitName(){
        ObservationUnitV2Criteria obsUnitCrit = new ObservationUnitV2Criteria();
        obsUnitCrit.setObservationUnitName(List.of("3171water_regimewatered"));
        BrapiListResponse<ObservationUnitV2VO> observationUnitVOs = observationUnitDao.findObservationUnitByCriteria(obsUnitCrit);
        assertThat(observationUnitVOs).isNotNull();
        //assertThat(observationUnitVOs.getMetadata().getPagination().getTotalCount()).isEqualTo(1);
        assertThat(observationUnitVOs.getResult().getData().get(0).getObservationUnitName()).isEqualTo("3171water_regimewatered");
    }
    @Test
    void custom_should_search_by_germplasmDbId(){
        ObservationUnitV2Criteria obsUnitCrit = new ObservationUnitV2Criteria();
        obsUnitCrit.setGermplasmDbId(List.of("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzEyNzQxMw=="));
        BrapiListResponse<ObservationUnitV2VO> observationUnitVOs = observationUnitDao.findObservationUnitByCriteria(obsUnitCrit);
        assertThat(observationUnitVOs).isNotNull();
        assertThat(observationUnitVOs.getMetadata().getPagination().getTotalCount()).isEqualTo(129);
        assertThat(observationUnitVOs.getResult().getData().get(0).getGermplasmDbId()).isEqualTo("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzEyNzQxMw==");
    }
    @Test
    void custom_should_search_by_germplasmName(){
        ObservationUnitV2Criteria obsUnitCrit = new ObservationUnitV2Criteria();
        obsUnitCrit.setGermplasmName(List.of("F05101_H"));
        BrapiListResponse<ObservationUnitV2VO> observationUnitVOs = observationUnitDao.findObservationUnitByCriteria(obsUnitCrit);
        assertThat(observationUnitVOs).isNotNull();
        assertThat(observationUnitVOs.getMetadata().getPagination().getTotalCount()).isEqualTo(102);
        assertThat(observationUnitVOs.getResult().getData().get(0).getGermplasmName()).isEqualTo("F05101_H");
    }
    // program always null
//    @Test
//    void custom_should_search_by_programDbId() {
//        ObservationUnitV2Criteria obsUnitCrit = new ObservationUnitV2Criteria();
//        obsUnitCrit.setProgramDbIds(List.of("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzEyNzQxMw=="));
//        BrapiListResponse<ObservationUnitV2VO> observationUnitVOs = observationUnitDao.findObservationUnitByCriteria(obsUnitCrit);
//        assertThat(observationUnitVOs).isNotNull();
//        assertThat(observationUnitVOs.getMetadata().getPagination().getTotalCount()).isEqualTo(129);
//        assertThat(observationUnitVOs.getResult().getData().get(0).getProgramDbId()).isEqualTo("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzEyNzQxMw==");
//    }
//
//    @Test
//    void custom_should_search_by_programName() {
//        ObservationUnitV2Criteria obsUnitCrit = new ObservationUnitV2Criteria();
//        obsUnitCrit.setProgramNames(List.of("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzEyNzQxMw=="));
//        BrapiListResponse<ObservationUnitV2VO> observationUnitVOs = observationUnitDao.findObservationUnitByCriteria(obsUnitCrit);
//        assertThat(observationUnitVOs).isNotNull();
//        assertThat(observationUnitVOs.getMetadata().getPagination().getTotalCount()).isEqualTo(129);
//        assertThat(observationUnitVOs.getResult().getData().get(0).getProgramName()).isEqualTo("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzEyNzQxMw==");
//    }
    @Test
    void custom_should_search_by_studyDbId(){
        ObservationUnitV2Criteria obsUnitCrit = new ObservationUnitV2Criteria();
        obsUnitCrit.setStudyDbId(List.of("dXJuOklOUkFFLVVSR0kvc3R1ZHkvQ2FtMTM="));
        BrapiListResponse<ObservationUnitV2VO> observationUnitVOs = observationUnitDao.findObservationUnitByCriteria(obsUnitCrit);
        assertThat(observationUnitVOs).isNotNull();
        assertThat(observationUnitVOs.getMetadata().getPagination().getTotalCount()).isEqualTo(1802);
        assertThat(observationUnitVOs.getResult().getData().get(0).getStudyDbId()).isEqualTo("dXJuOklOUkFFLVVSR0kvc3R1ZHkvQ2FtMTM=");
    }
    @Test
    void custom_should_search_by_studyName() {
        ObservationUnitV2Criteria obsUnitCrit = new ObservationUnitV2Criteria();
        obsUnitCrit.setStudyName(List.of("University_of_Bologna Cadriano 2012"));
        BrapiListResponse<ObservationUnitV2VO> observationUnitVOs = observationUnitDao.findObservationUnitByCriteria(obsUnitCrit);
        assertThat(observationUnitVOs).isNotNull();
        assertThat(observationUnitVOs.getMetadata().getPagination().getTotalCount()).isEqualTo(1737);
        assertThat(observationUnitVOs.getResult().getData().get(0).getStudyName()).isEqualTo("University_of_Bologna Cadriano 2012");
    }

    @Test
    void custom_should_search_by_studyLocationDbId() {
        ObservationUnitV2Criteria obsUnitCrit = new ObservationUnitV2Criteria();
        obsUnitCrit.setLocationDbId(List.of("dXJuOklOUkFFLVVSR0kvbG9jYXRpb24vNDA4MTU="));
        BrapiListResponse<ObservationUnitV2VO> observationUnitVOs = observationUnitDao.findObservationUnitByCriteria(obsUnitCrit);
        assertThat(observationUnitVOs).isNotNull();
        assertThat(observationUnitVOs.getMetadata().getPagination().getTotalCount()).isEqualTo(3843);
        assertThat(observationUnitVOs.getResult().getData().get(0).getStudyLocationDbId()).isEqualTo("dXJuOklOUkFFLVVSR0kvbG9jYXRpb24vNDA4MTU=");
    }
    @Test
    void custom_should_search_by_studyLocation() {
        ObservationUnitV2Criteria obsUnitCrit = new ObservationUnitV2Criteria();
        obsUnitCrit.setLocationName(List.of("Campagnola"));
        BrapiListResponse<ObservationUnitV2VO> observationUnitVOs = observationUnitDao.findObservationUnitByCriteria(obsUnitCrit);
        assertThat(observationUnitVOs).isNotNull();
        assertThat(observationUnitVOs.getMetadata().getPagination().getTotalCount()).isEqualTo(3843);
        assertThat(observationUnitVOs.getResult().getData().get(0).getStudyLocation()).isEqualTo("Campagnola");
    }
    @Test
    void custom_should_search_by_TrialDbId() {
        ObservationUnitV2Criteria obsUnitCrit = new ObservationUnitV2Criteria();
        obsUnitCrit.setTrialDbId(List.of("dXJuOklOUkFFLVVSR0kvdHJpYWwvNDI="));
        BrapiListResponse<ObservationUnitV2VO> observationUnitVOs = observationUnitDao.findObservationUnitByCriteria(obsUnitCrit);
        assertThat(observationUnitVOs).isNotNull();
        assertThat(observationUnitVOs.getMetadata().getPagination().getTotalCount()).isEqualTo(27470);
        assertThat(observationUnitVOs.getResult().getData().get(0).getTrialDbId()).isEqualTo("dXJuOklOUkFFLVVSR0kvdHJpYWwvNDI=");
    }
    @Test
    void custom_should_search_by_TrialName() {
        ObservationUnitV2Criteria obsUnitCrit = new ObservationUnitV2Criteria();
        obsUnitCrit.setTrialName(List.of("Drops Phenotyping Network"));
        BrapiListResponse<ObservationUnitV2VO> observationUnitVOs = observationUnitDao.findObservationUnitByCriteria(obsUnitCrit);
        assertThat(observationUnitVOs).isNotNull();
        assertThat(observationUnitVOs.getMetadata().getPagination().getTotalCount()).isEqualTo(27470);
        assertThat(observationUnitVOs.getResult().getData().get(0).getTrialName()).isEqualTo("Drops Phenotyping Network");
    }
    // germplasmGenus is null
//    @Test
//    void custom_should_search_by_germplasmGenus() {
//        ObservationUnitV2Criteria obsUnitCrit = new ObservationUnitV2Criteria();
//        obsUnitCrit.setGermplasmGenus(List.of(""));
//        BrapiListResponse<ObservationUnitV2VO> observationUnitVOs = observationUnitDao.findObservationUnitByCriteria(obsUnitCrit);
//        assertThat(observationUnitVOs).isNotNull();
//        assertThat(observationUnitVOs.getMetadata().getPagination().getTotalCount()).isEqualTo(3843);
//        assertThat(observationUnitVOs.getResult().getData().get(0).getGermplasmGenus()).isEqualTo("");
//    }
//    @Test
//    void custom_should_search_by_germplasmCollections() {
//        ObservationUnitV2Criteria obsUnitCrit = new ObservationUnitV2Criteria();
//        obsUnitCrit.setGermplasmCollections(List.of(""));
//        BrapiListResponse<ObservationUnitV2VO> observationUnitVOs = observationUnitDao.findObservationUnitByCriteria(obsUnitCrit);
//        assertThat(observationUnitVOs).isNotNull();
//        assertThat(observationUnitVOs.getMetadata().getPagination().getTotalCount()).isEqualTo(3843);
//        assertThat(observationUnitVOs.getResult().getData().get(0).getGermplasmCollections()).isEqualTo("");
//    }
}
