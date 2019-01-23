import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { delay } from 'rxjs/operators';
import { SiteModel, SiteResultModel } from './models/site.model';
import { BrapiGermplasmeData, BrapiObservationUnitsData, BrapiObservationVariablesData, BrapiResults, BrapiResult, BrapiTrial } from './model/brapi.model';


@Injectable({
    providedIn: 'root'
})
export class BrapiService {

    constructor(private http: HttpClient) {
    }

    germplasm(germplasmDbId: string): Observable<object> {
        return this.http.get<object>(`brapi/v1/germplasm/${germplasmDbId}`);
    }

    germplasmPedigree(germplasmDbId: string): Observable<object> {
        return this.http.get<object>(`brapi/v1/germplasm/${germplasmDbId}/pedigree`);
    }

    germplasmProgeny(germplasmDbId: string): Observable<object> {
        return this.http.get<object>(`brapi/v1/germplasm/${germplasmDbId}/progeny`);
    }

    germplasmAttributes(germplasmDbId: string): Observable<object> {
        return this.http.get<object>(`brapi/v1/germplasm/${germplasmDbId}/attributes`);
    }

    study(studyDbId: string): Observable<BrapiResult<BrapiStudyData>> {
        return this.http.get<BrapiResult<BrapiStudyData>>(`brapi/v1/studies/${studyDbId}`);
    }

    studyGermplasms(studyDbId: string): Observable<BrapiResults<BrapiGermplasmeData>> {
        return this.http.get<BrapiResults<BrapiGermplasmeData>>(`brapi/v1/studies/${studyDbId}/germplasm`);
    }

    studyObservationVariables(studyDbId: string): Observable<BrapiResults<BrapiObservationVariablesData>> {
        return this.http.get<BrapiResults<BrapiObservationVariablesData>>(`brapi/v1/studies/${studyDbId}/observationVariables`);
    }

    location(locationId: number): Observable<SiteModel> {
        return this.http.get<SiteModel>(`brapi/v1/locations/${locationId}`);
    }

    studyTrials(trialsId: string): Observable<BrapiResult<BrapiTrial>> {
        return this.http.get<BrapiResult<BrapiTrial>>(`brapi/v1/trials/${trialsId}`);
    }

}
