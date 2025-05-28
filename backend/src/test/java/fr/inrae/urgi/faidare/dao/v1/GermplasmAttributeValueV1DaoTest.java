package fr.inrae.urgi.faidare.dao.v1;

import fr.inrae.urgi.faidare.domain.brapi.v1.GermplasmAttributeV1VO;
import fr.inrae.urgi.faidare.domain.brapi.v1.GermplasmAttributeValueV1VO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.elasticsearch.DataElasticsearchTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataElasticsearchTest
class GermplasmAttributeValueV1DaoTest {

    @Autowired
    GermplasmAttributeV1Dao dao;

    /**
     * To be used in Faidare fr/inra/urgi/faidare/web/germplasm/GermplasmController.java
     * private List<BrapiGermplasmAttributeValue> getAttributes(GermplasmVO germplasm)
     */
    @Test
    void should_getByGermplasmDbId() {
        String germplasmDbId = "dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI1NjEy";
        GermplasmAttributeV1VO result = dao.getByGermplasmDbId(germplasmDbId);
        assertThat(result).isNotNull();
        assertThat(result.getGermplasmDbId()).isEqualTo(germplasmDbId);
        List<GermplasmAttributeValueV1VO> attributeValues = result.getData();
        assertThat(attributeValues).isNotEmpty().hasSize(4);
        assertThat(attributeValues.get(0).getAttributeName()).isEqualTo("Growth class");
    }
}
