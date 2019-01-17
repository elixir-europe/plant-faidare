package fr.inra.urgi.gpds.domain.criteria;

import fr.inra.urgi.gpds.domain.brapi.v1.criteria.BrapiStudyObservationUnitCriteria;
import fr.inra.urgi.gpds.domain.criteria.base.PaginationCriteriaImpl;

/**
 * @author gcornut
 *
 *
 */
public class StudyObservationUnitCriteria
		extends PaginationCriteriaImpl
		implements BrapiStudyObservationUnitCriteria {

	private String observationLevel;

	@Override
	public String getObservationLevel() {
		return observationLevel;
	}

	public void setObservationLevel(String observationLevel) {
		this.observationLevel = observationLevel;
	}

	@Override
	public Long getPage() {
		return super.getPage();
	}

	@Override
	public void setPage(Long page) {
		super.setPage(page);
	}

	@Override
	public Long getPageSize() {
		return super.getPageSize();
	}

	@Override
	public void setPageSize(Long pageSize) {
		super.setPageSize(pageSize);
	}
}
