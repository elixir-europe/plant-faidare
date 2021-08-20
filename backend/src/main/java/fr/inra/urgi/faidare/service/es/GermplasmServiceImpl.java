package fr.inra.urgi.faidare.service.es;

import com.opencsv.CSVWriter;
import fr.inra.urgi.faidare.api.faidare.v1.GnpISGermplasmController;
import fr.inra.urgi.faidare.domain.criteria.FaidareGermplasmPOSTShearchCriteria;
import fr.inra.urgi.faidare.domain.criteria.GermplasmSearchCriteria;
import fr.inra.urgi.faidare.domain.data.germplasm.*;
import fr.inra.urgi.faidare.domain.datadiscovery.response.GermplasmSearchResponse;
import fr.inra.urgi.faidare.domain.response.PaginatedList;
import fr.inra.urgi.faidare.repository.es.GermplasmRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

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
    public GermplasmMcpdVO getAsMcpdById(String germplasmDbId) {
        return germplasmRepository.getAsMcpdById(germplasmDbId);
    }

    @Override
    public File exportGermplasmMcpd(FaidareGermplasmPOSTShearchCriteria criteria) {
        try {
            Iterator<GermplasmMcpdVO> allGermplasm = germplasmRepository.scrollAllGermplasmMcpd(criteria);
            Path tmpDirPath = Files.createTempDirectory("germplasm");
            Path tmpFile = Files.createTempFile(tmpDirPath, "germplasm_", ".csv");
            writeMcpdToCSV(tmpFile, allGermplasm);
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




    private void writeMcpdToCSV(Path tmpFile, Iterator<GermplasmMcpdVO> germplasms) throws IOException {
        Writer fileStream = new OutputStreamWriter(new FileOutputStream(tmpFile.toFile()), StandardCharsets.UTF_8);
        CSVWriter csvWriter = new CSVWriter(fileStream, ';', '"', '\\', "\n");
        String[] header = new String[]{
            "PUID", "INSTCODE", "ACCENUMB", "COLLNUMB", "COLLCODE", "COLLNAME",
            "COLLINSTADDRESS", "COLLMISSID", "GENUS", "SPECIES", "SPAUTHOR", "SUBTAXA",
            "SUBTAUTHOR", "CROPNAME", "ACCENAME", "ACQDATE", "ORIGCTY", "COLLSITE",
            "DECLATITUDE", "LATITUDE", "DECLONGITUDE", "LONGITUDE", "COORDUNCERT",
            "COORDDATUM", "GEOREFMETH", "ELEVATION", "COLLDATE", "BREDCODE",
            "BREDNAME", "SAMPSTAT", "ANCEST","COLLSRC", "DONORCODE", "DONORNAME",
            "DONORNUMB", "OTHERNUMB", "DUPLSITE", "DUPLINSTNAME", "STORAGE", "MLSSTAT", "REMARKS",
            };
        csvWriter.writeNext(header);

        while (germplasms.hasNext()) {
            List<String> collectionNames = new ArrayList<>();
            List<String> panelNames = new ArrayList<>();
            GermplasmMcpdVO germplasmMcpdVO = germplasms.next();


            String[] line = new String[header.length];
            line[0] = germplasmMcpdVO.getGermplasmPUI();
            line[1] = germplasmMcpdVO.getInstituteCode();
            line[2] = germplasmMcpdVO.getAccessionNumber();
            line[3] = germplasmMcpdVO.getCollectingInfo().getCollectingNumber();
            line[4] = germplasmMcpdVO.getCollectingInfo().getCollectingInstitutes().stream()
                .map(InstituteVO::getInstituteCode).collect(Collectors.joining(";"));
            line[5] = germplasmMcpdVO.getCollectingInfo().getCollectingInstitutes().stream()
                .map(InstituteVO::getInstituteName).collect(Collectors.joining(";"));
            line[6] = germplasmMcpdVO.getCollectingInfo().getCollectingInstitutes().stream()
                .map(InstituteVO::getAddress).collect(Collectors.joining(";"));
            line[7] = germplasmMcpdVO.getCollectingInfo().getCollectingMissionIdentifier();
            line[8] = germplasmMcpdVO.getGenus();
            line[9] = germplasmMcpdVO.getSpecies();
            line[10] = germplasmMcpdVO.getSpeciesAuthority();
            line[11] = germplasmMcpdVO.getSubtaxon();
            line[12] = germplasmMcpdVO.getSubtaxonAuthority();
            line[13] = germplasmMcpdVO.getCommonCropName();
            line[14] = String.join(";",germplasmMcpdVO.getAccessionNames());
            line[15] = germplasmMcpdVO.getAcquisitionDate();
            line[16] = germplasmMcpdVO.getCountryOfOriginCode();
            line[18] = germplasmMcpdVO.getCollectingInfo().getCollectingSite().getSiteName();
            line[19] = germplasmMcpdVO.getCollectingInfo().getCollectingSite().getLatitudeDecimal();
            line[20] = germplasmMcpdVO.getCollectingInfo().getCollectingSite().getLatitudeDegrees();
            line[21] = germplasmMcpdVO.getCollectingInfo().getCollectingSite().getLongitudeDecimal();
            line[22] = germplasmMcpdVO.getCollectingInfo().getCollectingSite().getLongitudeDegrees();
            line[23] = germplasmMcpdVO.getCollectingInfo().getCollectingSite().getCoordinateUncertainty();
            line[24] = germplasmMcpdVO.getCollectingInfo().getCollectingSite().getSpatialReferenceSystem();
            line[25] = germplasmMcpdVO.getCollectingInfo().getCollectingSite().getGeoreferencingMethod();
            line[26] = germplasmMcpdVO.getCollectingInfo().getCollectingSite().getElevation();
            line[27] = germplasmMcpdVO.getCollectingInfo().getCollectingDate();
            line[28] = germplasmMcpdVO.getBreedingInstitutes().stream()
                .map(InstituteVO::getInstituteCode).collect(Collectors.joining(";"));
            line[29] = germplasmMcpdVO.getBreedingInstitutes().stream()
                .map(InstituteVO::getInstituteName).collect(Collectors.joining(";"));
            line[30] = germplasmMcpdVO.getBiologicalStatusOfAccessionCode();
            line[31] = germplasmMcpdVO.getAncestralData();
            line[32] = germplasmMcpdVO.getAcquisitionSourceCode();
            line[33] = germplasmMcpdVO.getDonorInfo().stream()
                .map(donorInfoVO -> donorInfoVO.getDonorInstitute().getInstituteCode()).collect(Collectors.joining(";"));
            line[34] = germplasmMcpdVO.getDonorInfo().stream()
                .map(donorInfoVO -> donorInfoVO.getDonorInstitute().getInstituteName()).collect(Collectors.joining(";"));
            line[35] = germplasmMcpdVO.getDonorInfo().stream()
                .map(DonorInfoVO::getDonorAccessionNumber).collect(Collectors.joining(";"));
            line[36] = String.join(";", germplasmMcpdVO.getAlternateIDs());
            line[37] = germplasmMcpdVO.getSafetyDuplicateInstitutes().stream()
                .map(InstituteVO::getInstituteName).collect(Collectors.joining(";"));
            line[38] = String.join(";", germplasmMcpdVO.getStorageTypeCodes());
            line[39] = germplasmMcpdVO.getMlsStatus();
            line[40] = germplasmMcpdVO.getRemarks();
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
