package fr.inra.urgi.faidare.service.es;

import com.opencsv.CSVReader;
import fr.inra.urgi.faidare.domain.criteria.GermplasmPOSTSearchCriteria;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmVO;
import fr.inra.urgi.faidare.repository.es.GermplasmRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author gcornut
 */

@ExtendWith(SpringExtension.class)
public class GermplasmServiceTest {

    @InjectMocks
    GermplasmServiceImpl germplasmService;

    @Mock
    GermplasmRepository repository;

    @Test
    void exportCSV() throws FileNotFoundException {
        GermplasmPOSTSearchCriteria criteria = new GermplasmPOSTSearchCriteria();
        Iterator<GermplasmVO> germplasmIterator = mockGermplasms();

        when(repository.scrollAll(criteria)).thenReturn(germplasmIterator);

        File csvFile = germplasmService.exportCSV(criteria);

        CSVReader strings = new CSVReader(new FileReader(csvFile));
        assertThat(strings.iterator()).hasSize(4);
        //TODO: Add more validation with mock data
    }

    private Iterator<GermplasmVO> mockGermplasms() {
        GermplasmVO g1 = new GermplasmVO();
        GermplasmVO g2 = new GermplasmVO();
        GermplasmVO g3 = new GermplasmVO();

        return Lists.newArrayList(g1, g2, g3).iterator();
    }


}
