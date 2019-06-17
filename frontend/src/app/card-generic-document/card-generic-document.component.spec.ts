import { async, TestBed } from '@angular/core/testing';

import { CardGenericDocumentComponent } from './card-generic-document.component';
import { ComponentTester, speculoosMatchers } from 'ngx-speculoos';
import { DataDiscoverySource } from '../models/data-discovery.model';
import { CardRowComponent } from '../card-row/card-row.component';
import { GnpisService } from '../gnpis.service';
import { of } from 'rxjs/internal/observable/of';


class CardGenericDocumentComponentTester extends ComponentTester<CardGenericDocumentComponent> {
    constructor() {
        super(CardGenericDocumentComponent);
    }

    get pui() {
        return this.element('.pui-row .value');
    }

    get dataSource() {
        return this.element('.data-source-row a');
    }

    get dataLink() {
        return this.element('.data-link-row a');
    }
}

describe('CardGenericDocumentComponent', () => {
    beforeEach(() => jasmine.addMatchers(speculoosMatchers));

    const dataSource: DataDiscoverySource = {
        '@id': 'urn:source',
        '@type': ['schema:DataCatalog'],
        'schema:name': 'Example source',
        'schema:url': 'http://example.com',
        'schema:image': 'http://example.com/logo.png'
    };

    beforeEach(async () => {
        const gnpisService = jasmine.createSpyObj(
            'GnpisService', [
                'getSource'
            ]
        );
        gnpisService.getSource.and.returnValue(of(dataSource));

        await TestBed.configureTestingModule({
            declarations: [CardRowComponent, CardGenericDocumentComponent],
            providers: [
                { provide: GnpisService, useValue: gnpisService }
            ]
        }).compileComponents();
    });

    it('should hide URN PUI', async(() => {
        const tester = new CardGenericDocumentComponentTester();
        tester.componentInstance.document = {
            '@id': 'urn:foo/bar'
        };
        tester.detectChanges();

        expect(tester.pui).toBeFalsy();
    }));

    it('should show non-URN PUI', async(() => {
        const tester = new CardGenericDocumentComponentTester();
        const document = {
            '@id': 'https://doi.org/....'
        };
        tester.componentInstance.document = document;
        tester.detectChanges();
        expect(tester.pui).toContainText(document['@id']);
    }));


    it('should hide data source', async(() => {
        const tester = new CardGenericDocumentComponentTester();
        tester.componentInstance.document = {
            'schema:includedInDataCatalog': dataSource['@id']
        };
        tester.detectChanges();

        expect(tester.componentInstance.dataSource).toBe(dataSource);
    }));


    it('should hide no data link', async(() => {
        const tester = new CardGenericDocumentComponentTester();
        tester.componentInstance.document = {
            'schema:includedInDataCatalog': dataSource['@id'],
            'schema:url': null
        };
        tester.detectChanges();

        expect(tester.componentInstance.dataSource).toBe(dataSource);
        expect(tester.dataLink).toBeFalsy();
    }));


    it('should show data link', async(() => {
        const tester = new CardGenericDocumentComponentTester();
        tester.componentInstance.document = {
            'schema:includedInDataCatalog': dataSource['@id'],
            'schema:url': 'http://example.com/...'
        };
        tester.detectChanges();

        expect(tester.componentInstance.dataSource).toBe(dataSource);
        expect(tester.dataLink).toBeTruthy();
        expect(tester.dataLink.attr('href')).toEqual(tester.componentInstance.document['schema:url']);
    }));
});
