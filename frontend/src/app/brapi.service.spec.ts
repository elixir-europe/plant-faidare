import { TestBed } from '@angular/core/testing';
import { BrapiService } from './brapi.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import {
    BrapiDescriptor,
    BrapiDonor,
    BrapiGermplasmAttributes,
    BrapiGermplasmAttributes,
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
import {
    BrapiDescriptor, BrapiDonor,
    BrapiGermplasmPedigree,
    BrapiGermplasmProgeny,
    BrapiInstitute,
    BrapiOrigin,
    BrapiSet,
    BrapiSibling,
    BrapiSite
} from './models/brapi.germplasm.model';
import { Germplasm, GermplasmData, GermplasmResult } from './models/gnpis.germplasm.model';

describe('BrapiService', () => {
    let brapiService: BrapiService;
    let http: HttpTestingController;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule]
        });
        brapiService = TestBed.get(BrapiService);
        http = TestBed.get(HttpTestingController);
    });

    afterAll(() => http.verify());

    const location: BrapiLocation = {
        locationDbId: '1',
        locationName: 'loc1',
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
                { studyDbId: 's1', studyName: 'study1' },
                { studyDbId: 's2', studyName: 'study2' }
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
        const mockResponse: BrapiResult<BrapiLocation> = {
            metadata: null,
            result: location
        };
        let actualLocation: BrapiLocation;
        const locationId = mockResponse.result.locationDbId;
        brapiService.location(mockResponse.result.locationDbId).subscribe(response => actualLocation = response.result);

        http.expectOne(`brapi/v1/locations/${locationId}`)
            .flush(mockResponse);

        expect(actualLocation).toEqual(location);
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
        result : {
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
            data: [ {
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
    const germplasmResultTest = {
        result: germplasmTest
    };
    it('should fetch the pedigree', () => {

        let fetchedGermplasmPedigree: GermplasmResult<BrapiGermplasmPedigree>;
        const germplasmDbId: string = brapiGermplasmPedigree.germplasmDbId;
        brapiService.germplasmPedigree(germplasmDbId).subscribe(response => {
            fetchedGermplasmPedigree = response;
        });

        http.expectOne(`/gnpis/v1/germplasm/pedigree/${germplasmDbId}`)
            .flush(germplasmTest);

        expect(fetchedGermplasmPedigree).toEqual(brapiGermplasmPedigree);

    });

    it('should fetch the progeny', () => {

        let fetchedGermplasmProgeny: GermplasmResult<BrapiGermplasmProgeny>;
        const germplasmDbId: string = brapiGermplasmProgeny.germplasmDbId;
        brapiService.germplasmProgeny(germplasmDbId).subscribe(response => {
            fetchedGermplasmProgeny = response;
        });
        http.expectOne(`/brapi/v1/germplasm/${germplasmDbId}/progeny`)
            .flush(brapiGermplasmProgeny);

        expect(fetchedGermplasmProgeny).toEqual(brapiGermplasmProgeny);

    });

    it('should fetch the germplasm', () => {
        let fetchedGermplasmPedigree: GermplasmResult<BrapiGermplasmPedigree>;
        const germplasmDbId: string = germplasmTest.germplasmDbId;
        brapiService.germplasmPedigree(germplasmDbId).subscribe(response => {
            fetchedGermplasmPedigree = response;
        });
        http.expectOne(`/brapi/v1/germplasm/${germplasmDbId}/pedigree`)
            .flush(brapiGermplasmPedigree);

        expect(fetchedGermplasmPedigree).toEqual(brapiGermplasmPedigree);

    });

    it('should fetch the germplasm attributes', () => {
        let fetchedGermplasmAttributes: GermplasmResult<GermplasmData<BrapiGermplasmAttributes[]>>;
        const germplasmDbId: string = germplasmTest.germplasmDbId;
        brapiService.germplasmAttributes(germplasmDbId).subscribe(response => {
            fetchedGermplasmAttributes = response;
        });
        http.expectOne(`/brapi/v1/germplasm/${germplasmDbId}/attributes`)
            .flush(brapiGermplasmAttributes);

        expect(fetchedGermplasmAttributes).toEqual(brapiGermplasmAttributes);

    });

});
