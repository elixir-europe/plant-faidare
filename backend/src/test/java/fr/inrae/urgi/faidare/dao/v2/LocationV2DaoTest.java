package fr.inrae.urgi.faidare.dao.v2;

import fr.inrae.urgi.faidare.api.brapi.v2.BrapiListResponse;
import fr.inrae.urgi.faidare.config.ElasticSearchConfig;
import fr.inrae.urgi.faidare.domain.brapi.v2.LocationV2VO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.elasticsearch.test.autoconfigure.DataElasticsearchTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import({ElasticSearchConfig.class})
@DataElasticsearchTest
public class LocationV2DaoTest {

    @Autowired
    protected LocationV2Dao locationDao;

    @Autowired
    private ElasticsearchOperations elasticsearchTemplate;

    @Test
    void getByLocationDbId_should_return_one_result() {
        LocationV2VO locationVo =
            locationDao.getByLocationDbId("dXJuOklOUkFFLVVSR0kvbG9jYXRpb24vMzQwNjQ=");

        assertThat(locationVo).isNotNull();
        assertThat(locationVo.getLocationDbId())
            .isEqualTo("dXJuOklOUkFFLVVSR0kvbG9jYXRpb24vMzQwNjQ=");
    }
    @Test
    void custom_should_search_by_countryCode(){
        LocationV2Criteria lCrit = new LocationV2Criteria();
        lCrit.setCountryCode(List.of("FRA"));
        BrapiListResponse<LocationV2VO> locationVOs = locationDao.findLocationsByCriteria(lCrit);
        assertThat(locationVOs).isNotNull();
        assertThat(locationVOs.getResult().getData().get(0).getCountryCode()).contains("FRA");
    }

    @Test
    void custom_should_search_by_countryName(){
        LocationV2Criteria lCrit = new LocationV2Criteria();
        lCrit.setCountryName(List.of("Italy"));
        BrapiListResponse<LocationV2VO> locationVOs = locationDao.findLocationsByCriteria(lCrit);
        assertThat(locationVOs).isNotNull();
        assertThat(locationVOs.getResult().getData().get(0).getCountryName()).contains("Italy");
    }

    @Test
    void custom_should_search_by_locationDbId(){
        LocationV2Criteria lCrit = new LocationV2Criteria();
        lCrit.setLocationDbId(List.of("dXJuOklOUkFFLVVSR0kvbG9jYXRpb24vMzQwNjQ="));
        BrapiListResponse<LocationV2VO> locationVOs = locationDao.findLocationsByCriteria(lCrit);
        assertThat(locationVOs).isNotNull();
        assertThat(locationVOs.getMetadata().getPagination().getTotalCount()).isEqualTo(1);
        assertThat(locationVOs.getResult().getData().get(0).getLocationDbId()).isEqualTo("dXJuOklOUkFFLVVSR0kvbG9jYXRpb24vMzQwNjQ=");
    }

    @Test
    void custom_should_search_by_locationName(){
        LocationV2Criteria lCrit = new LocationV2Criteria();
        lCrit.setLocationName(List.of("Le Moulon"));
        BrapiListResponse<LocationV2VO> locationVOs = locationDao.findLocationsByCriteria(lCrit);
        assertThat(locationVOs).isNotNull();
        assertThat(locationVOs.getMetadata().getPagination().getTotalCount()).isEqualTo(1);
        assertThat(locationVOs.getResult().getData().get(0).getLocationName()).isEqualTo("Le Moulon");
    }

    @Test
    void custom_should_search_by_locationType(){
        LocationV2Criteria lCrit = new LocationV2Criteria();
        lCrit.setLocationType(List.of("Breeding and Evaluation site"));
        BrapiListResponse<LocationV2VO> locationVOs = locationDao.findLocationsByCriteria(lCrit);
        assertThat(locationVOs).isNotNull();
        assertThat(locationVOs.getMetadata().getPagination().getTotalCount()).isEqualTo(2);
        assertThat(locationVOs.getResult().getData().get(0).getLocationType()).isEqualTo("Breeding and Evaluation site");
    }


    @Test
    void custom_should_search_by_locationName_pageSize1(){
        LocationV2Criteria lCrit = new LocationV2Criteria();
        lCrit.setLocationName(List.of("Lusignan"));
        lCrit.setPageSize(1);
        BrapiListResponse<LocationV2VO> locationVOs = locationDao.findLocationsByCriteria(lCrit);
        assertThat(locationVOs).isNotNull();
        assertThat(locationVOs.getMetadata().getPagination().getPageSize()).isEqualTo(1);
        assertThat(locationVOs.getMetadata().getPagination().getCurrentPage()).isEqualTo(0);
        assertThat(locationVOs.getMetadata().getPagination().getTotalCount()).isEqualTo(1);
        assertThat(locationVOs.getResult().getData().get(0).getLocationName()).isEqualTo("Lusignan");
    }

}
