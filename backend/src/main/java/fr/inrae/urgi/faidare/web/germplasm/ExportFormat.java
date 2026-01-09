package fr.inrae.urgi.faidare.web.germplasm;

import org.springframework.http.MediaType;

/**
 * An export format
 * @author JB Nizet
 */
public enum ExportFormat {
    CSV("text/csv", "csv"),
    EXCEL("application/vnd.openxmlformatsofficedocument.spreadsheetml.sheet", "xlsx"),
    JSON("application/json", "json");

    private final String contentType;
    private final MediaType mediaType;
    private final String extension;

    ExportFormat(String contentType, String extension) {
        this.contentType = contentType;
        this.mediaType = MediaType.parseMediaType(contentType);
        this.extension = extension;
    }

    public String getContentType() {
        return contentType;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public String getExtension() {
        return extension;
    }
}
