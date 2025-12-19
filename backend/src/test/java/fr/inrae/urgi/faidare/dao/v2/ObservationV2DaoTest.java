package fr.inrae.urgi.faidare.dao.v2;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import fr.inrae.urgi.faidare.api.brapi.v2.BrapiListResponse;
import fr.inrae.urgi.faidare.config.ElasticSearchConfig;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.ObservationVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.elasticsearch.test.autoconfigure.DataElasticsearchTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

@Import({ElasticSearchConfig.class})
@DataElasticsearchTest
public class ObservationV2DaoTest {
    @Autowired
    protected ObservationV2Dao observationDao;

    @Autowired
    private ElasticsearchOperations elasticsearchTemplate;

    @Test
    void getByObservationDbId_should_return_one_result() {
        ObservationVO observationvo =
            observationDao.getByObservationDbId("dXJuOklOUkFFLVVSR0kvb2JzZXJ2YXRpb24vMzQzMzU1Nw==");

        assertThat(observationvo).isNotNull();
        assertThat(observationvo.getObservationDbId())
            .isEqualTo("dXJuOklOUkFFLVVSR0kvb2JzZXJ2YXRpb24vMzQzMzU1Nw==");
    }

    @Test
    void custom_should_search_by_observationUnitDbId(){
        ObservationV2Criteria obsCrit = new ObservationV2Criteria();
        obsCrit.setObservationUnitDbId(List.of("dXJuOklOUkFFLVVSR0kvb2JzZXJ2YXRpb25Vbml0LzI4MTAyMjEtMjAwNQ=="));
        BrapiListResponse<ObservationVO> observationUnitVOs = observationDao.findObservationByCriteria(obsCrit);
        assertThat(observationUnitVOs).isNotNull();
        assertThat(observationUnitVOs.getMetadata().getPagination().getTotalCount()).isEqualTo(15);
        assertThat(observationUnitVOs.getResult().getData().get(0).getObservationUnitDbId()).isEqualTo("dXJuOklOUkFFLVVSR0kvb2JzZXJ2YXRpb25Vbml0LzI4MTAyMjEtMjAwNQ==");
    }
    @Test
    void custom_should_search_by_observationVariableDbId(){
        ObservationV2Criteria obsCrit = new ObservationV2Criteria();
        obsCrit.setObservationVariableDbId(List.of("dXJuOklOUkFFLVVSR0kvb2JzZXJ2YXRpb25WYXJpYWJsZS9DT18zMjElM0ExMDAwMjM2"));
        BrapiListResponse<ObservationVO> observationUnitVOs = observationDao.findObservationByCriteria(obsCrit);
        assertThat(observationUnitVOs).isNotNull();
        assertThat(observationUnitVOs.getMetadata().getPagination().getTotalCount()).isEqualTo(3867);
        assertThat(observationUnitVOs.getResult().getData().get(0).getObservationVariableDbId()).isEqualTo("dXJuOklOUkFFLVVSR0kvb2JzZXJ2YXRpb25WYXJpYWJsZS9DT18zMjElM0ExMDAwMjM2");
    }
//    @Test
//    void custom_should_search_by_season(){
//        ObservationV2Criteria obsCrit = new ObservationV2Criteria();
//        obsCrit.setSeasons(List.of("dXJuOklOUkFFLVVSR0kvb2JzZXJ2YXRpb25Vbml0LzI4MTAyMjEtMjAwNQ=="));
//        BrapiListResponse<ObservationVO> observationUnitVOs = observationDao.findObservationByCriteria(obsCrit);
//        assertThat(observationUnitVOs).isNotNull();
//        assertThat(observationUnitVOs.getMetadata().getPagination().getTotalCount()).isEqualTo(15);
//        assertThat(observationUnitVOs.getResult().getData().get(0).getSeason()).isEqualTo("dXJuOklOUkFFLVVSR0kvb2JzZXJ2YXRpb25Vbml0LzI4MTAyMjEtMjAwNQ==");
//    }
    @Test
    void custom_should_search_by_germplasmDbId(){
        ObservationV2Criteria obsCrit = new ObservationV2Criteria();
        obsCrit.setGermplasmDbId(List.of("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI2ODMw"));
        BrapiListResponse<ObservationVO> observationUnitVOs = observationDao.findObservationByCriteria(obsCrit);
        assertThat(observationUnitVOs).isNotNull();
        assertThat(observationUnitVOs.getMetadata().getPagination().getTotalCount()).isEqualTo(87);
        assertThat(observationUnitVOs.getResult().getData().get(0).getGermplasmDbId()).isEqualTo("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI2ODMw");
    }
    @Test
    void custom_should_search_by_germplasmName(){
        ObservationV2Criteria obsCrit = new ObservationV2Criteria();
        obsCrit.setGermplasmName(List.of("RE04009"));
        BrapiListResponse<ObservationVO> observationUnitVOs = observationDao.findObservationByCriteria(obsCrit);
        assertThat(observationUnitVOs).isNotNull();
        assertThat(observationUnitVOs.getMetadata().getPagination().getTotalCount()).isEqualTo(87);
        assertThat(observationUnitVOs.getResult().getData().get(0).getGermplasmName()).isEqualTo("RE04009");
    }
    @Test
    void custom_should_search_by_studyDbId(){
        ObservationV2Criteria obsCrit = new ObservationV2Criteria();
        obsCrit.setStudyDbId(List.of("dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0xlX01vdWxvbl8yMDA1X1RFQ0g="));
        BrapiListResponse<ObservationVO> observationUnitVOs = observationDao.findObservationByCriteria(obsCrit);
        assertThat(observationUnitVOs).isNotNull();
        assertThat(observationUnitVOs.getMetadata().getPagination().getTotalCount()).isEqualTo(1470);
        assertThat(observationUnitVOs.getResult().getData().get(0).getStudyDbId()).isEqualTo("dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0xlX01vdWxvbl8yMDA1X1RFQ0g=");
    }
    @Test
    void custom_should_search_by_studyName(){
        ObservationV2Criteria obsCrit = new ObservationV2Criteria();
        obsCrit.setStudyName(List.of("BTH_Le_Moulon_2005_TECH"));
        BrapiListResponse<ObservationVO> observationUnitVOs = observationDao.findObservationByCriteria(obsCrit);
        assertThat(observationUnitVOs).isNotNull();
        assertThat(observationUnitVOs.getMetadata().getPagination().getTotalCount()).isEqualTo(1470);
        assertThat(observationUnitVOs.getResult().getData().get(0).getStudyName()).isEqualTo("BTH_Le_Moulon_2005_TECH");
    }
    @Test
    void custom_should_search_by_trialDbId(){
        ObservationV2Criteria obsCrit = new ObservationV2Criteria();
        obsCrit.setTrialDbId(List.of("dXJuOklOUkFFLVVSR0kvdHJpYWwvNw=="));
        BrapiListResponse<ObservationVO> observationUnitVOs = observationDao.findObservationByCriteria(obsCrit);
        assertThat(observationUnitVOs).isNotNull();
        assertThat(observationUnitVOs.getMetadata().getPagination().getTotalCount()).isEqualTo(100043);
        assertThat(observationUnitVOs.getResult().getData().get(0).getTrialDbId()).isEqualTo("dXJuOklOUkFFLVVSR0kvdHJpYWwvNw==");
    }
    @Test
    void custom_should_search_by_trialName(){
        ObservationV2Criteria obsCrit = new ObservationV2Criteria();
        obsCrit.setTrialName(List.of("INRA Wheat Network technological variables"));
        BrapiListResponse<ObservationVO> observationUnitVOs = observationDao.findObservationByCriteria(obsCrit);
        assertThat(observationUnitVOs).isNotNull();
        assertThat(observationUnitVOs.getMetadata().getPagination().getTotalCount()).isEqualTo(100043);
        assertThat(observationUnitVOs.getResult().getData().get(0).getTrialName()).isEqualTo("INRA Wheat Network technological variables");
    }
//    @Test
//    void custom_should_search_by_studyLocation(){
//        ObservationV2Criteria obsCrit = new ObservationV2Criteria();
//        obsCrit.setStudyLocations(List.of("Le Moulon"));
//        BrapiListResponse<ObservationVO> observationUnitVOs = observationDao.findObservationByCriteria(obsCrit);
//        assertThat(observationUnitVOs).isNotNull();
//        assertThat(observationUnitVOs.getMetadata().getPagination().getTotalCount()).isEqualTo(15);
//        assertThat(observationUnitVOs.getResult().getData().get(0).getStudyLocation()).isEqualTo("Le Moulon");
//    }
    @Test
    void custom_should_search_by_studyLocationDbId(){
        ObservationV2Criteria obsCrit = new ObservationV2Criteria();
        obsCrit.setLocationDbId(List.of("dXJuOklOUkFFLVVSR0kvbG9jYXRpb24vMzQwNjQ="));
        BrapiListResponse<ObservationVO> observationUnitVOs = observationDao.findObservationByCriteria(obsCrit);
        assertThat(observationUnitVOs).isNotNull();
        assertThat(observationUnitVOs.getMetadata().getPagination().getTotalCount()).isEqualTo(19173);
        assertThat(observationUnitVOs.getResult().getData().get(0).getStudyLocationDbId()).isEqualTo("dXJuOklOUkFFLVVSR0kvbG9jYXRpb24vMzQwNjQ=");
    }
    @Test
    void custom_should_search_by_value(){
        ObservationV2Criteria obsCrit = new ObservationV2Criteria();
        obsCrit.setValue(List.of("77,9"));
        BrapiListResponse<ObservationVO> observationUnitVOs = observationDao.findObservationByCriteria(obsCrit);
        assertThat(observationUnitVOs).isNotNull();
        assertThat(observationUnitVOs.getMetadata().getPagination().getTotalCount()).isEqualTo(25);
        assertThat(observationUnitVOs.getResult().getData().get(0).getValue()).isEqualTo("77,9");
    }

    @Test
    void shouldFindByExportCriteria() {
        ObservationExportCriteria exportCriteria = new ObservationExportCriteria(
            "dXJuOklOUkFFLVVSR0kvdHJpYWwvNDI=",
            Set.of("Gaillac"),
            Set.of("2013"),
            Set.of("Psi_Fill")
        );
        try (Stream<ObservationVO> stream = observationDao.findByExportCriteria(exportCriteria)) {
            List<ObservationVO> result = stream.toList();
            assertThat(result).isNotEmpty();
            assertThat(result).allSatisfy(obs -> assertThat(obs.getTrialDbId()).isEqualTo("dXJuOklOUkFFLVVSR0kvdHJpYWwvNDI="));
            assertThat(result).allSatisfy(obs -> assertThat(obs.getStudyLocation()).isEqualTo("Gaillac"));
            assertThat(result).allSatisfy(obs -> assertThat(obs.getSeason().getSeasonName()).isEqualTo("2013"));
            assertThat(result).allSatisfy(obs -> assertThat(obs.getObservationVariableName()).isEqualTo("Psi_Fill"));
        }
    }
}
