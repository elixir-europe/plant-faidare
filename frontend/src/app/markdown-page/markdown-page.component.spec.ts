import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';

import { MarkdownPageComponent } from './markdown-page.component';
import { ActivatedRoute } from "@angular/router";
import { environment } from "../../environments/environment";
import { of } from "rxjs";

describe('MarkdownPageComponent', () => {
    let component: MarkdownPageComponent;
    let fixture: ComponentFixture<MarkdownPageComponent>;
    const route = ({ data: of({ mdFile: environment.helpMdFile }) } as any) as ActivatedRoute;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [ MarkdownPageComponent ],
            providers: [{ provide: ActivatedRoute, useValue: route }],
            schemas: [NO_ERRORS_SCHEMA]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(MarkdownPageComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
        expect(component.mdFile).toEqual('assets/help.md');
    });
});
