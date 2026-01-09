package fr.inrae.urgi.faidare.web.observationunit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import fr.inrae.urgi.faidare.web.germplasm.ExportFormat;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * A component that cleans the observation unit export directory of leftover
 * files at startup.
 * These files might exist because the server was shut down (or crashed) during
 * the 1 hour interval between the last access to the job and its removal.
 * @author JB Nizet
 */
@Component
public class ObservationUnitExportDirectoryCleaner implements CommandLineRunner {

    private final ObservationUnitExportProperties properties;

    public ObservationUnitExportDirectoryCleaner(ObservationUnitExportProperties properties) {
        this.properties = properties;
    }

    @Override
    public void run(String... args) {
        Path directory = properties.directory();
        try {
            Files.list(directory)
                 .filter(this::shouldDelete)
                 .forEach(file -> {
                     try {
                         Files.delete(file);
                     } catch (IOException e) {
                         // too bad
                     }
                 });
        } catch (IOException e) {
            // too bad
        }
    }

    private boolean shouldDelete(Path file) {
        if (!Files.isRegularFile(file)) {
            return false;
        }
        String fileName = file.getFileName().toString();
        return Arrays.stream(ExportFormat.values()).anyMatch(
            format -> fileName.endsWith("." + format.getExtension())
        );
    }
}
