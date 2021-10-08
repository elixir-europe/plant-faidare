package fr.inra.urgi.faidare.domain.xref;

import fr.inra.urgi.faidare.domain.criteria.base.PaginationCriteriaImpl;
import fr.inra.urgi.faidare.elasticsearch.criteria.annotation.CriteriaForDocument;
import fr.inra.urgi.faidare.elasticsearch.criteria.annotation.DocumentPath;

import java.util.Collections;
import java.util.List;

/**
 * Imported and adapted from unified-interface legacy
 */
@CriteriaForDocument(XRefDocumentVO.class)
public class XRefDocumentSearchCriteria extends PaginationCriteriaImpl {

    @DocumentPath("entryType")
	private String entryType;

    @DocumentPath("linkedResourcesID") // pragma: allowlist secret
	private List<String> linkedResourcesID;

    public static XRefDocumentSearchCriteria forXRefId(String resourceId) {
        XRefDocumentSearchCriteria criteria = new XRefDocumentSearchCriteria();
        criteria.setLinkedResourcesID(Collections.singletonList(resourceId));
        return criteria;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public List<String> getLinkedResourcesID() {
        return linkedResourcesID;
    }

    public void setLinkedResourcesID(List<String> linkedResourcesID) {
        this.linkedResourcesID = linkedResourcesID;
    }
}
