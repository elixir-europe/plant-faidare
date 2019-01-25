import { TestBed } from '@angular/core/testing';

import { BrapiService } from './brapi.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { SiteModel } from './models/site.model';

describe('BrapiService', () => {

    let brapiService: BrapiService;
    let http: HttpTestingController;

    beforeEach(() => TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
    }));

    beforeEach(() => {
        brapiService = TestBed.get(BrapiService);
        http = TestBed.get(HttpTestingController);
    });

    afterAll(() => http.verify());

    it('should return an Observable of 1 SiteModel', () => {
        const hardCodedSite: SiteModel = {
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
                additionalInfo: {}
            }
        };
        let actualSite: SiteModel;
        const locationId = hardCodedSite.result.locationDbId;
        brapiService.location(hardCodedSite.result.locationDbId).subscribe(site => actualSite = site);

        http.expectOne(`/brapi/v1/locations/${locationId}`)
            .flush(hardCodedSite);

        expect(actualSite).toEqual(hardCodedSite);
    });
});
