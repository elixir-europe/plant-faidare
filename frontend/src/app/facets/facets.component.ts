import { Component, Input, OnInit } from '@angular/core';
import {
    DataDiscoveryCriteria,
    DataDiscoveryFacet
} from '../models/data-discovery.model';
import { BehaviorSubject } from 'rxjs';
import { GermplasmSearchCriteria } from '../models/gnpis.model';

@Component({
    selector: 'faidare-facets',
    templateUrl: './facets.component.html',
    styleUrls: ['./facets.component.scss']
})
export class FacetsComponent implements OnInit {

    @Input() facets: DataDiscoveryFacet[];
    @Input() criteria$: BehaviorSubject<DataDiscoveryCriteria>;
    @Input() germplasmSearchCriteria$: BehaviorSubject<GermplasmSearchCriteria>;
    @Input() displayGermplasmResult$: BehaviorSubject<boolean>;

    constructor() {
    }

    ngOnInit(): void {
    }
}
