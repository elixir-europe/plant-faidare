import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GermplasmCardComponent } from './germplasm-card.component';

describe('GermplasmCardComponent', () => {
  let component: GermplasmCardComponent;
  let fixture: ComponentFixture<GermplasmCardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GermplasmCardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GermplasmCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
