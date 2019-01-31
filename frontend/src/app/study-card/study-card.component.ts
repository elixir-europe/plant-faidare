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
    studyGermplasms: BrapiGermplasm[] = [];
    studyObservationVariables: BrapiObservationVariable[] = [];
    additionalInfoKeys: string[] = [];
    studyDataset: BrapiTrial[] = [];
    trialsIds: string[] = [];
    loading = true;
    loaded: Promise<any>;

    constructor(private brapiService: BrapiService, private  gnpisService: GnpisService, private route: ActivatedRoute) {
    }

    ngOnInit() {
        const studyDbId = this.route.snapshot.paramMap.get('id');

        const study$ = this.brapiService.study(studyDbId).toPromise();
        study$
            .then(response => {
                this.study = response.result;
                if (response.result.additionalInfo) {
                    this.additionalInfoKeys = Object.keys(response.result.additionalInfo);
                }

                // Get study trials
                this.trialsIds = response.result.trialDbIds;
                if (this.trialsIds && this.trialsIds !== []) {
                    for (const trialsId of this.trialsIds) {
                        this.brapiService.studyTrials(trialsId)
                            .subscribe(trial => {
                                this.studyDataset.push(trial.result);
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

        const variable$ = this.brapiService.studyObservationVariables(studyDbId).toPromise();
        variable$
            .then(studyObsVar => {
                this.studyObservationVariables = studyObsVar.result.data;
            });

        const germplasm$ = this.brapiService.studyGermplasms(studyDbId).toPromise();
        germplasm$
            .then(studyGermplasm => {
                this.studyGermplasms = studyGermplasm.result.data;
            });

        this.loaded = Promise.all([study$, variable$, germplasm$]);
        this.loaded.then(() => {
            this.loading = false;
        });
    }

    checkLocation(location: BrapiLocation) {
        return location && location.longitude && location.latitude;
    }

    isNotURN(pui: string) {
        return !(pui.substring(0, 3) === 'urn');
    }

    getTrialStudies(studies: Array<{ studyDbId: string }>): string {
        return studies.map(study => study.studyDbId).join(', ');
    }
}
