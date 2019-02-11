import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CardRowComponent } from './card-row.component';

describe('CardRowComponent', () => {
    let component: CardRowComponent;
    let fixture: ComponentFixture<CardRowComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [CardRowComponent]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(CardRowComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
