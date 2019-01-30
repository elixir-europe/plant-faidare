import { Component, OnInit } from '@angular/core';
import { BrapiService } from '../brapi.service';
import { ActivatedRoute } from '@angular/router';
import { BrapiGermplasm, BrapiLocation, BrapiObservationVariables, BrapiStudy, BrapiTrial } from '../models/brapi.model';

import { GnpisService } from '../gnpis.service';
import { DataDiscoverySource } from '../models/data-discovery.model';
import { arrayToString } from '../utils';

@Component({
    selector: 'gpds-study-card',
    templateUrl: './study-card.component.html',
    styleUrls: ['./study-card.component.scss']
})
export class StudyCardComponent implements OnInit {

    study: BrapiStudy;
    studySource: DataDiscoverySource;
    studyGermplasms: BrapiGermplasm[] = [];
    studyObservationVariables: BrapiObservationVariables[] = [];
    sites: BrapiLocation[] = [];
    additionalInfoKeys: string[] = [];
    studyDataset: BrapiTrial[] = [];
    trialsIds: string[] = [];
    loading = true;
    loaded: Promise<any>;

    constructor(private brapiService: BrapiService, private  gnpisService: GnpisService, private route: ActivatedRoute) {
    }

    ngOnInit() {
        const studyDbId = this.route.snapshot.paramMap.get('id');
        const sourceId = this.route.snapshot.queryParams.source;

        const study$ = this.brapiService.getStudy(studyDbId).toPromise();
        study$
            .then(response => {
                this.study = response.result;
                this.study.seasons = [arrayToString(response.result.seasons, ', ')];
                this.sites = [response.result.location];
                if (response.result.additionalInfo) {
                    this.additionalInfoKeys = Object.keys(response.result.additionalInfo);
                }
                this.trialsIds = response.result.trialDbIds;
                if (this.trialsIds && this.trialsIds !== []) {
                    for (const trialsId of this.trialsIds) {
                        this.brapiService.getTrials(trialsId)
                            .subscribe(trial => {
                                this.studyDataset.push(trial.result);
                            });
                    }
                }
            });

        const variable$ = this.brapiService.getStudyObservationVariables(studyDbId).toPromise();
        variable$
            .then(studyObsVar => {
                this.studyObservationVariables = studyObsVar.result.data;
            });

        const germplasm$ = this.brapiService.getStudyGermplasms(studyDbId).toPromise();
        germplasm$
            .then(studyGermplasm => {
                this.studyGermplasms = studyGermplasm.result.data;
            });

        const source$ = this.gnpisService.getSource(sourceId);
        source$
            .subscribe(src => {
                this.studySource = src;
            });

        this.loaded = Promise.all([study$, variable$, germplasm$]);
        this.loaded.then(() => {
            this.loading = false;
        });
    }

    checkLocation(locations: BrapiLocation[]) {
        for (const location of locations) {
            if (location.longitude && location.latitude) {
                return true;
            }
        }
        return false;
    }

    puiFilter(pui: string) {
        return !(pui.substring(0, 3) === 'urn');
    }

    /* Function will be remove when DocumentationUrl will be added to the ObservationVariable result */
    getVariableLink(variable: BrapiObservationVariables) {
        return `https://urgi.versailles.inra.fr/ontologyportal.do#termIdentifier=${variable.observationVariableDbId}`;
    }

    getTrialLink(trial: BrapiTrial) {
        return `https://urgi.versailles.inra.fr/ephesis/ephesis/viewer.do#dataResults/trialSetIds=${trial.trialDbId}`;
    }

    getTrialStudies(studies: Array<{ studyDbId: string }>): string {
        return studies.map(study => study.studyDbId).join(', ');
    }
}
