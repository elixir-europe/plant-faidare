package fr.inrae.urgi.faidare.dao.v1;

import fr.inrae.urgi.faidare.config.ElasticSearchConfig;
import fr.inrae.urgi.faidare.domain.brapi.v1.TrialV1VO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.elasticsearch.test.autoconfigure.DataElasticsearchTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataElasticsearchTest
@Import({ElasticSearchConfig.class})
public class TrialV1DaoTest {

    @Autowired
    protected TrialV1Dao dao;

    @Test
    public void should_get_one_trialName_perDbId(){
        TrialV1VO vo = dao.getByTrialDbId("dXJuOklOUkFFLVVSR0kvdHJpYWwvNw==");
        assertThat(vo).isNotNull();
         assertThat(vo.getTrialName()).isEqualTo("INRA Wheat Network technological variables");

    }
}
