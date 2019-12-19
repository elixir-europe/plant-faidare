import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GermplasmResultPageComponent } from './germplasm-result-page.component';
import { RouterTestingModule } from '@angular/router/testing';
import { LoadingSpinnerComponent } from '../loading-spinner/loading-spinner.component';
import { GnpisService } from '../gnpis.service';
import { BrapiGermplasm, GermplasmResults } from '../models/brapi.model';
import { CardSectionComponent } from '../card-section/card-section.component';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import {
    DataDiscoveryCriteria,
    DataDiscoveryCriteriaUtils,
    DataDiscoveryFacet,
    DEFAULT_PAGE_SIZE,
    MAX_RESULTS
} from '../models/data-discovery.model';
import { GermplasmSearchCriteria } from '../models/gnpis.model';
import { BehaviorSubject, of } from 'rxjs';


describe('GermplasmResultPageComponent', () => {
    let component: GermplasmResultPageComponent;
    let fixture: ComponentFixture<GermplasmResultPageComponent>;

    const pagination = {
        startResult: 1,
        endResult: DEFAULT_PAGE_SIZE,
        totalResult: 2,
        currentPage: 0,
        pageSize: DEFAULT_PAGE_SIZE,
        totalPages: 1,
        totalCount: 2,
        maxResults: MAX_RESULTS
    };

    const facets: DataDiscoveryFacet[] = [
        {
            field: 'holdingInstitute',
            terms: [
                {
                    term: 'INRA',
                    label: 'INRA',
                    count: 74
                },
                {
                    term: 'INRA-ONF',
                    label: 'INRA-ONF',
                    count: 8282
                },
                {
                    term: 'USDA',
                    label: 'USDA',
                    count: 3871
                },
                {
                    term: 'IPK',
                    label: 'IPK',
                    count: 2606
                },
                {
                    term: 'INRA, CNRS',
                    label: 'INRA, CNRS',
                    count: 2170
                }
            ]
        },
        {
            field: 'biologicalStatus',
            terms: [
                {
                    term: 'Traditional cultivar/landrace',
                    label: 'Traditional cultivar/landrace',
                    count: 74
                },
                {
                    term: 'Wild',
                    label: 'Wild',
                    count: 7095
                },
                {
                    term: 'Hybrid',
                    label: 'Hybrid',
                    count: 478
                }
            ]
        }
    ];

    const germplasmSearchResult: GermplasmResults<BrapiGermplasm> = {
        metadata: {
            pagination: pagination
        },
        facets: facets,
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

    const criteria: GermplasmSearchCriteria = {
        accessionNumbers: ['G_20'],
        germplasmDbIds: null,
        germplasmGenus: null,
        germplasmNames: ['germplam1'],
        germplasmPUIs: null,
        germplasmSpecies: null,

        synonyms: null,
        panel: null,
        collection: null,
        population: null,
        commonCropName: null,
        species: null,
        genusSpecies: null,
        subtaxa: null,
        genusSpeciesSubtaxa: null,
        taxonSynonyms: null,
        biologicalStatus: null,
        geneticNature: null,
        holdingInstitute: null,
        sources: null,

        facetFields: null,
        sortBy: 'germplasmName',
        sortOrder: null,
        page: 1,
        pageSize: 10,
    };

    const dataDiscoveryCriteria: DataDiscoveryCriteria = {
        accessions: ['G_10', 'incisa', 'G_20'],
        crops: ['cheery', 'prunus'],
        facetFields: null,
        germplasmLists: null,
        observationVariableIds: null,
        sources: ['URGI'],
        types: null,
        topSelectedTraitOntologyIds: null,

        page: 1,
        pageSize: 10
    };


    const gnpisService = jasmine.createSpyObj(
        'GnpisService', [
            'germplasmSearch'
        ]
    );
    gnpisService.germplasmSearch.and.returnValue(of(germplasmSearchResult));


    beforeEach(async(() => {
        TestBed.configureTestingModule({
            imports: [RouterTestingModule],
            declarations: [GermplasmResultPageComponent, CardSectionComponent, LoadingSpinnerComponent],
            providers: [
                { provide: GnpisService, useValue: gnpisService }
            ],
            schemas: [NO_ERRORS_SCHEMA],
        })
            .compileComponents();

        fixture = TestBed.createComponent(GermplasmResultPageComponent);
        component = fixture.componentInstance;
        component.criteriaFromForm$ = new BehaviorSubject<DataDiscoveryCriteria>(DataDiscoveryCriteriaUtils
            .emptyCriteria());
        component.germplasmSearchCriteria$ = new BehaviorSubject<GermplasmSearchCriteria>(DataDiscoveryCriteriaUtils
            .emptyGermplasmSearchCriteria());

        component.germplasmFacets$ = new BehaviorSubject<DataDiscoveryFacet[]>(facets);
        fixture.detectChanges();
    }));

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('should fecth the germplasm', () => {
        component.germplasmSearchCriteria$.next(criteria);
        expect(component.localCriteria).toEqual(criteria);
        expect(gnpisService.germplasmSearch).toHaveBeenCalledWith(criteria);
        expect(component.germplasm).toEqual(germplasmSearchResult.result.data);

    });

    it('should get criteria information from data discovery criteria', () => {
        component.criteriaFromForm$.next(dataDiscoveryCriteria);
        fixture.detectChanges();

        expect(component.localCriteria.commonCropName).toContain('cheery');
        expect(component.localCriteria.commonCropName).toContain('prunus');
        expect(component.localCriteria.commonCropName).toContain('cheery');
        expect(component.localCriteria.genusSpecies).toContain('prunus');
        expect(component.localCriteria.genusSpeciesSubtaxa).toContain('prunus');

        expect(component.localCriteria.germplasmNames).toContain('G_10');
        expect(component.localCriteria.accessionNumbers).toContain('incisa');
        expect(component.localCriteria.synonyms).toContain('G_20');
    });
});
