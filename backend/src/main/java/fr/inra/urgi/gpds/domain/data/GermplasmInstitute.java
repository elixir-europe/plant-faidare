package fr.inra.urgi.gpds.domain.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;

/**
 * @author gcornut
 *
 *
 */
public interface GermplasmInstitute {
	@JsonView(JSONView.GnpISFields.class)
	Institute getInstitute();

	@JsonView(JSONView.GnpISFields.class)
	String getGermplasmPUI();

	@JsonView(JSONView.GnpISFields.class)
	String getAccessionNumber();

	@JsonView(JSONView.GnpISFields.class)
	Integer getAccessionCreationDate();

	@JsonView(JSONView.GnpISFields.class)
	String getMaterialType();

	@JsonView(JSONView.GnpISFields.class)
	String getCollectors();

	@JsonView(JSONView.GnpISFields.class)
	Integer getRegistrationYear();

	@JsonView(JSONView.GnpISFields.class)
	Integer getDeregistrationYear();

	@JsonView(JSONView.GnpISFields.class)
	String getDistributionStatus();
}
