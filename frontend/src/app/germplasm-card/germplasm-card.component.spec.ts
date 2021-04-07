import { async, TestBed } from '@angular/core/testing';
import { GermplasmCardComponent } from './germplasm-card.component';
import { ComponentTester, speculoosMatchers } from 'ngx-speculoos';
import { GnpisService } from '../gnpis.service';
import { BrapiService } from '../brapi.service';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { NgbPopoverModule } from '@ng-bootstrap/ng-bootstrap';
import { MomentModule } from 'ngx-moment';
import { LoadingSpinnerComponent } from '../loading-spinner/loading-spinner.component';
import { CardSectionComponent } from '../card-section/card-section.component';
import { CardRowComponent } from '../card-row/card-row.component';
import { CardTableComponent } from '../card-table/card-table.component';
import { MapComponent } from '../map/map.component';
import {
    BrapiCollectingSite,
    BrapiGermplasmAttributes,
    BrapiGermplasmMcpd,
    BrapiGermplasmPedigree,
    BrapiInstitute,
    BrapiResult,
    BrapiSibling
} from '../models/brapi.model';
import {
    Donor,
    Germplasm,
    GermplasmInstitute,
    GermplasmSet,
    Institute
} from '../models/gnpis.model';
import { DataDiscoverySource } from '../models/data-discovery.model';
import { MockComponent } from 'ng-mocks';
import { XrefsComponent } from '../xrefs/xrefs.component';
import { CardGenericDocumentComponent } from '../card-generic-document/card-generic-document.component';


