import { async, TestBed } from '@angular/core/testing';

import { SiteCardComponent } from './site-card.component';
import { MapComponent } from '../map/map.component';
import { BrapiService } from '../brapi.service';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';
import { BrapiLocation, BrapiResult } from '../models/brapi.model';

describe('SiteCardComponent', () => {
    const brapiService = jasmine.createSpyObj(
        'BrapiService', ['location']
    );
    const response: BrapiResult<BrapiLocation> = {
        metadata: null,
        result: {
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
        }
    };

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [SiteCardComponent, MapComponent],
            providers: [
                { provide: BrapiService, useValue: brapiService },
                {
                    provide: ActivatedRoute,
                    useValue: {
                        snapshot: {
                            paramMap: convertToParamMap( { id: 1 } )
                        }
                    }
                }
            ]
        });
    }));

    it('should create component', () => {
        const fixture = TestBed.createComponent(SiteCardComponent);
        const component = fixture.componentInstance;
        expect(component).toBeTruthy();
    });

    it('should display site', () => {
        const fixture = TestBed.createComponent(SiteCardComponent);
        const component = fixture.componentInstance;
        brapiService.location.and.returnValues(of(response));
        fixture.detectChanges();
        const element = fixture.nativeElement;
        expect(element.querySelector('h1').textContent).toBe(' Site: site1 ');
    });

    it('should display error message when site loading is in error', () => {
        const fixture = TestBed.createComponent(SiteCardComponent);
        const component = fixture.componentInstance;
        brapiService.location.and.returnValues(of(response));
        component.loadingError = true;
        fixture.detectChanges();
        const element = fixture.nativeElement;
        expect(element.querySelector('#error')).toBeTruthy();
    });
});
