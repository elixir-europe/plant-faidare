import { BrapiResults } from './brapi.model';
import { Params } from '@angular/router';
import { asArray } from '../utils';


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
            facetFields: ['sources', 'types'],
            germplasmLists: null,
            observationVariableIds: null,
            topSelectedTraitOntologyIds: null,
            sources: null,
            types: null,

            page: 0,
            pageSize: DEFAULT_PAGE_SIZE
        };
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
}

export interface DataDiscoverySource {
    ['@id']: string;
    ['@type']: ['schema:DataCatalog'];
    ['schema:identifier']: string;
    ['schema:name']: string;
    ['schema:url']: string;
    ['schema:image']: string;
}

export interface DataDiscoveryDocument {
    ['@id']: string;
    ['@type']: DataDiscoveryType[];
    ['schema:identifier']: string;
    ['schema:name']: string;
    ['schema:url']: string;
    ['schema:description']: string;
    ['schema:includedInDataCatalog']: DataDiscoverySource;
}

export type DataDiscoveryType = 'Germplasm' | 'Phenotyping Study';

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
