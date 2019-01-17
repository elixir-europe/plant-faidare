package fr.inra.urgi.gpds.domain.data.impl.germplasm;

import fr.inra.urgi.gpds.domain.data.Photo;

import java.io.Serializable;

public class PhotoVO implements Serializable, Photo {

	private static final long serialVersionUID = -4993890419772211643L;

	private String fileName;
	private String thumbnailFileName;
	private String photoName;
	private String description;
	private String copyright;

	@Override
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String getThumbnailFileName() {
		return thumbnailFileName;
	}

	public void setThumbnailFileName(String thumbnailFileName) {
		this.thumbnailFileName = thumbnailFileName;
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
