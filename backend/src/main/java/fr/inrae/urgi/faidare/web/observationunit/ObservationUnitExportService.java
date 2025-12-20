package fr.inrae.urgi.faidare.web.observationunit;

import static org.apache.poi.ss.util.WorkbookUtil.createSafeSheetName;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.opencsv.CSVWriter;
import fr.inrae.urgi.faidare.dao.v2.ObservationUnitV2Dao;
import fr.inrae.urgi.faidare.domain.brapi.v2.GermplasmV2VO;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.ObservationUnitV2VO;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.ObservationVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.TreatmentVO;
import fr.inrae.urgi.faidare.web.germplasm.GermplasmMiappeExportService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Service allowing to export observation units as Excel and CSV
 * @author JB Nizet
 */
@Component
public class ObservationUnitExportService {

    public void exportAsExcel(OutputStream out, List<ExportedObservationUnit> observationUnits) {
        List<Row> rows = createRows(observationUnits);
        List<Column> columns = createColumns(rows);

        try (SXSSFWorkbook workbook = new SXSSFWorkbook(100)) {
            String safeSheetName = createSafeSheetName("Observation units");
            SXSSFSheet sheet = workbook.createSheet(safeSheetName);

            AtomicInteger rowNum = new AtomicInteger(0);
            SXSSFRow headerRow = sheet.createRow(rowNum.getAndIncrement());
            writeHeaders(headerRow, columns);

            rows.forEach(row -> {
                SXSSFRow excelRow = sheet.createRow(rowNum.getAndIncrement());
                writeRow(row, columns, excelRow);
            });

            for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
                sheet.autoSizeColumn(columnIndex);
            }

            workbook.write(out);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void exportAsCsv(OutputStream out, List<ExportedObservationUnit> observationUnits) {
        List<Row> rows = createRows(observationUnits);
        List<Column> columns = createColumns(rows);

        try {
            CSVWriter csvWriter = new CSVWriter(new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8)), ';', '"', '\\', "\n");
            String[] header = columns.stream()
                                     .map(Column::getHeader)
                                     .toArray(String[]::new);
            csvWriter.writeNext(header);

            rows.forEach(row -> {
                String[] line =
                    columns.stream()
                           .map(column -> column.csvValue(row))
                           .toArray(String[]::new);
                csvWriter.writeNext(line);
            });
            csvWriter.flush();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private List<Row> createRows(List<ExportedObservationUnit> observationUnits) {
        return observationUnits.stream().map(Row::new).toList();
    }

    private List<Column> createColumns(List<Row> rows) {
        List<Column> columns = new ArrayList<>();

        columns.add(
            new Column(
                "Observation Unit ID",
                row -> row.observationUnit().getObservationUnitDbId()
            )
        );

        columns.add(
            new Column(
                "Observation Unit Name",
                row -> row.observationUnit().getObservationUnitName()
            )
        );

        columns.add(
            new Column(
                "Observation Level",
                row -> row.observationUnit().getObservationUnitPosition().getObservationLevel().getLevelName()
            )
        );

        columns.add(
            new Column(
                "Germplasm Name",
                row -> row.observationUnit().getGermplasmName()
            )
        );

        columns.add(
            new Column(
                "Germplasm Genus",
                row -> row.observationUnit().getGermplasmGenus()
            )
        );

        columns.add(
            new Column(
                "Trial Name",
                row -> row.observationUnit().getTrialName()
            )
        );

        columns.add(
            new Column(
                "Study Name",
                row -> row.observationUnit().getStudyName()
            )
        );

        columns.add(
            new Column(
                "Treatments",
                row -> row.observationUnit()
                          .getTreatments()
                          .stream()
                          .map(TreatmentVO::getModality)
                          .filter(Objects::nonNull)
                          .collect(Collectors.joining(", "))
            )
        );

        // TODO add season (column K), but where does it come from?
        // TODO add column L, but where does it come from and why isn't it repeated?

        List<String> distinctObservationVariableDbIds =
            rows.stream()
                .flatMap(row -> row.observationsByVariableDbId().keySet().stream())
                .distinct()
                .sorted()
                .toList();

        for (String observationVariableDbId : distinctObservationVariableDbIds) {
            columns.add(
                new Column(
                    "Observation Variable Name (ID)",
                    row -> {
                        ObservationVO observation = row.observationsByVariableDbId.get(observationVariableDbId);
                        if (observation == null) {
                            return null;
                        }
                        return String.format("%s (%s)", observation.getObservationVariableName(), observationVariableDbId);
                    }
                )
            );

            columns.add(
                new Column(
                    "Value",
                    row -> {
                        ObservationVO observation = row.observationsByVariableDbId.get(observationVariableDbId);
                        if (observation == null) {
                            return null;
                        }
                        return observation.getValue();
                    }
                )
            );

            // TODO missing column (O), but I have no idea what I should put there.

            // TODO what should be the header name and the value?
            columns.add(
                new Column(
                    "Timestamp",
                    row -> {
                        ObservationVO observation = row.observationsByVariableDbId.get(observationVariableDbId);
                        if (observation == null) {
                            return null;
                        }
                        return observation.getObservationTimeStamp();
                    }
                )
            );
        }

        return columns;
    }

    private void writeHeaders(SXSSFRow row, List<Column> columns) {
        row.getSheet().trackAllColumnsForAutoSizing();
        CellStyle headerStyle = createHeaderStyle(row.getSheet().getWorkbook());
        int columnIndex = 0;
        for (Column column : columns) {
            Cell cell = row.createCell(columnIndex, CellType.STRING);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(column.getHeader());
            columnIndex++;
        }
        // lock the header row so that it's always visible
        row.getSheet().createFreezePane(0, 1);
    }

    private void writeRow(Row row, List<Column> columns, SXSSFRow excelRow) {
        int columnIndex = 0;
        for (Column column : columns) {
            column.cell(excelRow, columnIndex, row);
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

    private record Row(
        ObservationUnitV2VO observationUnit,
        Map<String, ObservationVO> observationsByVariableDbId
    ) {

        Row(ExportedObservationUnit exportedObservationUnit) {
            this(
                exportedObservationUnit.observationUnit(),
                exportedObservationUnit
                    .observations()
                    .stream()
                    .collect(
                        Collectors.toMap(
                            ObservationVO::getObservationVariableDbId,
                            Function.identity()
                        )
                    )
            );
        }
    }

    private static class Column {
        private final String header;
        private final CellProducer cellProducer;
        private final Function<Row, String> csvValueProducer;

        public Column(
            String header,
            CellProducer cellProducer,
            Function<Row, String> csvValueProducer
        ) {
            this.header = header;
            this.cellProducer = cellProducer;
            this.csvValueProducer = csvValueProducer;
        }

        public Column(
            String header,
            Function<Row, String> csvValueProducer
        ) {
            this.header = header;
            this.csvValueProducer = csvValueProducer;
            this.cellProducer = (excelRow, columnIndex, row) -> {
                Cell cell = excelRow.createCell(columnIndex, CellType.STRING);
                cell.setCellValue(csvValueProducer.apply(row));
                return cell;
            };
        }

        public String getHeader() {
            return this.header;
        }

        public String csvValue(Row row) {
            return this.csvValueProducer.apply(row);
        }

        public Cell cell(SXSSFRow excelRow, int columnIndex, Row row) {
            return this.cellProducer.createCell(excelRow, columnIndex, row);
        }
    }

    @FunctionalInterface
    private interface CellProducer {
        Cell createCell(SXSSFRow excelRow, int columnIndex, Row row);
    }
}
