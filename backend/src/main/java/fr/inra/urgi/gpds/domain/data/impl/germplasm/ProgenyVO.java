package fr.inra.urgi.gpds.domain.data.impl.germplasm;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiParentProgeny;
import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiProgeny;
import fr.inra.urgi.gpds.elasticsearch.document.annotation.Document;

import java.io.Serializable;
import java.util.List;

/**
* Model taken from the breeding API Germplasm MCPD call
*
*@author mbuy
*
*/

@Document(type = "germplasmProgeny")
public class ProgenyVO implements Serializable, BrapiProgeny {

	private static final long serialVersionUID = -8748938185286594606L;
	private String germplasmDbId;
	private String defaultDisplayName;

	@JsonDeserialize(contentAs = ParentProgenyVO.class)
	private List<BrapiParentProgeny> progeny;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getGermplasmDbId() {
		return germplasmDbId;
	}

	public void setGermplasmDbId(String germplasmDbId) {
		this.germplasmDbId = germplasmDbId;
	}

	public String getDefaultDisplayName() {
		return defaultDisplayName;
	}

	public void setDefaultDisplayName(String defaultDisplayName) {
		this.defaultDisplayName = defaultDisplayName;
	}

	public void setProgeny(List<BrapiParentProgeny> progeny) {
		this.progeny = progeny;
	}

	@Override
	public List<BrapiParentProgeny> getProgeny() {
		return progeny;
	}
}
