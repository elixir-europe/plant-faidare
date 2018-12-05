import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ResultComponent } from './result.component';
import { Component, NO_ERRORS_SCHEMA } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { of } from 'rxjs';
import { fakeRoute } from 'ngx-speculoos';


@Component({
    selector: 'gpds-form',
    template: '<br/>'
})
class MockFormComponent {
}


describe('ResultComponent', () => {
    let component: ResultComponent;
    let fixture: ComponentFixture<ResultComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            imports: [
                RouterTestingModule,
            ],
            declarations: [ResultComponent, MockFormComponent],
            schemas: [NO_ERRORS_SCHEMA],
        });
        fixture = TestBed.createComponent(ResultComponent);
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
        const resultComponent = new ResultComponent(activatedRoute, router);
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

        const resultComponent = new ResultComponent(activatedRoute, router);

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
