import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BrapiService } from '../brapi.service';
import { GnpisService } from '../gnpis.service';
import { BrapiAttributeData, BrapiGermplasmPedigree, BrapiLocation, BrapiTaxonIds } from '../models/brapi.model';
import { Children, Germplasm, Site } from '../models/gnpis.model';
import { DataDiscoverySource } from '../models/data-discovery.model';

@Component({
    selector: 'faidare-germplasm-card',
    templateUrl: './germplasm-card.component.html',
    styleUrls: ['./germplasm-card.component.scss']
})

export class GermplasmCardComponent implements OnInit {

    alreadyInitialize = false;

    constructor(private brapiService: BrapiService,
                private gnpisService: GnpisService,
                private route: ActivatedRoute) {
    }

    NCBI_URL = 'https://www.ncbi.nlm.nih.gov/Taxonomy/Browser/wwwtax.cgi?mode=Info&id=';
    THEPLANTLIST_URL = 'http://www.theplantlist.org/tpl1.1/record/';
    TAXREF_URL = 'https://inpn.mnhn.fr/espece/cd_nom/';
    CATALOGUEOFLIFE_URL = 'http://www.catalogueoflife.org/col/details/species/id/';
    taxonIdsWithURL: BrapiTaxonIds[] = [];

    germplasmGnpis: Germplasm;
    germplasmPedigree: BrapiGermplasmPedigree;
    germplasmProgeny: Children[];
    germplasmAttributes: BrapiAttributeData[];
    germplasmLocations: BrapiLocation[] = [];
    germplasmId: string;
    germplasmPuid: string;
    germplasmTaxon: string;
    germplasmTaxonAuthor: string;
    germplasmSource: DataDiscoverySource;
    toReplace = /_/g;


    loaded: Promise<any>;
    loading = true;

    ngOnInit() {

        this.route.queryParams.subscribe(queryParams => {

            this.germplasmId = queryParams.id;
            this.germplasmPuid = queryParams.pui;

            const germplasm$ = this.getGermplasm(this.germplasmId, this.germplasmPuid);
            germplasm$.then(result => {
                const germplasmId = this.germplasmId ? this.germplasmId : result.germplasmDbId;

                // TODO use the progeny call when the information about parent will be added.
                /*const germplasmProgeny$ = this.brapiService.germplasmProgeny(germplasmId).toPromise();
                germplasmProgeny$
                    .then(germplasmProgeny => {
                        this.germplasmProgeny = germplasmProgeny.result;
                    });*/

                const germplasmPedigree$ = this.brapiService.germplasmPedigree(germplasmId).toPromise();
                germplasmPedigree$
                    .then(germplasmPedigree => {
                        this.germplasmPedigree = germplasmPedigree.result;
                    });

                const germplasmAttributes$ = this.brapiService.germplasmAttributes(germplasmId).toPromise();
                germplasmAttributes$
                    .then(germplasmAttributes => {
                        if (germplasmAttributes.result && germplasmAttributes.result.data) {
                            this.germplasmAttributes = germplasmAttributes.result.data.sort(this.compareAttributes);
                        }

                    });
            });

            this.loaded = Promise.all([germplasm$]);
            this.loaded.then(() => {
                this.loading = false;
                this.alreadyInitialize = true;
            });
        });
    }

    getGermplasm(id: string, pui: string): Promise<Germplasm> {
        let germplasm$: Promise<Germplasm>;
        if (id) {
            germplasm$ = this.gnpisService.germplasm(id).toPromise();
        } else {
            germplasm$ = this.gnpisService.germplasmByPuid(pui).toPromise();
        }
        germplasm$
            .then(germplasmGnpis => {
                this.germplasmGnpis = germplasmGnpis;
                // Get germplasm source
                const sourceURI = germplasmGnpis['schema:includedInDataCatalog'];
                this.getGermplasmSource(sourceURI);
                this.getTaxon(germplasmGnpis);
                this.reformatData(germplasmGnpis);
            });
        return germplasm$;
    }

    // TODO Remove the condition when the field includedInDataCatalog will be added to URGI study.
    getGermplasmSource(sourceURI: string) {
        if (sourceURI) {
            const source$ = this.gnpisService.getSource(sourceURI);
            source$
                .subscribe(src => {
                    this.germplasmSource = src;
                });
        } else {
            const urgiURI = 'https://urgi.versailles.inra.fr';
            const source$ = this.gnpisService.getSource(urgiURI);
            source$
                .subscribe(src => {
                    this.germplasmSource = src;
                });
        }
    }

    getTaxon(germplasmGnpis) {
        if (germplasmGnpis.genusSpeciesSubtaxa) {
            this.germplasmTaxon = germplasmGnpis.genusSpeciesSubtaxa;
            this.germplasmTaxonAuthor = germplasmGnpis.subtaxaAuthority;
        } else if (germplasmGnpis.genusSpecies) {
            this.germplasmTaxon = germplasmGnpis.genusSpecies;
            this.germplasmTaxonAuthor = germplasmGnpis.speciesAuthority;
        } else if (germplasmGnpis.subtaxa) {
            this.germplasmTaxon = germplasmGnpis.genus + ' ' + germplasmGnpis.species + ' ' + germplasmGnpis.subtaxa;
            this.germplasmTaxonAuthor = germplasmGnpis.subtaxaAuthority;
        } else if (germplasmGnpis.species) {
            this.germplasmTaxon = germplasmGnpis.genus + ' ' + germplasmGnpis.species;
            this.germplasmTaxonAuthor = germplasmGnpis.speciesAuthority;
        } else {
            this.germplasmTaxon = germplasmGnpis.genus;
            this.germplasmTaxonAuthor = '';
        }
    }

