package fr.inra.urgi.gpds.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;

import java.io.Serializable;

/**
 * @link https://github.com/plantbreeding/API/blob/master/Specification/Studies/SearchStudies.md
 * @link https://github.com/plantbreeding/API/blob/master/Specification/Studies/ListStudySummaries.md
 *
 * @author gcornut
 *
 *
 */
public interface BrapiStudySummary extends BrapiStudy, Serializable {

	// Location
	@JsonView(JSONView.BrapiFields.class)
	String getLocationDbId();

	@JsonView(JSONView.BrapiFields.class)
	String getLocationName();

	// Program
	@JsonView(JSONView.BrapiFields.class)
	String getProgramDbId();

	@JsonView(JSONView.BrapiFields.class)
	String getProgramName();

}
