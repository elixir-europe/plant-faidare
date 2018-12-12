import { Injectable } from '@angular/core';
import { Observable, ReplaySubject, zip } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { DataDiscoveryCriteria } from './model/dataDiscoveryCriteria';
import { BrapiResults } from './model/brapi';
import { DataDiscoveryDocument, DataDiscoverySource } from './model/dataDiscoveryDocument';
import { map } from 'rxjs/operators';

@Injectable({
    providedIn: 'root'
})
export class GnpisService {
    static BASE_URL = '/gnpis/v1/datadiscovery';
    sourceByURI$ = new ReplaySubject<{ [key: string]: DataDiscoverySource }>(1);

    constructor(private http: HttpClient) {
        this.fetchSources();
    }

    private fetchSources(): void {
        this.http.get(`${GnpisService.BASE_URL}/sources`).subscribe(
            (brapiResults: BrapiResults<DataDiscoverySource>) => {
                const sourceByURI = {};
                for (const source of brapiResults.result.data) {
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
            `${GnpisService.BASE_URL}/suggest`, criteria, { params }
        );
    }

    /**
     * Fetch data discovery documents by criteria
     * @param criteria the criteria
     * @return an observable of BrAPI results list of documents
     */
    search(
        criteria: DataDiscoveryCriteria
    ): Observable<BrapiResults<DataDiscoveryDocument>> {
        return zip(
            // Get source by URI
            this.sourceByURI$,
            // Get documents by criteria
            this.http.post<any>(`${GnpisService.BASE_URL}/search`, criteria)
        ).pipe(map(([sourceByURI, brapiResult]) => {
            // Extract BrAPI documents from result
            const documents = brapiResult.result.data;

            // Transform document to have the source details in place of the source URI
            brapiResult.result.data = documents.map(document => {
                const sourceURI = document['schema:includedInDataCatalog'] as string;
                document['schema:includedInDataCatalog'] = sourceByURI[sourceURI];
                return document;
            });
            return brapiResult;
        }));
    }

}
