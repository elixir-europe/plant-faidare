import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import {
    BrapiGermplasm,
    BrapiGermplasmAttributes,
    BrapiGermplasmPedigree,
    BrapiLocation,
    BrapiObservationVariable,
    BrapiResult,
    BrapiResults,
    BrapiStudy,
    BrapiTrial
} from './models/brapi.model';
import { Germplasm } from './models/gnpis.model';

export const BASE_URL = 'brapi/v1';

@Injectable({
    providedIn: 'root'
})
export class BrapiService {

    constructor(private http: HttpClient) {
    }

    germplasm(germplasmDbId: string): Observable<Germplasm> {
        return this.http
            .get<Germplasm>(`${BASE_URL}/germplasm/${germplasmDbId}`);
    }

    germplasmPedigree(germplasmDbId: string): Observable<BrapiResult<BrapiGermplasmPedigree>> {
        return this.http
            .get<BrapiResult<BrapiGermplasmPedigree>>(`${BASE_URL}/germplasm/${germplasmDbId}/pedigree`);
    }
    // TODO use the progeny call when the information about parent will be added
    /*germplasmProgeny(germplasmDbId: string): Observable<GermplasmResult<BrapiGermplasmProgeny>> {
        return this.http.get<GermplasmResult<BrapiGermplasmProgeny>>(`${BASE_URL}/germplasm/${germplasmDbId}/progeny`);
    }*/

    germplasmAttributes(germplasmDbId: string): Observable<BrapiResult<BrapiGermplasmAttributes>> {
        return this.http
            .get<BrapiResult<BrapiGermplasmAttributes>>(`${BASE_URL}/germplasm/${germplasmDbId}/attributes`);
    }

    study(studyDbId: string): Observable<BrapiResult<BrapiStudy>> {
        const options = { headers: { 'Accept': 'application/ld+json,application/json' } };
        return this.http
            .get<BrapiResult<BrapiStudy>>(`${BASE_URL}/studies/${studyDbId}`, options);
    }

    studyGermplasms(studyDbId: string): Observable<BrapiResults<BrapiGermplasm>> {
        return this.http
            .get<BrapiResults<BrapiGermplasm>>(`${BASE_URL}/studies/${studyDbId}/germplasm`);
    }

    studyObservationVariables(studyDbId: string): Observable<BrapiResults<BrapiObservationVariable>> {
        return this.http
            .get<BrapiResults<BrapiObservationVariable>>(`${BASE_URL}/studies/${studyDbId}/observationVariables`);
    }

    location(locationId: string): Observable<BrapiResult<BrapiLocation>> {
        return this.http
            .get<BrapiResult<BrapiLocation>>(`${BASE_URL}/locations/${locationId}`);
    }

    studyTrials(trialsId: string): Observable<BrapiResult<BrapiTrial>> {
        return this.http
            .get<BrapiResult<BrapiTrial>>(`${BASE_URL}/trials/${trialsId}`);
    }
}
