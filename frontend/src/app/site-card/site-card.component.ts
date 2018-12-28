import { Component, OnInit } from '@angular/core';
import { BrapiService } from '../brapi.service';
import { ActivatedRoute } from '@angular/router';
import { SiteModel } from '../models/site.model';

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
          site => { this.site = site; this.sites.push(site); /* this.sites.push(this.getRandomSite(site)); */ },
          () => console.log('Unable to load site...')
        );
    }

    getRandomSite(site: SiteModel): SiteModel {
        const newSite = site;
        newSite.result.latitude = site.result.latitude - 1;
        newSite.result.longitude = site.result.longitude - 1;
        return newSite;
    }

}
