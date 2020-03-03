import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
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
import { formatFacets } from '../facets.component';

@Component({
    selector: 'faidare-small-facets',
    templateUrl: './small-facets.component.html',
    styleUrls: ['./small-facets.component.scss']
})
export class SmallFacetsComponent implements OnInit {

    @Input() facet: DataDiscoveryFacet;
    @Input() criteria$: BehaviorSubject<DataDiscoveryCriteria>;
    @Input() germplasmSearchCriteria$: BehaviorSubject<GermplasmSearchCriteria>;
    @Input() displayGermplasmResult$: BehaviorSubject<boolean>;

    @Output() changed = new EventEmitter<boolean>();
    formatFacets = formatFacets;


    localCriteria: DataDiscoveryCriteria;
    germplasmLocalCriteria: GermplasmSearchCriteria;
    criteriaIsEmpty = true;
    queryParams: Params;
    checkBoxes: FormGroup = new FormGroup({});
    germplasmDisplayCurrentState = false;

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

        this.displayGermplasmResult$.subscribe(value => {
            this.germplasmDisplayCurrentState = value;
        });

        if (this.criteria$) {
            this.criteria$.pipe(filter(c => c !== this.localCriteria))
                .subscribe(criteria => {
                    this.localCriteria = criteria;
                    this.getSelectedTerms(criteria);

                    this.criteriaIsEmpty = DataDiscoveryCriteriaUtils.checkCriteriaIsEmpty(criteria);
                });
        }

        if (this.germplasmSearchCriteria$ && this.germplasmDisplayCurrentState) {
            this.germplasmSearchCriteria$.pipe(filter(c => c !== this.germplasmLocalCriteria))
                .subscribe(germplasmCriteria => {
                    this.germplasmLocalCriteria = germplasmCriteria;
                    if (this.germplasmDisplayCurrentState) {
                        this.getSelectedTerms(germplasmCriteria);
                    }
                });
        }

        this.checkBoxes.valueChanges.subscribe(checkBoxesValue => {
            const selectedTerms = Object.keys(checkBoxesValue).filter(key => checkBoxesValue[key]);
            const multiSelection = Object.keys(checkBoxesValue).filter(key => checkBoxesValue[key] && key !== 'Germplasm');
            const unselectGermplasm = Object.keys(checkBoxesValue).filter(key => key === 'Germplasm' && !checkBoxesValue[key]);

            if ((multiSelection.length > 0 && this.facet.field === 'types') || unselectGermplasm.length > 0) {
                this.localCriteria = {
                    ...this.localCriteria,
                    facetFields: ['types', 'sources'],
                    [this.facet.field]: selectedTerms
                };
                this.criteria$.next(this.localCriteria);
                this.displayGermplasmResult$.next(false);
            }

            if (this.criteria$) {
                this.localCriteria = {
                    ...this.localCriteria,
                    [this.facet.field]: selectedTerms
                };
                this.criteria$.next(this.localCriteria);
            }
            if (this.germplasmSearchCriteria$ && this.germplasmDisplayCurrentState) {
                this.germplasmLocalCriteria = {
                    ...this.germplasmLocalCriteria,
                    [this.facet.field]: selectedTerms
                };
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

        this.criteriaIsEmpty = DataDiscoveryCriteriaUtils.checkCriteriaIsEmpty(criteria);
    }
}
