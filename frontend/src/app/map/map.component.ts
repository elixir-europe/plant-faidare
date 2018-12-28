import { Component, Input, OnInit } from '@angular/core';
import { SiteModel } from '../model/site.model';
import * as L from 'leaflet';

@Component({
    selector: 'gpds-map',
    templateUrl: './map.component.html',
    styleUrls: ['./map.component.scss']
})
export class MapComponent implements OnInit {

    @Input() sites: Array<SiteModel>;

    constructor() { }

    ngOnInit() {
        // initialize map centered on the first site
        const firstSite: SiteModel = this.sites[0];
        const container = L.DomUtil.get('map');
        if (container) {
            const map = L.map('map').setView([firstSite.result.latitude, firstSite.result.longitude], 5);
            L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
                attribution: 'Frugal Map'
            }).addTo(map);
            // add markers for all sites
            for (const site of this.sites) {
                const icon = L.icon({
                    iconUrl: this.getMarkerIconUrl(site)
                });
                let iconText: string = '<b>' + site.result.name + '</b><br/>';
                iconText += site.result.locationType + '<br/>';
                iconText += `<a href="sites/${site.result.locationDbId}">Details</a>`;
                L.marker(
                    [site.result.latitude, site.result.longitude],
                    { icon: icon }
                ).bindPopup(iconText).addTo(map);
            }
        }
    }

    getMarkerIconUrl(site: SiteModel): string {
        if (site.result.locationType === 'Origin site') {
            return 'assets/gpds/images/marker-icon-red.png';
        }
        if (site.result.locationType === 'Collecting site') {
            return 'assets/gpds/images/marker-icon-blue.png';
        }
        if (site.result.locationType === 'Evaluation site') {
            return 'assets/gpds/images/marker-icon-green.png';
        }
        return 'assets/gpds/images/marker-icon-purple.png';
    }

}
