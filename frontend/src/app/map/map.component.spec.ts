import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MapComponent } from './map.component';
import { SiteModel } from '../models/site.model';

describe('MapComponent', () => {
    let component: MapComponent;
    let fixture: ComponentFixture<MapComponent>;
    const site: SiteModel = {
        result: {
            locationDbId: 1,
            latitude: 1,
            longitude: 1,
            altitude: 1,
            institutionName: '',
            institutionAdress: '',
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
        }
    };
    const sites: Array<SiteModel> = new Array<SiteModel>();
    sites.push(site);

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

    it('should create', () => {
        component.sites = sites;
        fixture.detectChanges();
        expect(component).toBeTruthy();
    });
});
