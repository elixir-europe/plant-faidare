import { TestBed } from '@angular/core/testing';

import { DocumentComponent } from './document.component';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { DataDiscoverySource } from '../../models/data-discovery.model';
import { ComponentTester, speculoosMatchers } from 'ngx-speculoos';

describe('DocumentComponent', () => {
    class DocumentComponentTester extends ComponentTester<DocumentComponent> {
        constructor() {
            super(DocumentComponent);
        }

        get types() {
            return this.elements('h5 span');
        }

        get source() {
            return this.element('h5 a.badge-source');
        }

        get title() {
            return this.element('h5 a.title');
        }

        get description() {
            return this.element('span.description');
        }

        get showMoreButton() {
            return this.button('span.description button');
        }
    }

    beforeEach(() => TestBed.configureTestingModule({
        declarations: [DocumentComponent],
        schemas: [NO_ERRORS_SCHEMA]
    }));

    beforeEach(() => jasmine.addMatchers(speculoosMatchers));

    it('should display document', () => {
        const tester = new DocumentComponentTester();
        const component = tester.componentInstance;

        component.document = {
            '@type': ['Germplasm', 'Phenotyping Study', 'Genotyping Study'],
            '@id': 'urn',
            'schema:identifier': 'doc1',
            'schema:name': 'doc_name',
            'schema:url': 'http://dco/url',
            'schema:description': 'description',
            'schema:includedInDataCatalog': {
                '@id': 'http://source1.com',
                '@type': ['schema:DataCatalog'],
                'schema:image': null,
                'schema:name': 'source1',
                'schema:url': 'http://dco/url'
            }
        };
        tester.detectChanges();
        expect(component).toBeTruthy();
        expect(tester.types[0]).toContainText('Germplasm');
        expect(tester.types[0].classes).toContain('badge-germplasm');

        expect(tester.types[1]).toContainText('Phenotyping Study');
        expect(tester.types[1].classes).toContain('badge-phenotyping-study');

        expect(tester.types[2]).toContainText('Genotyping Study');
        expect(tester.types[2].classes).toContain('badge-genotyping-study');

        expect(tester.source).toContainText('source1');
        expect(tester.source.attr('href')).toEqual('http://dco/url');

        expect(tester.title).toContainText('doc_name');
        expect(tester.title.nativeElement['routerLink']).toEqual('/germplasm');

        expect(tester.description).toContainText('description');

    });


    it('should generate router link', () => {
        const tester = new DocumentComponentTester();
        const component = tester.componentInstance;

        component.document = {
            '@type': ['Germplasm'],
            '@id': 'urn',
            'schema:identifier': 'g1',
            'schema:name': 'doc_name',
            'schema:url': null,
            'schema:description': 'description',
            'schema:includedInDataCatalog': {} as DataDiscoverySource
        };
        tester.detectChanges();
        expect(component).toBeTruthy();

        expect(tester.title).toContainText('doc_name');
        expect(tester.title.nativeElement['routerLink']).toEqual('/germplasm');
        expect(component.getQueryParam().id).toEqual('g1');

    });

    it('should truncate description', () => {
        const tester = new DocumentComponentTester();
        const component = tester.componentInstance;

        component.document = {
            '@type': ['Germplasm'],
            '@id': 'urn',
            'schema:identifier': 'g1',
            'schema:name': 'doc_name',
            'schema:url': null,
            'schema:description': 'Lorem ipsum dolor sit amet, consectetur ' +
                'adipiscing elit. Suspendisse velit purus, congue euismod ' +
                'leo vel, pharetra euismod lorem. Suspendisse sed tempus ante, ' +
                'eu mattis neque. Quisque eget dui feugiat, pulvinar mi vel, ' +
                'imperdiet orci. Morbi ac mollis ex. Aliuam justo.',
            'schema:includedInDataCatalog': {} as DataDiscoverySource
        };
        tester.detectChanges();
        expect(component).toBeTruthy();

        expect(tester.description).not.toContainText('justo');

        expect(tester.showMoreButton).toBeTruthy();
        tester.showMoreButton.click();

        expect(tester.description).toContainText('justo');

        expect(tester.showMoreButton).toBeTruthy();
        tester.showMoreButton.click();

        expect(tester.description).not.toContainText('justo');
    });
});
