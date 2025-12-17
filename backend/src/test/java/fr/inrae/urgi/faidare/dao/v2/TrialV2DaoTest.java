package fr.inrae.urgi.faidare.dao.v2;

import fr.inrae.urgi.faidare.api.brapi.v2.BrapiListResponse;
import fr.inrae.urgi.faidare.config.ElasticSearchConfig;
import fr.inrae.urgi.faidare.domain.brapi.StudySitemapVO;
import fr.inrae.urgi.faidare.domain.brapi.TrialSitemapVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.TrialV2VO;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.elasticsearch.DataElasticsearchTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataElasticsearchTest
@Import({ElasticSearchConfig.class})
public class TrialV2DaoTest {

    private static final Logger logger = LoggerFactory.getLogger(TrialV2DaoTest.class);
    @Autowired
    protected TrialV2Dao dao;

    @Test
    public void should_get_one_trialName_perDbId(){
        TrialV2VO vo = dao.getByTrialDbId("dXJuOklOUkFFLVVSR0kvdHJpYWwvNw==");
        assertThat(vo).isNotNull();
        assertThat(vo.getTrialName()).isEqualTo("INRA Wheat Network technological variables");


    }

    @Test
    public void should_search_by_triallDbIds() {
        TrialCriteria tCrit = new TrialCriteria();
        tCrit.setTrialDbId(List.of("dXJuOklOUkFFLVVSR0kvdHJpYWwvNw=="));
        BrapiListResponse<TrialV2VO> trialVOs = dao.findTrialsByCriteria(tCrit);
        assertThat(trialVOs).isNotNull();
        assertThat(trialVOs.getResult().getData().get(0).getTrialDbId()).isEqualTo("dXJuOklOUkFFLVVSR0kvdHJpYWwvNw==");
    }

    @Test
    public void should_search_by_trialNames() {
        TrialCriteria tCrit = new TrialCriteria();
        tCrit.setTrialName(List.of("INRA Wheat Network technological variables"));
        BrapiListResponse<TrialV2VO> trialVOs = dao.findTrialsByCriteria(tCrit);
        assertThat(trialVOs).isNotNull();
        assertThat(trialVOs.getResult().getData().get(0).getTrialName()).isEqualTo("INRA Wheat Network technological variables");
    }

    @Test
    public void should_search_by_trialTypes() {
        TrialCriteria tCrit = new TrialCriteria();
        tCrit.setTrialType(List.of("Network"));
        BrapiListResponse<TrialV2VO> trialVOs = dao.findTrialsByCriteria(tCrit);
        assertThat(trialVOs).isNotNull();
        assertThat(trialVOs.getResult().getData().get(0).getTrialType()).isEqualTo("Network");
    }

    @Test
    public void should_search_by_insituteName() {
        TrialCriteria tCrit = new TrialCriteria();
        tCrit.setInstituteName(List.of("BioForA - UMR Biologie intégrée pour la valorisation de la diversité des arbres et de la Forêt"));
        BrapiListResponse<TrialV2VO> trialVOs = dao.findTrialsByCriteria(tCrit);
        assertThat(trialVOs).isNotNull();
        assertThat(trialVOs.getResult().getData()).isNotEmpty();
        //assertThat(trialVOs.getResult().getData().get(0).getInstituteName()).isEqualTo("BioForA - UMR Biologie intégrée pour la valorisation de la diversité des arbres et de la Forêt");
    }

    public void should_search_by_programDbId() {
        TrialCriteria tCrit = new TrialCriteria();
        tCrit.setProgramDbId(List.of(""));
        BrapiListResponse<TrialV2VO> trialVOs = dao.findTrialsByCriteria(tCrit);
        assertThat(trialVOs).isNotNull();
        assertThat(trialVOs.getResult().getData().get(0).getProgramDbId()).isEqualTo("");
    }

    @Test
    public void should_search_by_programName() {
        TrialCriteria tCrit = new TrialCriteria();
        tCrit.setProgramName(List.of("Optimising the management and sustainable use of forest genetic resources in Europe"));
        BrapiListResponse<TrialV2VO> trialVOs = dao.findTrialsByCriteria(tCrit);
        assertThat(trialVOs).isNotNull();
        assertThat(trialVOs.getResult().getData().get(0).getProgramName()).isEqualTo("Optimising the management and sustainable use of forest genetic resources in Europe");
    }

    @Test
    public void should_search_by_studyDbId() {
        TrialCriteria tCrit = new TrialCriteria();
        tCrit.setStudyDbId(List.of("dXJuOklOUkFFLVVSR0kvc3R1ZHkvUE9QWU9NSUNTLVBPUDItSQ=="));
        BrapiListResponse<TrialV2VO> trialVOs = dao.findTrialsByCriteria(tCrit);
        assertThat(trialVOs).isNotNull();
//        assertThat(trialVOs.getResult().getData()).isNotEmpty();
//        assertThat(trialVOs.getResult().getData().get(0).getStudyDbIds()).isEqualTo("dXJuOklOUkFFLVVSR0kvc3R1ZHkvUE9QWU9NSUNTLVBPUDItSQ==");
    }

    @Test
    void findAllForSitemap() {
        List<TrialSitemapVO> list = dao.findAllForSitemap().toList();
        assertThat(list.size()).isGreaterThan(1);
        assertThat(list.get(0)).isInstanceOf(TrialSitemapVO.class);
        assertThat(list.get(0).getTrialDbId()).isNotNull();
    }
}
