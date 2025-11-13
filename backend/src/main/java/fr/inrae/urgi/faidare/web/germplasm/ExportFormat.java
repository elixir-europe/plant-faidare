package fr.inrae.urgi.faidare.web.germplasm;

import org.springframework.http.MediaType;

/**
 * An export format
 * @author JB Nizet
 */
public enum ExportFormat {
    CSV("text/csv"),
    EXCEL("application/vnd.openxmlformatsofficedocument.spreadsheetml.sheet");

    private final String contentType;
    private final MediaType mediaType;

    ExportFormat(String contentType) {
        this.contentType = contentType;
        this.mediaType = MediaType.parseMediaType(contentType);
    }

    public String getContentType() {
        return contentType;
    }

    public MediaType getMediaType() {
        return mediaType;
    }
}
