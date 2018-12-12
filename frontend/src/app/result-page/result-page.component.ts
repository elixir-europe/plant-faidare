import { Component, OnInit } from '@angular/core';
import { Param } from '../model/common';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { DataDiscoveryCriteria, newCriteria } from '../model/dataDiscoveryCriteria';
import { BehaviorSubject } from 'rxjs';
import { DataDiscoveryDocument } from '../model/dataDiscoveryDocument';
import { GnpisService } from '../gnpis.service';


function asArray(obj) {
    if (!obj) {
        return [];
    }
    if (Array.isArray(obj)) {
        return obj;
    }
    return [obj];
}

@Component({
    selector: 'gpds-result',
    templateUrl: './result-page.component.html',
    styleUrls: ['./result-page.component.scss'],
})
export class ResultPageComponent implements OnInit {

    static MAX_RESULTS = 10000;
    static PAGE_SIZE = 10;
    criteria$ = new BehaviorSubject<DataDiscoveryCriteria>(newCriteria());
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

    constructor(private route: ActivatedRoute, private router: Router, private gnpisService: GnpisService) {
    }

    /**
     * Update URL params with one param. Also reset page
     */
    updateParams(param: Param) {
        this.router.navigate(['.'], {
            relativeTo: this.route,
            queryParams: {
                page: null,
                [param.key]: param.value
            },
            queryParamsHandling: 'merge'
        });
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
        this.route.queryParams.subscribe((queryParams: Params) => {
            // Update criteria using URL query params
            const criteria: DataDiscoveryCriteria = {
                crops: asArray(queryParams.crops),
                germplasmLists: asArray(queryParams.germplasmLists),
                accessions: asArray(queryParams.accessions),

                page: queryParams.page - 1 || 0,
                pageSize: ResultPageComponent.PAGE_SIZE
            };
            this.criteria$.next(criteria);
            this.fetchDocuments(criteria);
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
    }

}
