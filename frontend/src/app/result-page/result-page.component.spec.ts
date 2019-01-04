import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ResultPageComponent } from './result-page.component';
import { Component, NO_ERRORS_SCHEMA } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';
import { ActivatedRoute, ActivatedRouteSnapshot, Params, Router } from '@angular/router';
import { of } from 'rxjs';
import { fakeRoute } from 'ngx-speculoos';
import { DocumentComponent } from './document/document.component';
import { DataDiscoveryCriteria, DataDiscoveryDocument, DataDiscoverySource } from '../model/data-discovery.model';
import { GnpisService } from '../gnpis.service';
import { BrapiResults } from '../model/brapi.model';


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
    } as Params;
    const activatedRoute = fakeRoute({
        queryParams: of(params),
        snapshot: {
            queryParams: params
        } as ActivatedRouteSnapshot
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


    it('should navigate on criteria change', () => {
        const router = TestBed.get(Router) as Router;
        spyOn(router, 'navigate');
        fixture.detectChanges();

        const criteria = { crops: ['Wheat', 'Vitis'] } as DataDiscoveryCriteria;
        component.criteria$.next(criteria);
        const newQueryParams = {
            crops: criteria.crops,
            accessions: criteria.accessions,
            germplasmLists: criteria.germplasmLists,
            observationVariableIds: criteria.topSelectedTraitOntologyIds,
            page: 1
        };

        expect(router.navigate).toHaveBeenCalledWith(['.'], {
            relativeTo: activatedRoute,
            queryParams: newQueryParams
        });

    });

    it('should fetch documents', () => {
        component.fetchDocumentsAndFacets();
        expect(component.documents).not.toBe(null);
    });

})
;
