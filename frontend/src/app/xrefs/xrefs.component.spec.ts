import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { XrefsComponent } from './xrefs.component';

describe('XrefsComponent', () => {
  let component: XrefsComponent;
  let fixture: ComponentFixture<XrefsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ XrefsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(XrefsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
