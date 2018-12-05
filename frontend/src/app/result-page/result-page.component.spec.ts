import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ResultPageComponent } from './result-page.component';
import { Component, NO_ERRORS_SCHEMA } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { of } from 'rxjs';
import { fakeRoute } from 'ngx-speculoos';
import { DocumentComponent } from './document/document.component';


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

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            imports: [
                RouterTestingModule,
            ],
            declarations: [ResultPageComponent, MockFormComponent, DocumentComponent],
            schemas: [NO_ERRORS_SCHEMA],
        });
        fixture = TestBed.createComponent(ResultPageComponent);
        component = fixture.componentInstance;
    }));

    it('should create', () => {
        fixture.detectChanges();
        expect(component).toBeTruthy();
    });


    it('should generate criteria from URL', () => {
        const router = TestBed.get(Router) as Router;
        spyOn(router, 'navigate');
        const params = {
            crops: 'Genus',
            germplasmLists: ['Panel 1', 'Collection 2']
        };
        const activatedRoute = fakeRoute({
            queryParams: of(params as Params)
        });
        const resultComponent = new ResultPageComponent(activatedRoute, router, service);
        resultComponent.ngOnInit();

        resultComponent.criteria$.subscribe(criteria => {
            expect(criteria.crops).toEqual([params.crops]);
            expect(criteria.germplasmLists).toEqual(params.germplasmLists);
        });
    });


    it('should navigate on selection change', () => {
        const router = TestBed.get(Router) as Router;
        spyOn(router, 'navigate');
        const params = {
            crops: 'Genus',
            germplasmLists: ['Panel 1', 'Collection 2']
        };
        const activatedRoute = {
            queryParams: of(params as Params)
        } as ActivatedRoute;

        const resultComponent = new ResultPageComponent(activatedRoute, router, service);

        resultComponent.onSelectionChanges({ name: 'crops', selection: ['Wheat', 'Vitis'] });

        expect(router.navigate).toHaveBeenCalledWith(['.'], {
            relativeTo: activatedRoute,
            queryParams: {
                crops: ['Wheat', 'Vitis']
            },
            queryParamsHandling: 'merge'
        });

    });
});
