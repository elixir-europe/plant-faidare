package fr.inrae.urgi.faidare.web.observation;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import fr.inrae.urgi.faidare.web.germplasm.ExportFormat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Unit tests for {@link ObservationExportDirectoryCleaner}
 * @author JB Nizet
 */
class ObservationExportDirectoryCleanerTest {
    @TempDir
    private Path exportDirectory;

    @Test
    void shouldCleanupExportedFiles() throws IOException {
        Files.createFile(exportDirectory.resolve(Path.of("foo." + ExportFormat.CSV.getExtension())));
        Files.createFile(exportDirectory.resolve(Path.of("bar." + ExportFormat.EXCEL.getExtension())));
        Files.createFile(exportDirectory.resolve("README.md"));
        Files.createDirectory(exportDirectory.resolve("subdir"));

        ObservationExportProperties properties = new ObservationExportProperties(exportDirectory);
        ObservationExportDirectoryCleaner cleaner = new ObservationExportDirectoryCleaner(properties);
        cleaner.run();

        List<String> remainingFileNames = Files.list(exportDirectory).map(file -> file.getFileName().toString()).toList();
        assertThat(remainingFileNames).containsOnly("README.md", "subdir");
    }
}
