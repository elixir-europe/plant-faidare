import { async, TestBed } from '@angular/core/testing';
import { GermplasmCardComponent } from './germplasm-card.component';
import { ComponentTester, fakeRoute, speculoosMatchers } from 'ngx-speculoos';
import { GnpisService } from '../gnpis.service';
import { BrapiService } from '../brapi.service';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';
import {
    BrapiDescriptor,
    BrapiDonor,
    BrapiGermplasmAttributes,
    BrapiGermplasmPedigree,
    BrapiGermplasmProgeny,
    BrapiSet,
    BrapiSibling,
    BrapiSite
} from '../models/brapi.germplasm.model';
import { Germplasm, GermplasmData, GermplasmResult, Institute, Origin } from '../models/gnpis.germplasm.model';
import { NgbPopoverModule } from '@ng-bootstrap/ng-bootstrap';
import { MomentModule } from 'ngx-moment';

describe('GermplasmCardComponent', () => {



    beforeEach(() => jasmine.addMatchers(speculoosMatchers));

    class GermplasmCardComponentTester extends ComponentTester<GermplasmCardComponent> {
        constructor() {
            super(GermplasmCardComponent);
        }

        get title() {
            return this.element('h3');
        }

        get headerTitle() {
            return this.elements('.headerTitle');
        }
    }

    const brapiService = jasmine.createSpyObj(
        'BrapiService', [
            'germplasm',
            'germplasmProgeny',
            'germplasmPedigree',
            'germplasmAttributes'
        ]
    );

    const gnpisService = jasmine.createSpyObj(
        'GnpisService', [
            'germplasm',
            'germplasmByPuid'
        ]
    );

    const activatedRoute = fakeRoute({
        params: of({ id: 'test' })
    });

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

    const brapiOrigin: Origin = {
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

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            imports: [RouterTestingModule, NgbPopoverModule, MomentModule],
            declarations: [
                GermplasmCardComponent
            ],
            providers: [
                { provide: ActivatedRoute, useValue: activatedRoute },
                { provide: BrapiService, useValue: brapiService },
                { provide: GnpisService, useValue: gnpisService }
            ]
        });
    }));


    gnpisService.germplasm.and.returnValue(of(germplasmTest));
    gnpisService.germplasmByPuid.and.returnValue(of(germplasmTest));
    brapiService.germplasmProgeny.and.returnValue(of(brapiGermplasmProgeny));
    brapiService.germplasmPedigree.and.returnValue(of(brapiGermplasmPedigree));
    brapiService.germplasmAttributes.and.returnValue(of(brapiGermplasmAttributes));

    it('should fetch germplasm information', async(() => {
        const tester = new GermplasmCardComponentTester();
        const component = tester.componentInstance;
        tester.detectChanges();
        component.loaded.then(() => {
            expect(component.germplasmGnpis).toBeTruthy();
            tester.detectChanges();
            expect(tester.title).toContainText('Germplasm: test');
            expect(tester.headerTitle[0]).toContainText('Identification');
            expect(tester.headerTitle[1]).toContainText('Holding');
            expect(tester.headerTitle[2]).toContainText('Breeder');
            expect(tester.headerTitle[3]).toContainText('Collecting');
        });
    }));
});

