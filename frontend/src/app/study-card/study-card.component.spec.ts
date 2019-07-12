import { async, TestBed } from '@angular/core/testing';

import { StudyCardComponent } from './study-card.component';
import { ComponentTester, fakeRoute, speculoosMatchers } from 'ngx-speculoos';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import {
    BrapiContacts,
    BrapiGermplasm,
    BrapiLocation,
    BrapiObservationVariable,
    BrapiResult,
    BrapiResults,
    BrapiStudy,
    BrapiTrial
} from '../models/brapi.model';
import { BrapiService } from '../brapi.service';
import { DataDiscoverySource } from '../models/data-discovery.model';
import { MapComponent } from '../map/map.component';
import { RouterTestingModule } from '@angular/router/testing';
import { CardSectionComponent } from '../card-section/card-section.component';
import { CardRowComponent } from '../card-row/card-row.component';
import { LoadingSpinnerComponent } from '../loading-spinner/loading-spinner.component';
import { CardTableComponent } from '../card-table/card-table.component';
import { MockComponent } from 'ng-mocks';
import { XrefsComponent } from '../xrefs/xrefs.component';
import { CardGenericDocumentComponent } from '../card-generic-document/card-generic-document.component';


describe('StudyCardComponent', () => {
    beforeEach(() => jasmine.addMatchers(speculoosMatchers));

    class StudyCardComponentTester extends ComponentTester<StudyCardComponent> {
        constructor() {
            super(StudyCardComponent);
        }

        get title() {
            return this.element('h3');
        }

        get cardHeader() {
            return this.elements('div.card-header');
        }

        get studyInfo() {
            return this.elements('div.col');
        }

        get map() {
            return this.element('div #map');
        }
    }

    const activatedRoute = fakeRoute({
        params: of({ id: 's1' })
    });

    const source: DataDiscoverySource = {
        '@id': 'src1',
        '@type': ['schema:DataCatalog'],
        'schema:name': 'source1',
        'schema:url': 'srcUrl',
        'schema:image': null
    };

    const location: BrapiResult<BrapiLocation> = {
        metadata: null,
        result: {
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
        }
    };

    const location2: BrapiResult<BrapiLocation> = {
        metadata: null,
        result: {
            locationDbId: '2',
            locationName: 'loc2',
            locationType: 'Collecting site',
            abbreviation: null,
            countryCode: 'Fr',
            countryName: 'France',
            institutionAddress: null,
            institutionName: 'Insti',
            altitude: null,
            latitude: 48.8534,
            longitude: 2.3488,
        }
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
            location: location.result,
            locationDbId: '1',
            locationName: 'loc1',
            contacts: [contacts],
            additionalInfo: null,
            documentationURL: 'http://example.com/Study/s1',
            dataLinks: [],
            'schema:includedInDataCatalog': source
        } as BrapiStudy
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
    const trial2: BrapiResult<BrapiTrial> = {
        metadata: null,
        result: {
            trialDbId: '20',
            trialName: 'trial_20',
            trialType: 'project',
            active: true,
            studies: [
                { studyDbId: 's3', studyName: 'study3' },
                { studyDbId: 's4', studyName: 'study4' }
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
                defaultDisplayName: 'germplam1',
                accessionNumber: 'G_10',
                germplasmName: 'germplam1',
                germplasmPUI: 'urn_g1',
                pedigree: null,
                seedSource: 'Versaille Institute',
                synonyms: null,
                commonCropName: 'cheery',
                instituteCode: '78',
                instituteName: 'Versaille Institute',
                biologicalStatusOfAccessionCode: null,
                countryOfOriginCode: 'FR',
                typeOfGermplasmStorageCode: null,
                taxonIds: null,
                genus: 'Populus',
                species: 'x generosa',
                speciesAuthority: 'Pop',
                subtaxa: 'subsp',
                subtaxaAuthority: '',
                donors: null,
                acquisitionDate: 'yesterday'
            }, {
                germplasmDbId: 'g2',
                defaultDisplayName: 'germplam2',
                accessionNumber: 'G_20',
                germplasmName: 'germplam2',
                germplasmPUI: 'urn_g2',
                pedigree: null,
                seedSource: 'Versaille Institute',
                synonyms: null,
                commonCropName: 'cheery',
                instituteCode: '78',
                instituteName: 'Versaille Institute',
                biologicalStatusOfAccessionCode: null,
                countryOfOriginCode: 'FR',
                typeOfGermplasmStorageCode: null,
                taxonIds: null,
                genus: 'Triticum',
                species: 'aestivum',
                speciesAuthority: 'Trit',
                subtaxa: 'subsp',
                subtaxaAuthority: '',
                donors: null,
                acquisitionDate: 'today'
            }],
        }
    };

    const brapiService = jasmine.createSpyObj(
        'BrapiService', [
            'study',
            'studyTrials',
            'studyObservationVariables',
            'studyGermplasms',
            'location'
        ]
    );
    brapiService.study.and.returnValue(of(searchStudy));
    brapiService.studyTrials.withArgs('10').and.returnValue(of(trial1));
    brapiService.studyTrials.withArgs('20').and.returnValue(of(trial2));
    brapiService.studyObservationVariables.and.returnValue(of(osbVariable));
    brapiService.studyGermplasms.and.returnValue(of(germplasm));
    brapiService.location.withArgs('1').and.returnValue(of(location));
    brapiService.location.withArgs('2').and.returnValue(of(location2));

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            imports: [RouterTestingModule],
            declarations: [
                StudyCardComponent, MapComponent, CardSectionComponent,
                CardRowComponent, LoadingSpinnerComponent, CardTableComponent,
                MockComponent(CardGenericDocumentComponent), MockComponent(XrefsComponent)
            ],
            providers: [
                { provide: ActivatedRoute, useValue: activatedRoute },
                { provide: BrapiService, useValue: brapiService }
            ]
        });
    }));


    it('should fetch the study data information but not display map', async(() => {
        searchStudy.result.locationDbId = '1';
        const tester = new StudyCardComponentTester();
        const component = tester.componentInstance;
        tester.detectChanges();

        component.loaded.then(() => {
            expect(component.study).toBeTruthy();
            expect(component.location.locationDbId).toEqual('1');

            tester.detectChanges();

            expect(tester.map).toBeFalsy();
            expect(tester.title).toContainText('Study phenotype: study1');

            expect(tester.cardHeader[0]).toContainText('Identification');

            expect(tester.studyInfo[0]).toContainText('study1');
            expect(tester.studyInfo[1]).toContainText('s1');

            expect(tester.cardHeader[1]).toContainText('Genotype');
            expect(component.studyGermplasms.length).toEqual(2);

            expect(tester.cardHeader[2]).toContainText('Variable');
            expect(component.studyObservationVariables.length).toEqual(1);

            expect(tester.cardHeader[3]).toContainText(' Data Set ');
            expect(component.trialsIds.length).toEqual(2);
            expect(component.studyDataset.length).toEqual(2);

            expect(tester.cardHeader[4]).toContainText('Contact');
        });
    }));

    it('should display map', async(() => {
        searchStudy.result.locationDbId = '2';
        const tester = new StudyCardComponentTester();
        const component = tester.componentInstance;
        tester.detectChanges();

        component.loaded.then(() => {
            expect(component.study).toBeTruthy();
            expect(component.location.locationDbId).toEqual('2');

            tester.detectChanges();

            expect(tester.map).toBeTruthy();
        });
    }));
});
