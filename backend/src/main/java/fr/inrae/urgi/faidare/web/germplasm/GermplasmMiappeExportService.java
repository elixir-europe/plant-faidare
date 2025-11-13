package fr.inrae.urgi.faidare.web.germplasm;

import static org.apache.poi.ss.util.WorkbookUtil.createSafeSheetName;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.opencsv.CSVWriter;
import fr.inrae.urgi.faidare.domain.SiteVO;
import fr.inrae.urgi.faidare.domain.TaxonSourceVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.GermplasmV2VO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Component;

/**
 * Service allowing to export germplasm in MIAPPE format (Excel)
 * @author JB Nizet
 */
@Component
public class GermplasmMiappeExportService {

    private final List<GermplasmMiappeColumn> columns;

    public GermplasmMiappeExportService() {
        columns = new ArrayList<GermplasmMiappeColumn>();

        columns.add(
            new GermplasmMiappeColumn(
                "biologicalMaterialId",
                germplasm -> null
            )
        );

        columns.add(
            new GermplasmMiappeColumn(
                "biologicalMaterialExtId",
                germplasm -> null
            )
        );

        columns.add(
            new GermplasmMiappeColumn(
                "organism",
                germplasm -> {
                    TaxonSourceVO taxonSource = (germplasm.getTaxonIds() == null || germplasm.getTaxonIds().isEmpty())
                        ? null
                        : germplasm
                            .getTaxonIds()
                            .stream()
                            .filter(taxonId -> "NCBI".equals(taxonId.getSourceName()))
                            .findFirst()
                            .orElse(germplasm.getTaxonIds().getFirst());
                    if (taxonSource == null) {
                        return null;
                    } else {
                        return Stream
                            .of(taxonSource.getSourceName(), taxonSource.getTaxonId())
                            .filter(Objects::nonNull)
                            .collect(Collectors.joining(":"));
                    }
                }
            )
        );

        columns.add(
            new GermplasmMiappeColumn(
                "genus",
                GermplasmV2VO::getGenus
            )
        );

        columns.add(
            new GermplasmMiappeColumn(
                "species",
                GermplasmV2VO::getSpecies
            )
        );

        columns.add(
            new GermplasmMiappeColumn(
                "infraspecificName",
                GermplasmV2VO::getSubtaxa
            )
        );

        columns.add(
            new GermplasmMiappeColumn(
                "biologicalMaterialLatitude",
                germplasm -> {
                    SiteVO site = getFirstEvaluationSite(germplasm);
                    return site == null ? null : site.getLatitudeDecimal();
                }
            )
        );

        columns.add(
            new GermplasmMiappeColumn(
                "biologicalMaterialLongitude",
                germplasm -> {
                    SiteVO site = getFirstEvaluationSite(germplasm);
                    return site == null ? null : site.getLongitudeDecimal();
                }
            )
        );

        columns.add(
            new GermplasmMiappeColumn(
                "biologicalMaterialAltitude",
                germplasm -> {
                    SiteVO site = getFirstEvaluationSite(germplasm);
                    return site == null ? null : site.getElevation();
                }
            )
        );

        columns.add(
            new GermplasmMiappeColumn(
                "biologicalMaterialCoordUncertainty",
                germplasm -> {
                    SiteVO site = getFirstEvaluationSite(germplasm);
                    return site == null ? null : site.getCoordinateUncertainty();
                }
            )
        );

        columns.add(
            new GermplasmMiappeColumn(
                "biologicalMaterialPreprocessing",
                GermplasmV2VO::getGermplasmPreprocessing
            )
        );

        columns.add(
            new GermplasmMiappeColumn(
                "materialSourceId",
                germplasm ->
                    Stream.of(germplasm.getInstituteCode(), germplasm.getAccessionNumber())
                          .filter(Objects::nonNull)
                          .collect(Collectors.joining(":"))
            )
        );

        columns.add(
            new GermplasmMiappeColumn(
                "materialSourceDoi",
                GermplasmV2VO::getGermplasmPUI
            )
        );

        columns.add(
            new GermplasmMiappeColumn(
                "materialSourceAccNumber",
                GermplasmV2VO::getAccessionNumber
            )
        );

        columns.add(
            new GermplasmMiappeColumn(
                "materialSourceAccName",
                GermplasmV2VO::getGermplasmName
            )
        );

        columns.add(
            new GermplasmMiappeColumn(
                "materialSourceInstCode",
                GermplasmV2VO::getInstituteCode
            )
        );

        columns.add(
            new GermplasmMiappeColumn(
                "materialSourceInstName",
                GermplasmV2VO::getInstituteName
            )
        );

        columns.add(
            new GermplasmMiappeColumn(
                "materialSourceOtherIds",
                germplasm -> null
            )
        );

        columns.add(
            new GermplasmMiappeColumn(
                "materialSourceLatitude",
                germplasm -> {
                    SiteVO collectingSite = germplasm.getCollectingSite();
                    return collectingSite == null ? null : collectingSite.getLatitudeDecimal();
                }
            )
        );

        columns.add(
            new GermplasmMiappeColumn(
                "materialSourceLongitude",
                germplasm -> {
                    SiteVO collectingSite = germplasm.getCollectingSite();
                    return collectingSite == null ? null : collectingSite.getLongitudeDecimal();
                }
            )
        );

        columns.add(
            new GermplasmMiappeColumn(
                "materialSourceAltitude",
                germplasm -> {
                    SiteVO collectingSite = germplasm.getCollectingSite();
                    return collectingSite == null ? null : collectingSite.getElevation();
                }
            )
        );

        columns.add(
            new GermplasmMiappeColumn(
                "materialSourceCoordUncertainty",
                germplasm -> {
                    SiteVO collectingSite = germplasm.getCollectingSite();
                    return collectingSite == null ? null : collectingSite.getCoordinateUncertainty();
                }
            )
        );

        columns.add(
            new GermplasmMiappeColumn(
                "materialSourceDesc",
                GermplasmV2VO::getSeedSourceDescription
            )
        );
    }

