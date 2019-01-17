package fr.inra.urgi.gpds.domain.criteria;

import fr.inra.urgi.gpds.domain.brapi.v1.criteria.BrapiLocationCriteria;
import fr.inra.urgi.gpds.domain.brapi.v1.criteria.BrapiPaginationCriteria;
import fr.inra.urgi.gpds.domain.criteria.base.PaginationCriteriaImpl;
import fr.inra.urgi.gpds.domain.data.impl.LocationVO;
import fr.inra.urgi.gpds.elasticsearch.criteria.annotation.CriteriaForDocument;
import fr.inra.urgi.gpds.elasticsearch.criteria.annotation.DocumentPath;

import java.util.Set;

/**
 * @author gcornut
 *
 *
 */
@CriteriaForDocument(LocationVO.class)
public class LocationCriteria extends PaginationCriteriaImpl
		implements BrapiPaginationCriteria, BrapiLocationCriteria {

	@DocumentPath("locationType")
	private Set<String> locationTypes;

	@Override
	public Set<String> getLocationTypes() {
		return locationTypes;
	}

	public void setLocationTypes(Set<String> locationTypes) {
		this.locationTypes = locationTypes;
	}

}
