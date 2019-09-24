import { Component, OnInit } from '@angular/core';
import { GermplasmService } from '../card-sortable-table/germplasm.services';

import { BrapiService } from '../brapi.service';
import {
    BrapiCriteriaUtils,
    BrapiGermplasm,
    GermplasmCriteria
} from '../models/brapi.model';
import { BehaviorSubject } from 'rxjs';
import { filter } from 'rxjs/operators';

@Component({
    selector: 'faidare-germplasm-result-page',
    templateUrl: './germplasm-result-page.component.html',
    styleUrls: ['./germplasm-result-page.component.scss']
})
export class GermplasmResultPageComponent implements OnInit {


    germplasm: BrapiGermplasm[];
    Germplasmcriteria$ = new BehaviorSubject<GermplasmCriteria> (BrapiCriteriaUtils.emptyCriteria());
    private localCriteria: GermplasmCriteria = BrapiCriteriaUtils.emptyCriteria();

    constructor(public service2: GermplasmService, public service: BrapiService) { }

    ngOnInit() {

        this.Germplasmcriteria$.pipe(filter(c => c !== this.localCriteria))
            .subscribe(newCriteria => {
                newCriteria.accessionNumbers = ["10936"];
                for (const field in newCriteria){
                    if (newCriteria[field]){
                        // this.localCriteria[field] = newCriteria[field];
                    }
                }
                this.searchGermplasm();

            });

        this.localCriteria.accessionNumbers = ["10936"];
        this.service2.data$.subscribe(germplasm =>{
            this.germplasm = germplasm;
        })

    }

    searchGermplasm() {
        this.service.germplasmSearch(this.localCriteria)
            .subscribe(response => {
                this.germplasm = response.result.data;
            });
    }
}

