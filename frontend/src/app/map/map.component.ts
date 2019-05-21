import { Component, Input, OnInit } from '@angular/core';
import * as L from 'leaflet';
import { MarkerClusterGroup } from 'leaflet.markercluster/src';
import { BrapiLocation } from '../models/brapi.model';

@Component({
    selector: 'faidare-map',
    templateUrl: './map.component.html',
    styleUrls: ['./map.component.scss']
})
export class MapComponent implements OnInit {

    @Input() locations: BrapiLocation[];
    curatedLocationList: BrapiLocation[] = [];

    constructor() {
    }

    ngOnInit() {

        this.removeEmptyLocations(this.locations);
        if (this.curatedLocationList.length > 0) {
            const map = L.map('map');

            // initialize map centered on the first site
            const firstLocation: BrapiLocation = this.curatedLocationList[0];
            if (firstLocation) {
                map.setView([firstLocation.latitude, firstLocation.longitude], 5);
            }

            L.tileLayer('https://server.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer/tile/{z}/{y}/{x}', {
                attribution: 'Tiles &copy; Esri &mdash; Source: Esri, DeLorme, NAVTEQ, USGS, Intermap, iPC, NRCAN, Esri Japan, METI, ' +
                    'Esri China (Hong Kong), Esri (Thailand), TomTom, 2012'
            }).addTo(map);

            // add markers for all locations using markercluster plugin
            const markers = new MarkerClusterGroup();
            for (const location of this.curatedLocationList) {
                const icon = L.icon({
                    iconUrl: this.getMarkerIconUrl(location)
                });
                const popupText = `
                  <b>${location.locationName}</b><br/>
                  ${location.locationType}<br/>
                  <a href="sites/${location.locationDbId}">Details</a>
                `;
                markers.addLayer(
                    L.marker(
                        [location.latitude, location.longitude],
                        { icon: icon }
                    ).bindPopup(popupText)
                );
            }
            map.addLayer(markers);
        } else {
            L.DomUtil.get('map').remove();
        }
    }


    getMarkerIconUrl(site: BrapiLocation): string {
        if (site.locationType === 'Origin site') {
            return 'assets/faidare/images/marker-icon-red.png';
        }
        if (site.locationType === 'Collecting site') {
            return 'assets/faidare/images/marker-icon-blue.png';
        }
        if (site.locationType === 'Evaluation site') {
            return 'assets/faidare/images/marker-icon-green.png';
        }
        return 'assets/faidare/images/marker-icon-purple.png';
    }

    removeEmptyLocations(locations: BrapiLocation[]) {
        for (const location of locations) {
            if (location && location.latitude && location.longitude) {
                this.curatedLocationList.push(location);
            }
        }
    }
}
