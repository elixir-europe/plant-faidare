import { async, TestBed } from '@angular/core/testing';

import { FormComponent } from './form.component';
import { Component, EventEmitter, Input, Output } from '@angular/core';

/**
 * Mock gpds-suggestion-field
 */
@Component({
    selector: 'gpds-suggestion-field',
    template: '<br/>'
})
class MockSuggestionFieldComponent {
    @Input() criteria$: any;
}

/**
 * Mock gpds-trait-ontology-widget
 */
@Component({
    selector: 'gpds-trait-ontology-widget',
    template: '<br/>'
})
class MockTraitWidgetComponent {
    @Input() criteria$: any;
    @Output() initialized = new EventEmitter();
}

describe('FormComponent', () => {

    beforeEach(async(() =>
        TestBed.configureTestingModule({
            declarations: [FormComponent, MockSuggestionFieldComponent, MockTraitWidgetComponent],
        }).compileComponents()
    ));

    it('should switch tabs', async(() => {
        const fixture = TestBed.createComponent(FormComponent);
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
