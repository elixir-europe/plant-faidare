import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BrapiService } from '../brapi.service';
import { GnpisService } from '../gnpis.service';

@Component({
    selector: 'gpds-germplasm-card',
    templateUrl: './germplasm-card.component.html',
    styleUrls: ['./germplasm-card.component.scss']


})
export class GermplasmCardComponent implements OnInit {

    constructor(private brapiService: BrapiService, private gnpisService: GnpisService, private route: ActivatedRoute) {
    }

    germplasm: object = {};
    germplasmGnpis: object = {};
    germplasmPedigree: object = {};
    germplasmProgeny: object = {};
    germplasmAttributes: object = {};

    ngOnInit() {
        const germplasmId = this.route.snapshot.paramMap.get('id');

        this.brapiService.germplasm(germplasmId)
            .subscribe(germplasm => {
                this.germplasm = germplasm;
            });

        this.brapiService.germplasmProgeny(germplasmId)
            .subscribe(germplasmProgeny => {
                this.germplasmProgeny = germplasmProgeny;
            });

        this.brapiService.germplasmPedigree(germplasmId)
            .subscribe(germplasmPedigree => {
                this.germplasmPedigree = germplasmPedigree;
            });

        this.brapiService.germplasmAttributes(germplasmId)
            .subscribe(germplasmAttributes => {
                this.germplasmAttributes = germplasmAttributes;
            });

        this.gnpisService.germplasm(germplasmId)
            .subscribe(germplasmGnpis => {
                this.germplasmGnpis = germplasmGnpis;
            });
    }

}
