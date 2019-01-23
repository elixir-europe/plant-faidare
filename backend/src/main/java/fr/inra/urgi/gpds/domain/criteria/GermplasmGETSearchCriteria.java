package fr.inra.urgi.gpds.domain.criteria;

import fr.inra.urgi.gpds.domain.brapi.v1.criteria.BrapiGermplasmGETSearchCriteria;
import fr.inra.urgi.gpds.domain.criteria.base.PaginationCriteriaImpl;
import fr.inra.urgi.gpds.domain.data.impl.germplasm.GermplasmVO;
import fr.inra.urgi.gpds.elasticsearch.criteria.annotation.CriteriaForDocument;
import fr.inra.urgi.gpds.elasticsearch.criteria.annotation.DocumentPath;

import java.util.List;

/**
 * @author gcornut
 */
@CriteriaForDocument(GermplasmVO.class)
public class GermplasmGETSearchCriteria
    extends PaginationCriteriaImpl
    implements GermplasmSearchCriteria, BrapiGermplasmGETSearchCriteria {

    @DocumentPath("germplasmDbId")
    private List<String> germplasmDbId;

    @DocumentPath("germplasmPUI")
    private List<String> germplasmPUI;

    @DocumentPath("germplasmName")
    private List<String> germplasmName;

    @Override
    public List<String> getGermplasmPUI() {
        return germplasmPUI;
    }

    public void setGermplasmPUI(List<String> germplasmPUI) {
        this.germplasmPUI = germplasmPUI;
    }

    @Override
    public List<String> getGermplasmDbId() {
        return germplasmDbId;
    }

    public void setGermplasmDbId(List<String> germplasmDbId) {
        this.germplasmDbId = germplasmDbId;
    }

    @Override
    public List<String> getGermplasmName() {
        return germplasmName;
    }

    public void setGermplasmName(List<String> germplasmName) {
        this.germplasmName = germplasmName;
    }

}
