import { Component, Input, OnInit } from '@angular/core';
import { SiteModel } from '../model/site.model';
import * as L from 'leaflet';

@Component({
    selector: 'gpds-map',
    templateUrl: './map.component.html',
    styleUrls: ['./map.component.scss']
})
export class MapComponent implements OnInit {

    @Input() site: SiteModel;

    constructor() { }

    ngOnInit() {
        // initialize map
        const container = L.DomUtil.get('map');
        if (container) {
            const myfrugalmap = L.map('map').setView([this.site.result.latitude, this.site.result.longitude], 5);
            L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
                attribution: 'Frugal Map'
            }).addTo(myfrugalmap);
            // add marker
            const myIcon = L.icon({
                iconUrl: this.getMarkerIconUrl()
            });
            L.marker(
                [this.site.result.latitude, this.site.result.longitude],
                { icon: myIcon }
                ).bindPopup(this.site.result.locationType).addTo(myfrugalmap).openPopup();
        }
    }

    getMarkerIconUrl(): string {
        if (this.site.result.locationType === 'Origin and Collecting site') {
            return 'assets/gpds/images/marker-icon-purple.png';
        }
        if (this.site.result.locationType === 'Origin site') {
            return 'assets/gpds/images/marker-icon-red.png';
        }
        if (this.site.result.locationType === 'Collecting site') {
            return 'assets/gpds/images/marker-icon-blue.png';
        }
        if (this.site.result.locationType === 'Evaluation site') {
            return 'assets/gpds/images/marker-icon-green.png';
        }
    }

}
