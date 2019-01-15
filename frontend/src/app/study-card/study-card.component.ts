import { Component, OnInit } from '@angular/core';
import { BrapiService } from '../brapi.service';
import { ActivatedRoute } from '@angular/router';
import { BrapiGermplasmeData, BrapiObservationUnitsData, BrapiObservationVariablesData, BrapiResults } from '../model/brapi.model';
import { arrayToString } from '../utils';
import { GnpisService } from '../gnpis.service';
import { DataDiscoverySource } from '../model/data-discovery.model';

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


    constructor(private brapiService: BrapiService, private  gnpisService: GnpisService, private route: ActivatedRoute) {
    }

    ngOnInit() {
        const studyDbId = this.route.snapshot.paramMap.get('id');
        const sourceId = this.route.snapshot.queryParams.source;

        this.brapiService.study(studyDbId)
            .subscribe(study => {
                this.study = study;
                this.study.result.seasons = [arrayToString(study.result.seasons, ', ')];
            });

        this.brapiService.studyObservationVariables(studyDbId)
            .subscribe(studyObsVar => {
                this.studyObservationVariables = studyObsVar.result.data;
            });

        this.brapiService.studyObservationUnits(studyDbId)
            .subscribe(obsUnit => {
                this.observationUnits = obsUnit.result.data;
            });

        this.brapiService.studyGermplasms(studyDbId)
            .subscribe(studyGermplasm => {
                this.studyGermplasms = studyGermplasm.result.data;
            });

        this.gnpisService.getSource(sourceId)
            .subscribe(src => {
                this.studySource = src;
            });
    }

    checkEmptyArray(array: any[]) {
        return !(array.length === 0);
    }

    puiFilter(pui: string) {
        return !(pui.substring(0, 3) === 'urn');
    }

    getVariableLink(variable: BrapiObservationVariablesData) {
        return `https://urgi.versailles.inra.fr/ontologyportal.do#termIdentifier=${variable.observationVariableDbId}`;
    }

    getTaxon(genu: string, specie: string) {
        return `${genu} ${specie}`;
    }
}
