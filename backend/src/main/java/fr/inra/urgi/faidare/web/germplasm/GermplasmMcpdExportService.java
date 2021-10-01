package fr.inra.urgi.faidare.web.germplasm;

import static fr.inra.urgi.faidare.web.germplasm.GermplasmMcpdExportableField.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.opencsv.CSVWriter;
import fr.inra.urgi.faidare.domain.data.germplasm.DonorInfoVO;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmMcpdVO;
import fr.inra.urgi.faidare.domain.data.germplasm.InstituteVO;
import org.springframework.stereotype.Component;

/**
 * Service allowing to export germplasm MCPDs as CSV
 * @author JB Nizet
 */
@Component
public class GermplasmMcpdExportService {

    private final Map<GermplasmMcpdExportableField, GermplasmMcpdExportableFieldDescriptor> descriptors;

    public GermplasmMcpdExportService() {
        Map<GermplasmMcpdExportableField, GermplasmMcpdExportableFieldDescriptor> map = new HashMap<>();

        map.put(PUID, withFieldAsHeader(PUID, vo -> vo.getGermplasmPUI()));
        map.put(INSTCODE, withFieldAsHeader(INSTCODE, vo -> vo.getInstituteCode()));
        map.put(ACCENUMB, withFieldAsHeader(ACCENUMB, vo -> vo.getAccessionNumber()));
        map.put(COLLNUMB, withFieldAsHeader(COLLNUMB, vo -> vo.getCollectingInfo().getCollectingNumber()));
        map.put(COLLCODE, withFieldAsHeader(COLLCODE, vo ->
            vo.getCollectingInfo()
              .getCollectingInstitutes()
              .stream()
              .map(InstituteVO::getInstituteCode).collect(Collectors.joining(";"))));
        map.put(COLLNAME, withFieldAsHeader(COLLNAME, vo ->
            vo.getCollectingInfo()
              .getCollectingInstitutes()
              .stream()
              .map(InstituteVO::getInstituteName)
              .collect(Collectors.joining(";"))));
        map.put(COLLINSTADDRESS, withFieldAsHeader(COLLINSTADDRESS, vo ->
            vo.getCollectingInfo()
              .getCollectingInstitutes()
              .stream()
              .map(InstituteVO::getAddress)
              .collect(Collectors.joining(";"))));
        map.put(COLLMISSID, withFieldAsHeader(COLLMISSID, vo -> vo.getCollectingInfo().getCollectingMissionIdentifier()));
        map.put(GENUS, withFieldAsHeader(GENUS, vo -> vo.getGenus()));
        map.put(SPECIES, withFieldAsHeader(SPECIES, vo -> vo.getSpecies()));
        map.put(SPAUTHOR, withFieldAsHeader(SPAUTHOR, vo -> vo.getSpeciesAuthority()));
        map.put(SUBTAXA, withFieldAsHeader(SUBTAXA, vo -> vo.getSubtaxon()));
        map.put(SUBTAUTHOR, withFieldAsHeader(SUBTAUTHOR, vo -> vo.getSubtaxonAuthority()));
        map.put(CROPNAME, withFieldAsHeader(CROPNAME, vo -> vo.getCommonCropName()));
        map.put(ACCENAME, withFieldAsHeader(ACCENAME, vo -> String.join(";", vo.getAccessionNames())));
        map.put(ACQDATE, withFieldAsHeader(ACQDATE, vo -> vo.getAcquisitionDate()));
        map.put(ORIGCTY, withFieldAsHeader(ORIGCTY, vo -> vo.getCountryOfOriginCode()));
        map.put(COLLSITE, withFieldAsHeader(COLLSITE, vo -> vo.getCollectingInfo().getCollectingSite().getSiteName()));
        map.put(DECLATITUDE, withFieldAsHeader(DECLATITUDE, vo -> vo.getCollectingInfo().getCollectingSite().getLatitudeDecimal()));
        map.put(LATITUDE, withFieldAsHeader(LATITUDE, vo -> vo.getCollectingInfo().getCollectingSite().getLatitudeDegrees()));
        map.put(DECLONGITUDE, withFieldAsHeader(DECLONGITUDE, vo -> vo.getCollectingInfo().getCollectingSite().getLongitudeDecimal()));
        map.put(LONGITUDE, withFieldAsHeader(LONGITUDE, vo -> vo.getCollectingInfo().getCollectingSite().getLongitudeDegrees()));
        map.put(COORDUNCERT, withFieldAsHeader(COORDUNCERT, vo -> vo.getCollectingInfo().getCollectingSite().getCoordinateUncertainty()));
        map.put(COORDDATUM, withFieldAsHeader(COORDDATUM, vo -> vo.getCollectingInfo().getCollectingSite().getSpatialReferenceSystem()));
        map.put(GEOREFMETH, withFieldAsHeader(GEOREFMETH, vo -> vo.getCollectingInfo().getCollectingSite().getGeoreferencingMethod()));
        map.put(ELEVATION, withFieldAsHeader(ELEVATION, vo -> vo.getCollectingInfo().getCollectingSite().getElevation()));
        map.put(COLLDATE, withFieldAsHeader(COLLDATE, vo -> vo.getCollectingInfo().getCollectingDate()));
        map.put(BREDCODE, withFieldAsHeader(BREDCODE, vo ->
            vo.getBreedingInstitutes()
              .stream()
              .map(InstituteVO::getInstituteCode)
              .collect(Collectors.joining(";"))));
        map.put(BREDNAME, withFieldAsHeader(BREDNAME, vo ->
            vo.getBreedingInstitutes()
              .stream()
              .map(InstituteVO::getInstituteName)
              .collect(Collectors.joining(";"))));
        map.put(SAMPSTAT, withFieldAsHeader(SAMPSTAT, vo -> vo.getBiologicalStatusOfAccessionCode()));
        map.put(ANCEST, withFieldAsHeader(ANCEST, vo -> vo.getAncestralData()));
        map.put(COLLSRC, withFieldAsHeader(COLLSRC, vo -> vo.getAcquisitionSourceCode()));
        map.put(DONORCODE, withFieldAsHeader(DONORCODE, vo ->
            vo.getDonorInfo()
              .stream()
              .map(donorInfoVO -> donorInfoVO.getDonorInstitute().getInstituteCode())
              .collect(Collectors.joining(";"))));
        map.put(DONORNAME, withFieldAsHeader(DONORNAME, vo ->
            vo.getDonorInfo()
              .stream()
              .map(donorInfoVO -> donorInfoVO.getDonorInstitute().getInstituteName())
              .collect(Collectors.joining(";"))));
        map.put(DONORNUMB, withFieldAsHeader(DONORNUMB, vo ->
            vo.getDonorInfo()
              .stream()
              .map(DonorInfoVO::getDonorAccessionNumber)
              .collect(Collectors.joining(";"))));
        map.put(OTHERNUMB, withFieldAsHeader(OTHERNUMB, vo -> String.join(";", vo.getAlternateIDs())));
        map.put(DUPLSITE, withFieldAsHeader(DUPLSITE, vo ->
            vo.getSafetyDuplicateInstitutes()
              .stream()
              .map(InstituteVO::getInstituteCode)
              .collect(Collectors.joining(";"))));
        map.put(DUPLINSTNAME, withFieldAsHeader(DUPLINSTNAME, vo ->
            vo.getSafetyDuplicateInstitutes()
              .stream()
              .map(InstituteVO::getInstituteName)
              .collect(Collectors.joining(";"))));
        map.put(STORAGE, withFieldAsHeader(STORAGE, vo -> String.join(";", vo.getStorageTypeCodes())));
        map.put(MLSSTAT, withFieldAsHeader(MLSSTAT, vo -> vo.getMlsStatus()));
        map.put(REMARKS, withFieldAsHeader(REMARKS, vo -> vo.getRemarks()));

        this.descriptors = Collections.unmodifiableMap(map);

        if (map.size() != GermplasmMcpdExportableField.values().length) {
            throw new IllegalStateException("Missing field descriptor");
        }
    }

