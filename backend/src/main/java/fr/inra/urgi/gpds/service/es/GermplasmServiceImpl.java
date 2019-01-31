package fr.inra.urgi.gpds.service.es;

import com.opencsv.CSVWriter;
import fr.inra.urgi.gpds.api.gnpis.v1.GnpISGermplasmController;
import fr.inra.urgi.gpds.domain.criteria.GermplasmSearchCriteria;
import fr.inra.urgi.gpds.domain.data.germplasm.GermplasmVO;
import fr.inra.urgi.gpds.domain.data.germplasm.PedigreeVO;
import fr.inra.urgi.gpds.domain.data.germplasm.ProgenyVO;
import fr.inra.urgi.gpds.domain.response.PaginatedList;
import fr.inra.urgi.gpds.repository.es.GermplasmRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

/**
 * @author cpommier, gcornut
 */
@Service("germplasmService")
public class GermplasmServiceImpl implements GermplasmService {

    private final static Logger LOGGER = LoggerFactory.getLogger(GnpISGermplasmController.class);

    @Resource
    GermplasmRepository germplasmRepository;

    @Override
    public File exportCSV(GermplasmSearchCriteria criteria) {
        try {
            Iterator<GermplasmVO> allGermplasm = germplasmRepository.scrollAll(criteria);
            Path tmpDirPath = Files.createTempDirectory("germplasm");
            Path tmpFile = Files.createTempFile(tmpDirPath, "germplasm_", ".csv");
            writeToCSV(tmpFile, allGermplasm);
            return tmpFile.toFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeToCSV(Path tmpFile, Iterator<GermplasmVO> germplasms) throws IOException {
        Writer fileStream = new OutputStreamWriter(new FileOutputStream(tmpFile.toFile()), StandardCharsets.UTF_8);
        CSVWriter csvWriter = new CSVWriter(fileStream, ';', '"', '\\', "\n");
        String[] header = new String[]{
            "DOI", "AccessionNumber", "AccessionName", "TaxonGroup", "HoldingInstitution",
            "LotName", "LotSynonym", "CollectionName", "CollectionType", "PanelName", "PanelSize"};
        csvWriter.writeNext(header);

        while (germplasms.hasNext()) {
            GermplasmVO germplasmVO = germplasms.next();
            String[] line = new String[header.length];
            line[0] = germplasmVO.getGermplasmPUI();
            line[1] = germplasmVO.getAccessionNumber();
            line[2] = germplasmVO.getGermplasmName();
            line[3] = germplasmVO.getCommonCropName();
            line[4] = germplasmVO.getInstituteName();
            csvWriter.writeNext(line);
        }
        csvWriter.close();
    }

    @Override
    public GermplasmVO getById(String germplasmDbId) {
        return germplasmRepository.getById(germplasmDbId);
    }

    @Override
    public PaginatedList<GermplasmVO> find(GermplasmSearchCriteria sCrit) {
        return germplasmRepository.find(sCrit);
    }

    @Override
    public PedigreeVO getPedigree(String germplasmDbId) {
        return germplasmRepository.findPedigree(germplasmDbId);
    }

    @Override
    public ProgenyVO getProgeny(String germplasmDbId) {
        return germplasmRepository.findProgeny(germplasmDbId);
    }
}
