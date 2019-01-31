import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GermplasmCardComponent } from './germplasm-card.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentTester, fakeRoute, speculoosMatchers } from 'ngx-speculoos';
import { GnpisService } from '../gnpis.service';
import { HomeComponent } from '../home/home.component';
import { BrapiService } from '../brapi.service';
import { ActivatedRoute, ActivatedRouteSnapshot, convertToParamMap, Params, RouterModule } from '@angular/router';
import { StudyCardComponent } from '../study-card/study-card.component';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';
import {
    BrapiDescriptor,
    BrapiDonor, BrapiGermplasmAttributes, BrapiGermplasmPedigree, BrapiGermplasmProgeny,
    BrapiInstitute,
    BrapiOrigin,
    BrapiSet,
    BrapiSibling,
    BrapiSite
} from '../models/brapi.germplasm.model';
import { GermplasmData, GermplasmRef, GermplasmResult } from '../models/gnpis.germplasm.model';
import { showWarningOnce } from 'tslint/lib/error';

describe('GermplasmCardComponent', () => {
    let component: GermplasmCardComponent;
    let fixture: ComponentFixture<GermplasmCardComponent>;


    beforeEach(() => jasmine.addMatchers(speculoosMatchers));

    class GermplasmCardComponentTester extends ComponentTester<GermplasmCardComponent> {
        constructor() {
            super(GermplasmCardComponent);
        }

        get title() {
            return this.element('h3');
        }

        get germplasmFields() {
            return this.elements('td.field');
        }

        get germplasmFieldsName() {
            return this.elements('th.fieldName');
        }

        get headerTitle() {
            return this.elements('th.headerTitle, h4.headerTitle');
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
            'germplasm'
        ]
    );

    const params = {
        source: 'source1'
    } as Params;


    const activatedRoute = fakeRoute({
        queryParams: of(params),
        snapshot: {
            queryParams: params,
            paramMap: convertToParamMap({ id: 's1' })
        } as ActivatedRouteSnapshot
    });

    const brapiSite: BrapiSite = {
        latitude: null,
        longitude: null,
        siteId: null,
        siteName: null,
        siteType:  null
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

    const brapiGermplasmPedigree: BrapiGermplasmPedigree = {
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
    };

    const brapiGermplasmProgeny: BrapiGermplasmProgeny = {
        germplasmDbId: '11',
        defaultDisplayName: '11',
        progeny: [brapiSibling]
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

    const germplasmTest: GermplasmData<GermplasmData<null>> = {
        data: null,
        url:  'www.cirad.fr',
        source: 'cirad',
        germplasmDbId:  'test',
        defaultDisplayName:  'test',
        accessionNumber:  'test',
        germplasmName:  'test',
        germplasmPUI:  'doi:1256',
        pedigree:  'tree',
        seedSource:  'inra',
        synonyms: null,
        commonCropName:  null,
        instituteCode:  'grc12',
        instituteName:  'institut',
        biologicalStatusOfAccessionCode:  null,
        countryOfOriginCode:  null,
        typeOfGermplasmStorageCode:  null,
        taxonIds:  null,
        genus:  'genre',
        species:  'esp',
        speciesAuthority:  'L',
        subtaxa:  null,
        subtaxaAuthority:  null,
        donors: [brapiDonor],
        acquisitionDate:  null,
        genusSpecies:  null,
        genusSpeciesSubtaxa:  null,
        taxonSynonyms: ['pomme', 'api'],
        taxonCommonNames: ['pomme', 'api'],
        geneticNature:  null,
        comment:  null,
        photo:  null,
        holdingInstitute: brapiInstitute,
        holdingGenbank: brapiInstitute,
        presenceStatus:  null,
        children:  null,
        descriptors: [brapiDescriptor],
        originSite:  null,
        collectingSite:  null,
        evaluationSites:  null,
        collector: brapiOrigin,
        breeder: brapiOrigin,
        distributors: [brapiOrigin],
        panel: [brapiSet],
        collection: [brapiSet],
        population: [brapiSet]
    };

    /*beforeEach(() => {
        fixture = TestBed.createComponent(GermplasmCardComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        // const service: BrapiService = TestBed.get(BrapiService) as BrapiService;
        // const component = new GermplasmCardComponent(service, null);
        expect(component).toBeTruthy();
    });*/

    gnpisService.germplasm.and.returnValue(of(germplasmTest));
    brapiService.germplasm.and.returnValue(of(germplasmTest));
    brapiService.germplasmProgeny.and.returnValue(of(germplasmTest));
    brapiService.germplasmPedigree.and.returnValue(of(germplasmTest));

  /*  it('should fetch germplasm information', async(() => {
        const tester = new GermplasmCardComponentTester();
        const comp = tester.componentInstance;
        tester.detectChanges();

        comp.loaded.then(() => {
            expect(comp.germplasm).toBeTruthy();
            expect(tester.title).toContainText('Germplasm: test');
            expect(tester.headerTitle[0]).toContainText('Identification');
            expect(tester.headerTitle[1]).toContainText('Holding');
            expect(tester.headerTitle[2]).toContainText('Collecting site');
            expect(tester.headerTitle[3]).toContainText('Breeder');
            expect(tester.headerTitle[4]).toContainText('Collecting');
            expect(tester.headerTitle[5]).toContainText('Donation');
            expect(tester.headerTitle[6]).toContainText('Distribution');
            expect(tester.headerTitle[8]).toContainText('Ascendants');
            expect(tester.headerTitle[9]).toContainText('Siblings');
            expect(tester.headerTitle[10]).toContainText('Collector');
            expect(tester.headerTitle[11]).toContainText('Evaluation Data');
            expect(tester.headerTitle[12]).toContainText('Collection');
            expect(tester.headerTitle[13]).toContainText('Panel');
        });
    }));*/
});

