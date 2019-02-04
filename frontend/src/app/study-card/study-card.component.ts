import { Component, OnInit } from '@angular/core';
import { BrapiService } from '../brapi.service';
import { ActivatedRoute } from '@angular/router';
import { BrapiGermplasm, BrapiLocation, BrapiObservationVariable, BrapiStudy, BrapiTrial } from '../models/brapi.model';

import { GnpisService } from '../gnpis.service';
import { DataDiscoverySource } from '../models/data-discovery.model';

@Component({
    selector: 'gpds-study-card',
    templateUrl: './study-card.component.html',
    styleUrls: ['./study-card.component.scss']
})
export class StudyCardComponent implements OnInit {

    study: BrapiStudy;
    studySource: DataDiscoverySource;
    studyGermplasms: BrapiGermplasm[];
    studyObservationVariables: BrapiObservationVariable[];
    additionalInfos: {
        key: string,
        value: string
    }[];
    studyDataset: BrapiTrial[];
    trialsIds: string[] = [];
    loading: boolean;
    loaded: Promise<any>;

    constructor(private brapiService: BrapiService, private  gnpisService: GnpisService, private route: ActivatedRoute) {
    }

    ngOnInit() {
        this.route.paramMap.subscribe(paramMap => {
            this.loading = true;
            const studyDbId = paramMap.get('id');

            const study$ = this.brapiService.study(studyDbId).toPromise();
            study$
                .then(response => {
                    this.study = response.result;

                    this.additionalInfos = [];
                    if (response.result.additionalInfo) {
                        this.additionalInfos = Object.entries(response.result.additionalInfo)
                            .filter(([key, value]) => !!key && !! value)
                            .map(([key, value]) => {
                                return { key, value };
                            });
                    }

                    // Get study trials
                    this.trialsIds = response.result.trialDbIds;
                    this.studyDataset = [];
                    if (this.trialsIds && this.trialsIds !== []) {
                        for (const trialsId of this.trialsIds) {
                            this.brapiService.studyTrials(trialsId)
                                .subscribe(trialResponse => {
                                    const trial = trialResponse.result;

                                    // Remove current study from trial studies
                                    trial.studies = trial.studies
                                        .filter(study => study.studyDbId !== studyDbId);
                                    this.studyDataset.push(trial);
                                });
                        }
                    }

                    // Get study source
                    const sourceURI = this.study['schema:includedInDataCatalog'];
                    if (sourceURI) {
                        const source$ = this.gnpisService.getSource(sourceURI);
                        source$
                            .subscribe(src => {
                                this.studySource = src;
                            });
                    }
                });

            this.studyObservationVariables = [];
            const variable$ = this.brapiService.studyObservationVariables(studyDbId).toPromise();
            variable$
                .then(studyObsVar => {
                    this.studyObservationVariables = studyObsVar.result.data;
                });

            this.studyGermplasms = [];
            const germplasm$ = this.brapiService.studyGermplasms(studyDbId).toPromise();
            germplasm$
                .then(studyGermplasm => {
                    this.studyGermplasms = studyGermplasm.result.data;
                });

            this.loaded = Promise.all([study$, variable$, germplasm$]);
            this.loaded.then(() => {
                this.loading = false;
            });
        });

    }

    checkLocation(location: BrapiLocation) {
        return location && location.longitude && location.latitude;
    }

    isNotURN(pui: string) {
        return !(pui.substring(0, 3) === 'urn');
    }

}
