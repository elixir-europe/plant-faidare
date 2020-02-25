import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { DataDiscoveryCriteria } from '../models/data-discovery.model';
import { BehaviorSubject } from 'rxjs';

enum Tabs {
    GERMPLASM = 'Germplasm',
    TRAIT = 'Trait'
}

@Component({
    selector: 'faidare-form',
    templateUrl: './form.component.html',
    styleUrls: ['./form.component.scss']
})
export class FormComponent implements OnInit {
    @Input() criteria$: BehaviorSubject<DataDiscoveryCriteria>;
    @Input() displayGermplasmResult$: BehaviorSubject<boolean>;
    @Output() traitWidgetInitialized = new EventEmitter();

    // Default active tab
    activeTab: Tabs = Tabs.GERMPLASM;

    // to give access in HTML template
    tabs = Tabs;
    displayGermplasmResult: boolean;

    getNavClass(tab: Tabs) {
        return this.activeTab === tab ? 'active' : '';
    }

    getTabClass(tab: Tabs) {
        return this.activeTab === tab ? 'visible' : 'd-none';
    }
    ngOnInit(): void {
        this.displayGermplasmResult$.subscribe(displayStatus => this.displayGermplasmResult = displayStatus);
    }
}
