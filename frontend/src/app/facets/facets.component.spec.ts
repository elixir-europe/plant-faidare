import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { FacetsComponent } from './facets.component';
import { BehaviorSubject } from 'rxjs';
import {
    DataDiscoveryCriteria,
    DataDiscoveryCriteriaUtils
} from '../models/data-discovery.model';
import { GermplasmSearchCriteria } from '../models/gnpis.model';
import { SmallFacetsComponent } from './small-facets/small-facets.component';
import { LargeFacetsComponent } from './large-facets/large-facets.component';
import { MockComponents } from 'ng-mocks';


describe('FacetsComponent', () => {
    let component: FacetsComponent;
    let fixture: ComponentFixture<FacetsComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [FacetsComponent, MockComponents(SmallFacetsComponent), MockComponents(LargeFacetsComponent)]
        })
            .compileComponents();


        fixture = TestBed.createComponent(FacetsComponent);
        component = fixture.componentInstance;

        component.criteria$ = new BehaviorSubject<DataDiscoveryCriteria>(DataDiscoveryCriteriaUtils.emptyCriteria());
        component.germplasmSearchCriteria$ = new BehaviorSubject<GermplasmSearchCriteria>(DataDiscoveryCriteriaUtils
            .emptyGermplasmSearchCriteria());
        component.displayGermplasmResult$ = new BehaviorSubject<boolean>(false);
        fixture.detectChanges();
    }));

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
