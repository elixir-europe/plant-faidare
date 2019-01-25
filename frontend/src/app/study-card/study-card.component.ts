import { Component, OnInit } from '@angular/core';
import { BrapiService } from '../brapi.service';
import { ActivatedRoute } from '@angular/router';

@Component({
    selector: 'gpds-study-card',
    templateUrl: './study-card.component.html',
    styleUrls: ['./study-card.component.scss']
})
export class StudyCardComponent implements OnInit {

    study: object = {};
    studyGermplasms: string[] = [];
    studyObservationVariables: string[] = [];
    objectKeys = Object.keys;

    constructor(private brapiService: BrapiService, private route: ActivatedRoute) {
    }

    ngOnInit() {
        const studyDbId = this.route.snapshot.paramMap.get('id');
        this.brapiService.study(studyDbId)
            .subscribe(study => {
                this.study = study;
            });
        this.brapiService.studyObservationVariables(studyDbId)
            .subscribe(studyObsVar => {
                this.studyObservationVariables.concat(studyObsVar);
            });
        this.brapiService.studyGermplasms(studyDbId)
            .subscribe(studyGermplasm => {
                this.studyGermplasms.concat(studyGermplasm);
            });
    }
}
