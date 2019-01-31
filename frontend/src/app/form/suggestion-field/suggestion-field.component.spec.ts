import { async, TestBed } from '@angular/core/testing';

import { SuggestionFieldComponent } from './suggestion-field.component';
import { ReactiveFormsModule } from '@angular/forms';
import { NgbTypeaheadModule } from '@ng-bootstrap/ng-bootstrap';
import { GnpisService } from '../../gnpis.service';
import { BehaviorSubject, of } from 'rxjs';
import { DataDiscoveryCriteriaUtils } from '../../model/data-discovery.model';
import { ComponentTester } from 'ngx-speculoos';
import { HttpClientTestingModule } from '@angular/common/http/testing';


describe('SuggestionFieldComponent', () => {

    class SuggestionFieldComponentTester extends ComponentTester<SuggestionFieldComponent> {
        constructor() {
            super(SuggestionFieldComponent);
        }
    }

    beforeEach(() => TestBed.configureTestingModule({
        imports: [
            ReactiveFormsModule,
            NgbTypeaheadModule,
            HttpClientTestingModule,
        ],
        declarations: [
            SuggestionFieldComponent
        ]
    }));

    it('should create', () => {
        const tester = new SuggestionFieldComponentTester();
        const component = tester.componentInstance;
        component.criteria$ = new BehaviorSubject(DataDiscoveryCriteriaUtils.emptyCriteria());
        tester.detectChanges();
        expect(component).toBeTruthy();
    });

    it('should fetch suggestion on text change', async(() => {
        const tester = new SuggestionFieldComponentTester();
        const component = tester.componentInstance;
        const criteria = DataDiscoveryCriteriaUtils.emptyCriteria();
        component.criteria$ = new BehaviorSubject(criteria);
        component.criteriaField = 'crops';

        const expectedSuggestions = ['a', 'b', 'c'];
        const service = TestBed.get(GnpisService) as GnpisService;
        spyOn(service, 'suggest').and.returnValue(of(expectedSuggestions));
        tester.detectChanges();

        component.search(of('bar'))
            .subscribe((actualSuggestions: string[]) => {
                expect(actualSuggestions).toEqual(expectedSuggestions);

                expect(service.suggest).toHaveBeenCalledWith(
                    component.criteriaField, 10, 'bar', criteria
                );
            });
    }));

    it('should display the selected criteria as pills', () => {
        const tester = new SuggestionFieldComponentTester();
        const component = tester.componentInstance;
        component.criteriaField = 'crops';
        const criteria = { ...DataDiscoveryCriteriaUtils.emptyCriteria(), crops: ['Zea', 'Wheat'] };
        component.criteria$ = new BehaviorSubject(criteria);

        tester.detectChanges();

        const pills = tester.nativeElement.querySelectorAll('.badge-pill');

        expect(pills.length).toBe(2);
        expect(pills[0].textContent).toContain('Zea');
        expect(pills[1].textContent).toContain('Wheat');
    });


    it('should fetch suggestion', async(() => {
        const tester = new SuggestionFieldComponentTester();
        const component = tester.componentInstance;
        component.criteriaField = 'crops';
        const selectedCrops = ['Zea', 'Wheat'];
        const criteria = { ...DataDiscoveryCriteriaUtils.emptyCriteria(), crops: selectedCrops };
        component.criteria$ = new BehaviorSubject(criteria);

        const allSuggestions = ['Zea', 'Wheat', 'Vitis', 'Grapevine'];
        const service = TestBed.get(GnpisService) as GnpisService;
        spyOn(service, 'suggest').and.returnValue(of(allSuggestions));

        const expectedSuggestions = allSuggestions.filter(s => selectedCrops.indexOf(s) < 0);

        tester.detectChanges();

        component.search(of('bar'))
            .subscribe((actualSuggestions: string[]) => {
                expect(actualSuggestions).toEqual(expectedSuggestions.concat(['REFINE']));

                expect(service.suggest).toHaveBeenCalledWith(
                    component.criteriaField, 10, 'bar', criteria
                );
            });
    }));
});
