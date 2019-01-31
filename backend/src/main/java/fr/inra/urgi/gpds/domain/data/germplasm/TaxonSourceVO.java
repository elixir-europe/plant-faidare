package fr.inra.urgi.gpds.domain.data.germplasm;

import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiGermplasmTaxonSource;

import java.io.Serializable;

/**
 * @author C. Michotey
 */
public class TaxonSourceVO implements Serializable, BrapiGermplasmTaxonSource {

    private static final long serialVersionUID = 3440255005695104200L;

    private String taxonId;
    private String sourceName;

    @Override
    public String getTaxonId() {
        return taxonId;
    }

    public void setTaxonId(String taxonId) {
        this.taxonId = taxonId;
    }

    @Override
    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

}
