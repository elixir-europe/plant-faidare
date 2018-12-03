import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BrapiService } from '../brapi.service';

@Component({
    selector: 'gpds-germplasm-card',
    templateUrl: './germplasm-card.component.html',
    styleUrls: ['./germplasm-card.component.scss']


})
export class GermplasmCardComponent implements OnInit {

    constructor(private brapiService: BrapiService, private route: ActivatedRoute) {
    }

    germplasm: object = {};

    ngOnInit() {
        const germplasmId = this.route.snapshot.paramMap.get('id');
        this.brapiService.germplasm(germplasmId)
        .subscribe(germplasm => {
            this.germplasm = germplasm;
        });

    }

}
