import { Component, Input, OnInit } from '@angular/core';
import { DataDiscoveryCriteria, DataDiscoveryFacet } from '../../models/data-discovery.model';
import { FormControl, FormGroup } from '@angular/forms';
import { BehaviorSubject } from 'rxjs';
import { filter } from 'rxjs/operators';

@Component({
    selector: 'gpds-facets',
    templateUrl: './facets.component.html',
    styleUrls: ['./facets.component.scss']
})
export class FacetsComponent implements OnInit {

    @Input() facet: DataDiscoveryFacet;
    @Input() criteria$: BehaviorSubject<DataDiscoveryCriteria>;


    localCriteria: DataDiscoveryCriteria;
    checkBoxes: FormGroup = new FormGroup({});

    constructor() {
    }

    ngOnInit(): void {
        for (const term of this.facet.terms) {
            const control = new FormControl(false);
            this.checkBoxes.addControl(term.term, control);
        }

        this.criteria$.pipe(filter(c => c !== this.localCriteria))
            .subscribe(criteria => {
                this.localCriteria = criteria;
                const selectedTerms = criteria[this.facet.field] || [];

                for (const [key, control] of Object.entries(this.checkBoxes.controls)) {
                    const isSelected = selectedTerms.indexOf(key) >= 0;
                    control.setValue(isSelected, { emitEvent: false });
                }
            });

        this.checkBoxes.valueChanges.subscribe(values => {
            const selectedTerms = Object.keys(values).filter(key => values[key]);
            this.localCriteria = {
                ...this.localCriteria,
                [this.facet.field]: selectedTerms
            };
            this.criteria$.next(this.localCriteria);
        });
    }


}
