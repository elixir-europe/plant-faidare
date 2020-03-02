import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SwitchButtonComponent } from './switch-button.component';
import { NgbPopoverModule } from '@ng-bootstrap/ng-bootstrap';
import {
    DataDiscoveryCriteria,
    DataDiscoveryCriteriaUtils
} from '../../models/data-discovery.model';
import { BehaviorSubject } from 'rxjs';

describe('SwitchButtonComponent', () => {
    let component: SwitchButtonComponent;
    let fixture: ComponentFixture<SwitchButtonComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            imports: [NgbPopoverModule],
            declarations: [SwitchButtonComponent]
        })
            .compileComponents();
    }));

    it('should create', () => {
        fixture = TestBed.createComponent(SwitchButtonComponent);
        component = fixture.componentInstance;
        component.displayGermplasmResult$ = new BehaviorSubject<boolean>(false);
        component.criteria$ = new BehaviorSubject<DataDiscoveryCriteria>(DataDiscoveryCriteriaUtils.emptyCriteria());
        fixture.detectChanges();
        expect(component).toBeTruthy();
    });
});
