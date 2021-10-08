import { async, TestBed } from '@angular/core/testing';

import { XrefsComponent } from './xrefs.component';
import { ComponentTester, speculoosMatchers } from 'ngx-speculoos';
import { XrefModel } from '../models/xref.model';
import { of } from 'rxjs';
import { GnpisService } from '../gnpis.service';
import { CardSectionComponent } from '../card-section/card-section.component';
import { CardTableComponent } from '../card-table/card-table.component';

describe('XrefsComponent', () => {
    beforeEach(() => jasmine.addMatchers(speculoosMatchers));

    class XrefsComponentTester extends ComponentTester<XrefsComponent> {
        constructor() {
            super(XrefsComponent);
        }

        get cardHeader() {
            return this.element('div.card-header');
        }

        get columns() {
            return this.elements('td');
        }
    }

    const gnpisService = jasmine.createSpyObj(
        'GnpisService', [
            'xref'
        ]
    );

    const xref: XrefModel[] = [{
        url: 'https://urgi.versailles.inra.fr/association/association/viewer.do#results/analysisIds=1808038',
        description: 'Col-Fa-b*_MLM+Q+K is a GWASd anté paneCol-Fa-b*_MLM+Q+K is aGAS anlysis involving CC_Qualité' +
            'djs dsqdsq djsqpodsjqodsqdsqkpdqpdWOLOLOLOOOOOOOsqpkdsqkdsqkdsqdsdsqdsqdsqddsqffjùsodfusjùfsfsd',
        databaseName: 'GnpIS',
        entryType: 'GWAS analysis',
        identifier: 'GWAS_ANALYSIS_1808038_1',
        name: 'Col-Fa-b*_MLM+Q+K'
    }];


    const xrefBlank: XrefModel[] = [];


    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [
                XrefsComponent, CardSectionComponent, CardTableComponent
            ],
            providers: [
                { provide: GnpisService, useValue: gnpisService },
            ]
        });
    }));

    it('should fetch the xref information', async(() => {
        gnpisService.xref.and.returnValue(of(xref));
        const tester = new XrefsComponentTester();

        tester.detectChanges();

        expect(tester.cardHeader).toContainText('Cross References');
        expect(tester.columns[0]).toContainText(xref[0].name);
        expect(tester.columns[1]).toContainText(xref[0].databaseName);
        expect(tester.columns[2]).toContainText(xref[0].entryType);
        expect(tester.columns[3].textContent.length).toBeLessThanOrEqual(124);

    }));


    it('should not display cross references', async(() => {
        gnpisService.xref.and.returnValue(of(xrefBlank));
        const tester = new XrefsComponentTester();
        tester.detectChanges();

        expect(tester.cardHeader).toBeFalsy();

    }));
});
