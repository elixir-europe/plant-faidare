package fr.inra.urgi.gpds.domain.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;
import fr.inra.urgi.gpds.domain.data.impl.germplasm.SimpleVO;

import java.util.List;

/**
 * @author gcornut
 *
 *
 */
public interface Genealogy {
	@JsonView(JSONView.GnpISFields.class)
	String getCrossingPlan();

	@JsonView(JSONView.GnpISFields.class)
	String getCrossingYear();

	@JsonView(JSONView.GnpISFields.class)
	String getFamilyCode();

	@JsonView(JSONView.GnpISFields.class)
	String getFirstParentName();

	@JsonView(JSONView.GnpISFields.class)
	String getFirstParentPUI();

	@JsonView(JSONView.GnpISFields.class)
	String getFirstParentType();

	@JsonView(JSONView.GnpISFields.class)
	String getSecondParentName();

	@JsonView(JSONView.GnpISFields.class)
	String getSecondParentPUI();

	@JsonView(JSONView.GnpISFields.class)
	String getSecondParentType();

	@JsonView(JSONView.GnpISFields.class)
	List<SimpleVO> getSibblings();
}
