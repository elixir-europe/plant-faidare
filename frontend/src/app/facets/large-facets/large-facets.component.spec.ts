import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LargeFacetsComponent } from './large-facets.component';

describe('LargeFacetsComponent', () => {
    let component: LargeFacetsComponent;
    let fixture: ComponentFixture<LargeFacetsComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [LargeFacetsComponent]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(LargeFacetsComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
