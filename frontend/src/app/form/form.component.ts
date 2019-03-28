import { Component, EventEmitter, Input, Output } from '@angular/core';
import { DataDiscoveryCriteria } from '../models/data-discovery.model';
import { BehaviorSubject } from 'rxjs';

enum Tabs {
    GERMPLASM = 'Germplasm',
    TRAIT = 'Trait'
}

@Component({
    selector: 'gpds-form',
    templateUrl: './form.component.html',
    styleUrls: ['./form.component.scss']
})
export class FormComponent {
    @Input() criteria$: BehaviorSubject<DataDiscoveryCriteria>;
    @Output() traitWidgetInitialized = new EventEmitter();

    // Default active tab
    activeTab: Tabs = Tabs.GERMPLASM;

    // to give access in HTML template
    tabs = Tabs;

    getNavClass(tab: Tabs) {
        return this.activeTab === tab ? 'active' : '';
    }

    getTabClass(tab: Tabs) {
        return this.activeTab === tab ? 'visible' : 'd-none';
    }
}
