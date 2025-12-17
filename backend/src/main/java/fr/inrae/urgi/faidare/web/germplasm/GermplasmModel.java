package fr.inrae.urgi.faidare.web.germplasm;

import fr.inrae.urgi.faidare.config.DataSource;
import fr.inrae.urgi.faidare.domain.GermplasmInstituteVO;
import fr.inrae.urgi.faidare.domain.SiteVO;
import fr.inrae.urgi.faidare.domain.XRefDocumentVO;
import fr.inrae.urgi.faidare.domain.brapi.v1.GermplasmAttributeValueV1VO;
import fr.inrae.urgi.faidare.domain.brapi.v1.GermplasmPedigreeV1VO;
import fr.inrae.urgi.faidare.domain.brapi.v2.GermplasmV2VO;
import fr.inrae.urgi.faidare.web.site.MapLocation;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * The model used by the germplasm page
 * @author JB Nizet
 */
public final class GermplasmModel {
    private final GermplasmV2VO germplasm;
    private final DataSource source;
    private final List<GermplasmAttributeValueV1VO> attributes;
    private final GermplasmPedigreeV1VO pedigree;
    private final List<XRefDocumentVO> crossReferences;
    private final String contextPath;

    public GermplasmModel(GermplasmV2VO germplasm,
                          DataSource source,
                          List<GermplasmAttributeValueV1VO> attributes,
                          GermplasmPedigreeV1VO pedigree,
                          List<XRefDocumentVO> crossReferences, String contextPath) {
        this.germplasm = germplasm;
        this.source = source;
        this.attributes = attributes;
        this.pedigree = pedigree;
        this.crossReferences = crossReferences;
        this.contextPath = contextPath;
    }

    public GermplasmV2VO getGermplasm() {
        return germplasm;
    }

    public DataSource getSource() {
        return source;
    }

    public List<GermplasmAttributeValueV1VO> getAttributes() {
        return attributes;
    }

    public GermplasmPedigreeV1VO getPedigree() {
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
        return this.germplasm.getBreeder() != null &&
            this.germplasm.getBreeder().getInstitute() != null &&
            StringUtils.hasText(this.germplasm.getBreeder().getInstitute().getInstituteName());
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
        return this.germplasm.getBreeder() != null &&
            (this.germplasm.getBreeder().getInstitute() != null ||
                StringUtils.hasText(this.germplasm.getBreeder().getAccessionNumber()));
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

    public String getContextPath() {
        return contextPath;
    }

    public boolean isPuiDisplayedAsLink() {
        String pui = this.germplasm.getGermplasmPUI();
        return pui != null && (pui.startsWith("https://doi.org") || pui.startsWith("http://doi.org"));
    }
}
