import { Component, OnInit } from '@angular/core';
import { NamedSelection } from '../form/suggestion-field/suggestion-field.component';
import { ActivatedRoute, Router } from '@angular/router';
import { DataDiscoveryCriteria, EMPTY_CRITERIA } from '../model/criteria/dataDiscoveryCriteria';
import { BehaviorSubject } from 'rxjs';


@Component({
    selector: 'gpds-result',
    templateUrl: './result.component.html',
    styleUrls: ['./result.component.scss'],
})
export class ResultComponent implements OnInit {

    criteria$ = new BehaviorSubject<DataDiscoveryCriteria>({ ...EMPTY_CRITERIA });

    constructor(private route: ActivatedRoute, private router: Router) {
    }

    onSelectionChanges(namedSelection: NamedSelection) {
        this.router.navigate(['.'], {
            relativeTo: this.route,
            queryParams: { [namedSelection.name]: namedSelection.selection },
            queryParamsHandling: 'merge'
        });
    }

    ngOnInit(): void {
        this.route.queryParams.subscribe(queryParams => {
            const criteria = this.criteria$.value;
            for (const key of Object.keys(queryParams)) {
                const value = queryParams[key];
                if (Array.isArray(value)) {
                    // Multiple value query param
                    criteria[key] = value;
                } else {
                    // Single value query param
                    criteria[key] = [value];
                }
            }
            this.criteria$.next(criteria);
        });
    }
}
