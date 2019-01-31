import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import {
    DataDiscoveryCriteria,
    DataDiscoveryCriteriaUtils,
    DataDiscoveryDocument,
    DataDiscoveryFacet,
    DEFAULT_PAGE_SIZE,
    MAX_RESULTS
} from '../models/data-discovery.model';
import { BehaviorSubject } from 'rxjs';
import { GnpisService } from '../gnpis.service';
import { filter } from 'rxjs/operators';
import { FormComponent } from '../form/form.component';


@Component({
    selector: 'gpds-result',
    templateUrl: './result-page.component.html',
    styleUrls: ['./result-page.component.scss'],
})
export class ResultPageComponent implements OnInit {

    @ViewChild('form') form: FormComponent;

    criteria$ = new BehaviorSubject<DataDiscoveryCriteria>(DataDiscoveryCriteriaUtils.emptyCriteria());
    documents: DataDiscoveryDocument[] = [];
    facets: DataDiscoveryFacet[] = [];
    pagination = {
        startResult: 1,
        endResult: DEFAULT_PAGE_SIZE,
        totalResult: null,
        currentPage: 0,
        pageSize: DEFAULT_PAGE_SIZE,
        totalPages: null,
        maxResults: MAX_RESULTS
    };

    criteriaIsEmpty = true;
    loading = true;

    constructor(private route: ActivatedRoute,
                private router: Router,
                private gnpisService: GnpisService,
    ) {
    }

    fetchDocumentsAndFacets() {
        this.loading = true;
        const criteria = this.criteria$.value;
        this.gnpisService.search(criteria)
            .subscribe(({ metadata, result, facets }) => {
                this.loading = false;
                this.documents = result.data;
                this.updatePagination(metadata.pagination);
                this.facets = facets;
            });
    }

    private updatePagination({ currentPage, pageSize, totalCount, totalPages }) {
        this.pagination.currentPage = currentPage;
        this.pagination.pageSize = pageSize;
        this.pagination.totalPages = totalPages;
        this.pagination.startResult = pageSize * currentPage + 1;
        this.pagination.endResult = this.pagination.startResult + pageSize - 1;
        this.pagination.totalResult = totalCount;
    }

    ngOnInit(): void {
        const queryParams = this.route.snapshot.queryParams;

        // Parse criteria from URL query params
        const initialCriteria = DataDiscoveryCriteriaUtils.fromQueryParams(queryParams);
        this.criteria$.next(initialCriteria);

        this.criteria$.subscribe(criteria => {
            this.criteriaIsEmpty = true;
            for (const field of Object.keys(criteria)) {
                if (field === 'facetFields') {
                    // Ignore facet fields criteria
                    continue;
                }

                if (criteria[field] && criteria[field].length) {
                    this.criteriaIsEmpty = false;
                    break;
                }
            }
        });

        this.form.traitWidgetInitialized.subscribe(() => {
            this.fetchDocumentsAndFacets();
        });

        this.criteria$
            .pipe(filter(c => c !== initialCriteria))
            .subscribe(newCriteria => {
                // Reset pagination
                newCriteria.page = 0;
                // Fetch documents and facets
                this.fetchDocumentsAndFacets();

                // Update URL query params
                this.router.navigate(['.'], {
                    relativeTo: this.route,
                    queryParams: DataDiscoveryCriteriaUtils.toQueryParams(newCriteria)
                });
            });
    }

    resultCount() {
        return Math.min(
            this.pagination.totalResult,
            MAX_RESULTS - DEFAULT_PAGE_SIZE
        );
    }

    resetAll() {
        this.router.navigate(['.'], {
            relativeTo: this.route,

            // Use of empty param to force re-parse of URL in ngOnInit
            queryParams: { empty: [] }
        });
        this.criteria$.next(DataDiscoveryCriteriaUtils.emptyCriteria());
    }

    changePage(page: number) {
        const criteria = this.criteria$.value;
        criteria.page = page - 1;
        this.fetchDocumentsAndFacets();
        this.router.navigate(['.'], {
            relativeTo: this.route,
            queryParams: { page },
            queryParamsHandling: 'merge'
        });
    }

}
