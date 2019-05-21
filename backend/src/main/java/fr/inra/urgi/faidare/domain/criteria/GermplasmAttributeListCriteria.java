package fr.inra.urgi.faidare.domain.criteria;

import fr.inra.urgi.faidare.domain.brapi.v1.criteria.BrapiGermplasmAttributeListCriteria;
import fr.inra.urgi.faidare.domain.criteria.base.PaginationCriteriaImpl;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmAttributeValueListVO;
import fr.inra.urgi.faidare.elasticsearch.criteria.annotation.CriteriaForDocument;
import fr.inra.urgi.faidare.elasticsearch.criteria.annotation.DocumentPath;

import java.util.List;

/**
 * @author gcornut
 */
@CriteriaForDocument(GermplasmAttributeValueListVO.class)
public class GermplasmAttributeListCriteria
    extends PaginationCriteriaImpl
    implements BrapiGermplasmAttributeListCriteria {

    @DocumentPath("attributeCategoryDbId")
    private List<String> attributeCategoryDbId;

    @Override
    public List<String> getAttributeCategoryDbId() {
        return attributeCategoryDbId;
    }

    public void setAttributeCategoryDbId(List<String> attributeCategoryDbId) {
        this.attributeCategoryDbId = attributeCategoryDbId;
    }
}
