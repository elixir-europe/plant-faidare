import { Component, OnInit } from '@angular/core';
import { NamedSelection } from '../form/suggestion-field/suggestion-field.component';
import { ActivatedRoute, Router } from '@angular/router';
import { DataDiscoveryCriteria, EMPTY_CRITERIA } from '../model/dataDiscoveryCriteria';
import { BehaviorSubject } from 'rxjs';
import { DataDiscoveryDocument } from '../model/dataDiscoveryDocument';
import { GnpisService } from '../gnpis.service';


@Component({
    selector: 'gpds-result',
    templateUrl: './result-page.component.html',
    styleUrls: ['./result-page.component.scss'],
})
export class ResultPageComponent implements OnInit {

    criteria$ = new BehaviorSubject<DataDiscoveryCriteria>({ ...EMPTY_CRITERIA });
    documents: DataDiscoveryDocument[] = [];

    constructor(private route: ActivatedRoute, private router: Router, private gnpisService: GnpisService) {
    }

    onSelectionChanges(namedSelection: NamedSelection) {
        this.router.navigate(['.'], {
            relativeTo: this.route,
            queryParams: { [namedSelection.name]: namedSelection.selection },
            queryParamsHandling: 'merge'
        });
    }

    fetchDocuments(criteria: DataDiscoveryCriteria) {
        this.gnpisService.search(criteria)
            .subscribe(documents => this.documents = documents);
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
            this.fetchDocuments(criteria);
        });
    }
}
