import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MapComponent } from './map.component';
import { BrapiLocation } from '../models/brapi.model';

describe('MapComponent', () => {
    let component: MapComponent;
    let fixture: ComponentFixture<MapComponent>;
    const location: BrapiLocation = {
        locationDbId: 1,
        latitude: 1,
        longitude: 1,
        altitude: 1,
        institutionName: '',
        institutionAddress: '',
        countryName: '',
        countryCode: '',
        locationType: '',
        abbreviation: '',
        name: 'site1',
        additionalInfo: {
            Topography: '',
            Slope: '',
            Comment: '',
            Exposure: '',
            'Coordinates precision': '',
            'Direction from city': '',
            'Distance to city': '',
            'Environment type': '',
            'Geographical location': '',
            'Site status': ''
        }
    };
    const locations: BrapiLocation[] = [];
    locations.push(location);

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [MapComponent]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(MapComponent);
        component = fixture.componentInstance;
    });

    it('should create component', () => {
        component.locations = locations;
        fixture.detectChanges();
        expect(component).toBeTruthy();
    });

    it('should display map', () => {
        component.locations = locations;
        fixture.detectChanges();
        const element = fixture.nativeElement;
        expect(element.querySelector('#map')).toBeTruthy();
    });

    it('should display map legend', () => {
        component.locations = locations;
        fixture.detectChanges();
        const element = fixture.nativeElement;
        expect(element.querySelector('#maplegend')).toBeTruthy();
    });
});
