import { Component, OnInit } from '@angular/core';
import { BrapiService } from '../brapi.service';
import { ActivatedRoute } from '@angular/router';
import * as L from 'leaflet';
import { assertNumber } from '@angular/core/src/render3/assert';
import { SiteModel, SiteResultModel } from '../model/site.model';

@Component({
    selector: 'gpds-site-card',
    templateUrl: './site-card.component.html',
    styleUrls: ['./site-card.component.scss']
})
export class SiteCardComponent implements OnInit {

    site: SiteModel;

    sites: Array<SiteModel> = new Array<SiteModel>();

    constructor(private brapiService: BrapiService, private route: ActivatedRoute) {
    }

    ngOnInit() {
        // initialize site from location index
        const locationId = +this.route.snapshot.paramMap.get('id');
        this.brapiService.location(locationId).subscribe(
          site => { this.site = site; this.sites.push(site); },
          () => console.log('Unable to load site...')
        );
    }

}
