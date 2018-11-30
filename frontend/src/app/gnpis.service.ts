import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { DataDiscoveryCriteria } from './model/criteria/dataDiscoveryCriteria';

const BASE_URL = '/gnpis/v1/datadiscovery';

@Injectable({
    providedIn: 'root'
})
export class GnpisService {

    constructor(private http: HttpClient) {
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

}
