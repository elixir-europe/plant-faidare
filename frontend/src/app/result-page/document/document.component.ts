import { Component, Input, OnInit } from '@angular/core';
import { DataDiscoveryDocument } from '../../model/dataDiscoveryDocument';

@Component({
    selector: 'gpds-document',
    templateUrl: './document.component.html',
    styleUrls: ['./document.component.scss']
})
export class DocumentComponent implements OnInit {
    @Input() document: DataDiscoveryDocument;

    constructor() {
    }

    ngOnInit() {
    }

}
