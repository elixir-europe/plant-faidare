import { TestBed } from '@angular/core/testing';

import { GnpisService } from './gnpis.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { BrapiMetaData, BrapiResults } from './model/brapi.model';
import { DataDiscoveryCriteria, DataDiscoveryResults, DataDiscoverySource } from './model/data-discovery.model';

describe('GnpisService', () => {
    let service: GnpisService;
    let httpMock;

    const source1: DataDiscoverySource = {
        '@id': 'id1',
        '@type': ['schema:DataCatalog'],
        'schema:identifier': 'ID 1',
        'schema:name': 'source 1',
        'schema:url': 'http://source1.com',
        'schema:image': 'image1',
    };
    const source2: DataDiscoverySource = {
        '@id': 'id2',
        '@type': ['schema:DataCatalog'],
        'schema:identifier': 'ID 2',
        'schema:name': 'source 2',
        'schema:url': 'http://source2.com',
        'schema:image': 'image2',
    };

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule],
            providers: [HttpClientTestingModule]
        });

        httpMock = TestBed.get(HttpTestingController);

        const sources: BrapiResults<DataDiscoverySource> = {
            result: {
                data: [source1, source2]
            }, metadata: {} as BrapiMetaData
        };

        service = TestBed.get(GnpisService);
        const req = httpMock.expectOne({
            method: 'GET',
            url: `${GnpisService.BASE_URL}/sources`
        });
        req.flush(sources);

    });

    it('should suggest with criteria', () => {
        const expectedSuggestions = ['a', 'b', 'c'];
        const field = 'foo';
        const text = 'bar';
        const criteria = { crops: ['d'] } as DataDiscoveryCriteria;
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

    it('should fetch sources', () => {
        service.sourceByURI$.subscribe(sourceByURI => {
            expect(sourceByURI).toEqual({
                'id1': source1,
                'id2': source2
            });
        });
    });

    it('should search documents with criteria', () => {
        const rawResult = {
            metadata: {} as BrapiMetaData,
            result: {
                data: [{
                    '@type': ['Germplasm'],
                    '@id': 'urn',
                    'schema:identifier': 'schema',
                    'schema:name': 'doc_name',
                    'schema:url': 'http://dco/url',
                    'schema:description': 'description',
                    'schema:includedInDataCatalog': source1['@id']
                }, {
                    '@type': ['Phenotyping Study'],
                    '@id': 'urn',
                    'schema:identifier': 'schema',
                    'schema:name': 'doc_name',
                    'schema:url': 'http://dco/url',
                    'schema:description': 'description',
                    'schema:includedInDataCatalog': source2['@id']
                }]
            },
            facets: []
        };

        const criteria = { crops: ['d'] } as DataDiscoveryCriteria;

        service.search(criteria).subscribe(result => {
            expect(result.result.data.length).toBe(2);
            expect(result.result.data[0]['schema:includedInDataCatalog']).toEqual(source1);
            expect(result.result.data[1]['schema:includedInDataCatalog']).toEqual(source2);
        });

        const req = httpMock.expectOne({
            url: `${GnpisService.BASE_URL}/search`,
            method: 'POST'
        });
        req.flush(rawResult);

        expect(req.request.body).toBe(criteria);
    });
})
;
