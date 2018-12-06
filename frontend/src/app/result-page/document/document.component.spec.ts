import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DocumentComponent } from './document.component';
import { NO_ERRORS_SCHEMA } from '@angular/core';

describe('DocumentComponent', () => {
    let component: DocumentComponent;
    let fixture: ComponentFixture<DocumentComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [DocumentComponent],
            schemas: [NO_ERRORS_SCHEMA]
        });

    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(DocumentComponent);
        component = fixture.componentInstance;
        component.document = {

            '@type': ['doc'],
            '@id': 'urn',
            'schema:identifier': 'schema',
            'schema:name': 'doc_name',
            'schema:url': 'http://dco/url',
            'schema:description': 'description',
            'schema:includedInDataCatalog': 'catalog'
        };
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
