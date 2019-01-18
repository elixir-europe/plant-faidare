import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StudyCardComponent } from './study-card.component';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('StudyCardComponent', () => {
    let component: StudyCardComponent;
    let fixture: ComponentFixture<StudyCardComponent>;
    // let httpMock;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [StudyCardComponent],
            schemas: [NO_ERRORS_SCHEMA]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule],
            providers: [HttpClientTestingModule]
        });

        fixture = TestBed.createComponent(StudyCardComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });
});
