package fr.inra.urgi.faidare.domain.data.germplasm;

import java.io.Serializable;

public class PhotoVO implements Serializable, Photo {

    private static final long serialVersionUID = -4993890419772211643L;

    private String file;
    private String thumbnailFile;
    private String photoName;
    private String description;
    private String copyright;

    @Override
    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public String getThumbnailFile() {
        return thumbnailFile;
    }

    public void setThumbnailFile(String thumbnailFile) {
        this.thumbnailFile = thumbnailFile;
    }

    @Override
    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

}
