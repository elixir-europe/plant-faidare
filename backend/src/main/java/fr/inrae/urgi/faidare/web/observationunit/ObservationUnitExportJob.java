package fr.inrae.urgi.faidare.web.observationunit;

import java.nio.file.Path;

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
    private final Path file;
    private Status status = Status.RUNNING;

    public ObservationUnitExportJob(String id, ExportFormat format, Path file) {
        this.id = id;
        this.format = format;
        this.file = file;
    }

    public String getId() {
        return id;
    }

    public ExportFormat getFormat() {
        return format;
    }

    public Path getFile() {
        return file;
    }

    public synchronized Status getStatus() {
        return this.status;
    }

    public synchronized void done() {
        this.status = Status.DONE;
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
