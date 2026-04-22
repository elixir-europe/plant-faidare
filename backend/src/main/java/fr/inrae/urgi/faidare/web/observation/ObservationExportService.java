package fr.inrae.urgi.faidare.web.observation;

import static org.apache.poi.ss.util.WorkbookUtil.createSafeSheetName;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.opencsv.CSVWriter;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.ObservationUnitV2VO;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.ObservationVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.TreatmentVO;
import fr.inrae.urgi.faidare.web.germplasm.ExportFormat;
import fr.inrae.urgi.faidare.web.observation.json.JsonObservationExport;
import fr.inrae.urgi.faidare.web.observation.json.JsonObservationVariable;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

/**
 * Service allowing to export observations as Excel and CSV
 * @author JB Nizet
 */
@Component
public class ObservationExportService {

    private final ObjectMapper objectMapper;

    public ObservationExportService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void exportAsExcel(OutputStream out, List<ExportedObservationUnit> observationUnits) {
        List<Row> rows = createRows(observationUnits);
        List<Column> columns = createColumns(rows, ExportFormat.EXCEL);

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
        List<Column> columns = createColumns(rows, ExportFormat.CSV);

        try {
            CSVWriter csvWriter = new CSVWriter(new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8)), ';', '"', '\\', "\n");
            String[] header = columns.stream()
                                     .map(column -> column.getHeader().label())
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

