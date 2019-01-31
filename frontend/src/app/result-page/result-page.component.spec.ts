import { TestBed } from '@angular/core/testing';

import { ResultPageComponent } from './result-page.component';
import { EventEmitter, NO_ERRORS_SCHEMA } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';
import { ActivatedRoute, ActivatedRouteSnapshot, Params, Router } from '@angular/router';
import { BehaviorSubject, of } from 'rxjs';
import { ComponentTester, fakeRoute } from 'ngx-speculoos';
import { DocumentComponent } from './document/document.component';
import {
    DataDiscoveryCriteria,
    DataDiscoveryCriteriaUtils,
    DataDiscoveryDocument,
    DataDiscoverySource
} from '../models/data-discovery.model';
import { GnpisService } from '../gnpis.service';
import { BrapiResults } from '../models/brapi.model';


class ResultPageComponentTester extends ComponentTester<ResultPageComponent> {
    constructor() {
        super(ResultPageComponent);
        this.componentInstance.form.traitWidgetInitialized = new EventEmitter();
        this.componentInstance.form.traitWidgetInitialized.emit();
    }
}

describe('ResultPageComponent', () => {

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

    beforeEach(() => TestBed.configureTestingModule({
        imports: [
            RouterTestingModule,
        ],
        declarations: [ResultPageComponent, DocumentComponent],
        providers: [
            { provide: GnpisService, useValue: service },
            { provide: ActivatedRoute, useValue: activatedRoute }
        ],
        schemas: [NO_ERRORS_SCHEMA],
    }));


    it('should generate criteria from URL', () => {
        const tester = new ResultPageComponentTester();
        const component = tester.componentInstance;
        component.criteria$ = new BehaviorSubject<DataDiscoveryCriteria>(DataDiscoveryCriteriaUtils.emptyCriteria());

        tester.detectChanges();
        component.criteria$.subscribe(criteria => {
            expect(criteria.crops).toEqual([params.crops]);
            expect(criteria.germplasmLists).toEqual(params.germplasmLists);
        });
    });


    it('should navigate on criteria change', () => {
        const tester = new ResultPageComponentTester();
        const component = tester.componentInstance;

        const router = TestBed.get(Router) as Router;
        spyOn(router, 'navigate');
        tester.detectChanges();

        const criteria = { crops: ['Wheat', 'Vitis'] } as DataDiscoveryCriteria;
        component.criteria$.next(criteria);
        const newQueryParams = {
            crops: criteria.crops,
            accessions: criteria.accessions,
            germplasmLists: criteria.germplasmLists,
            observationVariableIds: criteria.topSelectedTraitOntologyIds,
            sources: criteria.sources,
            types: criteria.types,
            page: 1
        };

        expect(router.navigate).toHaveBeenCalledWith(['.'], {
            relativeTo: activatedRoute,
            queryParams: newQueryParams
        });

    });

    it('should fetch documents', () => {
        const tester = new ResultPageComponentTester();
        const component = tester.componentInstance;
        component.fetchDocumentsAndFacets();
        expect(component.documents).not.toBe(null);
    });
});
