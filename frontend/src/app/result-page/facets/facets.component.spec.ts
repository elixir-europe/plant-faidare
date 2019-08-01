import { TestBed } from '@angular/core/testing';

import { FacetsComponent } from './facets.component';
import { ReactiveFormsModule } from '@angular/forms';
import { ComponentTester, speculoosMatchers } from 'ngx-speculoos';
import { DataDiscoveryCriteria, DataDiscoveryCriteriaUtils, DataDiscoveryFacet } from '../../models/data-discovery.model';
import { BehaviorSubject } from 'rxjs';
import { take } from 'rxjs/operators';
import { NO_ERRORS_SCHEMA } from '@angular/core';

describe('FacetsComponent', () => {

    class FacetsComponentTester extends ComponentTester<FacetsComponent> {
        constructor() {
            super(FacetsComponent);
        }

        get title() {
            return this.element('h3.card-title');
        }

        get terms() {
            return this.elements('label');
        }

        get checkboxes() {
            return this.elements('input');
        }

        get advanceSearchButton() {
            return this.elements('button');
        }

    }

    const exampleFacet1: DataDiscoveryFacet = {
        field: 'sources',
        terms: [
            {
                term: 'source 1',
                label: 'SOURCE 1',
                count: 10
            }, {
                term: 'source 2',
                label: 'SOURCE 2',
                count: 20
            }
        ]
    };

    const exampleFacet2: DataDiscoveryFacet = {
        field: 'types',
        terms: [
            {
                term: 'Germplasm',
                label: 'GERMPLASM',
                count: 10
            }
        ]
    };

    beforeEach(() => TestBed.configureTestingModule({
        imports: [ReactiveFormsModule],
        declarations: [FacetsComponent],
        schemas: [NO_ERRORS_SCHEMA]
    }));

    beforeEach(() => jasmine.addMatchers(speculoosMatchers));

    it('should display facet', () => {
        const tester = new FacetsComponentTester();

        const component = tester.componentInstance;
        component.facet = exampleFacet1;
        component.criteria$ = new BehaviorSubject<DataDiscoveryCriteria>(DataDiscoveryCriteriaUtils.emptyCriteria());
        tester.detectChanges();

        expect(tester.title).toContainText('Sources');

        expect(tester.terms[0]).toContainText('SOURCE 1 (10)');
        expect(tester.terms[0].attr('for')).toBe('source 1');
        expect(tester.checkboxes[0].attr('id')).toBe('source 1');

        expect(tester.terms[1]).toContainText('SOURCE 2 (20)');
        expect(tester.terms[1].attr('for')).toBe('source 2');
        expect(tester.checkboxes[1].attr('id')).toBe('source 2');
    });

    it('should initialize with criteria', () => {
        const tester = new FacetsComponentTester();

        const component = tester.componentInstance;
        component.facet = exampleFacet1;

        const criteria = {
            ...DataDiscoveryCriteriaUtils.emptyCriteria(),
            sources: ['source 2']
        };
        component.criteria$ = new BehaviorSubject<DataDiscoveryCriteria>(criteria);

        tester.detectChanges();


        expect(tester.checkboxes[0].nativeElement['checked']).toBe(false);
        expect(tester.checkboxes[1].nativeElement['checked']).toBe(true);
    });

    it('should change criteria on checkbox selection change', () => {
        const tester = new FacetsComponentTester();

        const component = tester.componentInstance;
        component.facet = exampleFacet1;
        component.criteria$ = new BehaviorSubject<DataDiscoveryCriteria>(DataDiscoveryCriteriaUtils.emptyCriteria());
        tester.detectChanges();

        // No sources selected in criteria
        component.criteria$.pipe(take(1)).subscribe(criteria => {
            expect(criteria.sources).toEqual(null);
        });

        // Click on checkbox
        // @ts-ignore
        tester.checkboxes[1].nativeElement.click();
        expect(tester.checkboxes[1].nativeElement['checked']).toBe(true);

        // Source 2 selected
        component.criteria$.subscribe(criteria => {
            expect(criteria.sources).toEqual(['source 2']);
        });
    });

    it('should display advance search button for germplasm', () => {
        const tester = new FacetsComponentTester();

        const component = tester.componentInstance;
        const criteria = {
            ...DataDiscoveryCriteriaUtils.emptyCriteria(),
            types: ['Germplasm']
        };
        component.criteria$ = new BehaviorSubject<DataDiscoveryCriteria>(criteria);
        component.facet = exampleFacet2;
        tester.detectChanges();

        expect(tester.advanceSearchButton.length).toEqual(1);
        expect(tester.advanceSearchButton[0]).toContainText('Advance search');

    });

    it('should not display advance search button for germplasm', () => {
        const tester = new FacetsComponentTester();

        const component = tester.componentInstance;
        const criteria = {
            ...DataDiscoveryCriteriaUtils.emptyCriteria(),
            types: ['Germplasm', 'Phenotyping Study']
        };
        component.criteria$ = new BehaviorSubject<DataDiscoveryCriteria>(criteria);
        component.facet = exampleFacet2;
        tester.detectChanges();

        expect(tester.advanceSearchButton).toEqual([]);

    });
});
