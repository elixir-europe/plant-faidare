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

    @DocumentPath("entry_type")
	private String entryType;

    @DocumentPath("linkedRessourcesID")
	private List<String> linkedRessourcesID;

    public static XRefDocumentSearchCriteria forXRefId(String resourceId) {
        XRefDocumentSearchCriteria criteria = new XRefDocumentSearchCriteria();
        criteria.setLinkedRessourcesID(Collections.singletonList(resourceId));
        return criteria;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public List<String> getLinkedRessourcesID() {
        return linkedRessourcesID;
    }

    public void setLinkedRessourcesID(List<String> linkedRessourcesID) {
        this.linkedRessourcesID = linkedRessourcesID;
    }
}
