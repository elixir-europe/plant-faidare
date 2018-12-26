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

    constructor(private brapiService: BrapiService, private route: ActivatedRoute) {
    }

    ngOnInit() {
        // initialize site from location index
        const locationId = +this.route.snapshot.paramMap.get('id');
        this.brapiService.location(locationId).subscribe(
          site => { this.site = site; },
          () => console.log('Unable to load site...')
        );
        // console.log(this.site.result.latitude);
        // initialize map
        /*const container = L.DomUtil.get('map');
        if (container) {
            // const result = this.site['result'];
            // const latitude = result['latitude'];
            // console.log(latitude);
            const myfrugalmap = L.map('map').setView([50.6311634, 3.0599573], 12);
            L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
                attribution: 'Frugal Map'
            }).addTo(myfrugalmap);
        }*/
    }

}
