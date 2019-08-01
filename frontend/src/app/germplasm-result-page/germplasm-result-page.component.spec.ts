import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GermplasmResultPageComponent } from './germplasm-result-page.component';

describe('GermplasmResultPageComponent', () => {
    let component: GermplasmResultPageComponent;
    let fixture: ComponentFixture<GermplasmResultPageComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [GermplasmResultPageComponent]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(GermplasmResultPageComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
