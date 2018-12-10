export interface DataDiscoverySource {
    ['@id']: string;
    ['@type']: string[];
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
