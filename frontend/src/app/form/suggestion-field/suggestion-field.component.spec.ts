import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SuggestionFieldComponent } from './suggestion-field.component';

describe('SuggestionFieldComponent', () => {
  let component: SuggestionFieldComponent;
  let fixture: ComponentFixture<SuggestionFieldComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SuggestionFieldComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SuggestionFieldComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
