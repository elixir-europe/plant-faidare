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
    displayAdvanceGermplasmSearchButton: boolean;
    displayGermplasmCurrentState = false;

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
            this.displayGermplasmCurrentState = value;
        });

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
                    if (this.displayGermplasmCurrentState) {
                        this.getSelectedTerms(germplasmCriteria);
                    }
                });
        }

        this.checkBoxes.valueChanges.subscribe(values => {
            const selectedTerms = Object.keys(values).filter(key => values[key]);
            const multiSelection = Object.keys(values).filter(key => values[key] && key !== 'Germplasm');
            const unselectGermplasm = Object.keys(values).filter(key => key === 'Germplasm' && !values[key]);

            if ((multiSelection.length > 0 && this.facet.field === 'types') || unselectGermplasm.length > 0) {
                this.switchGermplasmResult();
            }

            this.showAndHideAdvanceGermplasmSearch(selectedTerms);
            if (this.criteria$) {
                this.localCriteria = {
                    ...this.localCriteria,
                    [this.facet.field]: selectedTerms
                };
                this.criteria$.next(this.localCriteria);
            }
            if (this.germplasmSearchCriteria$) {
                this.germplasmLocalCriteria = {
                    ...this.germplasmLocalCriteria,
                    [this.facet.field]: selectedTerms
                };
            }
            this.germplasmSearchCriteria$.next(this.germplasmLocalCriteria);
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

    showAndHideAdvanceGermplasmSearch(typeList: String[]) {
        const facetIsTypes = this.facet.field === 'types';
        const GermplasmSelected = typeList.includes('Germplasm');
        this.displayAdvanceGermplasmSearchButton = facetIsTypes && GermplasmSelected;
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