    reformatData(germplasmGnpis) {
        if (germplasmGnpis.children) {
            this.germplasmProgeny = germplasmGnpis.children.sort();
        }
        if (germplasmGnpis.donors) {
            this.germplasmGnpis.donors.sort(this.compareDonorInstitutes);
        }
        if (germplasmGnpis.collection) {
            this.germplasmGnpis.collection.sort(this.compareCollectionPopulationPanel);
        }
        if (germplasmGnpis.population) {
            this.germplasmGnpis.population.sort(this.compareCollectionPopulationPanel);
        }
        if (this.germplasmGnpis.panel) {
            this.germplasmGnpis.panel.sort(this.compareCollectionPopulationPanel);
        }
        if (this.germplasmGnpis.collectingSite) {
            this.siteToBrapiLocation(this.germplasmGnpis.collectingSite);
        }
        if (this.germplasmGnpis.originSite) {
            this.siteToBrapiLocation(this.germplasmGnpis.originSite);
        }
        if (this.germplasmGnpis.evaluationSites && this.germplasmGnpis.evaluationSites.length > 0) {
            for (const site of this.germplasmGnpis.evaluationSites) {
                this.siteToBrapiLocation(site);
            }
        }
        if (this.germplasmGnpis.taxonIds && this.germplasmGnpis.taxonIds.length > 0) {
            this.addRefURL(this.germplasmGnpis.taxonIds);
        }
    }

    siteToBrapiLocation(site: Site) {
        if (site && site.siteId && site.latitude && site.longitude) {
            this.germplasmLocations.push({
                locationDbId: site.siteId,
                locationName: site.siteName,
                locationType: site.siteType,
                latitude: site.latitude,
                longitude: site.longitude
            });
        }
    }

    addRefURL(taxonIds: BrapiTaxonIds[]) {
        for (const taxonId of taxonIds) {
            if (taxonId.sourceName === 'NCBI') {
                taxonId.url = this.NCBI_URL + taxonId.taxonId;
            } else if (taxonId.sourceName === 'ThePlantList') {
                taxonId.url = this.THEPLANTLIST_URL + taxonId.taxonId;
            } else if (taxonId.sourceName === 'TAXREF') {
                taxonId.url = this.TAXREF_URL + taxonId.taxonId;
            } else if (taxonId.sourceName === 'CatalogueOfLife') {
                taxonId.url = this.CATALOGUEOFLIFE_URL + taxonId.taxonId;
            } else {
                taxonId.url = null;
            }
            this.taxonIdsWithURL.push(taxonId);
        }
    }

    // TODO: use a generic function to get path in object (or null if non-existent)
    checkProgeny() {
        return (this.germplasmProgeny
            && this.germplasmProgeny.length > 0);
    }

    checkBreeder() {
        return (this.germplasmGnpis.breeder)
            && ((this.germplasmGnpis.breeder.institute && this.germplasmGnpis.breeder.institute.instituteName)
                || this.germplasmGnpis.breeder.accessionCreationDate
                || this.germplasmGnpis.breeder.accessionNumber
                || this.germplasmGnpis.breeder.registrationYear
                || this.germplasmGnpis.breeder.deregistrationYear);
    }

    checkPedigree() {
        return (this.germplasmPedigree
            && (this.germplasmPedigree.parent1Name
                || this.germplasmPedigree.parent2Name
                || this.germplasmPedigree.crossingPlan
                || this.germplasmPedigree.crossingYear
                || this.germplasmPedigree.familyCode)
        );
    }

    checkCollectorInstituteObject() {
        return (
            this.germplasmGnpis.collector
            && this.germplasmGnpis.collector.institute
            && this.germplasmGnpis.collector.institute.instituteName);
    }

    checkCollectorInstituteFields() {
        return (this.germplasmGnpis.collector) &&
            (this.germplasmGnpis.collector.accessionNumber
                || this.germplasmGnpis.collector.accessionCreationDate
                || this.germplasmGnpis.collector.materialType
                || this.germplasmGnpis.collector.collectors
                || this.germplasmGnpis.collector.registrationYear
                || this.germplasmGnpis.collector.deregistrationYear
                || this.germplasmGnpis.collector.distributionStatus
            );
    }

    checkCollecting() {
        return (this.germplasmGnpis.collectingSite && this.germplasmGnpis.collectingSite.siteName)
            || (this.checkCollectorInstituteObject() || this.checkCollectorInstituteFields());
    }

    compareDonorInstitutes(a, b) {
        if (a.donorInstitute.instituteName < b.donorInstitute.instituteName) {
            return -1;
        }
        if (a.donorInstitute.instituteName > b.donorInstitute.instituteName) {
            return 1;
        }
        return 0;
    }

    compareAttributes(a, b) {
        if (a.attributeName < b.attributeName) {
            return -1;
        }
        if (a.attributeName > b.attributeName) {
            return 1;
        }
        return 0;
    }

    compareCollectionPopulationPanel(a, b) {
        if (a.name < b.name) {
            return -1;
        }
        if (a.name > b.name) {
            return 1;
        }
        return 0;
    }
}