    public void exportAsJson(OutputStream out, List<ExportedObservationUnit> observationUnits) {
        List<Row> rows = createJsonRows(observationUnits);
        List<Column> columns = createColumns(rows, ExportFormat.JSON);

        List<String> headerRow =
            columns
                .stream()
                .filter(column -> column.getHeader().jsonLabel() != null)
                .map(column -> column.getHeader().jsonLabel())
                .toList();

        List<JsonObservationVariable> observationVariables =
            getDistinctVariables(rows)
                .stream()
                .map(v -> new JsonObservationVariable(v.id(), v.name()))
                .toList();

        List<List<String>> data =
            rows
                .stream()
                .map(row ->
                    columns
                        .stream()
                        .map(column -> column.jsonValue(row))
                        .collect(Collectors.toList())
                )
                .toList();

        JsonObservationExport export = new JsonObservationExport(
            headerRow,
            observationVariables,
            data
        );

        objectMapper.writeValue(out, export);
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

        // each year/season + GDD has its row.
        // but if there are several observations for the same variable and the
        // same year + GDD, then the duplicates go to a different row
        Map<SeasonAndGdd, List<Row>> rowsBySeasonAndGdd = new TreeMap<>();
        List<ObservationVO> observationsSortedByTimestamp =
            exportedObservationUnit
                .observations()
                .stream()
                .sorted(Comparator.comparing(ObservationVO::getObservationTimeStamp, Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();
        for (var observation : observationsSortedByTimestamp) {
            String season = observation.getSeason().getSeasonName();
            Float gdd = observation.getGdd();
            SeasonAndGdd seasonAndGdd = new SeasonAndGdd(season, gdd);
            String observationVariableDbId = observation.getObservationVariableDbId();

            List<Row> rowsOfSeasonAndGdd = rowsBySeasonAndGdd.computeIfAbsent(seasonAndGdd, ignored -> new ArrayList<>());
            Row row = rowsOfSeasonAndGdd
                .stream()
                .filter(r -> !r.observationsByVariableDbId.containsKey(observationVariableDbId))
                .findFirst()
                .orElseGet(() -> {
                    Row newRow = new Row(exportedObservationUnit.observationUnit(), new HashMap<>());
                    rowsOfSeasonAndGdd.add(newRow);
                    return newRow;
                });
            row.observationsByVariableDbId().put(observationVariableDbId, observation);
        }
        return rowsBySeasonAndGdd.values().stream().flatMap(List::stream).toList();
    }

    private List<Row> createJsonRows(List<ExportedObservationUnit> observationUnits) {
        return observationUnits.stream().flatMap(
            exportedObservationUnit -> createJsonRows(exportedObservationUnit).stream()
        ).toList();
    }

    private List<Row> createJsonRows(ExportedObservationUnit exportedObservationUnit) {
        if (exportedObservationUnit.observations().isEmpty()) {
            return List.of();
        }

        // each observation has its row.
        return exportedObservationUnit
            .observations()
            .stream()
            .sorted(Comparator.comparing(ObservationVO::getObservationTimeStamp, Comparator.nullsLast(Comparator.naturalOrder())))
            .map(observation -> new Row(exportedObservationUnit.observationUnit(), Map.of(observation.getObservationVariableDbId(), observation)))
            .toList();
    }

    private List<Column> createColumns(List<Row> rows, ExportFormat exportFormat) {
        List<Column> columns = new ArrayList<>();

        columns.add(
            new Column(
                new Header("obsUnitId", "observationUnitDbId"),
                row -> decode(row.observationUnit().getObservationUnitDbId())
            )
        );

        columns.add(
            new Column(
                new Header("observationUnitName", "observationUnitName"),
                row -> row.observationUnit().getObservationUnitName()
            )
        );


        columns.add(
            new Column(
                new Header("obsUnitType", "observationLevel"),
                row -> row.observationUnit().getObservationUnitPosition().getObservationLevel().getLevelOrder()
            )
        );

        columns.add(
            new Column(
                new Header("germplasmId", "germplasmDbId"),
                row -> decode(row.observationUnit().getGermplasmDbId())
            )
        );

        columns.add(
            new Column(
                new Header("germplasmName", "germplasmName"),
                row -> row.observationUnit().getGermplasmName()
            )
        );


        columns.add(
            new Column(
                new Header("genus", "germplasmGenus"),
                row -> row.observationUnit().getGermplasmGenus()
            )
        );

        columns.add(
            new Column(
                new Header("trialName", "trialName"),
                row -> row.observationUnit().getTrialName()
            )
        );

        columns.add(
            new Column(
                new Header("studyId", "studyDbId"),
                row -> decode(row.observationUnit().getStudyDbId())
            )
        );

        columns.add(
            new Column(
                new Header("studyName", "studyName"),
                row -> row.observationUnit().getStudyName()
            )
        );

        columns.add(
            new Column(
                new Header("siteName", "studyLocation"),
                row -> row.observationUnit().getStudyLocation()
            )
        );

        List<String> distinctTreatmentFactors =
            rows.stream()
                .flatMap(row ->
                             row.observationUnit().getTreatments() == null
                                 ? Stream.empty()
                                 : row.observationUnit().getTreatments().stream()
                )
                .map(TreatmentVO::getFactor)
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .toList();

        for (String treatmentFactor: distinctTreatmentFactors) {
            columns.add(
                new Column(
                    new Header(treatmentFactor, treatmentFactor),
                    row -> row.observationUnit()
                              .getTreatments()
                              .stream()
                              .filter(treatment -> treatmentFactor.equals(treatment.getFactor()))
                              .map(TreatmentVO::getModality)
                              .findAny()
                              .orElse(null)
                )
            );
        }

        columns.add(
            new Column(
                new Header("season", "year"),
                Row::getSeason
            )
        );

        columns.add(
            new Column(
                new Header("gdd", "gdd"),
                Row::getGdd
            )
        );

        if (exportFormat == ExportFormat.JSON) {
            columns.add(
                new Column(
                    new Header(null, "observationTimeStamp"),
                    Row::getFirstObservationTimeStamp
                )
            );
        }

        List<Variable> distinctVariables = getDistinctVariables(rows);

        for (Variable variable : distinctVariables) {
            columns.add(
                new Column(
                    new Header(String.format("%s(%s)", variable.name(), variable.id()), null),
                    row -> {
                        ObservationVO observation = row.observationsByVariableDbId.get(variable.id());
                        if (observation == null) {
                            return null;
                        }
                        return observation.getValue();
                    }
                )
            );

            if (exportFormat != ExportFormat.JSON) {
                columns.add(
                    new Column(
                        new Header(String.format("%s(%s)_date", variable.name(), variable.id()), null),
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
            cell.setCellValue(column.getHeader().label());
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

    private String decode(String value) {
        if (value == null) {
            return null;
        }
        return new String(Base64.getDecoder().decode(value), StandardCharsets.UTF_8);
    }

    /**
     * A row, having at least one observation, and where all observations have the same year/season and GDD.
     * There is at most one observation for a given observationVariableDbId.
     * JSON rows have only one observation in the map.
     */
    private record Row(
        ObservationUnitV2VO observationUnit,
        Map<String, ObservationVO> observationsByVariableDbId
    ) {
        String getSeason() {
            return observationsByVariableDbId.values().iterator().next().getSeason().getSeasonName();
        }

        String getGdd() {
            Float gdd = observationsByVariableDbId.values().iterator().next().getGdd();
            return gdd == null ? null : gdd.toString();
        }

        String getFirstObservationTimeStamp() {
            return observationsByVariableDbId.values().iterator().next().getObservationTimeStamp();
        }
    }

    private static class Column {
        /**
         * The header, or null if it's a JSON column for an observation
         * variable which should thus not be included in the headerRow
         */
        private final Header header;
        private final CellProducer cellProducer;
        private final Function<Row, String> csvValueProducer;
        private final Function<Row, String> jsonValueProducer;

        public Column(
            Header header,
            CellProducer cellProducer,
            Function<Row, String> csvValueProducer,
            Function<Row, String> jsonValueProducer
        ) {
            this.header = header;
            this.cellProducer = cellProducer;
            this.csvValueProducer = csvValueProducer;
            this.jsonValueProducer = jsonValueProducer;
        }

        public Column(
            Header header,
            Function<Row, String> stringValueProducer
        ) {
            this.header = header;
            this.csvValueProducer = stringValueProducer;
            this.jsonValueProducer = stringValueProducer;
            this.cellProducer = (excelRow, columnIndex, row) -> {
                Cell cell = excelRow.createCell(columnIndex, CellType.STRING);
                cell.setCellValue(csvValueProducer.apply(row));
                return cell;
            };
        }

        public Header getHeader() {
            return this.header;
        }

        public String csvValue(Row row) {
            return this.csvValueProducer.apply(row);
        }

        public String jsonValue(Row row) {
            return this.jsonValueProducer.apply(row);
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

    private record SeasonAndGdd(String season, Float gdd) implements Comparable<SeasonAndGdd> {
        private static final Comparator<SeasonAndGdd> COMPARATOR =
            Comparator.comparing(SeasonAndGdd::season)
                      .thenComparing(SeasonAndGdd::gdd, Comparator.nullsFirst(Comparator.naturalOrder()));

        @Override
        public int compareTo(SeasonAndGdd o) {
            return COMPARATOR.compare(this, o);
        }
    }

    /**
     * A column header. The label is used for CSV and Excel. The jsonLabel is used
     * for JSON.
     * If the json label is null, then this column must not be part of the headerRow
     * of the JSON export.
     * @param label The header used for CSV and Excel
     * @param jsonLabel The header used for JSON.
     *                  If null, then this column must not be part of the headerRow of the JSON export.
     *
     * @author JB Nizet
     */
    private record Header(
        String label,
        String jsonLabel
    ) {}
}
