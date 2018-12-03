import { getTestBed, TestBed } from '@angular/core/testing';

import { GnpisService } from './gnpis.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

describe('GnpisService', () => {
    let injector;
    let service;
    let httpMock;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule],
            providers: [HttpClientTestingModule]
        });
        injector = getTestBed();
        service = injector.get(GnpisService);
        httpMock = injector.get(HttpTestingController);
    });

    it('should be created', () => {
        expect(service).toBeTruthy();
        httpMock.verify();
    });

    it('should suggest with criteria', () => {
        const expectedSuggestions = ['a', 'b', 'c'];
        const field = 'foo';
        const text = 'bar';
        const criteria = { crops: ['d'] };
        const fetchSize = 3;

        service.suggest(field, fetchSize, text, criteria).subscribe(suggestions => {
            expect(suggestions.length).toBe(3);
            expect(suggestions).toBe(expectedSuggestions);
        });

        const req = httpMock.expectOne({
            url: `${GnpisService.BASE_URL}/suggest?field=${field}&text=${text}&fetchSize=${fetchSize}`,
            method: 'POST'
        });
        req.flush(expectedSuggestions);
        expect(req.request.body).toBe(criteria);
    });
});
