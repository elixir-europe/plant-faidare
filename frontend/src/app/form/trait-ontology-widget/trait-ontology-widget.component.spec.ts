import { TestBed } from '@angular/core/testing';

import { CropOntologyWidgetFactory, TraitOntologyWidgetComponent } from './trait-ontology-widget.component';
import { GnpisService } from '../../gnpis.service';
import { BehaviorSubject } from 'rxjs';
import { emptyCriteria } from '../../model/dataDiscoveryCriteria';

describe('TraitOntologyWidgetComponent', () => {
    const service = jasmine.createSpyObj(
        'GnpisService', ['suggest']
    );

    const fakeWidget = {
        setHeight() { },
        reset() { },
        setSelectedNodeIds() { },
        showOnly() { },
        addSelectionChangeHandler() {},
        jsTreePanel: {
            jstree: {
                get_top_selected() {
                    return ['A', 'B'];
                },
                get_bottom_selected() {
                    return ['a', 'b', 'c'];
                }
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

    let component;
    let fixture;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [],
            declarations: [
                TraitOntologyWidgetComponent
            ],
            providers: [
                { provide: GnpisService, useValue: service },
                { provide: CropOntologyWidgetFactory, useValue: fakeFactory }
            ]
        });
        fixture = TestBed.createComponent(TraitOntologyWidgetComponent);
        component = fixture.componentInstance;
    });


    it('should create', () => {
        component.criteria$ = new BehaviorSubject(emptyCriteria());
        fixture.detectChanges();
        expect(component).toBeTruthy();
    });


    it('should initialize selection from criteria', () => {
        const criteria = emptyCriteria();
        criteria.topSelectedTraitOntologyIds = fakeWidget.jsTreePanel.jstree.get_top_selected();

        const expectedBottomIds = fakeWidget.jsTreePanel.jstree.get_bottom_selected();

        component.criteria$ = new BehaviorSubject(criteria);
        fixture.detectChanges();

        expect(component.localCriteria.observationVariableIds).toEqual(expectedBottomIds);
    });
});
