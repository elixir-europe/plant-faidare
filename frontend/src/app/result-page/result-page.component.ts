import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, NavigationStart, Router } from '@angular/router';
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
import { environment } from '../../environments/environment';
import { GermplasmSearchCriteria } from '../models/gnpis.model';


@Component({
    selector: 'faidare-result',
    templateUrl: './result-page.component.html',
    styleUrls: ['./result-page.component.scss'],
})
export class ResultPageComponent implements OnInit {

    @ViewChild('form') form: FormComponent;
    appTitle: string = environment.appTitle;

    criteria$ = new BehaviorSubject<DataDiscoveryCriteria>(DataDiscoveryCriteriaUtils.emptyCriteria());
    documents: DataDiscoveryDocument[] = [];
    facets: DataDiscoveryFacet[] = [];
    germplasmSearchCriteria$ = new BehaviorSubject<GermplasmSearchCriteria>(DataDiscoveryCriteriaUtils.emptyGermplasmSearchCriteria());
    germplasmfacets$ = new BehaviorSubject<DataDiscoveryFacet[]>([]);
    germplasmfacets: DataDiscoveryFacet[] = [];
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
    displayGermplasmResult$ = new BehaviorSubject(false);
    displayGermplasmResult = false;
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
                DataDiscoveryCriteriaUtils.updatePagination(this.pagination, metadata.pagination);
                this.facets = facets;
            });
    }

    // TODO : delete because move to DataDiscoveryCriteriaUtils
    /*private updatePagination({ currentPage, pageSize, totalCount, totalPages }) {
        this.pagination.currentPage = currentPage;
        this.pagination.pageSize = pageSize;
        this.pagination.totalPages = totalPages;
        this.pagination.startResult = pageSize * currentPage + 1;
        this.pagination.endResult = this.pagination.startResult + pageSize - 1;
        this.pagination.totalResult = totalCount;
    }*/

    ngOnInit(): void {
        const queryParams = this.route.snapshot.queryParams;
        this.router.events.subscribe((event) => {
            if (event instanceof NavigationStart) {
                this.displayGermplasmResult = false;
            }
        });

        // Parse criteria from URL query params
        const initialCriteria = DataDiscoveryCriteriaUtils.fromQueryParams(queryParams);
        this.criteria$.next(initialCriteria);

        this.criteriaIsEmpty = DataDiscoveryCriteriaUtils.checkCriteriaIsEmpty(initialCriteria);


        // TODO : delete because move to DataDiscoveryCriteriaUtils
        /*this.criteria$.subscribe(criteria => {
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
        });*/

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
                this.criteriaIsEmpty = DataDiscoveryCriteriaUtils.checkCriteriaIsEmpty(newCriteria);

                // Update URL query params
                this.router.navigate(['.'], {
                    relativeTo: this.route,
                    queryParams: DataDiscoveryCriteriaUtils.toQueryParams(newCriteria)
                });
                this.displayGermplasmResult$.subscribe(value => {
                    this.displayGermplasmResult = value;
                });
                this.displayGermplasmResult$.next(this.displayGermplasmResult);
            });

        /*this.displayGermplasmResult$.subscribe(value => {
            this.displayGermplasmResult = value;
        });
        this.displayGermplasmResult$.next(this.displayGermplasmResult);*/

        this.germplasmfacets$.subscribe(facets => {
            this.germplasmfacets = facets;
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
        this.displayGermplasmResult$.next(false);
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
