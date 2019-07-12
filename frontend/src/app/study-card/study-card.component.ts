import { Component, OnInit } from '@angular/core';
import { BrapiService } from '../brapi.service';
import { ActivatedRoute } from '@angular/router';
import { BrapiGermplasm, BrapiLocation, BrapiObservationVariable, BrapiStudy, BrapiTrial } from '../models/brapi.model';
import { KeyValueObject, toKeyValueObjects } from '../utils';

@Component({
    selector: 'faidare-study-card',
    templateUrl: './study-card.component.html',
    styleUrls: ['./study-card.component.scss']
})
export class StudyCardComponent implements OnInit {

    study: BrapiStudy;
    studyGermplasms: BrapiGermplasm[];
    studyObservationVariables: BrapiObservationVariable[];
    additionalInfos: KeyValueObject[];
    studyDataset: BrapiTrial[];
    trialsIds: string[] = [];
    location: BrapiLocation;
    loading: boolean;
    loaded: Promise<any>;

    constructor(private brapiService: BrapiService, private route: ActivatedRoute) {
    }

    async ngOnInit() {
        this.route.paramMap.subscribe(paramMap => {
            this.loading = true;
            const studyDbId = paramMap.get('id');

            const study$ = this.brapiService.study(studyDbId).toPromise();
            study$.then(studyResponse => {

                this.study = studyResponse.result;
                if (this.study.contacts) {
                    this.study.contacts.sort((var1, var2) =>
                        var1.name.localeCompare(var2.name));
                }

                this.additionalInfos = [];
                if (this.study.additionalInfo) {
                    this.additionalInfos = toKeyValueObjects(this.study.additionalInfo).sort();
                }


                // Get study trials
                if (this.study.trialDbIds) {
                    this.trialsIds = this.study.trialDbIds.sort();
                }

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
                    if (this.studyDataset) {
                        this.studyDataset.sort((var1, var2) =>
                            var1.trialName.localeCompare(var2.trialName));
                    }
                }

            });

            study$.finally(() => {
                this.location = null;
                this.brapiService.location(this.study.locationDbId).subscribe(
                    location => {
                        this.location = location.result;
                        console.log(this.location);
                    });

                this.studyObservationVariables = [];
                const variable$ = this.brapiService.studyObservationVariables(studyDbId).toPromise();
                variable$
                    .then(response => {
                        this.studyObservationVariables = response.result.data.sort((var1, var2) =>
                            var1.observationVariableDbId.localeCompare(var2.observationVariableDbId));
                    });

                this.studyGermplasms = [];
                const germplasm$ = this.brapiService.studyGermplasms(studyDbId).toPromise();
                germplasm$
                    .then(studyGermplasm => {
                        this.studyGermplasms = studyGermplasm.result.data.sort((var1, var2) =>
                            var1.germplasmName.localeCompare(var2.germplasmName));
                    });


                this.loaded = Promise.all([study$, variable$, germplasm$]);
                this.loaded.then(() => {
                    this.loading = false;
                });
            });
        });

    }
}
