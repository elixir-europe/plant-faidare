import { Germplasm, GermplasmData, GermplasmResult } from './models/gnpis.germplasm.model';
import { TestBed } from '@angular/core/testing';

import { BASE_URL, GnpisService } from './gnpis.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { BrapiMetaData } from './models/brapi.model';
import { DataDiscoveryCriteria } from './models/data-discovery.model';
import {
    BrapiDescriptor,
    BrapiDonor,
    BrapiGermplasmAttributes,
    BrapiGermplasmPedigree,
    BrapiGermplasmProgeny,
    BrapiInstitute,
    BrapiOrigin,
    BrapiSet,
    BrapiSibling,
    BrapiSite
} from './models/brapi.germplasm.model';

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
            url: `${BASE_URL}/sources`
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
            url: `${BASE_URL}/suggest?field=${field}&text=${text}&fetchSize=${fetchSize}`,
            method: 'POST'
        });
        req.flush(expectedSuggestions);
        expect(req.request.body).toBe(criteria);
    });

    let gnpisService: GnpisService;
    let http: HttpTestingController;

    let gnpisService: GnpisService;
    let http: HttpTestingController;
    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule]
        });
        gnpisService = TestBed.get(GnpisService);
        http = TestBed.get(HttpTestingController);
    });
    afterAll(() => http.verify());

    const brapiSite: BrapiSite = {
        latitude: null,
        longitude: null,
        siteId: null,
        siteName: null,
        siteType: null
    };

    const brapiSibling: BrapiSibling = {
        germplasmDbId: 'frere1',
        defaultDisplayName: 'frere1'
    };

    const brapiDescriptor: BrapiDescriptor = {
        name: 'caracteristique1',
        pui: '12',
        value: '32'
    };

    const brapiGermplasmPedigree: GermplasmResult<BrapiGermplasmPedigree> = {
        result: {
            germplasmDbId: '12',
            defaultDisplayName: '12',
            pedigree: null,
            crossingPlan: null,
            crossingYear: null,
            familyCode: null,
            parent1DbId: '11',
            parent1Name: 'parent',
            parent1Type: 'SELF',
            parent2DbId: null,
            parent2Name: null,
            parent2Type: null,
            siblings: [brapiSibling]
        }
    };

    const brapiGermplasmProgeny: GermplasmResult<BrapiGermplasmProgeny> = {
        result: {
            germplasmDbId: '11',
            defaultDisplayName: '11',
            progeny: [brapiSibling]
        }

    };

    const brapiInstitute: BrapiInstitute = {
        instituteName: 'urgi',
        instituteCode: 'inra',
        acronym: 'urgi',
        organisation: 'inra',
        instituteType: 'labo',
        webSite: 'www.labo.fr',
        address: '12',
        logo: null
    };

    const brapiOrigin: BrapiOrigin = {
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
        germplasmPUI: '12',
        accessionNumber: '12',
        donorInstituteCode: 'urgi'
    };

    const brapiSet: BrapiSet = {
        germplasmCount: 12,
        germplasmRef: null,
        id: 12,
        name: 'truc',
        type: 'plan'
    };

    const brapiGermplasmAttributes: GermplasmResult<GermplasmData<BrapiGermplasmAttributes[]>> = {
        result: {
            data: [{
                attributeName: 'longueur',
                value: '30'
            }]
        }
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
        originSite: null,
        collectingSite: null,
        evaluationSites: null,
        collector: brapiOrigin,
        breeder: brapiOrigin,
        distributors: [brapiOrigin],
        panel: [brapiSet],
        collection: [brapiSet],
        population: [brapiSet]
    };

    const germplasmResultTest = {
        result: germplasmTest
    };

    it('should be created', () => {
        const service: GnpisService = TestBed.get(GnpisService);
        expect(service).toBeTruthy();
    });

    it('should fetch the germplasm', () => {
        let fetchedGermplasm: Germplasm;
    });

    it('should fetch sources', () => {
        service.sourceByURI$.subscribe(sourceByURI => {
            expect(sourceByURI).toEqual({
                'id1': source1,
                'id2': source2
            });
        });
    });

    it('should fetch the GNPIS Germplasm', () => {

        let fetchedGermplasm: GermplasmResult<GermplasmData<null>>;
        const germplasmDbId: string = germplasmTest.germplasmDbId;
        gnpisService.germplasm(germplasmDbId).subscribe(response => {
            fetchedGermplasm = response;
        });
        http.expectOne(`/gnpis/v1/germplasm?id=${germplasmDbId}`)
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

        service.search(criteria).subscribe(result => {
            expect(result.result.data.length).toBe(2);
            expect(result.result.data[0]['schema:includedInDataCatalog']).toEqual(source1);
            expect(result.result.data[1]['schema:includedInDataCatalog']).toEqual(source2);
        });

        const req = httpMock.expectOne({
            url: `${BASE_URL}/search`,
            method: 'POST'
        });
        req.flush(rawResult);

        expect(req.request.body).toBe(criteria);
    });
})
;