    public void export(OutputStream out, Iterator<GermplasmMcpdVO> germplasms, List<GermplasmMcpdExportableField> fields) {
        try {
            CSVWriter csvWriter = new CSVWriter(new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8)), ';', '"', '\\', "\n");
            String[] header = fields.stream()
                                    .map(descriptors::get)
                                    .map(GermplasmMcpdExportableFieldDescriptor::getHeader)
                                    .toArray(String[]::new);
            csvWriter.writeNext(header);

            while (germplasms.hasNext()) {
                GermplasmMcpdVO vo = germplasms.next();
                String[] line =
                    fields.stream()
                          .map(descriptors::get)
                          .map(descriptor -> descriptor.export(vo))
                          .toArray(String[]::new);
                csvWriter.writeNext(line);
            }
            csvWriter.flush();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private GermplasmMcpdExportableFieldDescriptor withFieldAsHeader(
        GermplasmMcpdExportableField field,
        Function<GermplasmMcpdVO, String> exporter) {
        return new GermplasmMcpdExportableFieldDescriptor(field.name(), exporter);
    }

    private static class GermplasmMcpdExportableFieldDescriptor {
        private final String header;
        private final Function<GermplasmMcpdVO, String> exporter;

        public GermplasmMcpdExportableFieldDescriptor(String header,
                                                      Function<GermplasmMcpdVO, String> exporter) {
            this.header = header;
            this.exporter = exporter;
        }

        public String getHeader() {
            return this.header;
        }

        public String export(GermplasmMcpdVO germplasm) {
            return this.exporter.apply(germplasm);
        }
    }
}


