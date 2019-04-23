package fr.inra.urgi.gpds.domain.data.germplasm;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;
import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiGermplasm;
import fr.inra.urgi.gpds.domain.data.GnpISInternal;

import java.util.List;

/**
 * Extends the BrAPI germplasm with GnpIS fields
 *
 * @author gcornut
 */
public interface ExtendedGermplasm extends BrapiGermplasm, GnpISInternal {

    @JsonView(JSONView.GnpISFields.class)
    List<String> getTaxonSynonyms();

    @JsonView(JSONView.GnpISFields.class)
    List<String> getTaxonCommonNames();

    @JsonView(JSONView.GnpISFields.class)
    String getTaxonComment();

    @JsonView(JSONView.GnpISFields.class)
    String getGeneticNature();

    @JsonView(JSONView.GnpISFields.class)
    String getComment();

    @JsonView(JSONView.GnpISFields.class)
    Photo getPhoto();

    @JsonView(JSONView.GnpISFields.class)
    Institute getHoldingInstitute();

    @JsonView(JSONView.GnpISFields.class)
    Institute getHoldingGenbank();

    @JsonView(JSONView.GnpISFields.class)
    String getPresenceStatus();

    @JsonView(JSONView.GnpISFields.class)
    Genealogy getGenealogy();

    @JsonView(JSONView.GnpISFields.class)
    List<? extends Genealogy> getChildren();

    @JsonView(JSONView.GnpISFields.class)
    List<? extends PuiNameValue> getDescriptors();

    @JsonView(JSONView.GnpISFields.class)
    Site getOriginSite();

    @JsonView(JSONView.GnpISFields.class)
    Site getCollectingSite();

    @JsonView(JSONView.GnpISFields.class)
    List<? extends Site> getEvaluationSites();

    @JsonView(JSONView.GnpISFields.class)
    GermplasmInstitute getCollector();

    @JsonView(JSONView.GnpISFields.class)
    GermplasmInstitute getBreeder();

    @JsonView(JSONView.GnpISFields.class)
    List<? extends GermplasmInstitute> getDistributors();

    @JsonView(JSONView.GnpISFields.class)
    List<? extends CollPop> getPanel();

    @JsonView(JSONView.GnpISFields.class)
    List<? extends CollPop> getCollection();

    @JsonView(JSONView.GnpISFields.class)
    List<? extends CollPop> getPopulation();
}
