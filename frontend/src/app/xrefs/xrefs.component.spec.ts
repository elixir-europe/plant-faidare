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
            return this.elements('div.card-header');
        }
    }

    const gnpisService = jasmine.createSpyObj(
        'GnpisService', [
            'xref'
        ]
    );

    const xref: XrefModel[] = [{
        url: 'https://urgi.versailles.inra.fr/association/association/viewer.do#results/analysisIds=1808038',
        description: 'Col-Fa-b*_MLM+Q+K is a GWAS analysis involving CC_Qualité panel',
        database_name: 'GnpIS',
        entry_type: 'GWAS analysis',
        db_version: 'GWAS_ANALYSIS_1808038_1'
    }];


    gnpisService.xref.and.returnValue(of(xref));

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
        const tester = new XrefsComponentTester();
        tester.detectChanges();

        expect(tester.cardHeader[0]).toContainText('Cross References');

    }));
});
