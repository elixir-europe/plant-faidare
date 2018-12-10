import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Param } from '../model/common';
import { DataDiscoveryCriteria } from '../model/dataDiscoveryCriteria';
import { Observable } from 'rxjs';

@Component({
    selector: 'gpds-form',
    templateUrl: './form.component.html',
    styleUrls: ['./form.component.scss']
})
export class FormComponent implements OnInit {
    @Input() criteria$: Observable<DataDiscoveryCriteria>;

    @Output() selectionChange = new EventEmitter<Param>();

    constructor() {
    }

    ngOnInit() {
    }
}
