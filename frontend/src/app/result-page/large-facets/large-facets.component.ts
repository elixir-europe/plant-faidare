import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import {
    DataDiscoveryCriteria,
    DataDiscoveryFacet,
    DataDiscoverySource
} from '../../models/data-discovery.model';
import { BehaviorSubject, merge, Observable, Subject } from 'rxjs';
import { distinctUntilChanged, filter, map } from 'rxjs/operators';
import { FormControl } from '@angular/forms';
import { NgbTypeaheadSelectItemEvent } from '@ng-bootstrap/ng-bootstrap';
import { GermplasmSearchCriteria } from '../../models/gnpis.model';
import { GnpisService } from '../../gnpis.service';

export type FacetTermOrRefine = {
    term: string;
    label: string;
    count: number;
} | 'REFINE';
const maxResultsDisplayed = 8;

@Component({
    selector: 'faidare-large-facets',
    templateUrl: './large-facets.component.html',
    styleUrls: ['./large-facets.component.scss']
})
export class LargeFacetsComponent implements OnInit {

    @Input() facet: DataDiscoveryFacet;
    @Input() criteria$: BehaviorSubject<DataDiscoveryCriteria>;
    @Input() germplasmSearchCriteria$: BehaviorSubject<GermplasmSearchCriteria>;

    @ViewChild('typeahead') typeahead: ElementRef<HTMLInputElement>;

    localCriteria: DataDiscoveryCriteria;
    germplasmLocalCriteria: GermplasmSearchCriteria;
    focus$ = new Subject<string>();
    selectedTerms: { [key: string]: string[]; } = {};
    criterion = new FormControl('');
    sources: DataDiscoverySource[];

    constructor(private gnpisService: GnpisService) {
    }

    ngOnInit(): void {

        this.gnpisService.sources$.subscribe(sources => {
            this.sources = sources;
        });

        if (this.criteria$) {
            this.criteria$.pipe(filter(c => c !== this.localCriteria))
                .subscribe(criteria => {
                    this.localCriteria = { ...criteria };
                    this.selectedTerms[this.facet.field] = criteria[this.facet.field] || [];
                });
        }

        if (this.germplasmSearchCriteria$) {
            this.germplasmSearchCriteria$.pipe(filter(c => c !== this.germplasmLocalCriteria))
                .subscribe(germplasmCriteria => {
                    this.germplasmLocalCriteria = germplasmCriteria;
                    this.selectedTerms[this.facet.field] = germplasmCriteria[this.facet.field] || [];
                });
        }
    }

    search = (text$: Observable<string>): Observable<Array<FacetTermOrRefine>> => {
        const inputFocus$ = this.focus$;
        const merged$ = merge(text$, inputFocus$)
            .pipe(
                distinctUntilChanged(),
                map(searchTerm => {
                    const allMatchingBuckets = this.facet.terms
                        // returns values not already selected
                        .filter(terms => !this.selectedTerms[this.facet.field].includes(terms.term)
                            // and that contains the term, ignoring the case
                            && this.displayableKey(terms.label).toLowerCase().includes(searchTerm.toString().toLowerCase()));

                    // return the first N results
                    const result: Array<FacetTermOrRefine> = allMatchingBuckets.slice(0, maxResultsDisplayed);

                    // if more results exist, add a fake refine bucket
                    if (allMatchingBuckets.length > maxResultsDisplayed) {
                        result.push('REFINE');
                    }

                    return result;
                }));
        return merged$;
    };

    displayableKey(key: string): string {
        return key === 'NULL' ? 'None' : key;
    }

    displaySourceName(sourceId: string) {
        for (const source of this.sources) {
            if (source['@id'] === sourceId) {
                return source['schema:name'];
            }
        }
        return sourceId;
    }

    selectKey(event: NgbTypeaheadSelectItemEvent) {
        event.preventDefault();
        const selected: FacetTermOrRefine = event.item;
        if (selected !== 'REFINE') {
            // the item field of the event contains the facet term
            // we push the selected key to our collection of keys
            if (this.criteria$) {
                if (this.localCriteria[this.facet.field]) {
                    this.localCriteria[this.facet.field].push(event.item.term);
                } else {
                    this.localCriteria[this.facet.field] = [event.item.term];
                }
            }
            if (this.germplasmSearchCriteria$) {
                if (this.germplasmLocalCriteria[this.facet.field]) {
                    this.germplasmLocalCriteria[this.facet.field].push(event.item.term);
                } else {
                    this.germplasmLocalCriteria[this.facet.field] = [event.item.term];
                }
            }
            this.emitChanges();
        }
        this.typeahead.nativeElement.blur();
    }

    emitChanges() {
        if (this.criteria$) {
            this.criteria$.next(this.localCriteria);
        }
        if (this.germplasmSearchCriteria$) {
            this.germplasmSearchCriteria$.next(this.germplasmLocalCriteria);
        }
    }

    removeKey(key: string) {
        this.selectedTerms[this.facet.field] =
            this.removeFromList(this.selectedTerms[this.facet.field], key);
        if (this.criteria$) {
            this.localCriteria[this.facet.field] =
                this.removeFromList(this.localCriteria[this.facet.field], key);
        }
        if (this.germplasmSearchCriteria$) {
            this.germplasmLocalCriteria[this.facet.field] =
                this.removeFromList(this.germplasmLocalCriteria[this.facet.field], key);
        }
        this.emitChanges();
    }

    removeFromList(list: string[], elem: string) {
        const index = list.indexOf(elem);
        return [
            ...list.slice(0, index),
            ...list.slice(index + 1)];
    }

}
