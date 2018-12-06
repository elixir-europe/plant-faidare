import { TestBed } from '@angular/core/testing';

import { GnpisService } from './gnpis.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

describe('GnpisService', () => {
    let service;
    let httpMock;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule],
            providers: [HttpClientTestingModule]
        });
        service = TestBed.get(GnpisService);
        httpMock = TestBed.get(HttpTestingController);
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

    it('should search documents with criteria', () => {
        const expectedDocuments = [{
            '@type': ['doc'],
            '@id': 'urn',
            'schema:identifier': 'schema',
            'schema:name': 'doc_name',
            'schema:url': 'http://dco/url',
            'schema:description': 'description',
            'schema:includedInDataCatalog': 'catalog'
        }, {
            '@type': ['doc'],
            '@id': 'urn',
            'schema:identifier': 'schema',
            'schema:name': 'doc_name',
            'schema:url': 'http://dco/url',
            'schema:description': 'description',
            'schema:includedInDataCatalog': 'catalog'
        }];

        const criteria = { crops: ['d'] };

        service.search(criteria).subscribe(documents => {
            expect(documents.length).toBe(2);
            expect(documents).toBe(expectedDocuments);
        });

        const req = httpMock.expectOne({
            url: `${GnpisService.BASE_URL}/search`,
            method: 'POST'
        });
        req.flush(expectedDocuments);
        expect(req.request.body).toBe(criteria);
    });
});
