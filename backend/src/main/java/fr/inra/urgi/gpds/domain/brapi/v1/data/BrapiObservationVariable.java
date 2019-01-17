package fr.inra.urgi.gpds.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;

import java.util.List;

/**
 * @link https://github.com/plantbreeding/API/blob/master/Specification/ObservationVariables/VariableDetails.md
 * @link https://github.com/plantbreeding/API/blob/master/Specification/ObservationVariables/VariableList.md
 * @link https://github.com/plantbreeding/API/blob/master/Specification/ObservationVariables/VariableSearch.md
 *
 * @author gcornut
 *
 *
 */
public interface BrapiObservationVariable {
	@JsonView(JSONView.BrapiFields.class)
	String getObservationVariableDbId();

	@JsonView(JSONView.BrapiFields.class)
	String getName();

	@JsonView(JSONView.BrapiFields.class)
	String getOntologyDbId();

	@JsonView(JSONView.BrapiFields.class)
	String getOntologyName();

	@JsonView(JSONView.BrapiFields.class)
	List<String> getSynonyms();

	@JsonView(JSONView.BrapiFields.class)
	List<String> getContextOfUse();

	@JsonView(JSONView.BrapiFields.class)
	String getGrowthStage();

	@JsonView(JSONView.BrapiFields.class)
	String getStatus();

	@JsonView(JSONView.BrapiFields.class)
	String getXref();

	@JsonView(JSONView.BrapiFields.class)
	String getInstitution();

	@JsonView(JSONView.BrapiFields.class)
	String getScientist();

	@JsonView(JSONView.BrapiFields.class)
	String getDate();

	@JsonView(JSONView.BrapiFields.class)
	String getLanguage();

	@JsonView(JSONView.BrapiFields.class)
	String getCrop();

	@JsonView(JSONView.BrapiFields.class)
	BrapiTrait getTrait();

	@JsonView(JSONView.BrapiFields.class)
	BrapiMethod getMethod();

	@JsonView(JSONView.BrapiFields.class)
	BrapiScale getScale();
}
