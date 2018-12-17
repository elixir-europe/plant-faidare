import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';



@Injectable({
    providedIn: 'root'
})


export class BrapiService {

    constructor(private http: HttpClient) {
    }

    germplasm(germplasmDbId: string): Observable<object> {
        return this.http.get<object>(`/brapi/v1/germplasm/${germplasmDbId}`);
    }

    germplasmPedigree(germplasmDbId: string): Observable<object> {
        return this.http.get<object>(`/brapi/v1/germplasm/${germplasmDbId}/pedigree`);
    }

    germplasmProgeny(germplasmDbId: string): Observable<object> {
        return this.http.get<object>(`/brapi/v1/germplasm/${germplasmDbId}/progeny`);
    }

    germplasmAttributes(germplasmDbId: string): Observable<object> {
        return this.http.get<object>(`/brapi/v1/germplasm/${germplasmDbId}/attributes`);
    }

    study(studyDbId: string): Observable<object> {
        return this.http.get<object>(`/brapi/v1/studies/${studyDbId}`);
    }

    studyGermplasms(studyDbId: string): Observable<string[]> {
        return this.http.get<string[]>(`/brapi/v1/studies/${studyDbId}/germplasm`);
    }

    studyObservationVariables(studyDbId: string): Observable<string[]> {
        return this.http.get<string[]>(`/brapi/v1/studies/${studyDbId}/observationVariables`);
    }

}
