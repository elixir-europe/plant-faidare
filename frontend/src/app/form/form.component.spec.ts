import { async, TestBed } from '@angular/core/testing';

import { FormComponent } from './form.component';
import { MockComponents } from 'ng-mocks';
import { SuggestionFieldComponent } from './suggestion-field/suggestion-field.component';
import { TraitOntologyWidgetComponent } from './trait-ontology-widget/trait-ontology-widget.component';
import { BehaviorSubject } from 'rxjs';


describe('FormComponent', () => {

    beforeEach(async(() =>
        TestBed.configureTestingModule({
            declarations: [
                FormComponent, MockComponents(SuggestionFieldComponent), MockComponents(TraitOntologyWidgetComponent)
            ],
        }).compileComponents()
    ));

    it('should switch tabs', async(() => {
        const fixture = TestBed.createComponent(FormComponent);
        const component = fixture.componentInstance;
        component.displayGermplasmResult$ = new BehaviorSubject(false);
        fixture.detectChanges();

        const element: HTMLElement = fixture.nativeElement;
        const germplasmNav: HTMLElement = element.querySelector('a.germplasm');
        const germplasmTab: HTMLElement = element.querySelector('div.germplasm');
        const traitNav: HTMLElement = element.querySelector('a.trait');
        const traitTab: HTMLElement = element.querySelector('div.trait');

        // Check default tab is active
        expect(germplasmNav.getAttribute('class')).toContain('active');
        expect(germplasmTab.getAttribute('class')).toContain('visible');
        expect(traitNav.getAttribute('class')).not.toContain('active');
        expect(traitTab.getAttribute('class')).toContain('d-none');

        traitNav.click();
        fixture.detectChanges();

        // Check tab switched
        expect(traitNav.getAttribute('class')).toContain('active');
        expect(traitTab.getAttribute('class')).toContain('visible');
        expect(germplasmNav.getAttribute('class')).not.toContain('active');
        expect(germplasmTab.getAttribute('class')).toContain('d-none');

        germplasmNav.click();
        fixture.detectChanges();

        // Check tab switched back
        expect(germplasmNav.getAttribute('class')).toContain('active');
        expect(germplasmTab.getAttribute('class')).toContain('visible');
        expect(traitNav.getAttribute('class')).not.toContain('active');
        expect(traitTab.getAttribute('class')).toContain('d-none');
    }));
});
