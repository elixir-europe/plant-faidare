package fr.inra.urgi.faidare.service.es;

import com.opencsv.CSVWriter;
import fr.inra.urgi.faidare.api.faidare.v1.GnpISGermplasmController;
import fr.inra.urgi.faidare.domain.criteria.FaidareGermplasmPOSTShearchCriteria;
import fr.inra.urgi.faidare.domain.criteria.GermplasmSearchCriteria;
import fr.inra.urgi.faidare.domain.data.germplasm.CollPopVO;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmVO;
import fr.inra.urgi.faidare.domain.data.germplasm.PedigreeVO;
import fr.inra.urgi.faidare.domain.data.germplasm.ProgenyVO;
import fr.inra.urgi.faidare.domain.datadiscovery.response.GermplasmSearchResponse;
import fr.inra.urgi.faidare.domain.response.PaginatedList;
import fr.inra.urgi.faidare.repository.es.GermplasmRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmMcpdVO;


import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author cpommier, gcornut, jdestin
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

    @Override
    public File exportListGermplasmCSV(FaidareGermplasmPOSTShearchCriteria criteria) {
        try {
            Iterator<GermplasmVO> allGermplasm = germplasmRepository.scrollAllGermplasm(criteria);
            Path tmpDirPath = Files.createTempDirectory("germplasm");
            Path tmpFile = Files.createTempFile(tmpDirPath, "germplasm_", ".csv");
            writeToCSV(tmpFile, allGermplasm);
            return tmpFile.toFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GermplasmMcpdVO getMcpdById(String germplasmDbId) {
        return germplasmRepository.getMcpdById(germplasmDbId);
    }


    private void writeToCSV(Path tmpFile, Iterator<GermplasmVO> germplasms) throws IOException {
        Writer fileStream = new OutputStreamWriter(new FileOutputStream(tmpFile.toFile()), StandardCharsets.UTF_8);
        CSVWriter csvWriter = new CSVWriter(fileStream, ';', '"', '\\', "\n");
        String[] header = new String[]{
            "DOI", "AccessionNumber", "AccessionName", "TaxonGroup", "HoldingInstitution",
            "LotName", "LotSynonym", "CollectionName", "CollectionType", "PanelName", "PanelSize"};
        csvWriter.writeNext(header);

        while (germplasms.hasNext()) {
            List<String> collectionNames = new ArrayList<>();
            List<String> panelNames = new ArrayList<>();
            GermplasmVO germplasmVO = germplasms.next();

            if (germplasmVO.getCollection() != null) {
                for (CollPopVO collection : germplasmVO.getCollection()) {
                    collectionNames.add(collection.getName());
                }
            }

            if (germplasmVO.getPanel() != null) {
                for (CollPopVO panel : germplasmVO.getPanel()) {
                    panelNames.add(panel.getName());
                }
            }

            String[] line = new String[header.length];
            line[0] = germplasmVO.getGermplasmPUI();
            line[1] = germplasmVO.getAccessionNumber();
            line[2] = germplasmVO.getGermplasmName();
            line[3] = germplasmVO.getCommonCropName();
            line[4] = germplasmVO.getInstituteName();
            line[7] = (collectionNames != null) ? String.join(", ", collectionNames) : "";
            line[9] = (panelNames != null) ? String.join(", ", panelNames) : "";
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
    public GermplasmSearchResponse germplasmFind(FaidareGermplasmPOSTShearchCriteria germplasmSearchCriteria) {
        return germplasmRepository.germplasmFind(germplasmSearchCriteria);
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
