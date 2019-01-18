import { Injectable } from '@angular/core';
import { Observable, ReplaySubject, zip } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { DataDiscoveryCriteria, DataDiscoveryFacet, DataDiscoveryResults, DataDiscoverySource } from './models/data-discovery.model';
import { BrapiResults } from './models/brapi.model';
import { map } from 'rxjs/operators';
import { GermplasmResult } from './models/gnpis.germplasm.model';


export const BASE_URL = 'gnpis/v1/datadiscovery';

@Injectable({
    providedIn: 'root'
})
export class GnpisService {
    sourceByURI$ = new ReplaySubject<{ [key: string]: DataDiscoverySource }>(1);

    constructor(private http: HttpClient) {
        this.fetchSources();
    }

    private fetchSources(): void {
        this.http.get(`${BASE_URL}/sources`).subscribe(
            (response: BrapiResults<DataDiscoverySource>) => {
                const sourceByURI = {};
                for (const source of response.result.data) {
                    sourceByURI[source['@id']] = source;
                }
                this.sourceByURI$.next(sourceByURI);
            }
        );
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
        fetchSize: number,
        text: string = '',
        criteria: DataDiscoveryCriteria = null
    ): Observable<string[]> {
        const params = { field, text, fetchSize: fetchSize.toString() };
        return this.http.post<string[]>(
            `${BASE_URL}/suggest`, criteria, { params }
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
        return zip(
            // Get source by URI
            this.sourceByURI$,
            // Get documents by criteria
            this.http.post<any>(`${BASE_URL}/search`, criteria)
        ).pipe(map(([sourceByURI, response]) => {
            // Extract BrAPI documents from result
            const documents = response.result.data;

            // Transform document to have the source details in place of the source URI
            response.result.data = documents.map(document => {
                const sourceURI = document['schema:includedInDataCatalog'];
                document['schema:includedInDataCatalog'] = sourceByURI[sourceURI];
                return document;
            });
            if (response.facets) {
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
            return response;
        }));
    }

    /**
     * Get data source by URI
     */
    getSource(sourceURI: string): Observable<DataDiscoverySource> {
        return this.sourceByURI$.pipe(map(sourceByURI => sourceByURI[sourceURI]));
    }
    germplasm(germplasmDbId: string): Observable<GermplasmResult<null>> {
        return this.http.get<GermplasmResult<null>>(`/gnpis/v1/germplasm?id=${germplasmDbId}`);
    }
}
