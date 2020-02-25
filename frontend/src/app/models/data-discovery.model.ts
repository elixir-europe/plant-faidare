import { BrapiResults } from './brapi.model';
import { Params } from '@angular/router';
import { asArray } from '../utils';
import * as schema from './schema.org.model';
import { GermplasmSearchCriteria } from './gnpis.model';


export const MAX_RESULTS = 10000;
export const DEFAULT_PAGE_SIZE = 10;

export interface DataDiscoveryCriteria {
    accessions: string[];
    crops: string[];
    facetFields: string[];
    germplasmLists: string[];
    observationVariableIds: string[];
    sources: string[];
    types: string[];

    /**
     * Used to store the top selected node ids from the trait ontology widget
     */
    topSelectedTraitOntologyIds: string[];

    page: number;
    pageSize: number;
}

export class DataDiscoveryCriteriaUtils {
    static emptyCriteria(): DataDiscoveryCriteria {
        return {
            accessions: null,
            crops: null,
            facetFields: ['types', 'sources'],
            germplasmLists: null,
            observationVariableIds: null,
            topSelectedTraitOntologyIds: null,
            sources: null,
            types: null,

            page: 0,
            pageSize: DEFAULT_PAGE_SIZE
        };
    }

    static emptyGermplasmSearchCriteria(): GermplasmSearchCriteria {
        return {
            accessionNumbers: null,
            germplasmDbIds: null,
            germplasmGenus: null,
            germplasmNames: null,
            germplasmPUIs: null,
            germplasmSpecies: null,
            holdingInstitute: null,
            synonyms: null,
            panel: null,
            collection: null,
            population: null,
            commonCropName: null,
            species: null,
            genusSpecies: null,
            subtaxa: null,
            genusSpeciesSubtaxa: null,
            taxonSynonyms: null,
            taxonCommonNames: null,
            biologicalStatus: null,
            geneticNature: null,
            sources: null,
            types: 'Germplasm',

            facetFields: ['sources', 'holdingInstitute',
                'biologicalStatus', 'geneticNature', 'country'],
            sortBy: null,
            sortOrder: null,
            page: 0,
            pageSize: 10,
        };
    }

    static checkCriteriaIsEmpty(criteria): boolean {
        for (const field of Object.keys(criteria)) {
            if (field === 'facetFields') {
                // Ignore facet fields criteria
                continue;
            }
            if (criteria[field] && criteria[field].length) {
                return false;
            }

        }
        return true;
    }

    static fromQueryParams(queryParams: Params): DataDiscoveryCriteria {
        return {
            ...DataDiscoveryCriteriaUtils.emptyCriteria(),

            crops: asArray(queryParams.crops),
            germplasmLists: asArray(queryParams.germplasmLists),
            accessions: asArray(queryParams.accessions),
            sources: asArray(queryParams.sources),
            types: asArray(queryParams.types),

            // The URL should only contain top selected trait ontology node ids
            topSelectedTraitOntologyIds: asArray(queryParams.observationVariableIds),

            page: queryParams.page - 1 || 0
        };
    }

    static toQueryParams(newCriteria: DataDiscoveryCriteria) {
        return {
            crops: newCriteria.crops,
            accessions: newCriteria.accessions,
            germplasmLists: newCriteria.germplasmLists,
            sources: newCriteria.sources,
            types: newCriteria.types,

            // The URL should only contain top selected trait ontology node ids
            observationVariableIds: newCriteria.topSelectedTraitOntologyIds,

            page: newCriteria.page + 1
        };
    }

    static updatePagination(previousPagination, { currentPage, pageSize, totalCount, totalPages }) {
        previousPagination.currentPage = currentPage;
        previousPagination.pageSize = pageSize;
        previousPagination.totalPages = totalPages;
        previousPagination.startResult = pageSize * currentPage + 1;
        previousPagination.endResult = previousPagination.startResult + pageSize - 1;
        previousPagination.totalResult = totalCount;
    }
}

export type DataDiscoverySource = schema.DataCatalog;

export interface DataDiscoveryDocument extends schema.Dataset {
    ['@id']: string;
    ['@type']: DataDiscoveryType[];
    ['schema:identifier']: string;
    ['schema:name']: string;
    ['schema:url']: string;
    ['schema:description']: string;
    ['schema:includedInDataCatalog']: DataDiscoverySource;
}

// TODO: use enum
export type DataDiscoveryType = 'Germplasm' | 'Phenotyping Study' | 'Genotyping Study';

export interface DataDiscoveryResults extends BrapiResults<DataDiscoveryDocument> {
    facets: DataDiscoveryFacet[];
}

export interface DataDiscoveryFacet {
    field: string;
    terms: {
        term: string;
        label: string;
        count: number;
    }[];
}
