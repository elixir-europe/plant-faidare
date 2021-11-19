package fr.inra.urgi.faidare.web.germplasm;

import java.util.ArrayList;
import java.util.List;

import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiGermplasmAttributeValue;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmInstituteVO;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmVO;
import fr.inra.urgi.faidare.domain.data.germplasm.PedigreeVO;
import fr.inra.urgi.faidare.domain.data.germplasm.SiteVO;
import fr.inra.urgi.faidare.domain.datadiscovery.data.DataSource;
import fr.inra.urgi.faidare.domain.xref.XRefDocumentVO;
import fr.inra.urgi.faidare.web.site.MapLocation;
import org.springframework.util.StringUtils;

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
        if (StringUtils.hasText(this.germplasm.getGenusSpeciesSubtaxa())) {
            return this.germplasm.getGenusSpeciesSubtaxa();
        } else if (StringUtils.hasText(this.germplasm.getGenusSpecies())) {
            return this.germplasm.getGenusSpecies();
        } else if (StringUtils.hasText(this.germplasm.getSubtaxa())) {
            return this.germplasm.getGenus() + " " + this.germplasm.getSpecies() + " " + this.germplasm.getSubtaxa();
        } else if (StringUtils.hasText(this.germplasm.getSpecies())) {
            return this.germplasm.getGenus() + " " + this.germplasm.getSpecies();
        } else {
            return this.germplasm.getGenus();
        }
    }

    public String getTaxonAuthor() {
        if (StringUtils.hasText(this.germplasm.getGenusSpeciesSubtaxa())) {
            return this.germplasm.getSubtaxaAuthority();
        } else if (StringUtils.hasText(this.germplasm.getGenusSpecies())) {
            return this.germplasm.getSpeciesAuthority();
        } else if (StringUtils.hasText(this.germplasm.getSubtaxa())) {
            return this.germplasm.getSubtaxaAuthority();
        } else if (StringUtils.hasText(this.germplasm.getSpecies())) {
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
        return this.germplasm.getCollectingSite() != null && StringUtils.hasText(this.germplasm.getCollectingSite().getSiteName());
    }

    private boolean isCollectorInstitutePresent() {
        return this.germplasm.getCollector() != null &&
            this.germplasm.getCollector().getInstitute() != null &&
            StringUtils.hasText(this.germplasm.getCollector().getInstitute().getInstituteName());
    }

    private boolean isCollectorIntituteFieldPresent() {
        GermplasmInstituteVO collector = this.germplasm.getCollector();
        return (collector != null) &&
            (StringUtils.hasText(collector.getAccessionNumber())
                || collector.getAccessionCreationDate() != null
                || StringUtils.hasText(collector.getMaterialType())
                || StringUtils.hasText(collector.getCollectors())
                || collector.getRegistrationYear() != null
                || collector.getDeregistrationYear() != null
                || StringUtils.hasText(collector.getDistributionStatus())
            );
    }

    public boolean isBreeding() {
        GermplasmInstituteVO breeder = this.germplasm.getBreeder();
        return breeder != null &&
            ((breeder.getInstitute() != null && StringUtils.hasText(breeder.getInstitute().getInstituteName())) ||
                breeder.getAccessionCreationDate() != null ||
                StringUtils.hasText(breeder.getAccessionNumber()) ||
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
            (StringUtils.hasText(this.pedigree.getParent1Name())
            || StringUtils.hasText(this.pedigree.getParent2Name())
            || StringUtils.hasText(this.pedigree.getCrossingPlan())
            || StringUtils.hasText(this.pedigree.getCrossingYear())
            || StringUtils.hasText(this.pedigree.getFamilyCode()));
    }

    public List<MapLocation> getMapLocations() {
        List<SiteVO> sites = new ArrayList<>();
        if (germplasm.getCollectingSite() != null) {
            sites.add(germplasm.getCollectingSite());
        }
        if (germplasm.getOriginSite() != null) {
            sites.add(germplasm.getOriginSite());
        }
        if (germplasm.getEvaluationSites() != null) {
            sites.addAll(germplasm.getEvaluationSites());
        }

        return MapLocation.sitesToDisplayableMapLocations(sites);
    }

    public boolean isPuiDisplayedAsLink() {
        String pui = this.germplasm.getGermplasmPUI();
        return pui != null && (pui.startsWith("https://doi.org") || pui.startsWith("http://doi.org"));
    }
}
