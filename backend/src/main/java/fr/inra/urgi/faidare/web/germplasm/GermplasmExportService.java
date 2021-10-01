package fr.inra.urgi.faidare.web.germplasm;

import static fr.inra.urgi.faidare.web.germplasm.GermplasmExportableField.*;

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
import fr.inra.urgi.faidare.domain.data.germplasm.CollPopVO;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmMcpdVO;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmVO;
import org.springframework.stereotype.Component;

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
        map.put(COLLECTION_NAME, new GermplasmExportableFieldDescriptor("Collection name", vo -> vo.getCollection().stream().map(
            CollPopVO::getName).collect(Collectors.joining(", "))));
        map.put(COLLECTION_TYPE, new GermplasmExportableFieldDescriptor("Collection type", vo -> null));
        map.put(PANEL_NAME, new GermplasmExportableFieldDescriptor("Panel name", vo -> vo.getPanel().stream().map(CollPopVO::getName).collect(
            Collectors.joining(", "))));
        map.put(PANEL_SIZE, new GermplasmExportableFieldDescriptor("Panel size", vo -> null));

        this.descriptors = Collections.unmodifiableMap(map);
        if (map.size() != GermplasmExportableField.values().length) {
            throw new IllegalStateException("Missing field descriptor");
        }
    }

    public void export(OutputStream out, Iterator<GermplasmVO> germplasms, List<GermplasmExportableField> fields) {
        try {
            CSVWriter csvWriter = new CSVWriter(new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8)), ';', '"', '\\', "\n");
            String[] header = fields.stream()
                                    .map(descriptors::get)
                                    .map(GermplasmExportableFieldDescriptor::getHeader)
                                    .toArray(String[]::new);
            csvWriter.writeNext(header);

            while (germplasms.hasNext()) {
                GermplasmVO vo = germplasms.next();
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

    private static class GermplasmExportableFieldDescriptor {
        private final String header;
        private final Function<GermplasmVO, String> exporter;

        public GermplasmExportableFieldDescriptor(String header,
                                                  Function<GermplasmVO, String> exporter) {
            this.header = header;
            this.exporter = exporter;
        }

        public String getHeader() {
            return this.header;
        }

        public String export(GermplasmVO germplasm) {
            return this.exporter.apply(germplasm);
        }
    }
}


