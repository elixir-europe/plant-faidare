package fr.inra.urgi.gpds.domain.criteria;

import fr.inra.urgi.gpds.domain.brapi.v1.criteria.BrapiGermplasmAttributeCriteria;
import fr.inra.urgi.gpds.domain.criteria.base.PaginationCriteriaImpl;
import fr.inra.urgi.gpds.domain.data.impl.germplasm.GermplasmAttributeValueListVO;
import fr.inra.urgi.gpds.elasticsearch.criteria.annotation.CriteriaForDocument;
import fr.inra.urgi.gpds.elasticsearch.criteria.annotation.DocumentPath;

import java.util.List;

/**
 * @author gcornut
 */
@CriteriaForDocument(GermplasmAttributeValueListVO.class)
public class GermplasmAttributeCriteria
    extends PaginationCriteriaImpl
    implements BrapiGermplasmAttributeCriteria {

    @DocumentPath("germplasmDbId")
    private String germplasmDbId;

    @DocumentPath({"data", "attributeDbId"})
    private List<String> attributeList;

    @Override
    public List<String> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(List<String> attributeList) {
        this.attributeList = attributeList;
    }

    @Override
    public String getGermplasmDbId() {
        return germplasmDbId;
    }

    public void setGermplasmDbId(String germplasmDbId) {
        this.germplasmDbId = germplasmDbId;
    }
}
