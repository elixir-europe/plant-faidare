import { Component, OnInit } from '@angular/core';
import { BrapiService } from '../brapi.service';
import { ActivatedRoute } from '@angular/router';
import { BrapiLocation } from '../models/brapi.model';
import { KeyValueObject } from '../utils';

@Component({
    selector: 'gpds-site-card',
    templateUrl: './site-card.component.html',
    styleUrls: ['./site-card.component.scss']
})
export class SiteCardComponent implements OnInit {

    location: BrapiLocation;
    additionalInfos: KeyValueObject[];
    loading = true;


    constructor(private brapiService: BrapiService, private route: ActivatedRoute) {
    }

    ngOnInit() {
        this.route.paramMap.subscribe(paramMap => {
            // initialize site from location ID
            const locationId = paramMap.get('id');
            this.brapiService.location(locationId).subscribe(
                response => {
                    this.location = response.result;
                    this.additionalInfos = [];
                    if (this.location.additionalInfo) {
                        this.additionalInfos = KeyValueObject.fromObject(this.location.additionalInfo);
                    }
                    this.loading = false;
                }
            );
        });

    }
}
