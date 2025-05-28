package fr.inrae.urgi.faidare.web.germplasm;

import static fr.inrae.urgi.faidare.web.germplasm.GermplasmMcpdExportableField.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.opencsv.CSVWriter;
import fr.inrae.urgi.faidare.domain.GermplasmMcpdVO;
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

        map.put(PUID, withFieldAsHeader(PUID, vo -> vo.getPUID()));
        map.put(INSTCODE, withFieldAsHeader(INSTCODE, vo -> vo.getInstituteCode()));
        map.put(ACCENUMB, withFieldAsHeader(ACCENUMB, vo -> vo.getAccessionNumber()));
        // FIXME JBN uncomment this once germplasm mcpd has a collecting info
//        map.put(COLLNUMB, withFieldAsHeader(COLLNUMB, vo -> (vo.getCollectingInfo() == null)? null : vo.getCollectingInfo().getCollectingNumber()));
//        map.put(COLLCODE, withFieldAsHeader(COLLCODE, vo -> (vo.getCollectingInfo() == null)? null :
//            vo.getCollectingInfo()
//              .getCollectingInstitutes()
//              .stream()
//              .map(InstituteVO::getInstituteCode).collect(Collectors.joining(";"))));
//        map.put(COLLNAME, withFieldAsHeader(COLLNAME, vo -> (vo.getCollectingInfo() == null)? null :
//            vo.getCollectingInfo()
//              .getCollectingInstitutes()
//              .stream()
//              .map(InstituteVO::getInstituteName)
//              .collect(Collectors.joining(";"))));
//        map.put(COLLINSTADDRESS, withFieldAsHeader(COLLINSTADDRESS, vo -> (vo.getCollectingInfo() == null || vo.getCollectingInfo().getCollectingInstitutes() == null)? null :
//            vo.getCollectingInfo()
//              .getCollectingInstitutes()
//              .stream()
//              .map(InstituteVO::getAddress)
//              .collect(Collectors.joining(";"))));
//        map.put(COLLMISSID, withFieldAsHeader(COLLMISSID, vo -> (vo.getCollectingInfo() == null)? null : vo.getCollectingInfo().getCollectingMissionIdentifier()));
        map.put(COLLNUMB, withFieldAsHeader(COLLNUMB, vo -> null));
        map.put(COLLCODE, withFieldAsHeader(COLLCODE, vo -> null));
        map.put(COLLNAME, withFieldAsHeader(COLLNAME, vo -> null));
        map.put(COLLINSTADDRESS, withFieldAsHeader(COLLINSTADDRESS, vo -> null));

        map.put(GENUS, withFieldAsHeader(GENUS, vo -> vo.getGenus()));
        map.put(SPECIES, withFieldAsHeader(SPECIES, vo -> vo.getSpecies()));
        map.put(SPAUTHOR, withFieldAsHeader(SPAUTHOR, vo -> vo.getSpeciesAuthority()));

        // FIXME JBN uncomment this once germplasm mcpd has a subTaxon
        // map.put(SUBTAXA, withFieldAsHeader(SUBTAXA, vo -> vo.getSubtaxon()));
        map.put(SUBTAXA, withFieldAsHeader(SUBTAXA, vo -> null));

        // FIXME JBN uncomment this once germplasm mcpd has a subTaxonAuthority
        // map.put(SUBTAUTHOR, withFieldAsHeader(SUBTAUTHOR, vo -> vo.getSubtaxonAuthority()));
        map.put(SUBTAUTHOR, withFieldAsHeader(SUBTAUTHOR, vo -> null));

        // FIXME JBN uncomment this once germplasm mcpd has a commonCropName
        // map.put(CROPNAME, withFieldAsHeader(CROPNAME, vo -> vo.getCommonCropName()));
        map.put(CROPNAME, withFieldAsHeader(CROPNAME, vo -> null));

        map.put(ACCENAME, withFieldAsHeader(ACCENAME, vo -> String.join(";", vo.getAccessionNames())));
        map.put(ACQDATE, withFieldAsHeader(ACQDATE, vo -> vo.getAcquisitionDate()));
        map.put(ORIGCTY, withFieldAsHeader(ORIGCTY, vo -> vo.getCountryOfOriginCode()));

        // FIXME JBN uncomment this once germplasm mcpd has a collecting info
