package fr.inrae.urgi.faidare.web.germplasm;

import com.opencsv.CSVWriter;
import fr.inrae.urgi.faidare.domain.CollPopVO;
import fr.inrae.urgi.faidare.domain.brapi.v1.GermplasmV1VO;
import fr.inrae.urgi.faidare.domain.brapi.v2.GermplasmV2VO;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static fr.inrae.urgi.faidare.web.germplasm.GermplasmExportableField.*;

/**
 * Service allowing to export germplasms as CSV
 * @author JB Nizet
 */
@Component
public class GermplasmExportService {

    private final Map<GermplasmExportableField, GermplasmExportableFieldDescriptor> descriptors;

    public GermplasmExportService() {
        Map<GermplasmExportableField, GermplasmExportableFieldDescriptor> map = new HashMap<>();

        map.put(DOI, new GermplasmExportableFieldDescriptor("DOI", vo -> vo.getGermplasmPUI()));
        map.put(ACCESSION_NUMBER, new GermplasmExportableFieldDescriptor("Accession number", vo -> vo.getAccessionNumber()));
        map.put(ACCESSION_NAME, new GermplasmExportableFieldDescriptor("Accession name", vo -> vo.getGermplasmName()));
        map.put(TAXON_GROUP, new GermplasmExportableFieldDescriptor("Taxon group", vo -> vo.getCommonCropName()));
        map.put(HOLDING_INSTITUTION, new GermplasmExportableFieldDescriptor("Holding institution", vo -> vo.getInstituteName()));
        map.put(LOT_NAME, new GermplasmExportableFieldDescriptor("Lot name", vo -> null));
        map.put(LOT_SYNONYM, new GermplasmExportableFieldDescriptor("Lot synonym", vo -> null));
        map.put(COLLECTION_NAME, new GermplasmExportableFieldDescriptor("Collection name", vo -> (vo.getCollection() == null ) ? null : vo.getCollection().stream().map(
            CollPopVO::getName).collect(Collectors.joining(", "))));
        map.put(COLLECTION_TYPE, new GermplasmExportableFieldDescriptor("Collection type", vo -> null));
        map.put(PANEL_NAME, new GermplasmExportableFieldDescriptor("Panel name", vo -> (vo.getPanel() == null)? null : vo.getPanel().stream().map(CollPopVO::getName).collect(
            Collectors.joining(", "))));
        map.put(PANEL_SIZE, new GermplasmExportableFieldDescriptor("Panel size", vo -> null));

        this.descriptors = Collections.unmodifiableMap(map);
        if (map.size() != GermplasmExportableField.values().length) {
            throw new IllegalStateException("Missing field descriptor");
        }
    }

    public void export(OutputStream out, Stream<GermplasmV2VO> germplasms, List<GermplasmExportableField> fields) {
        try {
            CSVWriter csvWriter = new CSVWriter(new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8)), ';', '"', '\\', "\n");
            String[] header = fields.stream()
                                    .map(descriptors::get)
                                    .map(GermplasmExportableFieldDescriptor::getHeader)
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

    private static class GermplasmExportableFieldDescriptor {
        private final String header;
        private final Function<GermplasmV2VO, String> exporter;

        public GermplasmExportableFieldDescriptor(String header,
                                                  Function<GermplasmV2VO, String> exporter) {
            this.header = header;
            this.exporter = exporter;
        }

        public String getHeader() {
            return this.header;
        }

        public String export(GermplasmV2VO germplasm) {
            return this.exporter.apply(germplasm);
        }
    }
}


