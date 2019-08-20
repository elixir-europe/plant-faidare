import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CardSortableTableComponent } from './card-sortable-table.component';

describe('CardSortableTableComponent', () => {
  let component: CardSortableTableComponent;
  let fixture: ComponentFixture<CardSortableTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CardSortableTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CardSortableTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
