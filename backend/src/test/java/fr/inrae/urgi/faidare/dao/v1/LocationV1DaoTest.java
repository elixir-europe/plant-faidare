package fr.inrae.urgi.faidare.dao.v1;

import fr.inrae.urgi.faidare.config.ElasticSearchConfig;
import fr.inrae.urgi.faidare.domain.LocationVO;
import fr.inrae.urgi.faidare.domain.brapi.LocationSitemapVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.elasticsearch.test.autoconfigure.DataElasticsearchTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataElasticsearchTest
@Import({ElasticSearchConfig.class})
public class LocationV1DaoTest {

    @Autowired
    protected LocationV1Dao locDao;

    @Test
    public void should_get_one_location_perDbId(){
        LocationVO lVo = locDao.getByLocationDbId("dXJuOklOUkFFLVVSR0kvbG9jYXRpb24vMTk5NA==");
        assertThat(lVo).isNotNull();
        assertThat(lVo.getLocationDbId()).isEqualTo("dXJuOklOUkFFLVVSR0kvbG9jYXRpb24vMTk5NA==");
        assertThat(lVo.getLocationName()).isEqualTo("Clermont-Ferrand");
    }

    @Test
    void findAllForSitemap() {
        List<LocationSitemapVO> list = locDao.findAllForSitemap().toList();
        assertThat(list.size()).isGreaterThan(1);
        assertThat(list.get(0)).isInstanceOf(LocationSitemapVO.class);
        assertThat(list.get(0).getLocationDbId()).isNotNull();
    }

    @Test
    void getByLocationDbIdIn() {
        Set<String> locationDbIds = Set.of("dXJuOklOUkFFLVVSR0kvbG9jYXRpb24vMzM0Mjg=", "dXJuOklOUkFFLVVSR0kvbG9jYXRpb24vMzQwNjQ=");
        List<LocationVO> result = locDao.getByLocationDbIdIn(locationDbIds);
        assertThat(result).hasSize(2);
    }
}
