import { Component, OnInit } from '@angular/core';
import { BrapiService } from '../brapi.service';
import { ActivatedRoute } from '@angular/router';
import {
    BrapiGermplasmeData,
    BrapiLocation,
    BrapiObservationUnitsData,
    BrapiObservationVariablesData,
    BrapiResults,
    BrapiTrialsResult
} from '../models/brapi.model';
import { arrayToString } from '../utils';
import { GnpisService } from '../gnpis.service';
import { DataDiscoverySource } from '../models/data-discovery.model';

@Component({
    selector: 'gpds-study-card',
    templateUrl: './study-card.component.html',
    styleUrls: ['./study-card.component.scss']
})
export class StudyCardComponent implements OnInit {

    study: BrapiResults<null>;
    studySource: DataDiscoverySource;
    observationUnits: BrapiObservationUnitsData[];
    studyGermplasms: BrapiGermplasmeData[];
    studyObservationVariables: BrapiObservationVariablesData[];
    site: BrapiLocation[];
    additionalInfoKeys: string[] = [];
    studyDataset: BrapiTrialsResult[] = [];
    trialsIds: string[];
    loading = true;


    constructor(private brapiService: BrapiService, private  gnpisService: GnpisService, private route: ActivatedRoute) {
    }

    ngOnInit() {
        const studyDbId = this.route.snapshot.paramMap.get('id');
        const sourceId = this.route.snapshot.queryParams.source;

        this.brapiService.study(studyDbId)
            .subscribe(study => {
                this.loading = true;
                this.study = study;
                this.study.result.seasons = [arrayToString(study.result.seasons, ', ')];
                this.site = [study.result.location];
                if (study.result.additionalInfo) {
                    this.additionalInfoKeys = Object.keys(study.result.additionalInfo);
                }
                this.trialsIds = study.result.trialDbIds;
                if (this.trialsIds && this.trialsIds !== []) {
                    for (const trialsId of this.trialsIds) {
                        this.brapiService.studyTrials(trialsId)
                            .subscribe(trial => {
                                this.studyDataset.push(trial);
                            });
                    }
                }
                this.loading = false;
            });

        this.brapiService.studyObservationVariables(studyDbId)
            .subscribe(studyObsVar => {
                this.loading = true;
                this.studyObservationVariables = studyObsVar.result.data;
                this.loading = false;
            });

        this.brapiService.studyObservationUnits(studyDbId)
            .subscribe(obsUnit => {
                this.loading = true;
                this.observationUnits = obsUnit.result.data;
                this.loading = false;
            });

        this.brapiService.studyGermplasms(studyDbId)
            .subscribe(studyGermplasm => {
                this.loading = true;
                this.studyGermplasms = studyGermplasm.result.data;
                this.loading = false;
            });

        this.gnpisService.getSource(sourceId)
            .subscribe(src => {
                this.loading = true;
                this.studySource = src;
                this.loading = false;
            });
    }

    checkEmptyArray(array: any[]) {
        return !(array.length === 0);
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

    getVariableLink(variable: BrapiObservationVariablesData) {
        return `https://urgi.versailles.inra.fr/ontologyportal.do#termIdentifier=${variable.observationVariableDbId}`;
    }

    getTaxon(genu: string, specie: string, subtaxa: string) {
        return `${genu} ${specie} ${subtaxa}`;
    }

    getTrialStudies(studies: Array<{ studyDbId: string }>): string {
        const studiesPui: string[] = [];
        for (const study of studies) {
            studiesPui.push(study.studyDbId);
        }
        return arrayToString(studiesPui, ' ; ');
    }
}
