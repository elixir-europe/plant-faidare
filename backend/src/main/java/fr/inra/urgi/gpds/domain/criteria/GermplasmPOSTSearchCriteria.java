package fr.inra.urgi.gpds.domain.criteria;

import fr.inra.urgi.gpds.domain.brapi.v1.criteria.BrapiGermplasmPOSTSearchCriteria;
import fr.inra.urgi.gpds.domain.criteria.base.PaginationCriteriaImpl;
import fr.inra.urgi.gpds.domain.data.impl.germplasm.GermplasmVO;
import fr.inra.urgi.gpds.elasticsearch.criteria.annotation.CriteriaForDocument;
import fr.inra.urgi.gpds.elasticsearch.criteria.annotation.DocumentPath;

import java.util.List;

/**
 * @author gcornut
 *
 *
 */
@CriteriaForDocument(GermplasmVO.class)
public class GermplasmPOSTSearchCriteria
		extends PaginationCriteriaImpl
		implements GermplasmSearchCriteria, BrapiGermplasmPOSTSearchCriteria {

	@DocumentPath("germplasmDbId")
	private List<String> germplasmDbIds;

	@DocumentPath("germplasmPUI")
	private List<String> germplasmPUIs;

	@DocumentPath("species")
	private List<String> germplasmSpecies;

	@DocumentPath("genus")
	private List<String> germplasmGenus;

	@DocumentPath("germplasmName")
	private List<String> germplasmNames;

	@DocumentPath("accessionNumber")
	private List<String> accessionNumbers;

	@Override
	public List<String> getGermplasmPUIs() {
		return germplasmPUIs;
	}

	public void setGermplasmPUIs(List<String> germplasmPUIs) {
		this.germplasmPUIs = germplasmPUIs;
	}

	@Override
	public List<String> getGermplasmDbIds() {
		return germplasmDbIds;
	}

	public void setGermplasmDbIds(List<String> germplasmDbIds) {
		this.germplasmDbIds = germplasmDbIds;
	}

	@Override
	public List<String> getGermplasmSpecies() {
		return germplasmSpecies;
	}

	public void setGermplasmSpecies(List<String> germplasmSpecies) {
		this.germplasmSpecies = germplasmSpecies;
	}

	@Override
	public List<String> getGermplasmGenus() {
		return germplasmGenus;
	}

	public void setGermplasmGenus(List<String> germplasmGenus) {
		this.germplasmGenus = germplasmGenus;
	}

	@Override
	public List<String> getGermplasmNames() {
		return germplasmNames;
	}

	public void setGermplasmNames(List<String> germplasmNames) {
		this.germplasmNames = germplasmNames;
	}

	@Override
	public List<String> getAccessionNumbers() {
		return accessionNumbers;
	}

	public void setAccessionNumbers(List<String> accessionNumbers) {
		this.accessionNumbers = accessionNumbers;
	}
}
