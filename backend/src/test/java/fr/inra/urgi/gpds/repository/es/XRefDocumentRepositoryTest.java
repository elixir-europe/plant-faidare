package fr.inra.urgi.gpds.repository.es;

import fr.inra.urgi.gpds.Application;
import fr.inra.urgi.gpds.domain.response.PaginatedList;
import fr.inra.urgi.gpds.domain.xref.XRefDocumentSearchCriteria;
import fr.inra.urgi.gpds.domain.xref.XRefDocumentVO;
import fr.inra.urgi.gpds.repository.es.setup.ESSetUp;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author gcornut
 */

@ExtendWith(SpringExtension.class)
@Import({ESSetUp.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource("/test.properties")
@SpringBootTest(classes = Application.class)
class XRefDocumentRepositoryTest {

    @Autowired
    ESSetUp esSetUp;

    @BeforeAll
    void before() {
        esSetUp.initializeXref(0L);
    }

    @Autowired
    XRefDocumentRepository repository;

    @Test
    void should_Find_All() {
        XRefDocumentSearchCriteria criteria = new XRefDocumentSearchCriteria();
        PaginatedList<XRefDocumentVO> documents = repository.find(criteria);
        assertThat(documents).isNotNull().hasSize(3);
    }

    @Test
    void should_Find_By_Enty_Type() {
        String entryType = "Accession";
        XRefDocumentSearchCriteria criteria = new XRefDocumentSearchCriteria();
        criteria.setEntryType(entryType);
        PaginatedList<XRefDocumentVO> documents = repository.find(criteria);
        assertThat(documents).isNotNull().hasSize(1)
            .extracting("entryType").containsOnly(entryType);
    }

    @Test
    void should_Find_By_Linked_Id() {
        String id = "ID2";
        List<String> linkedRessourcesID = Collections.singletonList(id);
        XRefDocumentSearchCriteria criteria = new XRefDocumentSearchCriteria();
        criteria.setLinkedRessourcesID(linkedRessourcesID);
        PaginatedList<XRefDocumentVO> documents = repository.find(criteria);
        assertThat(documents).isNotNull().hasSize(2)
            .flatExtracting("linkedRessourcesID")
                .contains(id);
    }

}