//        map.put(COLLSITE, withFieldAsHeader(COLLSITE, vo -> (vo.getCollectingInfo() == null)? null : vo.getCollectingInfo().getCollectingSite().getSiteName()));
//        map.put(DECLATITUDE, withFieldAsHeader(DECLATITUDE, vo -> (vo.getCollectingInfo() == null)? null : vo.getCollectingInfo().getCollectingSite().getLatitudeDecimal()));
//        map.put(LATITUDE, withFieldAsHeader(LATITUDE, vo -> (vo.getCollectingInfo() == null)? null : vo.getCollectingInfo().getCollectingSite().getLatitudeDegrees()));
//        map.put(DECLONGITUDE, withFieldAsHeader(DECLONGITUDE, vo -> (vo.getCollectingInfo() == null)? null : vo.getCollectingInfo().getCollectingSite().getLongitudeDecimal()));
//        map.put(LONGITUDE, withFieldAsHeader(LONGITUDE, vo -> (vo.getCollectingInfo() == null)? null : vo.getCollectingInfo().getCollectingSite().getLongitudeDegrees()));
//        map.put(COORDUNCERT, withFieldAsHeader(COORDUNCERT, vo -> (vo.getCollectingInfo() == null)? null : vo.getCollectingInfo().getCollectingSite().getCoordinateUncertainty()));
//        map.put(COORDDATUM, withFieldAsHeader(COORDDATUM, vo -> (vo.getCollectingInfo() == null)? null : vo.getCollectingInfo().getCollectingSite().getSpatialReferenceSystem()));
//        map.put(GEOREFMETH, withFieldAsHeader(GEOREFMETH, vo -> (vo.getCollectingInfo() == null)? null : vo.getCollectingInfo().getCollectingSite().getGeoreferencingMethod()));
//        map.put(ELEVATION, withFieldAsHeader(ELEVATION, vo -> (vo.getCollectingInfo() == null)? null : vo.getCollectingInfo().getCollectingSite().getElevation()));
//        map.put(COLLDATE, withFieldAsHeader(COLLDATE, vo -> (vo.getCollectingInfo() == null)? null : vo.getCollectingInfo().getCollectingDate()));
        map.put(COLLSITE, withFieldAsHeader(COLLSITE, vo ->null));
        map.put(DECLATITUDE, withFieldAsHeader(DECLATITUDE, vo ->null));
        map.put(LATITUDE, withFieldAsHeader(LATITUDE, vo ->null));
        map.put(DECLONGITUDE, withFieldAsHeader(DECLONGITUDE, vo ->null));
        map.put(LONGITUDE, withFieldAsHeader(LONGITUDE, vo ->null));
        map.put(COORDUNCERT, withFieldAsHeader(COORDUNCERT, vo ->null));
        map.put(COORDDATUM, withFieldAsHeader(COORDDATUM, vo ->null));
        map.put(GEOREFMETH, withFieldAsHeader(GEOREFMETH, vo ->null));
        map.put(ELEVATION, withFieldAsHeader(ELEVATION, vo ->null));
        map.put(COLLDATE, withFieldAsHeader(COLLDATE, vo ->null));

        // FIXME JBN uncomment this once germplasm mcpd has breedingInstitutes
//        map.put(BREDCODE, withFieldAsHeader(BREDCODE, vo -> (vo.getBreedingInstitutes() == null)? null :
//            vo.getBreedingInstitutes()
//              .stream()
//              .map(InstituteVO::getInstituteCode)
//              .collect(Collectors.joining(";"))));
//        map.put(BREDNAME, withFieldAsHeader(BREDNAME, vo -> (vo.getBreedingInstitutes() == null)? null :
//            vo.getBreedingInstitutes()
//              .stream()
//              .map(InstituteVO::getInstituteName)
//              .collect(Collectors.joining(";"))));
        map.put(BREDCODE, withFieldAsHeader(BREDCODE, vo ->null));
        map.put(BREDNAME, withFieldAsHeader(BREDNAME, vo ->null));

        // FIXME JBN uncomment this once germplasm mcpd has biologicalStatusOfAccessionCode
        // map.put(SAMPSTAT, withFieldAsHeader(SAMPSTAT, vo -> vo.getBiologicalStatusOfAccessionCode()));
        map.put(SAMPSTAT, withFieldAsHeader(SAMPSTAT, vo -> null));

        map.put(ANCEST, withFieldAsHeader(ANCEST, vo -> vo.getAncestralData()));
        map.put(COLLSRC, withFieldAsHeader(COLLSRC, vo -> vo.getAcquisitionSourceCode()));

        // FIXME JBN uncomment this once germplasm mcpd has donorInfo
//        map.put(DONORCODE, withFieldAsHeader(DONORCODE, vo -> (vo.getDonorInfo() == null )? null :
//            vo.getDonorInfo()
//              .stream()
//              .map(donorInfoVO -> donorInfoVO.getDonorInstitute().getInstituteCode())
//              .collect(Collectors.joining(";"))));
//        map.put(DONORNAME, withFieldAsHeader(DONORNAME, vo -> (vo.getDonorInfo() == null)? null :
//            vo.getDonorInfo()
//              .stream()
//              .map(donorInfoVO -> donorInfoVO.getDonorInstitute().getInstituteName())
//              .collect(Collectors.joining(";"))));
//        map.put(DONORNUMB, withFieldAsHeader(DONORNUMB, vo -> (vo.getDonorInfo() == null )? null :
//            vo.getDonorInfo()
//              .stream()
//              .map(DonorInfoVO::getDonorAccessionNumber)
//              .collect(Collectors.joining(";"))));
        map.put(DONORCODE, withFieldAsHeader(DONORCODE, null));
        map.put(DONORNAME, withFieldAsHeader(COLLSRC, vo -> null));
        map.put(DONORNUMB, withFieldAsHeader(COLLSRC, vo -> null));

        map.put(OTHERNUMB, withFieldAsHeader(OTHERNUMB, vo -> String.join(";", vo.getAlternateIDs())));
        map.put(MLSSTAT, withFieldAsHeader(MLSSTAT, vo -> vo.getMlsStatus()));
        map.put(REMARKS, withFieldAsHeader(REMARKS, vo -> vo.getRemarks()));

        this.descriptors = Collections.unmodifiableMap(map);
    }

    public void export(OutputStream out, Stream<GermplasmMcpdVO> germplasms, List<GermplasmMcpdExportableField> fields) {
        try {
            CSVWriter csvWriter = new CSVWriter(new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8)), ';', '"', '\\', "\n");
            String[] header = fields.stream()
                                    .map(descriptors::get)
                                    .map(GermplasmMcpdExportableFieldDescriptor::getHeader)
                                    .toArray(String[]::new);
            csvWriter.writeNext(header);

            germplasms.forEach(vo -> {
                String[] line =
                    fields.stream()
                          .map(descriptors::get)
                          .map(descriptor -> descriptor.export(vo))
                          .toArray(String[]::new);
                csvWriter.writeNext(line);
            });
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


