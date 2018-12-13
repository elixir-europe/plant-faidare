import { Component, Injectable, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { CropOntologyWidget } from 'trait-ontology-widget/dist/module/cropOntologyWidget.module';
import { DataDiscoveryCriteria } from '../../model/dataDiscoveryCriteria';
import { BehaviorSubject } from 'rxjs';
import { GnpisService } from '../../gnpis.service';
import { filter } from 'rxjs/operators';

@Injectable({
    providedIn: 'root',
})
export class CropOntologyWidgetFactory {

    initialize(...args): CropOntologyWidget {
        return new CropOntologyWidget(...args);
    }

}

@Component({
    selector: 'gpds-trait-ontology-widget',
    templateUrl: './trait-ontology-widget.component.html',
    styleUrls: ['./trait-ontology-widget.component.scss'],

    // Necessary to import CSS style from the trait ontology widget
    encapsulation: ViewEncapsulation.None
})
export class TraitOntologyWidgetComponent implements OnInit {

    @Input() criteria$: BehaviorSubject<DataDiscoveryCriteria>;

    private localCriteria: DataDiscoveryCriteria = null;

    private widget: CropOntologyWidget;

    constructor(private gnpisService: GnpisService,
                private widgetFactory: CropOntologyWidgetFactory) {
    }

    ngOnInit() {
        this.widget = this.widgetFactory.initialize(
            '#trait-ontology-widget', {
                breedingAPIEndpoint: '/brapi/v1/',
                showCheckBoxes: true,
                useSearchField: true
            }
        );
        this.widget.setHeight(450);

        this.criteria$
            .pipe(filter(c => c !== this.localCriteria))
            .subscribe(newCriteria => {
                this.localCriteria = newCriteria;

                const selectedNodeIds = this.localCriteria.topSelectedTraitOntologyIds;
                this.widget.jsTreePanel.load().then(() => {
                    this.widget.reset();
                    this.widget.setSelectedNodeIds(selectedNodeIds);
                    this.localCriteria.observationVariableIds = this.getBottomSelected();
                    this.criteria$.next(this.localCriteria);
                });

                const field = 'observationVariableIds';
                const fetchSize = 2147483647;
                this.gnpisService.suggest(field, fetchSize, '', this.localCriteria)
                    .subscribe(ids => {
                        this.widget.showOnly(ids);
                    });
            });

        this.widget.addSelectionChangeHandler(() => {
            this.localCriteria = {
                ...this.localCriteria,
                observationVariableIds: this.getBottomSelected(),
                topSelectedTraitOntologyIds: this.getTopSelected()
            };
            this.criteria$.next(this.localCriteria);
        });
    }

    private getTopSelected(): string[] {
        return this.widget.jsTreePanel.jstree.get_top_selected();
    }

    private getBottomSelected(): string[] {
        return this.widget.jsTreePanel.jstree.get_bottom_selected();
    }
}
