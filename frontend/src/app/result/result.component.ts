import { Component, OnInit } from '@angular/core';
import { NamedSelection } from '../form/suggestion-field/suggestion-field.component';
import { ActivatedRoute, Router } from '@angular/router';
import { DataDiscoveryCriteria } from '../model/criteria/dataDiscoveryCriteria';
import { BehaviorSubject } from 'rxjs';


@Component({
    selector: 'gpds-result',
    templateUrl: './result.component.html',
    styleUrls: ['./result.component.scss'],
})
export class ResultComponent implements OnInit {

    static EMPTY_CRITERIA: DataDiscoveryCriteria = {
        crops: [],
        germplasmLists: []
    };

    criteria: DataDiscoveryCriteria = { ...ResultComponent.EMPTY_CRITERIA };
    criteria$ = new BehaviorSubject<DataDiscoveryCriteria>(this.criteria);

    constructor(private route: ActivatedRoute, private router: Router) {
    }

    onSelectionChanges(namedSelection: NamedSelection) {
        this.criteria[namedSelection.name] = namedSelection.selection;
        this.criteria$.next(this.criteria);

        this.router.navigate(['.'], {
            relativeTo: this.route,
            queryParams: this.criteria
        });
    }

    ngOnInit(): void {
        this.route.queryParams.subscribe(queryParams => {
            this.criteria = { ...ResultComponent.EMPTY_CRITERIA };
            for (const key of Object.keys(queryParams)) {
                const value = queryParams[key];
                if (Array.isArray(value)) {
                    // Multiple value query param
                    this.criteria[key] = value;
                } else {
                    // Single value query param
                    this.criteria[key].push(value);
                }
            }
            this.criteria$.next(this.criteria);
        });
    }
}
