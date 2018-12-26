import { Component, OnInit } from '@angular/core';
import { BrapiService } from '../brapi.service';
import { ActivatedRoute } from '@angular/router';

@Component({
    selector: 'gpds-site-card',
    templateUrl: './site-card.component.html',
    styleUrls: ['./site-card.component.scss']
})
export class SiteCardComponent implements OnInit {

    site: object = {};

    constructor(private brapiService: BrapiService, private route: ActivatedRoute) {
    }

    ngOnInit() {
        const locationId = +this.route.snapshot.paramMap.get('id');
        this.brapiService.location(locationId).subscribe(
          site => { this.site = site; },
          () => console.log('Unable to load site...')
        );
    }

}
