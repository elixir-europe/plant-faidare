package fr.inrae.urgi.faidare.web.observationunit;

import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicReference;

import fr.inrae.urgi.faidare.web.germplasm.ExportFormat;

/**
 * An observation unit export job, containing the status of the job, and the generated file
 * when done.
 * It's mutable, stored in a cache by {@link ObservationUnitExportJobService}, and accessed
 * concurrently, hence the synchronization to make it thread-safe.
 * @author JB Nizet
 */
public final class ObservationUnitExportJob {

    private final String id;
    private final ExportFormat format;
    private Status status = Status.RUNNING;
    private Path file;

    public ObservationUnitExportJob(String id, ExportFormat format) {
        this.id = id;
        this.format = format;
    }

    public String getId() {
        return id;
    }

    public ExportFormat getFormat() {
        return format;
    }

    public synchronized Status getStatus() {
        return this.status;
    }

    public synchronized Path getFile() {
        return file;
    }

    public synchronized void done(Path file) {
        this.status = Status.DONE;
        this.file = file;
    }

    public synchronized void fail() {
        this.status = Status.FAILED;
    }

    public enum Status {
        RUNNING,
        DONE,
        FAILED
    }
}
