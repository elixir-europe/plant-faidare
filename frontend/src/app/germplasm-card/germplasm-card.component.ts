import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BrapiService } from '../brapi.service';
import { GnpisService } from '../gnpis.service';
import { BrapiAttributeData, BrapiGermplasmPedigree, BrapiLocation, BrapiTaxonIds } from '../models/brapi.model';
import { Children, Germplasm, Site } from '../models/gnpis.model';
import { environment } from '../../environments/environment';

@Component({
    selector: 'faidare-germplasm-card',
    templateUrl: './germplasm-card.component.html',
    styleUrls: ['./germplasm-card.component.scss']
})

export class GermplasmCardComponent implements OnInit {

    constructor(private brapiService: BrapiService,
                private gnpisService: GnpisService,
                private route: ActivatedRoute) {
    }

    taxonIdsWithURL: BrapiTaxonIds[] = [];
    germplasmGnpis: Germplasm;
    germplasmPedigree: BrapiGermplasmPedigree;
    germplasmProgeny: Children[];
    germplasmAttributes: BrapiAttributeData[];
    germplasmLocations: BrapiLocation[] = [];
    germplasmTaxon: string;
    germplasmTaxonAuthor: string;
    toReplace = /_/g;

    loaded: Promise<void>;
    loading = true;

    async ngOnInit() {
        this.route.queryParams.subscribe(() => {
            const { id, pui } = this.route.snapshot.queryParams;

            this.loaded = this.gnpisService.getGermplasm({ id, pui }).toPromise()
                .then(germplasm => {
                    const germplasmId = id || germplasm.germplasmDbId;
                    this.germplasmGnpis = germplasm;
                    this.getTaxon();
                    this.reformatData();

                    // TODO use the progeny call when the information about parent will be added.
                    /*const germplasmProgeny$ = this.brapiService.germplasmProgeny(germplasmId).toPromise();
                    germplasmProgeny$
                        .then(germplasmProgeny => {
                            this.germplasmProgeny = germplasmProgeny.result;
                        });*/

                    this.germplasmPedigree = null;
                    this.brapiService.germplasmPedigree(germplasmId)
                        .subscribe(germplasmPedigree => {
                            this.germplasmPedigree = germplasmPedigree.result;
                        });

                    this.germplasmAttributes = [];
                    this.brapiService.germplasmAttributes(germplasmId)
                        .subscribe(germplasmAttributes => {
                            if (germplasmAttributes.result && germplasmAttributes.result.data) {
                                this.germplasmAttributes = germplasmAttributes.result.data.sort(this.compareAttributes);
                            }
                        });

                    this.loading = false;
                });
        });

    }

    getTaxon() {
        if (this.germplasmGnpis.genusSpeciesSubtaxa) {
            this.germplasmTaxon = this.germplasmGnpis.genusSpeciesSubtaxa;
            this.germplasmTaxonAuthor = this.germplasmGnpis.subtaxaAuthority;
        } else if (this.germplasmGnpis.genusSpecies) {
            this.germplasmTaxon = this.germplasmGnpis.genusSpecies;
            this.germplasmTaxonAuthor = this.germplasmGnpis.speciesAuthority;
        } else if (this.germplasmGnpis.subtaxa) {
            this.germplasmTaxon = this.germplasmGnpis.genus + ' ' + this.germplasmGnpis.species + ' ' + this.germplasmGnpis.subtaxa;
            this.germplasmTaxonAuthor = this.germplasmGnpis.subtaxaAuthority;
        } else if (this.germplasmGnpis.species) {
            this.germplasmTaxon = this.germplasmGnpis.genus + ' ' + this.germplasmGnpis.species;
            this.germplasmTaxonAuthor = this.germplasmGnpis.speciesAuthority;
        } else {
            this.germplasmTaxon = this.germplasmGnpis.genus;
            this.germplasmTaxonAuthor = '';
        }
    }

    // TODO: do this in ETL!
    reformatData() {
        if (this.germplasmGnpis.children) {
            this.germplasmProgeny = this.germplasmGnpis.children.sort();
        }
        if (this.germplasmGnpis.donors) {
            this.germplasmGnpis.donors.sort(this.compareDonorInstitutes);
        }
        if (this.germplasmGnpis.collection) {
            this.germplasmGnpis.collection.sort(this.compareCollectionPopulationPanel);
        }
        if (this.germplasmGnpis.population) {
            this.germplasmGnpis.population.sort(this.compareCollectionPopulationPanel);
        }
        if (this.germplasmGnpis.panel) {
            this.germplasmGnpis.panel.sort(this.compareCollectionPopulationPanel);
        }
        if (this.germplasmGnpis.collectingSite) {
            this.germplasmGnpis.collectingSite.siteId = btoa('urn:URGI/location/' + this.germplasmGnpis.collectingSite.siteId);
            this.siteToBrapiLocation(this.germplasmGnpis.collectingSite);
        }
        if (this.germplasmGnpis.originSite) {
            this.germplasmGnpis.originSite.siteId = btoa('urn:URGI/location/' + this.germplasmGnpis.originSite.siteId);
            this.siteToBrapiLocation(this.germplasmGnpis.originSite);
        }
        if (this.germplasmGnpis.evaluationSites && this.germplasmGnpis.evaluationSites.length > 0) {
            for (const site of this.germplasmGnpis.evaluationSites) {
                site.siteId = btoa('urn:URGI/location/' + site.siteId);
                this.siteToBrapiLocation(site);
            }
        }
        if (this.germplasmGnpis.taxonIds && this.germplasmGnpis.taxonIds.length > 0) {
            this.addRefURL(this.germplasmGnpis.taxonIds);
        }
    }

    // TODO: do this in ETL!
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
            if (environment.taxaLinks[taxonId.sourceName]) {
                taxonId.url = environment.taxaLinks[taxonId.sourceName] + taxonId.taxonId;
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
