import { Component, Input, OnInit } from '@angular/core';
import { GnpisService } from '../gnpis.service';
import { Germplasm, GermplasmSearchCriteria } from '../models/gnpis.model';


import { saveAs } from 'file-saver';
import { ActivatedRoute } from '@angular/router';
import {
    DataDiscoveryCriteria,
    DataDiscoveryCriteriaUtils,
    DataDiscoveryFacet,
    DEFAULT_PAGE_SIZE,
    MAX_RESULTS
} from '../models/data-discovery.model';
import { asArray } from '../utils';
import { BehaviorSubject } from 'rxjs';

@Component({
    selector: 'faidare-germplasm-result-page',
    templateUrl: './germplasm-result-page.component.html',
    styleUrls: ['./germplasm-result-page.component.scss']
})
export class GermplasmResultPageComponent implements OnInit {


    germplasm: Germplasm[];
    localCriteria: GermplasmSearchCriteria = DataDiscoveryCriteriaUtils.emptyGermplasmSearchCriteria();

    @Input() criteriaFromForm$: BehaviorSubject<DataDiscoveryCriteria>;
    @Input() germplasmSearchCriteria$: BehaviorSubject<GermplasmSearchCriteria>;
    @Input() germplasmFacets$: BehaviorSubject<DataDiscoveryFacet[]>;

    headers: string[] = ['germplasmName', 'accessionNumber', 'genusSpecies', 'instituteName', 'biologicalStatusOfAccessionCode'];
    elementPerPage: number[] = [15, 20, 25];
    loading: boolean;
    fieldSortState: object = {
        germplasmName: null,
        accessionNumber: null,
        genusSpecies: null,
        instituteName: null,
        biologicalStatusOfAccessionCode: null
    };

    pagination = {
        startResult: 1,
        endResult: DEFAULT_PAGE_SIZE,
        totalResult: null,
        currentPage: 0,
        pageSize: DEFAULT_PAGE_SIZE,
        totalPages: null,
        maxResults: MAX_RESULTS
    };

    constructor(public service: GnpisService, private route: ActivatedRoute) {
    }

    ngOnInit() {

        const queryParams = this.route.snapshot.queryParams;
        this.reassignCriteriaFieldFromDataDiscoveryFields(queryParams);

        this.criteriaFromForm$.subscribe(criteria => {
            this.reassignCriteriaFieldFromDataDiscoveryFields(criteria);
            this.germplasmSearchCriteria$.next(this.localCriteria);
        });

        this.germplasmSearchCriteria$
            .subscribe(criteria => {
                this.localCriteria = criteria;
                this.searchGermplasm(criteria);
            });

    }

    searchGermplasm(criteria: GermplasmSearchCriteria) {
        this.service.germplasmSearch(criteria)
            .subscribe(({ metadata, facets, result }) => {
                this.germplasm = result.data;
                this.germplasmFacets$.next(facets);
                DataDiscoveryCriteriaUtils.updatePagination(this.pagination, metadata.pagination);
            });
    }


    reassignCriteriaFieldFromDataDiscoveryFields(criteria) {

        this.localCriteria = {
            ...this.localCriteria,
            commonCropName: asArray(criteria.crops),
            species: asArray(criteria.crops),
            germplasmGenus: asArray(criteria.crops),
            genusSpecies: asArray(criteria.crops),
            subtaxa: asArray(criteria.crops),
            genusSpeciesSubtaxa: asArray(criteria.crops),
            taxonSynonyms: asArray(criteria.crops),

            panel: asArray(criteria.germplasmLists),
            collection: asArray(criteria.germplasmLists),
            population: asArray(criteria.germplasmLists),

            germplasmNames: asArray(criteria.accessions),
            accessionNumbers: asArray(criteria.accessions),
            synonyms: asArray(criteria.accessions),

            // Do not use the source as a criterion because of the ES should request.
            // sources: asArray(criteria.sources)
        };

        this.germplasmSearchCriteria$.next(this.localCriteria);

    }

    exportPlantMaterial(criteria: GermplasmSearchCriteria) {
        this.loading = true;
        this.service.plantMaterialExport(criteria).subscribe(
            result => {
                const blob = new Blob([result], { type: 'text/plain;charset=utf-8' });
                saveAs(blob, 'germplasm.gnpis.csv');
                this.loading = false;
            },
            error => {
                console.log(error);
            });
    }

    getTabField(tabField) {

        this.switchSortOrder(tabField);
        this.resetOtherFieldSort(tabField);

        this.localCriteria.sortOrder = this.fieldSortState[tabField];

        this.germplasmSearchCriteria$.next(this.localCriteria);

        return tabField;
    }

    switchSortOrder(tabField) {
        if (this.fieldSortState[tabField] === 'asc') {
            this.localCriteria.sortBy = null;
            return this.fieldSortState[tabField] = null;
        }
        if (this.fieldSortState[tabField] === 'desc') {
            this.localCriteria.sortBy = tabField;
            return this.fieldSortState[tabField] = 'asc';
        }
        if (!this.fieldSortState[tabField]) {
            this.localCriteria.sortBy = tabField;
            return this.fieldSortState[tabField] = 'desc';
        }
    }

    resetOtherFieldSort(tabField) {
        const otherHeaders = this.headers.filter(header => header !== tabField);
        for (const header of otherHeaders) {
            this.fieldSortState[header] = null;
        }
    }

    resultCount() {
        return Math.min(
            this.pagination.totalResult,
            MAX_RESULTS - DEFAULT_PAGE_SIZE
        );
    }

    changePage(page: number) {
        this.localCriteria.page = page - 1;
        this.germplasmSearchCriteria$.next(this.localCriteria);
        /*this.router.navigate(['.'], {
            relativeTo: this.route,
            queryParams: { page },
            queryParamsHandling: 'merge'
        });*/
    }

    changeNbElementPerPage(pageSize: number) {
        this.pagination.pageSize = pageSize;
        this.elementPerPage = [10, 15, 20, 25];
        this.elementPerPage = this.elementPerPage
            .filter(otherPageSize => otherPageSize !== pageSize);
        this.localCriteria.pageSize = pageSize;
        this.germplasmSearchCriteria$.next(this.localCriteria);
    }

    // Format facets by renaming and merging some facets in one facet.
    /*formatFacets(facets: DataDiscoveryFacet[]): DataDiscoveryFacet[] {
        const bioStatusAndGeneticNature = [];
        let newFacets: DataDiscoveryFacet[] = [];
        for (const facet of facets) {
            if (facet.field === 'biologicalStatus' || facet.field === 'geneticNature') {
                for (const term of facet.terms) {
                    bioStatusAndGeneticNature
                        .push(term);
                }
            } else if (facet.field === 'holdingInstitute') {
                facet.field = 'holding institute';
                newFacets.push(facet);
            } else {
                newFacets.push(facet);
            }
        }
        newFacets = [
            {
                field: 'Biological status / Genetic nature',
                terms: bioStatusAndGeneticNature
            },
            ...newFacets
        ];
        return newFacets;
    }*/
}
