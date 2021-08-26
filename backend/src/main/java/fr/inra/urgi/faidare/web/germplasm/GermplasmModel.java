package fr.inra.urgi.faidare.web.germplasm;

import java.util.List;

import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiGermplasmAttributeValue;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmInstituteVO;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmVO;
import fr.inra.urgi.faidare.domain.data.germplasm.PedigreeVO;
import fr.inra.urgi.faidare.domain.datadiscovery.data.DataSource;
import fr.inra.urgi.faidare.domain.xref.XRefDocumentVO;
import org.apache.logging.log4j.util.Strings;

/**
 * The model used by the germplasm page
 * @author JB Nizet
 */
public final class GermplasmModel {
    private final GermplasmVO germplasm;
    private final DataSource source;
    private final List<BrapiGermplasmAttributeValue> attributes;
    private final PedigreeVO pedigree;
    private final List<XRefDocumentVO> crossReferences;

    public GermplasmModel(GermplasmVO germplasm,
                          DataSource source,
                          List<BrapiGermplasmAttributeValue> attributes,
                          PedigreeVO pedigree,
                          List<XRefDocumentVO> crossReferences) {
        this.germplasm = germplasm;
        this.source = source;
        this.attributes = attributes;
        this.pedigree = pedigree;
        this.crossReferences = crossReferences;
    }

    public GermplasmVO getGermplasm() {
        return germplasm;
    }

    public DataSource getSource() {
        return source;
    }

    public List<BrapiGermplasmAttributeValue> getAttributes() {
        return attributes;
    }

    public PedigreeVO getPedigree() {
        return pedigree;
    }

    public List<XRefDocumentVO> getCrossReferences() {
        return crossReferences;
    }

    public String getTaxon() {
        if (Strings.isNotBlank(this.germplasm.getGenusSpeciesSubtaxa())) {
            return this.germplasm.getGenusSpeciesSubtaxa();
        } else if (Strings.isNotBlank(this.germplasm.getGenusSpecies())) {
            return this.germplasm.getGenusSpecies();
        } else if (Strings.isNotBlank(this.germplasm.getSubtaxa())) {
            return this.germplasm.getGenus() + " " + this.germplasm.getSpecies() + " " + this.germplasm.getSubtaxa();
        } else if (Strings.isNotBlank(this.germplasm.getSpecies())) {
            return this.germplasm.getGenus() + " " + this.germplasm.getSpecies();
        } else {
            return this.germplasm.getGenus();
        }
    }

    public String getTaxonAuthor() {
        if (Strings.isNotBlank(this.germplasm.getGenusSpeciesSubtaxa())) {
            return this.germplasm.getSubtaxaAuthority();
        } else if (Strings.isNotBlank(this.germplasm.getGenusSpecies())) {
            return this.germplasm.getSpeciesAuthority();
        } else if (Strings.isNotBlank(this.germplasm.getSubtaxa())) {
            return this.germplasm.getSubtaxaAuthority();
        } else if (Strings.isNotBlank(this.germplasm.getSpecies())) {
            return this.germplasm.getSpeciesAuthority();
        } else {
            return null;
        }
    }

    public boolean isCollecting() {
        return this.isCollectingSitePresent()
            || this.isCollectorInstitutePresent()
            || this.isCollectorIntituteFieldPresent();
    }

    private boolean isCollectingSitePresent() {
        return this.germplasm.getCollectingSite() != null && Strings.isNotBlank(this.germplasm.getCollectingSite().getSiteName());
    }

    private boolean isCollectorInstitutePresent() {
        return this.germplasm.getCollector() != null &&
            this.germplasm.getCollector().getInstitute() != null &&
            Strings.isNotBlank(this.germplasm.getCollector().getInstitute().getInstituteName());
    }

    private boolean isCollectorIntituteFieldPresent() {
        GermplasmInstituteVO collector = this.germplasm.getCollector();
        return (collector != null) &&
            (Strings.isNotBlank(collector.getAccessionNumber())
                || collector.getAccessionCreationDate() != null
                || Strings.isNotBlank(collector.getMaterialType())
                || Strings.isNotBlank(collector.getCollectors())
                || collector.getRegistrationYear() != null
                || collector.getDeregistrationYear() != null
                || Strings.isNotBlank(collector.getDistributionStatus())
            );
    }

    public boolean isBreeding() {
        GermplasmInstituteVO breeder = this.germplasm.getBreeder();
        return breeder != null &&
            ((breeder.getInstitute() != null && Strings.isNotBlank(breeder.getInstitute().getInstituteName())) ||
                breeder.getAccessionCreationDate() != null ||
                Strings.isNotBlank(breeder.getAccessionNumber()) ||
                breeder.getRegistrationYear() != null ||
                breeder.getDeregistrationYear() != null);
    }

    public boolean isGenealogyPresent() {
        return isPedigreePresent() || isProgenyPresent();
    }

    private boolean isProgenyPresent() {
        return germplasm.getChildren() != null && !germplasm.getChildren().isEmpty();
    }

    private boolean isPedigreePresent() {
        return this.pedigree != null &&
            (Strings.isNotBlank(this.pedigree.getParent1Name())
            || Strings.isNotBlank(this.pedigree.getParent2Name())
            || Strings.isNotBlank(this.pedigree.getCrossingPlan())
            || Strings.isNotBlank(this.pedigree.getCrossingYear())
            || Strings.isNotBlank(this.pedigree.getFamilyCode()));
    }
}
