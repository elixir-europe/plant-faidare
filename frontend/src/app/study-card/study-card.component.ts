import { Component, OnInit } from '@angular/core';
import { BrapiService } from '../brapi.service';
import { ActivatedRoute } from '@angular/router';
import { BrapiGermplasm, BrapiObservationVariable, BrapiStudy, BrapiTrial } from '../models/brapi.model';

import { GnpisService } from '../gnpis.service';
import { DataDiscoverySource } from '../models/data-discovery.model';
import { KeyValueObject } from '../utils';

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
                        this.study.contacts.sort(this.compareContacts);
                    }

                    this.additionalInfos = [];
                    if (this.study.additionalInfo) {
                        this.additionalInfos = KeyValueObject.fromObject(this.study.additionalInfo).sort();
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
                            this.studyDataset.sort(this.compareTrials);
                        }
                    }

                    // Get study source
                    const sourceURI = this.study['schema:includedInDataCatalog'];
                    if (sourceURI) {
                        const source$ = this.gnpisService.getSource(sourceURI);
                        source$
                            .subscribe(src => {
                                console.log(src);
                                this.studySource = src;
                            });
                    }
                });

            this.studyObservationVariables = [];
            const variable$ = this.brapiService.studyObservationVariables(studyDbId).toPromise();
            variable$
                .then(response => {
                    this.studyObservationVariables = response.result.data.sort(this.compareVariables);
                });

            this.studyGermplasms = [];
            const germplasm$ = this.brapiService.studyGermplasms(studyDbId).toPromise();
            germplasm$
                .then(studyGermplasm => {
                    this.studyGermplasms = studyGermplasm.result.data.sort(this.compareStudyGermplasm);
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

    compareTrials(a, b) {
        if (a.trialName < b.trialName) {
            return -1;
        }
        if (a.trialName > b.trialName) {
            return 1;
        }
        return 0;
    }

    compareStudyGermplasm(a, b) {
        if (a.germplasmName < b.germplasmName) {
            return -1;
        }
        if (a.germplasmName > b.germplasmName) {
            return 1;
        }
        return 0;
    }

    compareVariables(a, b) {
        if (a.observationVariableDbId < b.observationVariableDbId) {
            return -1;
        }
        if (a.observationVariableDbId > b.observationVariableDbId) {
            return 1;
        }
        return 0;
    }

    compareContacts(a, b) {
        if (a.name < b.name) {
            return -1;
        }
        if (a.name > b.name) {
            return 1;
        }
        return 0;
    }
}
