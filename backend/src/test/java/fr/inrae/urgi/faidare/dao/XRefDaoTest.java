package fr.inrae.urgi.faidare.dao;

import fr.inrae.urgi.faidare.config.ElasticSearchConfig;
import fr.inrae.urgi.faidare.domain.XRefDocumentVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.elasticsearch.test.autoconfigure.DataElasticsearchTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataElasticsearchTest
@Import({ElasticSearchConfig.class})
public class XRefDaoTest {

    @Autowired
    protected XRefDocumentDao dao;

    /**
     * To be used in thymeleaf controllers for the following code :
     * List<XRefDocumentVO> crossReferences = xRefDocumentRepository.find(
     *             XRefDocumentSearchCriteria.forXRefId(study.getStudyDbId())
     */
    @Test
    public void should_get_perDbId(){
        List<XRefDocumentVO> lVos = dao.findByLinkedResourcesID("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzQ1MDI5");
        assertThat(lVos).hasSize(3);
        assertThat(lVos).allMatch(vo -> !vo.getUrl().isBlank());
        assertThat(lVos).anyMatch(vo -> vo.getDatabaseName().equals("GnpIS"));
        assertThat(lVos).anyMatch(vo -> vo.getEntryType().equals("Phenotyping study"));
        assertThat(lVos).anyMatch(vo -> vo.getSpecies().get(0).equals("Populus deltoides"));

    }
}
