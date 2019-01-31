import { Component, OnInit } from '@angular/core';
import { BrapiService } from '../brapi.service';
import { ActivatedRoute } from '@angular/router';
import { BrapiLocation } from '../models/brapi.model';

@Component({
    selector: 'gpds-site-card',
    templateUrl: './site-card.component.html',
    styleUrls: ['./site-card.component.scss']
})
export class SiteCardComponent implements OnInit {

    site: BrapiLocation;

    additionalInfoKeys: string[] = [];

    loadingError = false;

    constructor(private brapiService: BrapiService, private route: ActivatedRoute) {
    }

    ngOnInit() {
        // initialize site from location index
        const locationId = +this.route.snapshot.paramMap.get('id');
        this.brapiService.location(locationId).subscribe(
          response => {
              this.site = response.result;
          },
          () => { console.log('Unable to load site...'); this.loadingError = true; }
        );
    }
}