describe('GermplasmCardComponent', () => {

    beforeEach(() => jasmine.addMatchers(speculoosMatchers));

    class GermplasmCardComponentTester extends ComponentTester<GermplasmCardComponent> {
        constructor() {
            super(GermplasmCardComponent);
        }

        get title() {
            return this.element('h3');
        }

        get cardHeader() {
            return this.elements('div.card-header');
        }
    }

    const brapiSibling: BrapiSibling = {
        germplasmDbId: 'frere1',
        defaultDisplayName: 'frere1'
    };


    const brapiGermplasmPedigree: BrapiResult<BrapiGermplasmPedigree> = {
        metadata: null,
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

    /*const brapiGermplasmProgeny: GermplasmResult<BrapiGermplasmProgeny> = {
        result: {
            germplasmDbId: '11',
            defaultDisplayName: '11',
            progeny: [brapiSibling]
        }
    };*/

    const gnpisInstitute: Institute = {
        instituteName: 'urgi',
        instituteCode: 'inra',
        acronym: 'urgi',
        organisation: 'inra',
        instituteType: 'labo',
        webSite: 'www.labo.fr',
        address: '12',
        logo: null
    };

    const brapiInstitute: BrapiInstitute = {
        instituteName: 'INRAE URGI',
        instituteCode: '78000',
        acronym: 'INRAE',
        organisation: 'inrae',
        instituteType: 'lab',
        webSite: 'www.labo.fr',
        instituteAddress: '18',
        logo: null
    };

    const gnpisGermplasmInstitute: GermplasmInstitute = {
        ...gnpisInstitute,
        institute: gnpisInstitute,
        accessionNumber: '12',
        accessionCreationDate: '1993',
        materialType: 'feuille',
        collectors: null,
        registrationYear: '1996',
        deregistrationYear: '1912',
        distributionStatus: null
    };

    const collectingSite: BrapiCollectingSite = {
        locationDbId: 'FR-78-INRAE',
        locationName: 'Versailles',
        coordinateUncertainty: null,
        elevation: null,
        georeferencingMethod: null,
        latitudeDecimal: null,
        latitudeDegrees: null,
        locationDescription: null,
        longitudeDecimal: null,
        longitudeDegrees: null,
        spatialReferenceSystem: null,
    };

    const gnpisDonor: Donor = {
        donorInstitute: gnpisInstitute,
        donorGermplasmPUI: '12',
        donorAccessionNumber: '12',
        donorInstituteCode: 'urgi',
        donationDate: null
    };

    const gnpisGermplasmSet: GermplasmSet = {
        germplasmCount: 12,
        germplasmRef: null,
        id: 12,
        name: 'truc',
        type: 'plan'
    };

    const brapiGermplasmAttributes: BrapiResult<BrapiGermplasmAttributes> = {
        metadata: null,
        result: {
            germplasmDbId: '12',
            data: [{
                attributeName: 'longueur',
                value: '30',
                attributeDbId: '1',
                attributeCode: 'longeur',
                determinedDate: 'today'
            }]
        }
    };

    const source: DataDiscoverySource = {
        '@id': 'src1',
        '@type': ['schema:DataCatalog'],
        'schema:name': 'source1',
        'schema:url': 'srcUrl',
        'schema:image': null
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
        donors: [gnpisDonor],
        acquisitionDate: null,
        genusSpecies: null,
        genusSpeciesSubtaxa: null,
        taxonSynonyms: ['pomme', 'api'],
        taxonCommonNames: ['pomme', 'api'],
        taxonComment: null,
        geneticNature: null,
        comment: null,
        photo: null,
        holdingInstitute: gnpisInstitute,
        holdingGenbank: gnpisInstitute,
        presenceStatus: null,
        children: null,
        originSite: null,
        collectingSite: null,
        evaluationSites: null,
        collector: gnpisGermplasmInstitute,
        breeder: gnpisGermplasmInstitute,
        distributors: [gnpisGermplasmInstitute],
        panel: [gnpisGermplasmSet],
        collection: [gnpisGermplasmSet],
        population: [gnpisGermplasmSet],
        'schema:includedInDataCatalog': source
    };

    const germplasmMcpdTest: BrapiGermplasmMcpd = {
        groupId: '0',
        accessionNames: ['test accession'],
        accessionNumber: '01',
        acquisitionDate: '2021',
        acquisitionSourceCode: 'FR-urgi',
        alternateIDs: ['Id1', 'Id2'],
        ancestralData: null,
        biologicalStatusOfAccessionCode: 'maintained',
        breedingInstitutes: brapiInstitute,
        collectingInfo: {
            collectingDate: '2021',
            collectingInstitutes: brapiInstitute,
            collectingMissionIdentifier: '007',
            collectingNumber: '3',
            collectors: 'urgi',
            materialType: 'germplasm',
            collectingSite: collectingSite,
        },
        commonCropName: 'wheat',
        countryOfOriginCode: 'FR',
        donorInfo: {
            donorAccessionNumber: 'ING007',
            donorInstitute: brapiInstitute,
            donationDate: '2021',
        },
        genus: 'Triti',
        germplasmDbId: 'Fr-007',
        germplasmPUI: 'urn/fr-007',
        instituteCode: 'FR-INRAE',
        mlsStatus: '0',
        remarks: null,
        safetyDuplicateInstitutes: null,
        species: 'Triti',
        speciesAuthority: null,
        storageTypeCodes: null,
        subtaxon: null,
        subtaxonAuthority: null,
        breederAccessionNumber: null,
        breedingCreationYear: null,
        catalogRegistrationYear: null,
        catalogDeregistrationYear: null,
        originLocationDbId: 'FR-Ver',
        originLocationName: 'Versailles',
        holdingInstitute: brapiInstitute,
        holdingGenbank: brapiInstitute,
        geneticNature: 'hybrid',
        presenceStatus: null,
        distributorInfos: null
    };

    const gnpisService = jasmine.createSpyObj(
        'GnpisService', [
            'getGermplasm',
            'getSource'
        ]
    );
    gnpisService.getGermplasm.and.returnValue(of(germplasmTest));
    gnpisService.getSource.and.returnValue(of(source));

    const brapiService = jasmine.createSpyObj(
        'BrapiService', [
            // 'germplasmProgeny',
            'germplasmPedigree',
            'germplasmAttributes',
            'germplasmMcpd'
        ]
    );
    // brapiService.germplasmProgeny.and.returnValue(of(brapiGermplasmProgeny));
    brapiService.germplasmPedigree.and.returnValue(of(brapiGermplasmPedigree));
    brapiService.germplasmAttributes.and.returnValue(of(brapiGermplasmAttributes));
    brapiService.germplasmMcpd.and.returnValue(of(germplasmMcpdTest));


    const activatedRouteParams = {
        queryParams: of({ id: 'test' }),
        snapshot: {
            queryParams: convertToParamMap({
                id: 'test'
            })
        }
    };


    beforeEach(async(() => {

        TestBed.configureTestingModule({
            imports: [RouterTestingModule, NgbPopoverModule, MomentModule],
            declarations: [
                GermplasmCardComponent, LoadingSpinnerComponent, MockComponent(XrefsComponent), CardSectionComponent,
                CardRowComponent, LoadingSpinnerComponent, CardTableComponent,
                MapComponent, MockComponent(CardGenericDocumentComponent)
            ],
            providers: [
                { provide: BrapiService, useValue: brapiService },
                { provide: GnpisService, useValue: gnpisService },
                { provide: ActivatedRoute, useValue: activatedRouteParams },
            ]
        });
    }));

    it('should fetch germplasm information', async(() => {
        const tester = new GermplasmCardComponentTester();
        const component = tester.componentInstance;
        tester.detectChanges();

        component.loaded.then(() => {
            expect(component.germplasmGnpis).toBeTruthy();
            tester.detectChanges();
            expect(tester.title).toContainText('Germplasm: test');
            expect(tester.cardHeader[0]).toContainText('Identification');
            expect(tester.cardHeader[1]).toContainText('Depositary');
            expect(tester.cardHeader[2]).toContainText('Collector');
            expect(tester.cardHeader[3]).toContainText('Breeder');
            expect(tester.cardHeader[4]).toContainText('Donor');
            expect(tester.cardHeader[5]).toContainText('Distributor');
            expect(tester.cardHeader[6]).toContainText('Evaluation Data');
        });
    }));

})
;

