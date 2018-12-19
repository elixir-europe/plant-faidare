import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { BehaviorSubject, merge, Observable, of, Subject } from 'rxjs';
import { FormControl } from '@angular/forms';
import { NgbTypeahead, NgbTypeaheadSelectItemEvent } from '@ng-bootstrap/ng-bootstrap';
import { GnpisService } from '../../gnpis.service';
import { debounceTime, filter, map, switchMap } from 'rxjs/operators';
import { DataDiscoveryCriteria } from '../../model/data-discovery.model';

@Component({
    selector: 'gpds-suggestion-field',
    templateUrl: './suggestion-field.component.html',
    styleUrls: ['./suggestion-field.component.scss']
})
export class SuggestionFieldComponent implements OnInit {

    @Input() criteriaField: string;
    @Input() inputId: string;
    @Input() criteria$: BehaviorSubject<DataDiscoveryCriteria>;
    @Input() placeholder: string;

    private selectedKeys: string[] = [];

    focus$ = new Subject();

    click$ = new Subject();

    input = new FormControl();

    @ViewChild('inputElement') inputElement: ElementRef;

    @ViewChild('typeahead') typeahead: NgbTypeahead;

    private localCriteria: DataDiscoveryCriteria = null;
    private criteriaChanged = false;
    private lastSuggestions: string[] = null;

    constructor(private gnpisService: GnpisService) {
    }

    ngOnInit(): void {
        this.criteria$
            .pipe(filter(c => c !== this.localCriteria))
            .subscribe(newCriteria => {
                // When criteria changes
                this.criteriaChanged = true;

                this.localCriteria = newCriteria;

                const selectedInCriteria = newCriteria[this.criteriaField];
                if (selectedInCriteria) {
                    // Add selection from criteria into list of selected keys
                    this.selectedKeys = [...selectedInCriteria];
                } else {
                    // Clear list of selected keys
                    this.selectedKeys = [];
                }
            });
    }

    /**
     * Search elements corresponding to the user entry and return a list of
     * suggestions
     */
    search = (text$: Observable<string>): Observable<string[]> => {
        // Observable of clicks when the suggestion popup is closed
        const clicksWithClosedPopup$ = this.click$.pipe(
            filter(() => !this.typeahead.isPopupOpen())
        );

        let lastSearchTerm = '';

        // When new text or focus or click with popup closed
        return merge(text$, this.focus$, clicksWithClosedPopup$).pipe(
            debounceTime(250),
            switchMap((term: string) => {
                // Criteria hasn't changed and text hasn't changed and
                // suggestions already fetched
                if (
                    !this.criteriaChanged
                    && lastSearchTerm === term
                    && this.lastSuggestions
                ) {
                    // Return last suggestions without selected values
                    return of(this.removeAlreadySelected(this.lastSuggestions));
                }

                // Store text and suggestions for re-use
                lastSearchTerm = term;
                this.criteriaChanged = false;

                // Otherwise, we fetch new suggestions
                return this.fetchSuggestion(term);
            })
        );
    };

    /**
     * Fetch new suggestions for term
     */
    private fetchSuggestion(term: string): Observable<string[]> {
        // Fetch suggestions
        const suggestions$ = this.gnpisService.suggest(
            this.criteriaField, 10, term, this.localCriteria
        );

        // Filter out already selected suggestions
        return suggestions$.pipe(map(suggestions => {
            const filteredSuggestions = this.removeAlreadySelected(suggestions);
            this.lastSuggestions = filteredSuggestions;
            return filteredSuggestions;
        }));
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
        this.emitSelectionChange();

        // Empty input value and blur
        this.input.setValue('');
        setTimeout(() => {
            this.inputElement.nativeElement.blur();
        }, 200);
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
        this.localCriteria = {
            ...this.localCriteria,
            [this.criteriaField]: [...this.selectedKeys]
        };
        this.criteria$.next(this.localCriteria);
    }
}
