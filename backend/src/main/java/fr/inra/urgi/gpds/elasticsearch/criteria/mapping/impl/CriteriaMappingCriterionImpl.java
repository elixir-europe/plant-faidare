package fr.inra.urgi.gpds.elasticsearch.criteria.mapping.impl;

import fr.inra.urgi.gpds.elasticsearch.criteria.annotation.QueryType;
import fr.inra.urgi.gpds.elasticsearch.criteria.mapping.CriteriaMappingCriterion;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author gcornut
 *
 *
 */
public class CriteriaMappingCriterionImpl implements CriteriaMappingCriterion {

	private final String documentFieldName;
	private final List<String> documentFieldPath;
	private final String criterionName;
	private final boolean virtualField;
	private final PropertyDescriptor criteriaProperty;
	private final Class<? extends QueryBuilder> queryType;

	public CriteriaMappingCriterionImpl(String documentFieldName, List<String> documentFieldPath,
			PropertyDescriptor criteriaProperty, Field criterionField, boolean virtualField) {
		this.documentFieldName = documentFieldName;
		this.documentFieldPath = documentFieldPath;
		this.virtualField = virtualField;
		this.criteriaProperty = criteriaProperty;
		this.queryType = findQueryType(criterionField);
		this.criterionName = criterionField.getName();
	}

	private Class<? extends QueryBuilder> findQueryType(Field criterionField) {
		// Default ES query type
		Class<? extends QueryBuilder> queryBuilder = TermQueryBuilder.class;

		// EQ query type given via @QueryType annotation
		QueryType queryType = criterionField.getAnnotation(QueryType.class);
		if (queryType != null) {
			queryBuilder = queryType.value();
		}
		return queryBuilder;
	}

	@Override
	public String getName() {
		return criterionName;
	}

	@Override
	public String getDocumentFieldName() {
		return documentFieldName;
	}

	@Override
	public List<String> getDocumentFieldPath() {
		return documentFieldPath;
	}

	@Override
	public boolean isVirtualField() {
		return virtualField;
	}

	@Override
	public Object getValue(Object searchCriteria) throws InvocationTargetException, IllegalAccessException {
		return criteriaProperty.getReadMethod().invoke(searchCriteria);
	}

	@Override
	public void setValue(Object searchCriteria, Object criteriaValue)
			throws InvocationTargetException, IllegalAccessException {
		criteriaProperty.getWriteMethod().invoke(searchCriteria, criteriaValue);
	}

	@Override
	public Class<? extends QueryBuilder> getQueryType() {
		return queryType;
	}

}
