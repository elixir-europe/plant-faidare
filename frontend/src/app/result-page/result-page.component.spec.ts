import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ResultPageComponent } from './result-page.component';
import { Component, NO_ERRORS_SCHEMA } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { of } from 'rxjs';
import { fakeRoute } from 'ngx-speculoos';
import { DocumentComponent } from './document/document.component';
import { newCriteria } from '../model/dataDiscoveryCriteria';
import { GnpisService } from '../gnpis.service';
import { DataDiscoveryDocument, DataDiscoverySource } from '../model/dataDiscoveryDocument';
import { BrapiResults } from '../model/brapi';


@Component({
    selector: 'gpds-form',
    template: '<br/>'
})
class MockFormComponent {
}


describe('ResultPageComponent', () => {
    let component: ResultPageComponent;
    let fixture: ComponentFixture<ResultPageComponent>;

    const service = jasmine.createSpyObj(
        'GnpisService', ['search']
    );
    const params = {
        crops: 'Genus',
        germplasmLists: ['Panel 1', 'Collection 2']
    };
    const activatedRoute = fakeRoute({
        queryParams: of(params as Params)
    });

    const document: DataDiscoveryDocument = {
        '@type': ['Germplasm'],
        '@id': 'urn',
        'schema:identifier': 'schema',
        'schema:name': 'doc_name',
        'schema:url': 'http://dco/url',
        'schema:description': 'description',
        'schema:includedInDataCatalog': {} as DataDiscoverySource
    };
    const searchedDocuments: BrapiResults<DataDiscoveryDocument> = {
        result: {
            data: [document]
        },
        metadata: {
            pagination: {
                pageSize: 10,
                currentPage: 0,
                totalPages: 1,
                totalCount: 1
            }
        }
    };
    service.search.and.returnValue(of(searchedDocuments));

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            imports: [
                RouterTestingModule,
            ],
            declarations: [ResultPageComponent, MockFormComponent, DocumentComponent],
            providers: [
                { provide: GnpisService, useValue: service },
                { provide: ActivatedRoute, useValue: activatedRoute }
            ],
            schemas: [NO_ERRORS_SCHEMA],
        });
        fixture = TestBed.createComponent(ResultPageComponent);
        component = fixture.componentInstance;
    }));


    it('should generate criteria from URL', () => {
        fixture.detectChanges();

        component.criteria$.subscribe(criteria => {
            expect(criteria.crops).toEqual([params.crops]);
            expect(criteria.germplasmLists).toEqual(params.germplasmLists);
        });
    });


    it('should navigate on selection change', () => {
        const router = TestBed.get(Router) as Router;
        spyOn(router, 'navigate');

        component.updateParams({ key: 'crops', value: ['Wheat', 'Vitis'] });


        expect(router.navigate).toHaveBeenCalledWith(['.'], {
            relativeTo: activatedRoute,
            queryParams: {
                page: null,
                crops: ['Wheat', 'Vitis']
            },
            queryParamsHandling: 'merge'
        });

    });

    it('should fetch documents', () => {
        const criteria = newCriteria();
        component.fetchDocuments(criteria);
        expect(component.documents).not.toBe(null);
    });


})
;
