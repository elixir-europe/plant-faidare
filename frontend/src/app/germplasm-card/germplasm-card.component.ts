import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BrapiService } from '../brapi.service';
import { GnpisService } from '../gnpis.service';
import { Germplasm, GermplasmResult } from '../models/gnpis.germplasm.model';
import { BrapiGermplasmAttributes, BrapiGermplasmPedigree, BrapiGermplasmProgeny } from '../models/brapi.germplasm.model';

@Component({
    selector: 'gpds-germplasm-card',
    templateUrl: './germplasm-card.component.html',
    styleUrls: ['./germplasm-card.component.scss']
})

export class GermplasmCardComponent implements OnInit {

    constructor(private brapiService: BrapiService, private gnpisService: GnpisService, private route: ActivatedRoute) {
    }

    germplasmGnpis: Germplasm;
    germplasmPedigree: GermplasmResult<BrapiGermplasmPedigree>;
    germplasmProgeny: GermplasmResult<BrapiGermplasmProgeny>;
    germplasmAttributes: BrapiGermplasmAttributes[];
    germplasmId: string;
    germplasmPuid: string;

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
            const germplasmProgeny$ =  this.brapiService.germplasmProgeny(germplasmId).toPromise();
            germplasmProgeny$
                .then(germplasmProgeny => {
                    this.germplasmProgeny = germplasmProgeny;
                });

            const germplasmPedigree$ = this.brapiService.germplasmPedigree(germplasmId).toPromise();
            germplasmPedigree$
                .then(germplasmPedigree => {
                    this.germplasmPedigree = germplasmPedigree;
                });

            const germplasmAttributes$ = this.brapiService.germplasmAttributes(germplasmId).toPromise();
            germplasmAttributes$
                .then(germplasmAttributes => {
                    this.germplasmAttributes = germplasmAttributes.result.data;
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
}

