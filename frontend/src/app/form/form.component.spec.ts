import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormComponent } from './form.component';
import { Component, NO_ERRORS_SCHEMA } from '@angular/core';


@Component({
    selector: 'gpds-suggestion-field',
    template: '<br/>'
})
class MockFieldComponent {
}

describe('FormComponent', () => {
    let component: FormComponent;
    let fixture: ComponentFixture<FormComponent>;

    beforeEach(() => {
        TestBed.configureTestingModule({
            declarations: [FormComponent, MockFieldComponent],
            schemas: [NO_ERRORS_SCHEMA]
        }).compileComponents();

        fixture = TestBed.createComponent(FormComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        fixture.detectChanges();
        expect(component).toBeTruthy();
    });
});
