import { BrapiResults } from './brapi.model';

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

export function emptyCriteria(): DataDiscoveryCriteria {
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
        pageSize: 10
    };
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
