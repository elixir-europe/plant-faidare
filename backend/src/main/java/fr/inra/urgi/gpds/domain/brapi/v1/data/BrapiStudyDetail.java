package fr.inra.urgi.gpds.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;

import java.io.Serializable;
import java.util.List;

/**
 * @link https://github.com/plantbreeding/API/blob/master/Specification/Studies/StudyDetails.md
 *
 * @author gcornut
 *
 *
 */
public interface BrapiStudyDetail extends BrapiStudy, Serializable {

	// Study description
	@JsonView(JSONView.BrapiFields.class)
	String getStudyDescription();

	// Study last update
	@JsonView(JSONView.BrapiFields.class)
	BrapiStudyLastUpdate getLastUpdate();

	// Contacts
	@JsonView(JSONView.BrapiFields.class)
	List<BrapiContact> getContacts();

	// Location
	@JsonView(JSONView.BrapiFields.class)
	BrapiLocation getLocation();

	// Data links
	@JsonView(JSONView.BrapiFields.class)
	List<BrapiStudyDataLink> getDataLinks();

}
