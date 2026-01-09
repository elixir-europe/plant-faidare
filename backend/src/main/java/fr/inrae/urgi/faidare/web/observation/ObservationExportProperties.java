package fr.inrae.urgi.faidare.web.observation;

import java.nio.file.Files;
import java.nio.file.Path;

import jakarta.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Properties for observation exports
 * @author JB Nizet
 *
 * @param directory The directory where observation exports are generated before being downloaded.
 *                  At startup, all files in this directory that ends with an export fomat extension
 *                  (.csv or .xlsx) are deleted.
 */
@Validated
@ConfigurationProperties(prefix = "faidare.observation-export")
public record ObservationExportProperties(
    @NotNull Path directory
) {
    public ObservationExportProperties {
        if (!Files.exists(directory)) {
            throw new IllegalArgumentException("The configured directory (" + directory.toAbsolutePath() + ") does not exist");
        }
        if (!Files.isDirectory(directory)) {
            throw new IllegalArgumentException("The configured directory (" + directory.toAbsolutePath() + ") is not a directory");
        }
        if (!Files.isWritable(directory)) {
            throw new IllegalArgumentException("The configured directory (" + directory.toAbsolutePath() + ") is not writable");
        }
    }
}
