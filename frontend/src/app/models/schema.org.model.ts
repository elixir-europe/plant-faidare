/**
 * Basic JSON-LD object (non exhaustive list of properties)
 */
export interface JsonLD {
    ['@context']?: any;
    ['@id']?: string;
    ['@type']?: string | string[];
}

/**
 * https://schema.org/DataCatalog
 */
export interface DataCatalog extends JsonLD {
    ['@id']: string;
    ['@type']: ['schema:DataCatalog'];
    ['schema:name']: string;
    ['schema:url']: string;
    ['schema:image']: string;
}

/**
 * https://schema.org/Dataset
 */
export interface Dataset extends JsonLD {
    // Identifier
    ['schema:identifier']?: string;

    // Display name
    ['schema:name']?: string;

    // Equivalent to BrAPI 'documentationURL'
    ['schema:url']?: string;

    // Source data catalog (string URI or data catalog object)
    ['schema:includedInDataCatalog']?: string | DataCatalog;
}

