import { async, TestBed } from '@angular/core/testing';

import { CardRowComponent } from './card-row.component';
import { ComponentTester, speculoosMatchers } from 'ngx-speculoos';
import { Component, ViewChild } from '@angular/core';

class CardRowComponentTester extends ComponentTester<CardRowComponent> {
    constructor() {
        super(CardRowComponent);
    }

    get rowDiv() {
        return this.element('div.row');
    }

    get labelDiv() {
        return this.element('div.field');
    }

    get valueDiv() {
        return this.element('div.col');
    }
}


/**
 * Test gpds-card-row with a provided `ng-template`
 */
@Component({
    selector: 'gpds-test',
    template: `
        <gpds-card-row>
            <ng-template>
                <strong>Value HTML template</strong>
            </ng-template>
        </gpds-card-row>`
})
class CardRowWithTemplateComponent {
    @ViewChild(CardRowComponent) component: CardRowComponent;
}

describe('CardRowComponent', () => {
    beforeEach(() => jasmine.addMatchers(speculoosMatchers));

    beforeEach(async(() =>
        TestBed.configureTestingModule({
            declarations: [CardRowComponent, CardRowWithTemplateComponent]
        }).compileComponents()
    ));


    it('should hide falsy value', () => {
        const tester = new CardRowComponentTester();
        tester.componentInstance.label = 'Label1';
        tester.componentInstance.value = null;
        tester.detectChanges();

        expect(tester.rowDiv).toBeFalsy();
    });

    it('should show truthy value', () => {
        const tester = new CardRowComponentTester();
        tester.componentInstance.label = 'Label1';
        tester.componentInstance.value = 'Value1';
        tester.detectChanges();

        expect(tester.rowDiv).toBeTruthy();
        expect(tester.labelDiv).toContainText(tester.componentInstance.label);
        expect(tester.valueDiv).toContainText(tester.componentInstance.value);
    });

    it('should hide falsy test', () => {
        const tester = new CardRowComponentTester();
        tester.componentInstance.label = 'Label1';
        tester.componentInstance.value = 'Value1';
        tester.componentInstance.test = false;
        tester.detectChanges();

        expect(tester.rowDiv).toBeFalsy();
    });

    it('should hide truthy test, falsy value', () => {
        const tester = new CardRowComponentTester();
        tester.componentInstance.label = 'Label1';
        tester.componentInstance.value = '';
        tester.componentInstance.test = true;
        tester.detectChanges();

        expect(tester.rowDiv).toBeFalsy();
    });

    it('should hide truthy test, falsy value', () => {
        const tester = new CardRowComponentTester();
        tester.componentInstance.label = 'Label1';
        tester.componentInstance.value = '';
        tester.componentInstance.test = true;
        tester.detectChanges();

        expect(tester.rowDiv).toBeFalsy();
    });

    it('should show truthy test, truthy value', () => {
        const tester = new CardRowComponentTester();
        tester.componentInstance.label = 'Label1';
        tester.componentInstance.value = 'Value1';
        tester.componentInstance.test = true;
        tester.detectChanges();

        expect(tester.rowDiv).toBeTruthy();
        expect(tester.labelDiv).toContainText(tester.componentInstance.label);
        expect(tester.valueDiv).toContainText(tester.componentInstance.value);
    });


    it('should hide falsy test, provided template', async(() => {
        const fixture = TestBed.createComponent(CardRowWithTemplateComponent);
        fixture.componentInstance.component.label = 'Label1';
        fixture.componentInstance.component.test = '';
        fixture.detectChanges();

        const element: HTMLElement = fixture.nativeElement;
        expect(element.querySelector('div.row')).toBeFalsy();
    }));


    it('should show truthy test, provided template', async(() => {
        const fixture = TestBed.createComponent(CardRowWithTemplateComponent);
        const component = fixture.componentInstance.component;
        component.label = 'Label2';
        component.test = true;
        fixture.detectChanges();

        const element: HTMLElement = fixture.nativeElement;
        expect(element.querySelector('div.row')).toBeTruthy();
        expect(element.querySelector('div.field').textContent).toContain(component.label);
        expect(element.querySelector('div.col').textContent).toContain('Value HTML template');
    }));
});
