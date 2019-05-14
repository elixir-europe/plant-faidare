package fr.inra.urgi.faidare.elasticsearch.criteria.mapping;

import fr.inra.urgi.faidare.elasticsearch.document.DocumentMetadata;

import java.util.List;
import java.util.Map;

/**
 * A criteria mapping from document field tree to criterion field (from criteria
 * object)
 *
 * @author gcornut
 */
public interface CriteriaMapping extends CriteriaMappingTreeNode {

    /**
     * Get document field path using a criteria name
     *
     * @param criterionName  name of the criterion in the criteria class
     * @param noVirtualField whether or not you want the path to the virtual field or the parent concrete field
     */
    String getDocumentPath(String criterionName, boolean noVirtualField);

    /**
     * Criteria class mapped in this mapping
     */
    Class<?> getCriteriaClass();

    @Override
    String getDocumentFieldName();

    @Override
    List<String> getDocumentFieldPath();

    @Override
    Map<List<String>, CriteriaMappingTree> getMappingTree();

    @Override
    <N extends CriteriaMappingTree> N getMapping(Class<N> clazz, List<String> documentFieldPath);

    DocumentMetadata<?> getDocumentMetadata();

}
