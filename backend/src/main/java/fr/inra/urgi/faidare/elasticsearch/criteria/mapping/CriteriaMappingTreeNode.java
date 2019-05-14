package fr.inra.urgi.faidare.elasticsearch.criteria.mapping;

import java.util.List;
import java.util.Map;

/**
 * Node in the mapping from document to criteria which has children nodes
 * (nested object or simple object in the document)
 */
public interface CriteriaMappingTreeNode extends CriteriaMappingTree {

    @Override
    List<String> getDocumentFieldPath();

    @Override
    String getDocumentFieldName();

    /**
     * List children criteria
     */
    Map<List<String>, CriteriaMappingTree> getMappingTree();

    /**
     * Get criterion from document field path
     */
    <N extends CriteriaMappingTree> N getMapping(Class<N> clazz, List<String> documentFieldPath);
}
