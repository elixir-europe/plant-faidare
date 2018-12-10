import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { merge, Observable, of, Subject } from 'rxjs';
import { FormControl } from '@angular/forms';
import { NgbTypeahead, NgbTypeaheadSelectItemEvent } from '@ng-bootstrap/ng-bootstrap';
import { GnpisService } from '../../gnpis.service';
import { debounceTime, filter, map, switchMap } from 'rxjs/operators';
import { DataDiscoveryCriteria } from '../../model/dataDiscoveryCriteria';
import { Param } from '../../model/common';

@Component({
    selector: 'gpds-suggestion-field',
    templateUrl: './suggestion-field.component.html',
    styleUrls: ['./suggestion-field.component.scss']
})
export class SuggestionFieldComponent implements OnInit {

    @Input() criteriaField: string;
    @Input() inputId: string;
    @Input() criteria$: Observable<DataDiscoveryCriteria>;

    @Output() selectionChange = new EventEmitter<Param>();

    focus$ = new Subject();
    click$ = new Subject();

    input = new FormControl();

    selectedKeys: string[] = [];

    @ViewChild('instance') instance: NgbTypeahead;

    private criteria: DataDiscoveryCriteria = null;
    private criteriaChanged = true;

    constructor(private gnpisService: GnpisService) {
    }

    ngOnInit(): void {
        this.criteria = null;
        this.criteria$.subscribe(criteria => {
            // When criteria changes
            this.criteriaChanged = true;

            if (!this.criteria) {
                // Criteria first initialized
                this.selectedKeys = criteria[this.criteriaField];
            }

            this.criteria = criteria;
        });
    }

    /**
     * Search elements corresponding to the user entry and return a list of
     * suggestions
     */
    search = (text$: Observable<string>): Observable<string[]> => {
        // Observable of clicks when the suggestion popup is closed
        const clicksWithClosedPopup$ = this.click$.pipe(
            filter(() => !this.instance.isPopupOpen())
        );

        let lastSearchTerm: string = null;
        let lastSuggestions: string[] = null;

        // When new text or focus or click with popup closed
        return merge(text$, this.focus$, clicksWithClosedPopup$).pipe(
            debounceTime(250),
            switchMap((term: string) => {
                // Criteria hasn't changed and text hasn't changed and
                // suggestions already fetched
                if (
                    !this.criteriaChanged
                    && lastSearchTerm === term
                    && lastSuggestions
                ) {
                    // Return last suggestions without selected values
                    return of(this.removeAlreadySelected(lastSuggestions));
                }

                // Otherwise, we fetch new suggestions
                const suggestions$ = this.fetchSuggestion(term);

                // Store text and suggestions for re-use
                lastSearchTerm = term;
                this.criteriaChanged = false;
                return suggestions$.pipe(map(suggestions => {
                    lastSuggestions = suggestions;
                    return suggestions;
                }));
            })
        );
    };

    /**
     * Fetch new suggestions for term
     */
    private fetchSuggestion(term: string): Observable<string[]> {
        // Fetch suggestions
        const suggestions$ = this.gnpisService.suggest(
            this.criteriaField, 10, term, this.criteria
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

    /**
     * Emit current selection when changed
     */
    private emitSelectionChange() {
        this.selectionChange.emit({
            key: this.criteriaField,
            value: this.selectedKeys
        });
    }
}
