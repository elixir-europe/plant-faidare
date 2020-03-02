import { Component, Input, OnInit } from '@angular/core';
import {
    DataDiscoveryCriteria,
    DataDiscoveryCriteriaUtils,
    DataDiscoverySource
} from '../../models/data-discovery.model';
import { BehaviorSubject } from 'rxjs';

@Component({
    selector: 'faidare-switch-button',
    templateUrl: './switch-button.component.html',
    styleUrls: ['./switch-button.component.scss']
})
export class SwitchButtonComponent implements OnInit {


    @Input() criteria$: BehaviorSubject<DataDiscoveryCriteria>;
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
                facetFields: ['types']
            };
        } else {
            this.localCriteria = {
                ...this.localCriteria,
                facetFields: ['types', 'sources']
            };
        }
        this.criteria$.next(this.localCriteria);
        this.displayGermplasmResult$.next(!this.germplasmDisplayCurrentState);
    }
}
