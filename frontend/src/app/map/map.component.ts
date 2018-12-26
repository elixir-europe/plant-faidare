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
                iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.2.0/images/marker-icon.png'
            });
            L.marker(
                [this.site.result.latitude, this.site.result.longitude],
                { icon: myIcon }
                ).bindPopup(this.site.result.locationType).addTo(myfrugalmap).openPopup();
        }
    }

}
