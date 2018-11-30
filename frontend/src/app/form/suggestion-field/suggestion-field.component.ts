import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { merge, Observable, of, Subject } from 'rxjs';
import { FormControl } from '@angular/forms';
import { NgbTypeahead, NgbTypeaheadSelectItemEvent } from '@ng-bootstrap/ng-bootstrap';
import { GnpisService } from '../../gnpis.service';
import { debounceTime, filter, map, switchMap } from 'rxjs/operators';
import { DataDiscoveryCriteria } from '../../model/criteria/dataDiscoveryCriteria';

export interface NamedSelection {
    name: string;
    selection: string[];
}

@Component({
    selector: 'gpds-suggestion-field',
    templateUrl: './suggestion-field.component.html',
    styleUrls: ['./suggestion-field.component.scss']
})
export class SuggestionFieldComponent implements OnInit {

    @Input() criteriaField: string;
    @Input() inputId: string;
    @Input() criteria$: Observable<DataDiscoveryCriteria>;

    @Output() selectionChange = new EventEmitter<NamedSelection>();

    focus$ = new Subject();
    click$ = new Subject();

    input = new FormControl();

    selectedKeys: string[] = [];

    @ViewChild('instance') instance: NgbTypeahead;

    private lastCriteria: DataDiscoveryCriteria;
    private criteriaChanged = true;

    constructor(private gnpisService: GnpisService) {
    }

    ngOnInit(): void {
        this.criteria$.subscribe(criteria => {
            this.criteriaChanged = true;
            this.lastCriteria = criteria;
            this.selectedKeys = criteria[this.criteriaField];
        });
    }

    /**
     * Search elements corresponding to the user entry and return a list of
     * suggestions
     */
    search = (text$: Observable<string>): Observable<string[]> => {
        const clicksWithClosedPopup$ = this.click$.pipe(
            filter(() => !this.instance.isPopupOpen())
        );

        let lastSearchTerm: string = null;
        let lastSuggestions: string[] = null;

        return merge(text$, this.focus$, clicksWithClosedPopup$).pipe(
            debounceTime(250),
            switchMap((term: string) => {
                if (!this.criteriaChanged && lastSearchTerm === term && lastSuggestions) {
                    return of(this.removeAlreadySelected(lastSuggestions));
                }
                const suggestions$ = this.fetchSuggestion(term);
                lastSearchTerm = term;
                this.criteriaChanged = false;
                return suggestions$.pipe(map(suggestions => {
                    lastSuggestions = suggestions;
                    return suggestions;
                }));
            })
        );
    };

    fetchSuggestion(term: string): Observable<string[]> {
        // Fetch suggestions
        const suggestions$ = this.gnpisService.suggest(
            this.criteriaField, 10, term, this.lastCriteria
        );

        // Filter out already selected suggestions
        return suggestions$.pipe(
            map(this.removeAlreadySelected.bind(this))
        );
    }

    /**
     * Remove suggestions that are in the current selection
     * @param suggestions a list of suggestion
     * @return list of suggestion that are not selected (in this.selectedKeys)
     */
    private removeAlreadySelected(suggestions: string[]) {
        return suggestions.filter(suggestion => {
            return this.selectedKeys.indexOf(suggestion) < 0;
        });
    }

    /**
     * Add the element selected by the user in the list of the selected elements
     */
    selectKey($event: NgbTypeaheadSelectItemEvent) {
        $event.preventDefault();
        this.selectedKeys.push($event.item);
        this.input.setValue('');
        this.emitSelectionChange();
    }

    /**
     * Remove the element selected by the user in the list of the selected
     * elements
     */
    removeKey(key: string) {
        const index = this.selectedKeys.indexOf(key);
        this.selectedKeys = [
            ...this.selectedKeys.slice(0, index),
            ...this.selectedKeys.slice(index + 1)
        ];
        this.emitSelectionChange();
    }

    private emitSelectionChange() {
        this.selectionChange.emit({
            name: this.criteriaField,
            selection: this.selectedKeys
        });
    }
}
