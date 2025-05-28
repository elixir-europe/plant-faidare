package fr.inrae.urgi.faidare.dao;

import fr.inrae.urgi.faidare.dao.v2.GermplasmMcpdDao;
import fr.inrae.urgi.faidare.domain.GermplasmMcpdVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.elasticsearch.DataElasticsearchTest;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataElasticsearchTest
class GermplasmMcpdDaoTest {

    @Autowired
    protected GermplasmMcpdDao germplasmMcpdDao;


    @Test
    void getByPUID() {
        GermplasmMcpdVO germplasmVo =
                germplasmMcpdDao.getByPUID("https://doi.org/10.15454/4NCDUP");
        assertThat(germplasmVo).isNotNull();
        assertThat(germplasmVo).isInstanceOf(GermplasmMcpdVO.class);
        assertThat(germplasmVo.getAncestralData()).isNotNull();
        assertThat(germplasmVo.getAncestralData()).isEqualTo("RE99102");

    }

    @Test
    void getByGermplasmDbId() {
        GermplasmMcpdVO germplasmVo =
            germplasmMcpdDao.getByGermplasmDbId("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0MDU5");
        assertThat(germplasmVo).isNotNull();
        assertThat(germplasmVo).isInstanceOf(GermplasmMcpdVO.class);
        assertThat(germplasmVo.getAncestralData()).isNotNull();
        assertThat(germplasmVo.getAncestralData()).isEqualTo("RECITAL");
    }

    @Test
    void findByGermplasmDbIdIn() {
        List<GermplasmMcpdVO> list = germplasmMcpdDao.findByGermplasmDbIdIn(Set.of("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0MDU5", "not-existing")).toList();
        assertThat(list).extracting(GermplasmMcpdVO::getGermplasmDbId).containsOnly("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0MDU5");
    }
}
