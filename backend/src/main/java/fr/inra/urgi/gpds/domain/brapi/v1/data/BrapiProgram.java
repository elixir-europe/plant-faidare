package fr.inra.urgi.gpds.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;

import java.io.Serializable;

/**
 * @link https://github.com/plantbreeding/API/blob/master/Specification/Programs/ListPrograms.md
 * @link https://github.com/plantbreeding/API/blob/master/Specification/Programs/ProgramSearch.md
 *
 * @author gcornut
 *
 *
 */
public interface BrapiProgram extends Serializable {

	@JsonView(JSONView.BrapiFields.class)
	String getProgramDbId();

	@JsonView(JSONView.BrapiFields.class)
	String getName();

	@JsonView(JSONView.BrapiFields.class)
	String getAbbreviation();

	@JsonView(JSONView.BrapiFields.class)
	String getLeadPerson();

	@JsonView(JSONView.BrapiFields.class)
	String getObjective();

}
