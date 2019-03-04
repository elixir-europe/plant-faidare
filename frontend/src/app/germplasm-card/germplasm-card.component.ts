import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { BrapiService } from '../brapi.service';
import { GnpisService } from '../gnpis.service';
import { Germplasm, GermplasmProgeny } from '../models/gnpis.germplasm.model';
import { BrapiGermplasmAttributes, BrapiGermplasmPedigree } from '../models/brapi.germplasm.model';

@Component({
    selector: 'gpds-germplasm-card',
    templateUrl: './germplasm-card.component.html',
    styleUrls: ['./germplasm-card.component.scss']
})

export class GermplasmCardComponent implements OnInit {


    constructor(private brapiService: BrapiService,
                private gnpisService: GnpisService,
                private route: ActivatedRoute,
                private router: Router) {

        this.router.events.subscribe((event: any) => {
            // If it is a NavigationEnd event re-initalise the component
            if (event instanceof NavigationEnd) {
                this.ngOnInit();
            }
        });
    }

    germplasmGnpis: Germplasm;
    germplasmPedigree: BrapiGermplasmPedigree;
    germplasmProgeny: GermplasmProgeny[];
    germplasmAttributes: BrapiGermplasmAttributes[];
    germplasmId: string;
    germplasmPuid: string;
    IMAGES_SIREGAL_URL = 'https://urgi.versailles.inra.fr/files/siregal/images/accession';

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

    // TODO: use a generic function to get path in object (or null if non-existent)
    testProgeny() {
        return (this.germplasmProgeny
            && this.germplasmProgeny.length > 0);
    }

    testPedigree() {
        return (this.germplasmPedigree
            && (this.germplasmPedigree.parent1Name
                || this.germplasmPedigree.parent2Name
                || this.germplasmPedigree.crossingPlan
                || this.germplasmPedigree.crossingYear
                || this.germplasmPedigree.familyCode)
        );
    }


    testCollectorInstituteObject() {
        return (
            this.germplasmGnpis.collector
            && this.germplasmGnpis.collector.institute
            && this.germplasmGnpis.collector.institute.instituteName);
    }

    testCollectorInstituteFields() {
        return (
            this.germplasmGnpis.collector.germplasmPUI
            || this.germplasmGnpis.collector.accessionNumber
            || this.germplasmGnpis.collector.accessionCreationDate
            || this.germplasmGnpis.collector.materialType
            || this.germplasmGnpis.collector.collectors
            || this.germplasmGnpis.collector.registrationYear
            || this.germplasmGnpis.collector.deregistrationYear
            || this.germplasmGnpis.collector.distributionStatus
        );
    }

    testOrigin() {

        return (this.germplasmGnpis.originSite && this.germplasmGnpis.originSite.siteName)
            || (this.germplasmGnpis.donors && this.germplasmGnpis.donors.length > 0)
            || ((this.testCollectorInstituteObject() || this.testCollectorInstituteFields())
                || (this.germplasmGnpis.collectingSite && this.germplasmGnpis.collectingSite.siteName))
            || (this.germplasmGnpis.breeder);
    }
}

