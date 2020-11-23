import { Injectable } from '@angular/core';
import { Observable, ReplaySubject, zip } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import {
    DataDiscoveryCriteria,
    DataDiscoveryFacet,
    DataDiscoveryResults,
    DataDiscoverySource
} from './models/data-discovery.model';
import {
    BrapiResults,
    GermplasmCriteria,
    GermplasmResults
} from './models/brapi.model';
import { map } from 'rxjs/operators';
import { Germplasm, GermplasmSearchCriteria } from './models/gnpis.model';
import { XrefResponse } from './models/xref.model';
import { removeNullUndefined } from './utils';


export const BASE_URL = 'faidare/v1';

@Injectable({
    providedIn: 'root'
})
export class GnpisService {
    static URGI_SOURCE_URI = 'https://urgi.versailles.inra.fr';

    sourceByURI$ = new ReplaySubject<Record<string, DataDiscoverySource>>(1);
    sources$ = new ReplaySubject<DataDiscoverySource[]>(1);

    constructor(private http: HttpClient) {
        // Get data sources
        this.http.get<BrapiResults<DataDiscoverySource>>(`${BASE_URL}/datadiscovery/sources`)
            .pipe(map(response => response.result.data))
            .subscribe(dataSources => {
                // Index by URI
                const sourceByURI = {};
                for (const dataSource of dataSources) {
                    sourceByURI[dataSource['@id']] = dataSource;
                }
                this.sources$.next(dataSources);
                this.sourceByURI$.next(sourceByURI);
            });
    }

    /**
     * Fetch value suggestion for field.
     * @param field the field on which to suggest values
     * @param fetchSize number of values to fetch
     * @param text search text that needs to match in field values
     * @param criteria used to filter document before suggestion
     * @return an observable of field values
     */
    suggest(
        field: string,
        fetchSize: number = null,
        text: string = '',
        criteria: DataDiscoveryCriteria = null
    ): Observable<string[]> {
        const params = removeNullUndefined({ field, text, fetchSize });
        return this.http.post<string[]>(
            `${BASE_URL}/datadiscovery/suggest`, criteria, { params }
        );
    }

    /**
     * Fetch data discovery documents by criteria
     * @param criteria the criteria
     * @return an observable of BrAPI results list of documents
     */
    search(
        criteria: DataDiscoveryCriteria
    ): Observable<DataDiscoveryResults> {
        return this.mapSources( zip(
            // Get source by URI
            this.sourceByURI$,
            // Get documents by criteria
            this.http.post<any>(`${BASE_URL}/datadiscovery/search`, criteria)
        ));
    }


    mapSources(httpResponse: Observable<any>) {
        return httpResponse.pipe(map(([sourceByURI, response]) => {
            // Extract BrAPI documents from result
            const documents = response.result.data;

            // Transform document to have the source details in place of the source URI
            response.result.data = documents.map(document => {
                const sourceURI = document['schema:includedInDataCatalog'];
                document['schema:includedInDataCatalog'] = sourceByURI[sourceURI];
                return document;
            });
            if (response.facets) {
                this.getSourcesName(sourceByURI, response);
            }
            return response;
        }));

    }




    /**
     * Get germplasm by ID or PUI with data source (present in JSON-LD response)
     * @param params containing Id or PUI
     */
    getGermplasm(params: { id?: string, pui?: string }): Observable<Germplasm> {
        return this.http.get<Germplasm>(
            `${BASE_URL}/germplasm`,
            {
                params: removeNullUndefined(params),
                headers: { 'Accept': 'application/ld+json,application/json' }
            }
        );
    }

    germplasmSearch(criteria: GermplasmCriteria): Observable<GermplasmResults<Germplasm>> {

        return this.mapSources(zip(
            // Get source by URI
            this.sourceByURI$,
            // Get documents by criteria
            this.http.post<GermplasmResults<Germplasm>>(`${BASE_URL}/germplasm/search`,
                criteria,
                { headers: { 'Accept': 'application/ld+json,application/json' } })));
    }

    /**
     * Get data source by URI
     */
    getSource(sourceURI: string): Observable<DataDiscoverySource> {
        return this.sourceByURI$.pipe(map(sourceByURI => sourceByURI[sourceURI]));
    }

    xref(xrefId: string): Observable<XrefResponse> {
        return this.http.get<XrefResponse>(`${BASE_URL}/xref/documentbyfulltextid?linkedRessourcesID=${xrefId}`);
    }

    // TODO Change the service's response to return an object with the number of results and handle here if the number is over the limit
    plantMaterialExport(criteria: GermplasmSearchCriteria): Observable<any> {
        const requestOptions: Object = {
            /* other options here */
            responseType: 'text'
        };
        return this.http.post<any>(
            `${BASE_URL}/germplasm/germplasm-list-csv`,
            criteria,
            requestOptions
        );
    }

    getSourcesName(sourceByURI, response) {
        response.facets = response.facets.map((facet: DataDiscoveryFacet) => {
            facet.terms = facet.terms.map(term => {
                if (facet.field === 'sources') {
                    term.label = sourceByURI[term.term]['schema:name'];
                } else {
                    term.label = term.term;
                }
                return term;
            });
            return facet;
        });
    }

}
