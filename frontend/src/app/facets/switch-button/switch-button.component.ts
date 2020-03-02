import { Component, Input, OnInit } from '@angular/core';
import {
    DataDiscoveryCriteria,
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
    displayGermplasmCurrentState = false;

    constructor() {
    }

    ngOnInit() {

        this.displayGermplasmResult$.subscribe(value => {
            this.displayGermplasmCurrentState = value;
        });
    }

    switchGermplasmResult() {
        if (!this.displayGermplasmCurrentState) {
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
        this.displayGermplasmResult$.next(!this.displayGermplasmCurrentState);
    }
}
