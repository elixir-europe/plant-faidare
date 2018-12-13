import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DataDiscoveryCriteria, emptyCriteria } from '../model/dataDiscoveryCriteria';
import { BehaviorSubject } from 'rxjs';
import { DataDiscoveryDocument } from '../model/dataDiscoveryDocument';
import { GnpisService } from '../gnpis.service';
import { asArray } from '../utils';


export interface URLCriteria {
    accessions: string[];
    crops: string[];
    germplasmLists: string[];
    observationVariableIds: string[];

    page: number;
}

@Component({
    selector: 'gpds-result',
    templateUrl: './result-page.component.html',
    styleUrls: ['./result-page.component.scss'],
})
export class ResultPageComponent implements OnInit {

    static MAX_RESULTS = 10000;
    static PAGE_SIZE = 10;

    criteria$ = new BehaviorSubject<DataDiscoveryCriteria>(emptyCriteria());
    documents: DataDiscoveryDocument[] = [];
    pagination = {
        startResult: 1,
        endResult: 10,
        totalResult: null,
        currentPage: 0,
        pageSize: ResultPageComponent.PAGE_SIZE,
        totalPages: null,
        maxResults: ResultPageComponent.MAX_RESULTS
    };

    constructor(private route: ActivatedRoute,
                private router: Router,
                private gnpisService: GnpisService
    ) {
    }

    fetchDocuments(criteria: DataDiscoveryCriteria) {
        this.gnpisService.search(criteria)
            .subscribe(({ metadata, result }) => {
                this.documents = result.data;
                const { currentPage, pageSize, totalCount, totalPages } = metadata.pagination;
                this.pagination.currentPage = currentPage;
                this.pagination.pageSize = pageSize;
                this.pagination.totalPages = totalPages;
                this.pagination.startResult = pageSize * currentPage + 1;
                this.pagination.endResult = this.pagination.startResult + pageSize - 1;
                this.pagination.totalResult = totalCount;
            });
    }

    ngOnInit(): void {
        const queryParams = this.route.snapshot.queryParams;

        // Update criteria using URL query params
        const criteria: DataDiscoveryCriteria = {
            crops: asArray(queryParams.crops),
            germplasmLists: asArray(queryParams.germplasmLists),
            accessions: asArray(queryParams.accessions),
            topSelectedTraitOntologyIds: asArray(queryParams.observationVariableIds),
            observationVariableIds: [],

            page: queryParams.page - 1 || 0,
            pageSize: ResultPageComponent.PAGE_SIZE
        };
        this.criteria$.next(criteria);

        this.criteria$.subscribe(newCriteria => {
            newCriteria.page = 0;
            this.fetchDocuments(newCriteria);

            const newQueryParams: URLCriteria = {
                crops: newCriteria.crops,
                accessions: newCriteria.accessions,
                germplasmLists: newCriteria.germplasmLists,
                observationVariableIds: newCriteria.topSelectedTraitOntologyIds,
                page: 1
            };
            this.router.navigate(['.'], {
                relativeTo: this.route,
                queryParams: newQueryParams
            });
        });
    }

    collectionSize() {
        return Math.min(
            this.pagination.totalResult,
            ResultPageComponent.MAX_RESULTS - ResultPageComponent.PAGE_SIZE
        );
    }

    resetAll() {
        this.router.navigate(['.'], {
            relativeTo: this.route,

            // Use of empty param to force re-parse of URL in ngOnInit
            queryParams: { empty: [] }
        });
        this.criteria$.next(emptyCriteria());
    }

    changePage(page: number) {
        const criteria = this.criteria$.value;
        criteria.page = page - 1;
        this.fetchDocuments(criteria);
        this.router.navigate(['.'], {
            relativeTo: this.route,
            queryParams: { page },
            queryParamsHandling: 'merge'
        });
    }
}
