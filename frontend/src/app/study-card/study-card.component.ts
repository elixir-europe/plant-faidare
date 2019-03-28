import { Component, OnInit } from '@angular/core';
import { BrapiService } from '../brapi.service';
import { ActivatedRoute } from '@angular/router';
import { BrapiGermplasm, BrapiObservationVariable, BrapiStudy, BrapiTrial } from '../models/brapi.model';

import { GnpisService } from '../gnpis.service';
import { DataDiscoverySource } from '../models/data-discovery.model';
import { KeyValueObject, toKeyValueObjects } from '../utils';

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
    additionalInfos: KeyValueObject[];
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

                    // Get study source
                    // TODO Remove the condition when the field includedInDataCatalog will be added to URGI study.
                    const sourceURI = this.study['schema:includedInDataCatalog'];
                    if (sourceURI) {
                        const source$ = this.gnpisService.getSource(sourceURI);
                        source$
                            .subscribe(src => {
                                this.studySource = src;
                            });
                    } else {
                        const urgiURI = 'https://urgi.versailles.inra.fr';
                        const source$ = this.gnpisService.getSource(urgiURI);
                        source$
                            .subscribe(src => {
                                this.studySource = src;
                            });
                    }
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

    }

    isNotURN(pui: string) {
        return !(pui.substring(0, 3) === 'urn');
    }
}
