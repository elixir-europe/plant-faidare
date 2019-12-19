import { LargeFacetsComponent } from './large-facets.component';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import {
    DataDiscoveryCriteria, DataDiscoveryCriteriaUtils,
    DataDiscoveryFacet
} from '../../models/data-discovery.model';
import { NgbTypeaheadModule } from '@ng-bootstrap/ng-bootstrap';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { BehaviorSubject } from 'rxjs';
import { GermplasmSearchCriteria } from '../../models/gnpis.model';
import { ComponentTester } from 'ngx-speculoos';


describe('LargeFacetsComponent', () => {
    let component: LargeFacetsComponent;
    let fixture: ComponentFixture<LargeFacetsComponent>;

    class LargeFacetsComponentTester extends ComponentTester<LargeFacetsComponent> {
        constructor() {
            super(LargeFacetsComponent);
        }

        get  facetTitle() {
            return this.element('h3')
        }

        get facetInput() {
            return this.element('input')
        }
    }

    const largeFacet: DataDiscoveryFacet = {
        field: 'large Facet',
        terms: [
            {
                term: 'source 1',
                label: 'SOURCE 1',
                count: 10
            }, {
                term: 'source 2',
                label: 'SOURCE 2',
                count: 20
            }, {
                term: 'Germplasm',
                label: 'GERMPLASM',
                count: 10
            },{
                term: 'Traditional cultivar/landrace',
                label: 'Traditional cultivar/landrace',
                count: 74
            }, {
                term: 'Wild',
                label: 'Wild',
                count: 7095
            }, {
                term: 'Hybrid',
                label: 'Hybrid',
                count: 478
            },{
                term: 'INRA',
                label: 'INRA',
                count: 74
            }, {
                term: 'INRA-ONF',
                label: 'INRA-ONF',
                count: 8282
            }, {
                term: 'USDA',
                label: 'USDA',
                count: 3871
            }, {
                term: 'IPK',
                label: 'IPK',
                count: 2606
            }, {
                term: 'INRA, CNRS',
                label: 'INRA, CNRS',
                count: 2170
            }
        ]
    };

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            imports: [NgbTypeaheadModule, ReactiveFormsModule, HttpClientTestingModule ],
            declarations: [LargeFacetsComponent]
        })
            .compileComponents();

        fixture = TestBed.createComponent(LargeFacetsComponent);
        component = fixture.componentInstance;

        component.criteria$ = new BehaviorSubject<DataDiscoveryCriteria>(DataDiscoveryCriteriaUtils.emptyCriteria());
        component.germplasmSearchCriteria$ = new BehaviorSubject<GermplasmSearchCriteria>(DataDiscoveryCriteriaUtils
            .emptyGermplasmSearchCriteria());
        component.facet = largeFacet;
        fixture.detectChanges();
    }));

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it ('should display search box', () => {
        const tester = new LargeFacetsComponentTester();
        const component = tester.componentInstance;
        component.facet = largeFacet;
        tester.detectChanges();

        expect(tester.facetTitle.textContent).toEqual('Large Facet');
        expect(tester.facetInput).toBeTruthy();

    });
});
