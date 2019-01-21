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

    additionalInfoKeys: string[] = [];

    loadingError = false;

    constructor(private brapiService: BrapiService, private route: ActivatedRoute) {
    }

    ngOnInit() {
        // initialize site from location index
        const locationId = +this.route.snapshot.paramMap.get('id');
        this.brapiService.location(locationId).subscribe(
          site => {
              this.site = site; this.sites.push(site);
              if (site.result.additionalInfo) {
                  this.additionalInfoKeys = Object.keys(site.result.additionalInfo);
              }
          },
          () => { console.log('Unable to load site...'); this.loadingError = true; }
        );
    }
}
