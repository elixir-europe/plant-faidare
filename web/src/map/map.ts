import * as L from 'leaflet';
import 'leaflet.markercluster';

interface MapLocation {
    locationDbId: string;
    locationType: 'Origin site' | 'Collecting site' | 'Evaluation site' | null;
    locationName: string;
    latitude: number;
    longitude: number;
}

interface MapOptions {
    contextPath: string;
    locations: Array<MapLocation>;
}

function markerColor(location: MapLocation) {
    switch (location.locationType) {
        case 'Origin site':
            return 'red';
        case 'Collecting site':
            return 'blue';
        case 'Evaluation site':
            return 'green';
    }
    return 'purple';
}

function markerIconUrl(contextPath: string, location: MapLocation) {
    return `${contextPath}/resources/images/marker-icon-${markerColor(location)}.png`;
}

export function initializeMap(options: MapOptions) {
    if (!options.locations.length) {
        return;
    }

    const mapContainerElement = document.querySelector('#map-container');
    mapContainerElement!.classList.remove('d-none');
    const mapElement = document.querySelector('#map') as HTMLElement;
    const map = L.map(mapElement);
    L.tileLayer('https://server.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer/tile/{z}/{y}/{x}', {
        attribution:
            'Tiles &copy; Esri &mdash; Source: Esri, DeLorme, NAVTEQ, USGS, Intermap, iPC, NRCAN, Esri Japan, METI, ' +
            'Esri China (Hong Kong), Esri (Thailand), TomTom, 2012'
    }).addTo(map);

    const firstLocation = options.locations[0];
    map.setView([firstLocation.latitude, firstLocation.longitude], 5);

    const markers = L.markerClusterGroup();
    const mapMarkers: Array<L.Marker> = [];
    for (const location of options.locations) {
        const icon = L.icon({
            iconUrl: markerIconUrl(options.contextPath, location),
            iconAnchor: [12, 41] // point of the icon which will correspond to marker's location
        });

        const popupElement = document.createElement('div');

        const titleElement = document.createElement('strong');
        titleElement.innerText = location.locationName;
        popupElement.appendChild(titleElement);
        popupElement.appendChild(document.createElement('br'));

        if (location.locationType) {
            const typeElement = document.createElement('span');
            typeElement.innerText = location.locationType;
            popupElement.appendChild(typeElement);
            popupElement.appendChild(document.createElement('br'));
        }

        const linkElement = document.createElement('a');
        linkElement.innerText = 'Details';
        linkElement.href = `${options.contextPath}/sites/${location.locationDbId}`;
        popupElement.appendChild(linkElement);

        const marker = L.marker([location.latitude, location.longitude], { icon: icon });
        markers.addLayer(marker.bindPopup(popupElement));
        mapMarkers.push(marker);
    }
    const initialZoom = map.getZoom();

    map.fitBounds(L.featureGroup(mapMarkers).getBounds());
    const markerZoom = map.getZoom();

    setTimeout(() => {
        map.setZoom(Math.min(initialZoom, markerZoom));
        map.addLayer(markers);
    }, 100);
}
