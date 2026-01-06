package fr.inrae.urgi.faidare.web.observationunit;

import static org.apache.poi.ss.util.WorkbookUtil.createSafeSheetName;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
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
        return observationUnits.stream().flatMap(
            exportedObservationUnit -> createRows(exportedObservationUnit).stream()
        ).toList();
    }

    private List<Row> createRows(ExportedObservationUnit exportedObservationUnit) {
        if (exportedObservationUnit.observations().isEmpty()) {
            return List.of();
        }

        // each year/season has its row.
        // but if there are several observation for the same variable and the same year, then it goes to a different row
        Map<String, List<Row>> rowsBySeason = new TreeMap<>();
        List<ObservationVO> observationsSortedByTimestamp =
            exportedObservationUnit
                .observations()
                .stream()
                .sorted(Comparator.comparing(ObservationVO::getObservationTimeStamp, Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();
        for (var observation : observationsSortedByTimestamp) {
            String season = observation.getSeason().getSeasonName();
            String observationVariableDbId = observation.getObservationVariableDbId();

            List<Row> rowsOfSeason = rowsBySeason.computeIfAbsent(season, ignored -> new ArrayList<>());
            Row row = rowsOfSeason
                .stream()
                .filter(r -> !r.observationsByVariableDbId.containsKey(observationVariableDbId))
                .findFirst()
                .orElseGet(() -> {
                    Row newRow = new Row(exportedObservationUnit.observationUnit(), new HashMap<>());
                    rowsOfSeason.add(newRow);
                    return newRow;
                });
            row.observationsByVariableDbId().put(observationVariableDbId, observation);
        }
        return rowsBySeason.values().stream().flatMap(List::stream).toList();
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
                row -> row.observationUnit().getObservationUnitPosition().getObservationLevel().getLevelOrder()
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
                "Study Location",
                row -> row.observationUnit().getStudyLocation()
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

        columns.add(
            new Column(
                "Season",
                Row::getSeason
            )
        );


        List<Variable> distinctVariables = getDistinctVariables(rows);

        for (Variable variable : distinctVariables) {
            columns.add(
                new Column(
                    String.format("%s(%s)", variable.name(), variable.id()),
                    row -> {
                        ObservationVO observation = row.observationsByVariableDbId.get(variable.id());
                        if (observation == null) {
                            return null;
                        }
                        return observation.getValue();
                    }
                )
            );

            columns.add(
                new Column(
                    String.format("%s(%s)_date", variable.name(), variable.id()),
                    row -> {
                        ObservationVO observation = row.observationsByVariableDbId.get(variable.id());
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

    private List<Variable> getDistinctVariables(List<Row> rows) {
        Map<String, Variable> variablesById = new HashMap<>();
        for (Row row : rows) {
            for (var observation : row.observationsByVariableDbId.values()) {
                variablesById.put(
                    observation.getObservationVariableDbId(),
                    new Variable(observation.getObservationVariableDbId(), observation.getObservationVariableName())
                );
            }
        }
        return variablesById
            .values()
            .stream()
            .sorted(Comparator.comparing(Variable::name).thenComparing(Variable::id))
            .toList();
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

    /**
     * A row, having at least one observation, and where all observations have the same year/season.
     * There is at most one observation for a given observationVariableDbId
     */
    private record Row(
        ObservationUnitV2VO observationUnit,
        Map<String, ObservationVO> observationsByVariableDbId
    ) {
        String getSeason() {
            return observationsByVariableDbId.values().iterator().next().getSeason().getSeasonName();
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

    private record Variable(String id, String name) {}
}
