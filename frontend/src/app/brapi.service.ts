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


import { GermplasmMcpd } from './models/gnpis.model';


export const BASE_URL = 'brapi/v1';

@Injectable({
    providedIn: 'root'
})
export class BrapiService {

    constructor(private http: HttpClient) {
    }

    germplasmPedigree(germplasmDbId: string): Observable<BrapiResult<BrapiGermplasmPedigree>> {
        return this.http
            .get<BrapiResult<BrapiGermplasmPedigree>>(`${BASE_URL}/germplasm/${germplasmDbId}/pedigree`);
    }

    // TODO use the progeny call when the information about parent will be added
    /* germplasmProgeny(germplasmDbId: string): Observable<GermplasmResult<BrapiGermplasmProgeny>> {
        return this.http.get<GermplasmResult<BrapiGermplasmProgeny>>(`${BASE_URL}/germplasm/${germplasmDbId}/progeny`);
    } */

    germplasmAttributes(germplasmDbId: string): Observable<BrapiResult<BrapiGermplasmAttributes>> {
        return this.http
            .get<BrapiResult<BrapiGermplasmAttributes>>(`${BASE_URL}/germplasm/${germplasmDbId}/attributes`);
    }

    study(studyDbId: string): Observable<BrapiResult<BrapiStudy>> {
        return this.getWithSource(`${BASE_URL}/studies/${studyDbId}`);
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
        return this.getWithSource(`${BASE_URL}/locations/${locationId}`);
    }

    studyTrials(trialsId: string): Observable<BrapiResult<BrapiTrial>> {
        return this.http
            .get<BrapiResult<BrapiTrial>>(`${BASE_URL}/trials/${trialsId}`);
    }
    germplasmMcpd(germplasmDbId: string): Observable<BrapiResult<GermplasmMcpd>> {
        return this.http
            .get<BrapiResult<GermplasmMcpd>>(`${BASE_URL}/germplasm/${germplasmDbId}/mcpd`);
    }


    /**
     * Get BrAPI single result response and replace the 'schema:includedInDataCatalog' URI value to the actual source object value.
     */
    private getWithSource<T>(url: string): Observable<BrapiResult<T>> {
        return this.http.get<BrapiResult<T>>(
            url,
            // Ask JSON-LD (or JSON) response
            { headers: { 'Accept': 'application/ld+json,application/json' } }
        );
    }
}
