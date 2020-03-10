import { Component, Input, OnInit } from '@angular/core';
import {
    DataDiscoveryCriteria,
    DataDiscoveryCriteriaUtils,
    DataDiscoverySource
} from '../../models/data-discovery.model';
import { BehaviorSubject } from 'rxjs';
import { GermplasmSearchCriteria } from '../../models/gnpis.model';

@Component({
    selector: 'faidare-switch-button',
    templateUrl: './switch-button.component.html',
    styleUrls: ['./switch-button.component.scss']
})
export class SwitchButtonComponent implements OnInit {


    @Input() criteria$: BehaviorSubject<DataDiscoveryCriteria>;
    @Input() germplasmSearchCriteria$: BehaviorSubject<GermplasmSearchCriteria>;
    @Input() displayGermplasmResult$: BehaviorSubject<boolean>;
    @Input() facetFiled: string;
    @Input() switchTitle: string;

    localCriteria: DataDiscoveryCriteria;
    sources: DataDiscoverySource[];
    criteriaIsEmpty = true;
    germplasmDisplayCurrentState = false;

    constructor() {
    }

    ngOnInit() {

        this.displayGermplasmResult$.subscribe(value => {
            this.germplasmDisplayCurrentState = value;
        });

        if (this.criteria$) {
            this.criteria$.subscribe(criteria => {
                this.localCriteria = criteria;
                this.criteriaIsEmpty = DataDiscoveryCriteriaUtils.checkCriteriaIsEmpty(criteria);
            });
        }
    }

    switchGermplasmResult() {

        if (!this.germplasmDisplayCurrentState) {
            this.localCriteria = {
                ...this.localCriteria,
                facetFields: ['types'],
                germplasmLists: null,
                types: this.localCriteria.types ? this.localCriteria.types : ['Germplasm']
            };
        } else {
            this.localCriteria = {
                ...this.localCriteria,
                facetFields: ['types', 'sources'],
            };
            this.germplasmSearchCriteria$.next(DataDiscoveryCriteriaUtils.emptyGermplasmSearchCriteria());
        }
        this.criteria$.next(this.localCriteria);
        this.displayGermplasmResult$.next(!this.germplasmDisplayCurrentState);
    }
}
