import { async, TestBed } from '@angular/core/testing';

import { CardSectionComponent } from './card-section.component';
import { speculoosMatchers } from 'ngx-speculoos';
import { Component, ViewChild } from '@angular/core';


/**
 * Test faidare-card-section with a provided `ng-template`
 */
@Component({
    selector: 'faidare-test',
    template: `
        <faidare-card-section>
            <ng-template>
                <div class="test-body">Body HTML template</div>
            </ng-template>
        </faidare-card-section>`
})
class CardSectionTestWrapperComponent {
    @ViewChild(CardSectionComponent) component: CardSectionComponent;
}

describe('CardSectionComponent', () => {
    beforeEach(() => jasmine.addMatchers(speculoosMatchers));

    beforeEach(async(() =>
        TestBed.configureTestingModule({
            declarations: [CardSectionComponent, CardSectionTestWrapperComponent]
        }).compileComponents()
    ));

    it('should hide falsy test', async(() => {
        const fixture = TestBed.createComponent(CardSectionTestWrapperComponent);
        const component = fixture.componentInstance.component;
        component.header = 'Header1';
        component.test = '';
        fixture.detectChanges();

        const element: HTMLElement = fixture.nativeElement;
        const cardDiv = element.querySelector('div.card');
        expect(cardDiv).toBeFalsy();
    }));


    it('should show truthy test', async(() => {
        const fixture = TestBed.createComponent(CardSectionTestWrapperComponent);
        const component = fixture.componentInstance.component;
        component.header = 'Header2';
        component.test = true;
        fixture.detectChanges();

        const element: HTMLElement = fixture.nativeElement;
        const cardDiv = element.querySelector('div.card');
        const headerDiv = element.querySelector('div.card-header');
        const bodyDiv = element.querySelector('div.test-body');
        expect(cardDiv).toBeTruthy();
        expect(headerDiv.textContent).toContain(component.header);
        expect(bodyDiv.textContent).toContain('Body HTML template');
    }));
});
