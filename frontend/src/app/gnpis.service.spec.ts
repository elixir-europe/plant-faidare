import { Germplasm, Institute, Origin, Site } from './models/gnpis.germplasm.model';

import { BASE_URL, BASE_URL_GERMPLASM, GnpisService } from './gnpis.service';
import { BrapiMetaData, BrapiResults } from './models/brapi.model';
import { DataDiscoveryCriteria, DataDiscoverySource } from './models/data-discovery.model';
import { BrapiDescriptor, BrapiDonor, BrapiSet } from './models/brapi.germplasm.model';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

describe('GnpisService', () => {

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

    const site: Site = {
        latitude: null,
        longitude: null,
        siteId: 1,
        siteName: 'Nantes',
        siteType: null
    };

    const brapiDescriptor: BrapiDescriptor = {
        name: 'caracteristique1',
        pui: '12',
        value: '32'
    };

    const brapiInstitute: Institute = {
        instituteName: 'urgi',
        instituteCode: 'inra',
        acronym: 'urgi',
        organisation: 'inra',
        instituteType: 'labo',
        webSite: 'www.labo.fr',
        address: '12',
        logo: null
    };

    const brapiOrigin: Origin = { ...brapiInstitute,
        institute: brapiInstitute,
        germplasmPUI: '12',
        accessionNumber: '12',
        accessionCreationDate: '1993',
        materialType: 'feuille',
        collectors: null,
        registrationYear: '1996',
        deregistrationYear: '1912',
        distributionStatus: null
    };

    const brapiDonor: BrapiDonor = {
        donorInstitute: brapiInstitute,
        donorGermplasmPUI: '12',
        donorAccessionNumber: '12',
        donorInstituteCode: 'urgi',
        donationDate: null
    };

    const brapiSet: BrapiSet = {
        germplasmCount: 12,
        germplasmRef: null,
        id: 12,
        name: 'truc',
        type: 'plan'
    };

    const germplasmTest: Germplasm = {
        url: 'www.cirad.fr',
        source: 'cirad',
        germplasmDbId: 'test',
        defaultDisplayName: 'test',
        accessionNumber: 'test',
        germplasmName: 'test',
        germplasmPUI: 'doi:1256',
        pedigree: 'tree',
        seedSource: 'inra',
        synonyms: null,
        commonCropName: null,
        instituteCode: 'grc12',
        instituteName: 'institut',
        biologicalStatusOfAccessionCode: null,
        countryOfOriginCode: null,
        typeOfGermplasmStorageCode: null,
        taxonIds: null,
        genus: 'genre',
        species: 'esp',
        speciesAuthority: 'L',
        subtaxa: null,
        subtaxaAuthority: null,
        donors: [brapiDonor],
        acquisitionDate: null,
        genusSpecies: null,
        genusSpeciesSubtaxa: null,
        taxonSynonyms: ['pomme', 'api'],
        taxonCommonNames: ['pomme', 'api'],
        geneticNature: null,
        comment: null,
        photo: null,
        holdingInstitute: brapiInstitute,
        holdingGenbank: brapiInstitute,
        presenceStatus: null,
        children: null,
        descriptors: [brapiDescriptor],
        originSite: site,
        collectingSite: null,
        evaluationSites: null,
        collector: brapiOrigin,
        breeder: brapiOrigin,
        distributors: [brapiOrigin],
        panel: [brapiSet],
        collection: [brapiSet],
        population: [brapiSet]
    };

    let gnpisService: GnpisService;
    let http: HttpTestingController;
    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule],
            providers: [HttpClientTestingModule]
        });
        gnpisService = TestBed.get(GnpisService);
        http = TestBed.get(HttpTestingController);

        const sources: BrapiResults<DataDiscoverySource> = {
            result: {
                data: [source1, source2]
            }, metadata: {} as BrapiMetaData
        };

        const req = http.expectOne({
            method: 'GET',
            url: `${BASE_URL}/sources`
        });
        req.flush(sources);

    });


    afterEach(() => http.verify());


    it('should suggest with criteria', () => {
        const expectedSuggestions = ['a', 'b', 'c'];
        const field = 'foo';
        const text = 'bar';
        const criteria = { crops: ['d'] } as DataDiscoveryCriteria;
        const fetchSize = 3;

        gnpisService.suggest(field, fetchSize, text, criteria).subscribe(suggestions => {
            expect(suggestions.length).toBe(3);
            expect(suggestions).toBe(expectedSuggestions);
        });

        const req = http.expectOne({
            url: `${BASE_URL}/suggest?field=${field}&text=${text}&fetchSize=${fetchSize}`,
            method: 'POST'
        });
        req.flush(expectedSuggestions);
        expect(req.request.body).toBe(criteria);
    });


    afterAll(() => http.verify());

    it('should fetch the germplasm', () => {
        let fetchedGermplasm: Germplasm;
        const germplasmDbId: string = germplasmTest.germplasmDbId;
        gnpisService.germplasm(germplasmDbId).subscribe(response => {
            fetchedGermplasm = response;
        });
        http.expectOne(`${BASE_URL_GERMPLASM}/germplasm?id=${germplasmDbId}`)
            .flush(germplasmTest);

        expect(fetchedGermplasm).toEqual(germplasmTest);
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

        gnpisService.search(criteria).subscribe(result => {
            expect(result.result.data.length).toBe(2);
            expect(result.result.data[0]['schema:includedInDataCatalog']).toEqual(source1);
            expect(result.result.data[1]['schema:includedInDataCatalog']).toEqual(source2);
        });

        const req = http.expectOne({
            url: `${BASE_URL}/search`,
            method: 'POST'
        });
        req.flush(rawResult);

        expect(req.request.body).toBe(criteria);
    });

    it('should fetch sources', () => {
        gnpisService.sourceByURI$.subscribe(sourceByURI => {
            expect(sourceByURI).toEqual({
                'id1': source1,
                'id2': source2
            });
        });
    });
})
;
