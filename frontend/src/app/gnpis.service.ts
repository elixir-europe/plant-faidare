import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { HttpClient } from '@angular/common/http';

const BASE_URL = '/gnpis/v1/datadiscovery';


@Injectable({
  providedIn: 'root'
})
export class GnpisService {

  constructor(private http: HttpClient) {}

  suggest(field: string, fetchSize: number, text: string = ''): Observable<string[]> {
    const params = {field, text, fetchSize: fetchSize.toString()};
    return this.http.post<string[]>(`${BASE_URL}/suggest`, null, { params });
  }

}
