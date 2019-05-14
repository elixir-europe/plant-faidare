package fr.inra.urgi.faidare.elasticsearch.criteria.mapping.impl;

import com.google.common.base.Joiner;
import fr.inra.urgi.faidare.elasticsearch.criteria.mapping.CriteriaMappingTree;
import fr.inra.urgi.faidare.elasticsearch.criteria.mapping.CriteriaMappingTreeNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gcornut
 */
public class CriteriaMappingTreeNodeImpl implements CriteriaMappingTreeNode {

    private final String documentFieldName;
    private final List<String> documentFieldPath;
    private final Map<List<String>, CriteriaMappingTree> mappingSubTree;

    public CriteriaMappingTreeNodeImpl(String documentFieldName, List<String> documentFieldPath) {
        this.documentFieldName = documentFieldName;
        this.documentFieldPath = documentFieldPath;
        this.mappingSubTree = new HashMap<>();
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
    public Map<List<String>, CriteriaMappingTree> getMappingTree() {
        return mappingSubTree;
    }

    @Override
    public <N extends CriteriaMappingTree> N getMapping(Class<N> clazz, List<String> documentFieldPath) {
        CriteriaMappingTree node = mappingSubTree.get(documentFieldPath);
        try {
            return clazz.cast(node);
        } catch (ClassCastException | NullPointerException e) {
            throw new RuntimeException("Could not find criterion for document field path '"
                + Joiner.on(".").join(documentFieldPath) + "'");
        }
    }
}
