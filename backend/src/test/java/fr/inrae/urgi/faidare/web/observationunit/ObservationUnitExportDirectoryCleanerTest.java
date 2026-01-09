package fr.inrae.urgi.faidare.web.observationunit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import fr.inrae.urgi.faidare.web.germplasm.ExportFormat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Unit tests for {@link ObservationUnitExportDirectoryCleaner}
 * @author JB Nizet
 */
class ObservationUnitExportDirectoryCleanerTest {
    @TempDir
    private Path exportDirectory;

    @Test
    void shouldCleanupExportedFiles() throws IOException {
        Files.createFile(exportDirectory.resolve(Path.of("foo." + ExportFormat.CSV.getExtension())));
        Files.createFile(exportDirectory.resolve(Path.of("bar." + ExportFormat.EXCEL.getExtension())));
        Files.createFile(exportDirectory.resolve("README.md"));
        Files.createDirectory(exportDirectory.resolve("subdir"));

        ObservationUnitExportProperties properties = new ObservationUnitExportProperties(exportDirectory);
        ObservationUnitExportDirectoryCleaner cleaner = new ObservationUnitExportDirectoryCleaner(properties);
        cleaner.run();

        List<String> remainingFileNames = Files.list(exportDirectory).map(file -> file.getFileName().toString()).toList();
        assertThat(remainingFileNames).containsOnly("README.md", "subdir");
    }
}
