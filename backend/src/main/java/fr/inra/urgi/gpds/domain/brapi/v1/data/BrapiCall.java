package fr.inra.urgi.gpds.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;

import java.util.List;

/**
 * @link https://github.com/plantbreeding/API/blob/master/Specification/Calls/Calls.md
 *
 * @author gcornut
 *
 *
 */
public interface BrapiCall {

	@JsonView(JSONView.BrapiFields.class)
	String getCall();

	@JsonView(JSONView.BrapiFields.class)
	List<String> getDatatypes();

	@JsonView(JSONView.BrapiFields.class)
	List<String> getDataTypes();

	@JsonView(JSONView.BrapiFields.class)
	List<String> getMethods();

	@JsonView(JSONView.BrapiFields.class)
	List<String> getVersions();
}
