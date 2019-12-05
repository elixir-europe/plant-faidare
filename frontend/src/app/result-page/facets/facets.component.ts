import { Component, Input, OnInit } from '@angular/core';
import {
    DataDiscoveryCriteria,
    DataDiscoveryCriteriaUtils,
    DataDiscoveryFacet
} from '../../models/data-discovery.model';
import { FormControl, FormGroup } from '@angular/forms';
import { BehaviorSubject } from 'rxjs';
import { filter } from 'rxjs/operators';
import { Params } from '@angular/router';
import { GermplasmSearchCriteria } from '../../models/gnpis.model';

@Component({
    selector: 'faidare-facets',
    templateUrl: './facets.component.html',
    styleUrls: ['./facets.component.scss']
})
export class FacetsComponent implements OnInit {

    @Input() facet: DataDiscoveryFacet;
    @Input() criteria$: BehaviorSubject<DataDiscoveryCriteria>;
    @Input() germplasmSearchCriteria$: BehaviorSubject<GermplasmSearchCriteria>;
    @Input() displayGermplasmResult$: BehaviorSubject<boolean>;


    localCriteria: DataDiscoveryCriteria;
    germplasmLocalCriteria: GermplasmSearchCriteria;
    criteriaIsEmpty = true;
    queryParams: Params;
    checkBoxes: FormGroup = new FormGroup({});
    displayAdvanceGermplasmSearchButton: boolean;

    constructor() {
    }

    ngOnInit(): void {
        for (const term of this.facet.terms) {
            const control = new FormControl(false);
            this.checkBoxes.addControl(term.term, control);
        }
        if (this.facet.field === 'types') {
            const switchControl = new FormControl(false);
            this.checkBoxes.addControl('selectSwitchButton', switchControl);
        }

        if (this.criteria$) {
            this.criteria$.pipe(filter(c => c !== this.localCriteria))
                .subscribe(criteria => {
                    this.localCriteria = criteria;
                    this.getSelectedTerms(criteria);

                    if (criteria.types) {
                        this.showAndHideAdvanceGermplasmSearch(criteria.types);
                    }
                });
        }

        if (this.germplasmSearchCriteria$) {
            this.germplasmSearchCriteria$.pipe(filter(c => c !== this.germplasmLocalCriteria))
                .subscribe(germplasmCriteria => {
                    this.germplasmLocalCriteria = germplasmCriteria;
                    this.getSelectedTerms(germplasmCriteria);
                });
        }

        this.checkBoxes.valueChanges.subscribe(values => {
            const selectedTerms = Object.keys(values).filter(key => values[key]);
            this.showAndHideAdvanceGermplasmSearch(selectedTerms);
            if (this.criteria$) {
                this.localCriteria = {
                    ...this.localCriteria,
                    [this.facet.field]: selectedTerms
                };
                this.criteria$.next(this.localCriteria);
            }
            if (this.germplasmSearchCriteria$) {
                const field = this.facet.field;
                if (field === 'holding institute') {
                    this.germplasmLocalCriteria = {
                        ...this.germplasmLocalCriteria,
                        holdingInstitute: selectedTerms
                    };
                }
                if (field === 'Biological status / Genetic nature') {
                    this.germplasmLocalCriteria = {
                        ...this.germplasmLocalCriteria,
                        biologicalStatus: selectedTerms,
                        geneticNature: selectedTerms
                    };
                }
                if (field !== 'Biological status / Genetic nature' && field !== 'holding institute') {
                    this.germplasmLocalCriteria = {
                        ...this.germplasmLocalCriteria,
                        [this.facet.field]: selectedTerms
                    };
                }
                this.germplasmSearchCriteria$.next(this.germplasmLocalCriteria);
            }
        });
    }

    getSelectedTerms(criteria) {
        const selectedTerms = criteria[this.facet.field] || [];

        for (const [key, control] of Object.entries(this.checkBoxes.controls)) {
            const isSelected = selectedTerms.indexOf(key) >= 0;
            control.setValue(isSelected, { emitEvent: false });
        }

        // this.queryParams = this.queryParamsForGermplasmPage(criteria);

        this.criteriaIsEmpty = DataDiscoveryCriteriaUtils.checkCriteriaIsEmpty(criteria);
    }

    showAndHideAdvanceGermplasmSearch(typeList: String[]) {
        const facetIsTypes = this.facet.field === 'types';
        const GermplasmSelected = typeList.includes('Germplasm');
        this.displayAdvanceGermplasmSearchButton = facetIsTypes && GermplasmSelected;
    }

    switchToGermplasmResult() {
        let currentState = false;
        this.displayGermplasmResult$.subscribe(value => {
            currentState = value;
        });
        for (const [key, control] of Object.entries(this.checkBoxes.controls)) {
            if (key === 'selectSwitchButton') {
                control.setValue(currentState, { emitEvent: false });
            }
        }
        this.displayGermplasmResult$.next(!currentState);
    }
}
