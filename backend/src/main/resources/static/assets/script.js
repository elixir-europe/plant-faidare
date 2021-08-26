const faidare = (() => {
    function initializePopovers() {
        const popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'))
        popoverTriggerList.forEach(popoverTriggerEl => {
            const options = {};
            const contentSelector = popoverTriggerEl.dataset.bsElement;
            if (contentSelector) {
                const content = document.querySelector(contentSelector);
                if (content) {
                    options.content = () => {
                        const element = document.createElement('div');
                        element.innerHTML = content.innerHTML;
                        return element;
                    };
                    options.html = true;
                } else {
                    throw new Error('element with selector ' + contentSelector + ' not found');
                }
            }
            return new bootstrap.Popover(popoverTriggerEl, options);
        });
    }

    function markerColor(location) {
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

    function markerIconUrl(contextPath, location) {
        return `${contextPath}/assets/images/marker-icon-${markerColor(location)}.png`;
    }

    function initializeMap(options) {
        if (!options.locations.length) {
            return;
        }

        const mapContainerElement = document.querySelector('#map-container');
        mapContainerElement.classList.remove("d-none");
        const mapElement = document.querySelector('#map');
        const map = L.map(mapElement);
        L.tileLayer('https://server.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer/tile/{z}/{y}/{x}', {
            attribution: 'Tiles &copy; Esri &mdash; Source: Esri, DeLorme, NAVTEQ, USGS, Intermap, iPC, NRCAN, Esri Japan, METI, ' +
                'Esri China (Hong Kong), Esri (Thailand), TomTom, 2012'
        }).addTo(map);

        const firstLocation = options.locations[0];
        map.setView([firstLocation.latitude, firstLocation.longitude], 5);

        const markers = L.markerClusterGroup();
        const mapMarkers = [];
        for (const location of options.locations) {
            const icon = L.icon({
                iconUrl: markerIconUrl(options.contextPath, location),
                iconAnchor: [12, 41], // point of the icon which will correspond to marker's location
            });
            const popupElement = document.createElement('div');
            const titleElement = document.createElement('strong');
            titleElement.innerText = location.locationName;
            const typeElement = document.createElement('div');
            typeElement.innerText = location.locationType;
            const linkElement = document.createElement('a');
            linkElement.innerText = 'Details';
            linkElement.href = `${options.contextPath}/sites/${location.locationDbId}`;
            popupElement.appendChild(titleElement);
            popupElement.appendChild(typeElement);
            popupElement.appendChild(linkElement);

            const marker = L.marker(
                [location.latitude, location.longitude],
                { icon: icon }
            );
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

    return {
        initializePopovers,
        initializeMap
    };
})();


