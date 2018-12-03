import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

const BASE_URL = '/brapi/v1/germplasm';

@Injectable({
    providedIn: 'root'
})


export class BrapiService {

    constructor(private http: HttpClient) {
    }

    germplasm(germplasmDbId: string): Observable<object> {
        return this.http.get<object>(`${BASE_URL}/${germplasmDbId}`);
    }
}
