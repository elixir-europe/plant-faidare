import { Component, Input, OnInit } from '@angular/core';
import * as L from 'leaflet';
import { MarkerClusterGroup } from 'leaflet.markercluster/src';
import { BrapiLocation } from '../models/brapi.model';

@Component({
    selector: 'gpds-map',
    templateUrl: './map.component.html',
    styleUrls: ['./map.component.scss']
})
export class MapComponent implements OnInit {

    @Input() locations: BrapiLocation[];

    constructor() {
    }

    ngOnInit() {
        // initialize map centered on the first site
        const firstLocation: BrapiLocation = this.locations[0];
        const container = L.DomUtil.get('map');
        if (container) {
            const map = L.map('map').setView([firstLocation.latitude, firstLocation.longitude], 5);
            L.tileLayer('https://server.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer/tile/{z}/{y}/{x}', {
                attribution: 'Tiles &copy; Esri &mdash; Source: Esri, DeLorme, NAVTEQ, USGS, Intermap, iPC, NRCAN, Esri Japan, METI, ' +
                    'Esri China (Hong Kong), Esri (Thailand), TomTom, 2012'
            }).addTo(map);
            // add markers for all locations using markercluster plugin
            const markers = new MarkerClusterGroup();
            for (const site of this.locations) {
                const icon = L.icon({
                    iconUrl: this.getMarkerIconUrl(site)
                });
                let iconText: string = '<b>' + site.name + '</b><br/>';
                iconText += site.locationType + '<br/>';
                iconText += `<a href="sites/${site.locationDbId}">Details</a>`;
                markers.addLayer(L.marker(
                    [site.latitude, site.longitude],
                    { icon: icon }
                    ).bindPopup(iconText)
                );
                /* L.marker(
                    [site.result.latitude, site.result.longitude],
                    { icon: icon }
                ).bindPopup(iconText).addTo(map); */
            }
            map.addLayer(markers);
        }
    }

    getMarkerIconUrl(site: BrapiLocation): string {
        if (site.locationType === 'Origin site') {
            return 'assets/gpds/images/marker-icon-red.png';
        }
        if (site.locationType === 'Collecting site') {
            return 'assets/gpds/images/marker-icon-blue.png';
        }
        if (site.locationType === 'Evaluation site') {
            return 'assets/gpds/images/marker-icon-green.png';
        }
        return 'assets/gpds/images/marker-icon-purple.png';
    }
}
