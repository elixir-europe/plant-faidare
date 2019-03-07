import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { BrapiService } from '../brapi.service';
import { GnpisService } from '../gnpis.service';
import { Germplasm, GermplasmProgeny, Site } from '../models/gnpis.germplasm.model';
import { BrapiGermplasmAttributes, BrapiGermplasmPedigree } from '../models/brapi.germplasm.model';
import { BrapiLocation } from '../models/brapi.model';

@Component({
    selector: 'gpds-germplasm-card',
    templateUrl: './germplasm-card.component.html',
    styleUrls: ['./germplasm-card.component.scss']
})

export class GermplasmCardComponent implements OnInit {

    alreadyInitialize = false;

    constructor(private brapiService: BrapiService,
                private gnpisService: GnpisService,
                private route: ActivatedRoute,
                private router: Router) {

        this.router.events.subscribe((event: any) => {
            // If it is a NavigationEnd event re-initalise the component
            if (this.alreadyInitialize && event instanceof NavigationEnd) {
                this.ngOnInit();
            }
        });
    }

    germplasmGnpis: Germplasm;
    germplasmPedigree: BrapiGermplasmPedigree;
    germplasmProgeny: GermplasmProgeny[];
    germplasmAttributes: BrapiGermplasmAttributes[];
    germplasmLocations: BrapiLocation[] = [];
    germplasmId: string;
    germplasmPuid: string;
    IMAGES_ACCESSION_URL = 'https://urgi.versailles.inra.fr/files/siregal/images/accession/';
    IMAGES_INSTITUTION_URL = 'https://urgi.versailles.inra.fr/files/siregal/images//institution/';
    IMAGES_BRC_URL = 'https://urgi.versailles.inra.fr/files/siregal/images/grc/inra_brc_en.png';


    loaded: Promise<any>;
    loading = true;


    ngOnInit() {

        // console.log(this.route.snapshot);
        // console.log(this.route);
        this.germplasmId = this.route.snapshot.queryParams.id;
        this.germplasmPuid = this.route.snapshot.queryParams.pui;

        const germplasm$ = this.getGermplasm(this.germplasmId, this.germplasmPuid);
        germplasm$.then(result => {
            const germplasmId = this.germplasmId ? this.germplasmId : result.germplasmDbId;

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
                    if (germplasmAttributes.result) {
                        this.germplasmAttributes = germplasmAttributes.result.data;
                    }

                });
        });

        this.loaded = Promise.all([germplasm$]);
        this.loaded.then(() => {
            this.loading = false;
            this.alreadyInitialize = true;
        });

    }

    getGermplasm(id: string, pui: string): Promise<Germplasm> {
        let germplasm$: Promise<Germplasm>;
        if (id) {
            germplasm$ = this.gnpisService.germplasm(id).toPromise();
            germplasm$
                .then(germplasmGnpis => {
                    this.germplasmGnpis = germplasmGnpis;
                    this.germplasmProgeny = germplasmGnpis.children;
                    this.siteToBrapiLocation(this.germplasmGnpis.collectingSite);
                    this.siteToBrapiLocation(this.germplasmGnpis.originSite);
                    for (const site of this.germplasmGnpis.evaluationSites) {
                        this.siteToBrapiLocation(site);
                    }
                });
        } else {
            germplasm$ = this.gnpisService.germplasmByPuid(pui).toPromise();
            germplasm$
                .then(germplasmGnpis => {
                    this.germplasmGnpis = germplasmGnpis;
                });
        }
        return germplasm$;
    }

    siteToBrapiLocation(site: Site) {
        if (site.siteId && site.latitude && site.longitude) {
            this.germplasmLocations.push({
                locationDbId: site.siteId,
                locationName: site.siteName,
                locationType: site.siteType,
                latitude: site.latitude,
                longitude: site.longitude
            });
        }
    }

    // TODO: use a generic function to get path in object (or null if non-existent)
    checkProgeny() {
        return (this.germplasmProgeny
            && this.germplasmProgeny.length > 0);
    }

    checkBreeder() {
        return (this.germplasmGnpis.breeder)
            && (this.germplasmGnpis.breeder.institute.instituteName
                || this.germplasmGnpis.breeder.germplasmPUI
                || this.germplasmGnpis.breeder.accessionNumber
                || this.germplasmGnpis.breeder.accessionCreationDate
                || this.germplasmGnpis.breeder.materialType
                || this.germplasmGnpis.breeder.collectors
                || this.germplasmGnpis.breeder.registrationYear
                || this.germplasmGnpis.breeder.deregistrationYear
                || this.germplasmGnpis.breeder.distributionStatus);
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
            (this.germplasmGnpis.collector.germplasmPUI
                || this.germplasmGnpis.collector.accessionNumber
                || this.germplasmGnpis.collector.accessionCreationDate
                || this.germplasmGnpis.collector.materialType
                || this.germplasmGnpis.collector.collectors
                || this.germplasmGnpis.collector.registrationYear
                || this.germplasmGnpis.collector.deregistrationYear
                || this.germplasmGnpis.collector.distributionStatus
            );
    }

    checkOriginCollecting() {

        return (this.germplasmGnpis.originSite && this.germplasmGnpis.originSite.siteName)
            || (this.germplasmGnpis.collectingSite && this.germplasmGnpis.collectingSite.siteName)
            || (this.checkCollectorInstituteObject() || this.checkCollectorInstituteFields());
    }
}
