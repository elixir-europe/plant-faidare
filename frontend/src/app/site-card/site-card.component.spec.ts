import { async, TestBed } from '@angular/core/testing';

import { SiteCardComponent } from './site-card.component';
import { MapComponent } from '../map/map.component';
import { BrapiService } from '../brapi.service';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';
import { BrapiLocation, BrapiResult } from '../models/brapi.model';
import { CardRowComponent } from '../card-row/card-row.component';
import { LoadingSpinnerComponent } from '../loading-spinner/loading-spinner.component';
import { CardTableComponent } from '../card-table/card-table.component';
import { CardSectionComponent } from '../card-section/card-section.component';
import { DataDiscoverySource } from '../models/data-discovery.model';
import { GnpisService } from '../gnpis.service';
import { MockComponent } from 'ng-mocks';
import { XrefsComponent } from '../xrefs/xrefs.component';
import { CoordinatesPipe, CoordinatesService } from 'angular-coordinates';


describe('SiteCardComponent', () => {
    const brapiService = jasmine.createSpyObj(
        'BrapiService', ['location']
    );
    const gnpisService = jasmine.createSpyObj(
        'GnpisService', ['getSource']
    );
    const coordinatesService = jasmine.createSpyObj(
        'CoordinatesService', ['transform']
    );

    const response: BrapiResult<BrapiLocation> = {
        metadata: null,
        result: {
            locationDbId: '1',
            latitude: 1,
            longitude: 1,
            altitude: 1,
            institutionName: '',
            institutionAddress: '',
            countryName: '',
            countryCode: '',
            locationType: '',
            abbreviation: '',
            locationName: 'site1',
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

    const source: DataDiscoverySource = {
        '@id': 'src1',
        '@type': ['schema:DataCatalog'],
        'schema:identifier': 'srcId',
        'schema:name': 'source1',
        'schema:url': 'srcUrl',
        'schema:image': null
    };

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [
                SiteCardComponent, MapComponent, LoadingSpinnerComponent,
                CardRowComponent, CardSectionComponent, CardTableComponent, CoordinatesPipe,
                MockComponent(XrefsComponent)
            ],
            providers: [
                { provide: BrapiService, useValue: brapiService },
                { provide: GnpisService, useValue: gnpisService },
                { provide: CoordinatesService, useValue: coordinatesService },
                {
                    provide: ActivatedRoute,
                    useValue: {
                        paramMap: of(convertToParamMap({ id: 1 }))
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
        brapiService.location.and.returnValues(of(response));
        gnpisService.getSource.and.returnValue(of(source));
        fixture.detectChanges();
        const element = fixture.nativeElement;
        expect(element.querySelector('h3').textContent).toBe(' Site: site1 ');
    });
});
