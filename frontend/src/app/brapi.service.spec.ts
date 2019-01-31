import { TestBed } from '@angular/core/testing';

import { BrapiService } from './brapi.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import {
    BrapiContacts,
    BrapiGermplasm,
    BrapiLocation,
    BrapiObservationVariable,
    BrapiResult,
    BrapiResults,
    BrapiStudy,
    BrapiTrial
} from './models/brapi.model';
import { DataDiscoverySource } from './models/data-discovery.model';

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

    const location: BrapiLocation = {
        locationDbId: 1,
        name: 'loc1',
        locationType: 'Collecting site',
        abbreviation: null,
        countryCode: 'Fr',
        countryName: 'France',
        institutionAddress: null,
        institutionName: 'Insti',
        altitude: null,
        latitude: null,
        longitude: null,
    };

    const contacts: BrapiContacts = {
        contactDbId: 'c1',
        name: 'contact1',
        email: 'contact1@email.com',
        type: 'contact',
        institutionName: 'Inst',
    };

    const searchStudy: BrapiResult<BrapiStudy> = {
        metadata: null,
        result: {
            studyDbId: 's1',
            studyType: 'phenotype',
            studyName: 'study1',
            studyDescription: null,
            seasons: ['winter', '2019'],
            startDate: '2018',
            endDate: null,
            active: true,
            programDbId: 'p1',
            programName: 'program1',
            trialDbIds: ['10', '20'],
            location: location,
            contacts: [contacts],
            additionalInfo: null,
            dataLinks: []
        }
    };

    const trial1: BrapiResult<BrapiTrial> = {
        metadata: null,
        result: {
            trialDbId: '10',
            trialName: 'trial_10',
            trialType: 'project',
            active: true,
            studies: [
                { studyDbId: 's1' },
                { studyDbId: 's2' }
            ]

        }
    };

    const osbVariable: BrapiResults<BrapiObservationVariable> = {
        metadata: null,
        result: {
            data: [{
                observationVariableDbId: 'var1',
                contextOfUse: null,
                institution: 'Insti',
                crop: 'WoodyPlant',
                name: 'varaiable1',
                ontologyDbId: 'WPO',
                ontologyName: 'Woody Plant Ontology',
                synonyms: ['First synonym'],
                language: 'EN',
                trait: {
                    traitDbId: 't1',
                    name: 'trait1',
                    description: null,
                },
                documentationURL: null,
            }]
        }
    };

    const germplasm: BrapiResults<BrapiGermplasm> = {
        metadata: null,
        result: {
            data: [{
                germplasmDbId: 'g1',
                accessionNumber: 'G_10',
                germplasmName: 'germplam1',
                genus: 'Populus',
                species: 'x generosa',
                subtaxa: ''
            }, {
                germplasmDbId: 'g2',
                accessionNumber: 'G_20',
                germplasmName: 'germplam2',
                genus: 'Triticum',
                species: 'aestivum',
                subtaxa: 'subsp'
            }],
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

    it('should fetch the study', () => {
        let fetchedStudy: BrapiResult<BrapiStudy>;
        const studyDbId: string = searchStudy.result.studyDbId;
        brapiService.study(searchStudy.result.studyDbId).subscribe(response => {
            fetchedStudy = response;
        });
        http.expectOne(`brapi/v1/studies/${studyDbId}`)
            .flush(searchStudy);

        expect(fetchedStudy).toEqual(searchStudy);

    });

    it('should fetch the germplasm', () => {

        let fetchedGermplasm: BrapiResults<BrapiGermplasm>;
        const studyDbId: string = searchStudy.result.studyDbId;
        brapiService.studyGermplasms(searchStudy.result.studyDbId).subscribe(response => {
            fetchedGermplasm = response;
        });
        http.expectOne(`brapi/v1/studies/${studyDbId}/germplasm`)
            .flush(germplasm);

        expect(fetchedGermplasm).toEqual(germplasm);

    });

    it('should fetch the variables', () => {

        let fetchedVariables: BrapiResults<BrapiObservationVariable>;
        const studyDbId: string = searchStudy.result.studyDbId;
        brapiService.studyObservationVariables(searchStudy.result.studyDbId).subscribe(response => {
            fetchedVariables = response;
        });
        http.expectOne(`brapi/v1/studies/${studyDbId}/observationVariables`)
            .flush(osbVariable);

        expect(fetchedVariables).toEqual(osbVariable);

    });

    it('should fetch the trials', () => {

        let fetchedTrials: BrapiResult<BrapiTrial>;
        const trialDbId: string = trial1.result.trialDbId;
        brapiService.studyTrials(trialDbId).subscribe(response => {
            fetchedTrials = response;
        });
        http.expectOne(`brapi/v1/trials/${trialDbId}`)
            .flush(trial1);

        expect(fetchedTrials).toEqual(trial1);

    });

    it('should fetch 1 location', () => {
        const expectedLocation = {
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
            additionalInfo: {}
        };
        const mockResponse: BrapiResult<BrapiLocation> = {
            metadata: null,
            result: expectedLocation
        };
        let actualLocation: BrapiLocation;
        const locationId = mockResponse.result.locationDbId;
        brapiService.location(mockResponse.result.locationDbId).subscribe(response => actualLocation = response.result);

        http.expectOne(`brapi/v1/locations/${locationId}`)
            .flush(mockResponse);

        expect(actualLocation).toEqual(expectedLocation);
    });

});
