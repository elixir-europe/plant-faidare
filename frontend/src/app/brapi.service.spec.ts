import { BrapiService } from './brapi.service';
import {
    BrapiContacts, BrapiData, BrapiDonor,
    BrapiGermplasm, BrapiGermplasmAttributes, BrapiGermplasmPedigree, BrapiGermplasmProgeny,
    BrapiLocation,
    BrapiObservationVariable,
    BrapiResult, BrapiResults, BrapiSibling,
    BrapiStudy, BrapiTaxonIds,
    BrapiTrial
} from './models/brapi.model';
import { DataDiscoverySource } from './models/data-discovery.model';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import {Germplasm, GermplasmInstitute, GermplasmSet, Institute, Site} from "./models/gnpis.model";
import Result = jasmine.Result;

describe('BrapiService', () => {

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
                defaultDisplayName: null,
                germplasmPUI: null,
                pedigree: null,
                seedSource: null,
                synonyms: null,
                commonCropName: null,
                instituteCode: null,
                instituteName: null,
                biologicalStatusOfAccessionCode: null,
                countryOfOriginCode: null,
                typeOfGermplasmStorageCode: null,
                taxonIds: null,
                germplasmName: 'germplam1',
                genus: 'Populus',
                species: 'x generosa',
                subtaxa: '',
                speciesAuthority: null,
                subtaxaAuthority: null,
                donors: null,
                acquisitionDate: null
            }, {
                germplasmDbId: 'g2',
                accessionNumber: 'G_20',
                germplasmName: 'germplam2',
                genus: 'Triticum',
                species: 'aestivum',
                subtaxa: 'subsp',
                defaultDisplayName: null,
                germplasmPUI: null,
                pedigree: null,
                seedSource: null,
                synonyms: null,
                commonCropName: null,
                instituteCode: null,
                instituteName: null,
                biologicalStatusOfAccessionCode: null,
                countryOfOriginCode: null,
                typeOfGermplasmStorageCode: null,
                taxonIds: null,
                speciesAuthority: null,
                subtaxaAuthority: null,
                donors: null,
                acquisitionDate: null
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

    const brapiSite: Site = {
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

    const brapiGermplasmPedigree: BrapiResult<BrapiGermplasmPedigree> = {
        result: {
            germplasmDbId: 'test',
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

    const brapiGermplasmProgeny: BrapiResult<BrapiGermplasmProgeny> = {
        result: {
            germplasmDbId: 'test',
            defaultDisplayName: '11',
            progeny: [brapiSibling]
        }

    };

    const institute: Institute = {
        instituteName: 'urgi',
        instituteCode: 'inra',
        acronym: 'urgi',
        organisation: 'inra',
        instituteType: 'labo',
        webSite: 'www.labo.fr',
        address: '12',
        logo: null
    };
    const origin: GermplasmInstitute = { ... institute,
        institute: institute,
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
        donorInstitute: institute,
        donorGermplasmPUI: '12',
        donorAccessionNumber: '12',
        donorInstituteCode: 'urgi',
        donationDate: null
    };

    const germplasmSet: GermplasmSet = {
        germplasmCount: 12,
        germplasmRef: null,
        id: 12,
        name: 'truc',
        type: 'plan'
    };

    const brapiGermplasmAttributes: BrapiResult<BrapiData<BrapiGermplasmAttributes[]>> = {
        result: {
            data: [{
                attributeName: 'longueur',
                value: '30'
            }]
        }
    };

    const germplasmTest: Germplasm = {
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
        holdingInstitute: institute,
        holdingGenbank: institute,
        presenceStatus: null,
        children: null,
        originSite: null,
        collectingSite: null,
        evaluationSites: null,
        collector: origin,
        breeder: origin,
        distributors: [origin],
        panel: [germplasmSet],
        collection: [germplasmSet],
        population: [germplasmSet]
    };

    const germplasmResultTest = {
        result: germplasmTest
    };

    let brapiService: BrapiService;
    let http: HttpTestingController;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule]
        });
        brapiService = TestBed.get(BrapiService);
        http = TestBed.get(HttpTestingController);
    });

    afterEach(() => http.verify());

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

    it('should fetch the germplasm of studies call', () => {

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

    it('should fetch the pedigree', () => {

        let fetchedGermplasmPedigree: BrapiResult<BrapiGermplasmPedigree>;
        const germplasmDbId: string = brapiGermplasmPedigree.result.germplasmDbId;
        brapiService.germplasmPedigree(germplasmDbId).subscribe(response => {
            fetchedGermplasmPedigree = response;
        });
        http.expectOne(`brapi/v1/germplasm/${germplasmDbId}/pedigree`)
            .flush(brapiGermplasmPedigree);

        expect(fetchedGermplasmPedigree).toEqual(brapiGermplasmPedigree);

    });

    /*it('should fetch the germplasm progeny', () => {

        let fetchedGermplasmProgeny: GermplasmResult<BrapiGermplasmProgeny>;
        const germplasmDbId: string = brapiGermplasmProgeny.result.germplasmDbId;
        brapiService.germplasmProgeny(germplasmDbId).subscribe(response => {
            fetchedGermplasmProgeny = response;
        });
        http.expectOne(`brapi/v1/germplasm/${germplasmDbId}/progeny`)
            .flush(brapiGermplasmProgeny);

        expect(fetchedGermplasmProgeny).toEqual(brapiGermplasmProgeny);

    });*/

    it('should fetch the germplasm attributes', () => {

        let fetchedGermplasmAttributes: BrapiResult<BrapiData<BrapiGermplasmAttributes[]>>;
        const germplasmDbId: string = germplasmTest.germplasmDbId;
        brapiService.germplasmAttributes(germplasmDbId).subscribe(response => {
            fetchedGermplasmAttributes = response;
        });
        http.expectOne(`brapi/v1/germplasm/${germplasmDbId}/attributes`)
            .flush(brapiGermplasmAttributes);

        expect(fetchedGermplasmAttributes).toEqual(brapiGermplasmAttributes);

    });

});
