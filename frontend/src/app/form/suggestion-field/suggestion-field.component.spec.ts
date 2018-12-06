import { async, TestBed } from '@angular/core/testing';

import { SuggestionFieldComponent } from './suggestion-field.component';
import { ReactiveFormsModule } from '@angular/forms';
import { NgbTypeaheadModule } from '@ng-bootstrap/ng-bootstrap';
import { GnpisService } from '../../gnpis.service';
import { of } from 'rxjs';
import { newCriteria } from '../../model/dataDiscoveryCriteria';


describe('SuggestionFieldComponent', () => {
    const service = jasmine.createSpyObj(
        'GnpisService', ['suggest']
    );
    let component;
    let fixture;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [
                ReactiveFormsModule,
                NgbTypeaheadModule
            ],
            declarations: [
                SuggestionFieldComponent
            ],
            providers: [
                { provide: GnpisService, useValue: service }
            ]
        });
        fixture = TestBed.createComponent(SuggestionFieldComponent);
        component = fixture.componentInstance;
        component.criteria$ = of(newCriteria());
    });

    it('should create', () => {
        fixture.detectChanges();
        expect(component).toBeTruthy();
    });

    it('should fetch suggestion', async(() => {
        component.criteriaField = 'crops';
        const expectedSuggestions = ['a', 'b', 'c'];
        service.suggest.and.returnValue(of(expectedSuggestions));

        component.search(of('bar'))
            .subscribe((actualSuggestions: string[]) => {
                expect(actualSuggestions).toEqual(expectedSuggestions);
            });
    }));

    it('should display the selected criteria as pills', () => {
        component.criteriaField = 'crops';
        component.criteria$ = of({ ...newCriteria(), crops: ['Zea', 'Wheat'] });

        fixture.detectChanges();

        const pills = fixture.nativeElement.querySelectorAll('.badge-pill');

        expect(pills.length).toBe(2);
        expect(pills[0].textContent).toContain('Zea');
        expect(pills[1].textContent).toContain('Wheat');
    });


    it('should fetch suggestion', async(() => {
        component.criteriaField = 'crops';
        const selectedCrops = ['Zea', 'Wheat'];
        component.criteria$ = of({ ...newCriteria(), crops: selectedCrops });

        const allSuggestions = ['Zea', 'Wheat', 'Vitis', 'Grapevine'];
        service.suggest.and.returnValue(of(allSuggestions));

        const expectedSuggestions = allSuggestions.filter(s => selectedCrops.indexOf(s) < 0);

        fixture.detectChanges();

        component.search(of('bar'))
            .subscribe((actualSuggestions: string[]) => {
                expect(actualSuggestions).toEqual(expectedSuggestions);
            });
    }));

});
