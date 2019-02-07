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
        this.route.paramMap.subscribe(paramMap => {
            this.germplasmId = paramMap.get('id');
            this.germplasmPuid = paramMap.get('puid');
        });

        const germplasmProgeny$ =  this.brapiService.germplasmProgeny(this.germplasmId).toPromise();
        germplasmProgeny$
            .then(germplasmProgeny => {
                this.germplasmProgeny = germplasmProgeny;
            });

        const germplasmPedigree$ = this.brapiService.germplasmPedigree(this.germplasmId).toPromise();
        germplasmPedigree$
            .then(germplasmPedigree => {
                this.germplasmPedigree = germplasmPedigree;
            });

        const germplasmAttributes$ = this.brapiService.germplasmAttributes(this.germplasmId).toPromise();
        germplasmAttributes$
            .then(germplasmAttributes => {
                this.germplasmAttributes = germplasmAttributes.result.data;
            });

        const germplasm$ = this.gnpisService.germplasm(this.germplasmId).toPromise();
        germplasm$
            .then(germplasmGnpis => {
                this.germplasmGnpis = germplasmGnpis;
            });

        this.loaded = Promise.all([germplasmProgeny$, germplasmPedigree$, germplasmAttributes$, germplasm$]);
        this.loaded.then(() => {
            this.loading = false;
        });


        // this.gnpisService.germplasmByPuid(germplasmPuid)
        //     .subscribe(germplasmGnpis => {
        //         this.germplasmGnpis = germplasmGnpis;
        //     });


    }
}

