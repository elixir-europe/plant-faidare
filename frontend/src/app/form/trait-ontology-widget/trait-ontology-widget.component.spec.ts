import { TestBed } from '@angular/core/testing';

import { CropOntologyWidgetFactory, TraitOntologyWidgetComponent } from './trait-ontology-widget.component';
import { GnpisService } from '../../gnpis.service';
import { BehaviorSubject, of } from 'rxjs';
import { DataDiscoveryCriteriaUtils } from '../../models/data-discovery.model';
import { NgbAlertModule } from '@ng-bootstrap/ng-bootstrap';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('TraitOntologyWidgetComponent', () => {
    const fakeWidget = {
        setHeight() {},
        reset() {},
        setSelectedNodeIds() {},
        showOnly() {},
        addSelectionChangeHandler() {},
        jsTreePanel: {
            jstree: {
                get_top_selected() {
                    return ['A', 'B'];
                },
                get_bottom_selected() {
                    return ['a', 'b', 'c'];
                },
                select_node(id) {}
            },
            load() {
                return {
                    then(callback) {
                        callback();
                    }
                };
            }
        }
    };

    const fakeFactory: CropOntologyWidgetFactory = {
        initialize() {
            return fakeWidget;
        }
    };


    beforeEach(() => TestBed.configureTestingModule({
        imports: [NgbAlertModule, HttpClientTestingModule],
        declarations: [
            TraitOntologyWidgetComponent
        ],
        providers: [
            { provide: CropOntologyWidgetFactory, useValue: fakeFactory }
        ]
    }));


    it('should create', () => {
        const fixture = TestBed.createComponent(TraitOntologyWidgetComponent);
        const component = fixture.componentInstance;
        component.criteria$ = new BehaviorSubject(DataDiscoveryCriteriaUtils.emptyCriteria());

        const service = TestBed.get(GnpisService) as GnpisService;
        spyOn(service, 'suggest').and.returnValue(of(['var1', 'var2']));

        fixture.detectChanges();

        expect(component).toBeTruthy();
    });


    it('should initialize selection from criteria', () => {
        const fixture = TestBed.createComponent(TraitOntologyWidgetComponent);
        const component = fixture.componentInstance;
        const criteria = DataDiscoveryCriteriaUtils.emptyCriteria();
        criteria.topSelectedTraitOntologyIds = fakeWidget.jsTreePanel.jstree.get_top_selected();

        const expectedBottomIds = fakeWidget.jsTreePanel.jstree.get_bottom_selected();

        component.criteria$ = new BehaviorSubject(criteria);

        const service = TestBed.get(GnpisService) as GnpisService;
        spyOn(service, 'suggest').and.returnValue(of(['var1', 'var2']));

        fixture.detectChanges();

        expect(component.criteria$.value.observationVariableIds).toEqual(expectedBottomIds);
    });
});