    public void exportAsExcel(OutputStream out, Stream<GermplasmV2VO> germplasms) {
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(100)) {
            String safeSheetName = createSafeSheetName("Germplasms");
            SXSSFSheet sheet = workbook.createSheet(safeSheetName);

            AtomicInteger rowNum = new AtomicInteger(0);
            SXSSFRow headerRow = sheet.createRow(rowNum.getAndIncrement());
            writeHeaders(headerRow);

            germplasms.forEach(germplasm -> {
                SXSSFRow row = sheet.createRow(rowNum.getAndIncrement());
                writeGermplasm(germplasm, row);
            });

            for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
                sheet.autoSizeColumn(columnIndex);
            }

            workbook.write(out);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void exportAsCsv(OutputStream out, Stream<GermplasmV2VO> germplasms) {
        try {
            CSVWriter csvWriter = new CSVWriter(new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8)), ';', '"', '\\', "\n");
            String[] header = columns
                .stream()
                .map(GermplasmMiappeColumn::getHeader)
                .toArray(String[]::new);
            csvWriter.writeNext(header);

            germplasms.forEach(germplasm -> {
                String[] line = columns
                    .stream()
                    .map(column -> column.csvValue(germplasm))
                    .toArray(String[]::new);
                csvWriter.writeNext(line);
            });
            csvWriter.flush();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void writeHeaders(SXSSFRow row) {
        row.getSheet().trackAllColumnsForAutoSizing();
        CellStyle headerStyle = createHeaderStyle(row.getSheet().getWorkbook());
        int columnIndex = 0;
        for (GermplasmMiappeColumn column : columns) {
            Cell cell = row.createCell(columnIndex, CellType.STRING);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(column.getHeader());
            columnIndex++;
        }
        // lock the header row so that it's always visible
        row.getSheet().createFreezePane(0, 1);
    }

    private void writeGermplasm(GermplasmV2VO germplasm, SXSSFRow row) {
        int columnIndex = 0;
        for (GermplasmMiappeColumn column : columns) {
            column.cell(row, columnIndex, germplasm);
            columnIndex++;
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);
        return headerStyle;
    }

    private static class GermplasmMiappeColumn {
        private final String header;
        private final CellProducer cellProducer;
        private final Function<GermplasmV2VO, String> csvValueProducer;

        public GermplasmMiappeColumn(
            String header,
            CellProducer cellProducer,
            Function<GermplasmV2VO, String> csvValueProducer
        ) {
            this.header = header;
            this.cellProducer = cellProducer;
            this.csvValueProducer = csvValueProducer;
        }

        public GermplasmMiappeColumn(
            String header,
            Function<GermplasmV2VO, String> csvValueProducer
        ) {
            this.header = header;
            this.csvValueProducer = csvValueProducer;
            this.cellProducer = (row, columnIndex, germplasm) -> {
                Cell cell = row.createCell(columnIndex, CellType.STRING);
                cell.setCellValue(csvValueProducer.apply(germplasm));
                return cell;
            };
        }

        public String getHeader() {
            return this.header;
        }

        public Cell cell(SXSSFRow row, int columnIndex, GermplasmV2VO germplasm) {
            return this.cellProducer.createCell(row, columnIndex, germplasm);
        }

        public String csvValue(GermplasmV2VO germplasm) {
            return this.csvValueProducer.apply(germplasm);
        }
    }

    @FunctionalInterface
    private interface CellProducer {
        Cell createCell(SXSSFRow row, int columnIndex, GermplasmV2VO germplasm);
    }

    private SiteVO getFirstEvaluationSite(GermplasmV2VO germplasm) {
        return (germplasm.getEvaluationSites() == null || germplasm.getEvaluationSites().isEmpty())
            ? null
            : germplasm.getEvaluationSites().getFirst();
    }
}


