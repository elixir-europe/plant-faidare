package fr.inra.urgi.gpds.elasticsearch.criteria.mapping;

import org.elasticsearch.index.query.QueryBuilder;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Leaf node of the criteria mapping representing a criterion (part of a larger
 * criteria object)
 */
public interface CriteriaMappingCriterion extends CriteriaMappingTree {

	@Override
	List<String> getDocumentFieldPath();

	@Override
	String getDocumentFieldName();

	/**
	 * Whether or not the criterion maps to a virtual field in the document
	 */
	boolean isVirtualField();

	/**
	 * Get the criterion value from the parent criteria object
	 */
	Object getValue(Object searchCriteria) throws InvocationTargetException, IllegalAccessException;

	/**
	 * Set the criterion value from the parent criteria object
	 */
	void setValue(Object searchCriteria, Object criteriaValue) throws InvocationTargetException, IllegalAccessException;

	/**
	 * Get the criterion query type
	 */
	Class<? extends QueryBuilder> getQueryType();

	/**
	 * Get the criterion name
	 */
	String getName();

}
